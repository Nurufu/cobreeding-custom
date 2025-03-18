package ludichat.cobbreeding.mixin;

import com.cobblemon.mod.common.block.entity.PokemonPastureBlockEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import ludichat.cobbreeding.*;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static ludichat.cobbreeding.PastureUtilities.chooseEgg;
import static net.minecraft.util.math.MathHelper.floor;

@Mixin(PokemonPastureBlockEntity.class)
public abstract class PokemonPastureBlockEntityMixin implements PastureInventory, SidedInventory {
    @Unique
    private static final Logger LOGGER = Cobbreeding.LOGGER;
    @Unique
    private static final BooleanProperty HAS_EGG = CustomProperties.HAS_EGG;

    @Override
    public DefaultedList<ItemStack> getItems() {
        int hash = this.hashCode();
        PastureBreedingData data;

        if (PastureBreedingData.registry.containsKey(hash)) {
            data = PastureBreedingData.registry.get(hash);
        } else {
            data = new PastureBreedingData(getConfig().getEggCheckTicks(), DefaultedList.ofSize(1, ItemStack.EMPTY));
            PastureBreedingData.registry.put(hash, data);
        }

        return data.getEgg();
    }

    /**
     * Get mod configuration
     *
     * @return Configuration instance
     */
    @Unique
    private static Config getConfig() {
        return Cobbreeding.INSTANCE.getConfig();
    }

    @Inject(at = @At("HEAD"), method = "TICKER$lambda$11")
    private static void init(World world, BlockPos pos, BlockState state, PokemonPastureBlockEntity blockEntity, CallbackInfo ci) {
        if (world.isClient) return;

        int hash = blockEntity.hashCode();
        PastureBreedingData data;

        if (PastureBreedingData.registry.containsKey(hash)) {
            data = PastureBreedingData.registry.get(hash);
        } else {
            data = new PastureBreedingData(getConfig().getEggCheckTicks(), DefaultedList.ofSize(1, ItemStack.EMPTY));
            PastureBreedingData.registry.put(hash, data);
        }

        world.setBlockState(pos, state.with(HAS_EGG, !data.getEgg().get(0).isEmpty()));

        int time = data.getTime();

        time--;
        if (time <= 0) {
            time = getConfig().getEggCheckTicks();
            List<PokemonPastureBlockEntity.Tethering> tetheredPokemon = blockEntity.getTetheredPokemon();
            List<Pokemon> pokemon = PastureUtilities.getPokemon(tetheredPokemon);
            // Applying Mirror Herb effect to Pokemon holding it
            PastureUtilities.applyMirrorHerb(pokemon);

            double randomNumber = Math.random();
            double eggChance = getConfig().getEggCheckChance();

            LOGGER.trace("Trying egg, roll: %b (%f >= 1 - %f)".formatted(randomNumber >= 1 - getConfig().getEggCheckChance(), randomNumber, eggChance));

            if (data.getEgg().get(0).isEmpty() && randomNumber >= 1 - eggChance) {
                PokemonEgg.Info eggData = chooseEgg(pokemon);

                if (eggData != null && eggData.component1() != null)
                {
                    // Creating the egg
                    ItemStack eggItem = new ItemStack(Cobbreeding.EGG_ITEM.get());
                    eggData.toNbt(eggItem.getOrCreateNbt());
                    // Set the egg timer
                    eggItem.getOrCreateNbt().putInt("timer", floor(eggData.component1().getEggCycles() * 600 * getConfig().getEggHatchMultiplier()));
                    // Adding the egg to the inventory
                    data.getEgg().set(0, eggItem);
                    // Playing a sound
                    world.playSound(null, pos, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.BLOCKS, 1f, 1f);
                }
            }
        }

        data.setTime(time);
    }

    @Inject(at = @At("HEAD"), method = "writeNbt")
    private void writeNbt(NbtCompound nbt, CallbackInfo ci) {
        Inventories.writeNbt(nbt, getItems());
    }

    @Inject(at = @At("TAIL"), method = "readNbt")
    private void readNbt(NbtCompound nbt, CallbackInfo ci) {
        Inventories.readNbt(nbt, getItems());
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction direction) {
        return true;
    }

    @Override
    public int[] getAvailableSlots(Direction direction) {
        return new int[] {0};
    }
}
