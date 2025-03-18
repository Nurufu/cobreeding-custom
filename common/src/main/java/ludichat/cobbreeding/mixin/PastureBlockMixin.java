package ludichat.cobbreeding.mixin;

import com.cobblemon.mod.common.block.PastureBlock;
import ludichat.cobbreeding.Cobbreeding;
import ludichat.cobbreeding.Config;
import ludichat.cobbreeding.CustomProperties;
import ludichat.cobbreeding.PastureBreedingData;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PastureBlock.class)
public abstract class PastureBlockMixin extends Block {
    @Unique
    private static final BooleanProperty HAS_EGG = CustomProperties.HAS_EGG;

    public PastureBlockMixin(Settings settings) {
        super(settings);
    }

    @Unique
    private static Config getConfig() {
        return Cobbreeding.INSTANCE.getConfig();
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(AbstractBlock.Settings properties, CallbackInfo ci) {
        setDefaultState(getDefaultState().with(HAS_EGG, false));
    }

    @Inject(at = @At("TAIL"), method = "appendProperties")
    private void appendProperties(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(HAS_EGG);
    }

    @Inject(at = @At("HEAD"), method = "onUse", cancellable = true)
    private void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (!world.isClient)
        {
            boolean is_bottom_part = state.get(PastureBlock.Companion.getPART()) == PastureBlock.PasturePart.BOTTOM;
            Inventory pastureInventory = (Inventory) world.getBlockEntity(pos);

            if (pastureInventory != null && !pastureInventory.getStack(0).isEmpty() && is_bottom_part) {
                ItemStack itemStack = player.getStackInHand(hand);

                if (hand == Hand.MAIN_HAND && itemStack.isEmpty()) {
                    ItemStack egg = pastureInventory.getStack(0);

                    // Get the egg
                    player.getInventory().offerOrDrop(pastureInventory.getStack(0));
                    pastureInventory.removeStack(0);
                    player.getInventory().updateItems();

                    cir.setReturnValue(ActionResult.SUCCESS);
                }
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "onBreak")
    private void onBroken(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo ci) {
        if (!world.isClient) {
            int hash = state.hashCode();

            PastureBreedingData.registry.remove(hash);
        }
    }
}
