package ludichat.cobbreeding

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.moves.BenchedMove
import com.cobblemon.mod.common.api.moves.MoveTemplate
import com.cobblemon.mod.common.api.pokemon.Natures
import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.api.pokemon.feature.FlagSpeciesFeature
import com.cobblemon.mod.common.api.pokemon.feature.StringSpeciesFeature
import com.cobblemon.mod.common.pokemon.FormData
import com.cobblemon.mod.common.pokemon.IVs
import com.cobblemon.mod.common.pokemon.Nature
import com.cobblemon.mod.common.pokemon.Species
import de.erdbeerbaerlp.dcintegration.common.DiscordIntegration
import de.erdbeerbaerlp.dcintegration.common.storage.Configuration
import de.erdbeerbaerlp.dcintegration.common.util.DiscordMessage
import de.erdbeerbaerlp.dcintegration.common.util.MessageUtils
import de.erdbeerbaerlp.dcintegration.common.util.TextColors
import ludichat.cobbreeding.PastureUtilities.toIVs
import ludichat.cobbreeding.PastureUtilities.toIdArray
import ludichat.cobbreeding.PastureUtilities.toIntArray
import ludichat.cobbreeding.PastureUtilities.toMoves
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.world.World
import java.util.*
import kotlin.math.floor

/**
 * Pokémon egg item.
 */
class PokemonEgg(settings: Settings?) : Item(settings) {

    data class Info(
        val species: Species?,
        val ivs: IVs?,
        val nature: Nature?,
        val form: FormData?,
        val eggMoves: Set<MoveTemplate>?,
        val ability: String,
        val pokeballName: String?,
        val shiny: Boolean?
    ) {
        enum class Keys(val key: String) {
            Species("species"),
            IVs("ivs"),
            Nature("nature"),
            Form("form"),
            EggMoves("egg_moves"),
            Ability("ability"),
            Pokeball("pokeball"),
            Shiny("shiny"),
        }

        companion object {
            @JvmStatic
            fun fromNbt(nbt: NbtCompound): Info {
                val speciesNbt = nbt.getString(Keys.Species.key)
                val ivsNbt = nbt.getIntArray(Keys.IVs.key)
                val natureNbt = nbt.getString(Keys.Nature.key)
                val formNbt = nbt.getString(Keys.Form.key)
                val eggMovesNbt = nbt.getIntArray(Keys.EggMoves.key)
                val abilityNbt = nbt.getString(Keys.Ability.key)
                val pokeballNbt = nbt.getString(Keys.Pokeball.key)
                val shinyNbt = nbt.getBoolean(Keys.Shiny.key)

                val species = speciesNbt.let { PokemonSpecies.getByName(speciesNbt) }
                    ?: PokemonSpecies.getByPokedexNumber(nbt.getInt(Keys.Species.key))
                return Info(
                    species,
                    ivsNbt?.toIVs(),
                    natureNbt?.let { Natures.getNature(Identifier(it)) },
                    species?.forms?.find { it.formOnlyShowdownId() == formNbt },
                    eggMovesNbt?.toMoves(),
                    abilityNbt,
                    pokeballNbt,
                    shinyNbt
                )
            }
        }

        fun toNbt(nbt: NbtCompound) {
            nbt.putString(Keys.Species.key, species?.showdownId() ?: "")
            nbt.putIntArray(Keys.IVs.key, ivs?.toIntArray() ?: intArrayOf())
            nbt.putString(Keys.Nature.key, nature?.name.toString())
            nbt.putString(Keys.Form.key, form?.formOnlyShowdownId().toString())
            nbt.putIntArray(Keys.EggMoves.key, eggMoves?.toIdArray() ?: intArrayOf())
            nbt.putString(Keys.Ability.key, ability)
            nbt.putString(Keys.Pokeball.key, pokeballName)
            shiny?.let { nbt.putBoolean(Keys.Shiny.key, it) }
        }
    }

    override fun appendTooltip(
        stack: ItemStack?, world: World?, tooltip: MutableList<Text>?, context: TooltipContext?
    ) {
        super.appendTooltip(stack, world, tooltip, context)

        val species = stack?.orCreateNbt?.run {
            getString(Info.Keys.Species.key)?.let { PokemonSpecies.getByName(it) }
                ?: PokemonSpecies.getByPokedexNumber(getInt(Info.Keys.Species.key))
        }

        val form = species?.forms?.find { it.name == stack.nbt?.getString(Info.Keys.Form.key) }
        tooltip?.add(species?.translatedName ?: Text.literal("Bad egg"))
        form?.let { if (it.name != "Normal") tooltip?.add(Text.literal(it.name)) }

        val timer = stack?.nbt?.getInt("timer")
        timer?.let {tooltip?.add(Text.literal(ticksToTime(timer)))}
    }

