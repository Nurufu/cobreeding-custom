package ludichat.cobbreeding.fabric

import ludichat.cobbreeding.Cobbreeding
import ludichat.cobbreeding.CobbreedingClient.init
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.ResourcePackActivationType
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.text.Text
import net.minecraft.util.Identifier

@Suppress("Unused")
class CobbreedingClient : ClientModInitializer {
    override fun onInitializeClient() {
        init()
        ResourceManagerHelper.registerBuiltinResourcePack(
            Identifier("cobbreeding", "pasturefix"),
            FabricLoader.getInstance().getModContainer(Cobbreeding.MOD_ID).get(),
            Text.literal("Pasture Block Fix"),
            ResourcePackActivationType.ALWAYS_ENABLED
        )
    }
}