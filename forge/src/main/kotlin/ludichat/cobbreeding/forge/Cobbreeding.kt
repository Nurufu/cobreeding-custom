package ludichat.cobbreeding.forge

import dev.architectury.platform.forge.EventBuses
import ludichat.cobbreeding.Cobbreeding.MOD_ID
import ludichat.cobbreeding.Cobbreeding.init
import ludichat.cobbreeding.CobbreedingClient
import net.minecraft.resource.ResourcePackProfile
import net.minecraft.resource.ResourcePackSource
import net.minecraft.resource.ResourceType
import net.minecraft.text.Text
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.event.AddPackFindersEvent
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.ModList
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.resource.PathPackResources
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(MOD_ID)
object Cobbreeding {
    init {
        EventBuses.registerModEventBus(MOD_ID, MOD_BUS)
        init()

        DistExecutor.safeRunWhenOn(Dist.CLIENT) { DistExecutor.SafeRunnable(CobbreedingClient::init) }

        with(MOD_BUS)
        {
            addListener(this@Cobbreeding::onAddPackFindersEvent)
        }
    }

    fun onAddPackFindersEvent(event: AddPackFindersEvent) {
        if (event.packType != ResourceType.CLIENT_RESOURCES) {
            return
        }

        val modFile = ModList.get().getModFileById("cobbreeding").file
        val path = modFile.findResource("pasturefix")
        val factory = ResourcePackProfile.PackFactory { name -> PathPackResources(name, true, path) }
        val profile = ResourcePackProfile.create(
            path.toString(),
            Text.literal("Pasture Block Fix"),
            true,
            factory,
            ResourceType.CLIENT_RESOURCES,
            ResourcePackProfile.InsertionPosition.TOP, // Go top to match Fabric behaviour
            ResourcePackSource.BUILTIN
        )
        event.addRepositorySource { consumer -> consumer.accept(profile) }
    }
}
