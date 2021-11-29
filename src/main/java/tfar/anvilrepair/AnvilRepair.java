package tfar.anvilrepair;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class AnvilRepair implements ModInitializer, UseBlockCallback {
	@Override
	public void onInitialize() {
		DispenserBlock.registerBehavior(Items.IRON_INGOT,new RepairAnvilBehavior());
		UseBlockCallback.EVENT.register(this);
	}

	@Override
	public InteractionResult interact(Player player, Level world, InteractionHand hand, BlockHitResult hitResult) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getItem() == Items.IRON_INGOT) {
			BlockPos pos = hitResult.getBlockPos();
			BlockState anvil = world.getBlockState(pos);
			if (RepairAnvilBehavior.canRepair(anvil.getBlock())) {
				world.setBlock(pos,RepairAnvilBehavior.getRepair(anvil.getBlock()).defaultBlockState().setValue(AnvilBlock.FACING,anvil.getValue(AnvilBlock.FACING)),2);
				stack.shrink(1);
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}
}
