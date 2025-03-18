package ludichat.cobbreeding

import dev.architectury.registry.client.rendering.ColorHandlerRegistry
import ludichat.cobbreeding.Cobbreeding.EGG_ITEM
import ludichat.cobbreeding.Cobbreeding.ITEMS

object CobbreedingClient {
    fun init() {
        ITEMS.registrar.listen(EGG_ITEM.id) {
            ColorHandlerRegistry.registerItemColors(
                { _, tintIndex -> if (tintIndex == 0) 0xffffcc else 0x99ff66 },
                EGG_ITEM.get()
            )
        }
    }
}