    override fun inventoryTick(stack: ItemStack?, world: World?, entity: Entity?, slot: Int, selected: Boolean) {
        super.inventoryTick(stack, world, entity, slot, selected)

        if (entity != null && entity is PlayerEntity && stack != null && !world?.isClient!!) {
            // handling tickTime so the method only plays every second
            var tickTime = stack.nbt?.getInt("tickTime") ?: 0
            stack.getOrCreateNbt().putInt("tickTime", tickTime + 1)
            tickTime += 1
            if (tickTime < 20) return
            stack.getOrCreateNbt().putInt("tickTime", 0)

            val timer = stack.nbt?.getInt("timer") ?: 100
            stack.getOrCreateNbt().putInt("timer", timer - 20)

            // Egg hatches faster is the player has a pokemon with an ability increasing hatching speed
            if(Cobbreeding.INCUBATOR_ABILITIES_REGISTRY.shouldHatchFaster(entity.uuidAsString))
                stack.getOrCreateNbt().putInt("timer", timer - 40)

            if (timer <= 0) {
                val info = stack.nbt?.let { Info.fromNbt(it) }
                if (info != null)
                {
                    val pokemonProperties = PokemonProperties()

                    info.species?.run {
                        pokemonProperties.species = showdownId()
                        forms.find { it.formOnlyShowdownId() == info.form?.formOnlyShowdownId() }?.run {
                            aspects.forEach {
                                // alternative form
                                pokemonProperties.customProperties.add(FlagSpeciesFeature(it, true))
                                // regional bias
                                pokemonProperties.customProperties.add(StringSpeciesFeature("region_bias", it.split("-").last()))
                                // basculin wants to be special
                                pokemonProperties.customProperties.add(StringSpeciesFeature("fish_stripes", it.removeSuffix("striped")))
                            }
                        }
                    }
                    info.ivs?.let { pokemonProperties.ivs = it }
                    info.nature?.let { pokemonProperties.nature = it.name.toString() }
                    pokemonProperties.ability = info.ability
                    info.pokeballName?.let { pokemonProperties.pokeball = it }
                    pokemonProperties.shiny = info.shiny

                    try {
                        val pokemon = pokemonProperties.create()
                        pokemon.setFriendship(120)

                        for (move in info.eggMoves ?: setOf()) {
                            // Always adding the move to benched moves to easily add it in its allAccessibleMoves so
                            // the pokemon will be able to transfer its eggmoves to its children anytime
                            pokemon.benchedMoves.add(BenchedMove(move, 0))
                            if (pokemon.moveSet.hasSpace()) {
                                pokemon.moveSet.add(move.create())
                            }
                        }

                        Cobblemon.storage.getParty(entity.uuid).add(pokemon)
                        if(pokemonProperties.shiny == true){
                            val u = entity as ServerPlayerEntity
                            u.server.playerManager.broadcast(
                                Text.translatable("shiny_egg", *arrayOf<Any>(u.displayName), "§e${pokemonProperties.species.toString()
                                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}!"), false
                            )
                            try{
                            var embedBuilder = Configuration.instance().embedMode.chatMessages.toEmbed()
                            embedBuilder = embedBuilder.setColor(TextColors.generateFromUUID(u.uuid))
                            embedBuilder = embedBuilder.setAuthor(u.name.string, null, Configuration.instance().webhook.playerAvatarURL.replace("%uuid%", u.uuidAsString).replace("%uuid_dashless%", u.uuidAsString.replace("-", "")).replace("%name%", u.name.string).replace("%randomUUID%", UUID.randomUUID().toString()))
                                .setDescription("${u.displayName.string} just hatched a strangely-colored ${pokemonProperties.species.toString().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}!")

                            DiscordIntegration.INSTANCE.sendMessage(DiscordMessage(embedBuilder.build()),DiscordIntegration.INSTANCE.getChannel(Configuration.instance().advanced.chatOutputChannelID))
                            }catch(e: NoClassDefFoundError) {null}
                    }} catch (e: Exception) {
                        Cobbreeding.LOGGER.error("Egg hatching failed: ${e.message}\n${e.stackTrace}")
                        entity.sendMessage(Text.translatable("cobbreeding.msg.egg_hatch.fail"))
                    } finally {
                        stack.decrement(1)
                    }
                }
            }
        }
    }

    /**
     * Convert ticks to minutes and seconds
     * @return string representing time.
     */
    private fun ticksToTime(ticks: Int) : String
    {
        val minutes = floor((ticks/1200).toFloat()).toInt()
        val seconds = floor((ticks%1200/20).toFloat()).toInt()

        return if (minutes <= 0)
            seconds.toString()
        else
            "$minutes:$seconds"
    }
}
