package ludichat.cobbreeding

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.platform.events.PlatformEvents
import com.mojang.logging.LogUtils.getLogger
import dev.architectury.registry.ReloadListenerRegistry
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.resource.ResourceType
import net.minecraft.resource.SynchronousResourceReloader
import net.minecraft.util.Identifier
import org.slf4j.Logger
import java.io.File

object Cobbreeding {
    const val MOD_ID = "cobbreeding"

    private const val CONFIG_PATH = "config/cobbreeding/main.json"

    internal val ITEMS: DeferredRegister<Item> = DeferredRegister.create(MOD_ID, RegistryKeys.ITEM)

    @JvmField
    val LOGGER: Logger = getLogger()

    @JvmField
    val EGG_ITEM: RegistrySupplier<PokemonEgg> = ITEMS.register("pokemon_egg") {
        PokemonEgg(Item.Settings().maxCount(1))
    }

    // Cache per players indicating if their egg should hatch faster
    val INCUBATOR_ABILITIES_REGISTRY: IncubatorAbilitiesRegistry = IncubatorAbilitiesRegistry()

    lateinit var config: Config

    fun init() {
        ITEMS.register()

        ReloadListenerRegistry.register(ResourceType.SERVER_DATA, SynchronousResourceReloader {
            LOGGER.info("Loading configuration")
            loadConfig()
        }, Identifier("cobbreeding-data"))


        // Adding and removing players from the incubators cache
        PlatformEvents.SERVER_PLAYER_LOGIN.subscribe {
            INCUBATOR_ABILITIES_REGISTRY.add(it.player)
        }
        PlatformEvents.SERVER_PLAYER_LOGOUT.subscribe {
            INCUBATOR_ABILITIES_REGISTRY.remove(it.player)
        }


    }

    @OptIn(ExperimentalSerializationApi::class)
    fun loadConfig() {
        val configFile = File(CONFIG_PATH)
        configFile.parentFile.mkdirs()

        config = if (configFile.exists()) {
            try {
                Json.decodeFromStream<Config>(configFile.inputStream())
            } catch (e: Exception) {
                LOGGER.error("Config error: ${e.message}")
                LOGGER.warn("Config has been reset.")
                Config()
            }
        } else {
            Config()
        }

        saveConfig()
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun saveConfig() {
        val configFile = File(CONFIG_PATH)
        configFile.parentFile.mkdirs()

        Json.encodeToStream(config, configFile.outputStream())
    }
}
