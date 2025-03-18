package ludichat.cobbreeding

import net.minecraft.item.ItemStack
import net.minecraft.util.collection.DefaultedList

class PastureBreedingData(var time: Int, var egg: DefaultedList<ItemStack>) {
    companion object {
        @JvmField
        val registry: Map<Int, PastureBreedingData> = hashMapOf()
    }
}
