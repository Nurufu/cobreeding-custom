package ludichat.cobbreeding

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.reactive.ObservableSubscription
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore
import net.minecraft.server.network.ServerPlayerEntity
import org.apache.commons.lang3.tuple.MutablePair

// This class keeps and handles a cache per player that indicates if their egg must hatch faster
class IncubatorAbilitiesRegistry {
    val registry = mutableMapOf<String, MutablePair<ObservableSubscription<Unit>, Boolean>>()

    fun add(player: ServerPlayerEntity)
    {
        val party = Cobblemon.storage.getParty(player.uuid)
        val subscription = party.getAnyChangeObservable().subscribe {
            update(player)
        }
        registry[player.uuidAsString] = MutablePair(subscription, isIncubator(party))
    }

    fun update(player: ServerPlayerEntity)
    {
        if (registry[player.uuidAsString] == null)
        {
            Cobbreeding.LOGGER.warn("${player.name} can't be found in Incubator Ability Registry.")
            return
        }
        registry[player.uuidAsString]!!.right = isIncubator(Cobblemon.storage.getParty(player.uuid))
    }

    fun remove(player: ServerPlayerEntity)
    {
        registry[player.uuidAsString]!!.left.unsubscribe()
        registry.remove(player.uuidAsString)
    }

    fun isIncubator(party: PlayerPartyStore): Boolean
    {
        for (i in 0..5) {
            val pokemon = party.get(i)
            // Looking for an ability reducing hatching time in party
            val incubator = pokemon?.let {
                val ability = it.ability.template.name
                (ability == "magmaarmor" || ability == "flamebody" || ability == "steamengine")
            } ?: false
            if (incubator) {
                return true
            }
        }

        return false
    }

    fun shouldHatchFaster(player: String): Boolean
    {
        if (registry.containsKey(player))
            return registry[player]!!.right
        return false
    }
}