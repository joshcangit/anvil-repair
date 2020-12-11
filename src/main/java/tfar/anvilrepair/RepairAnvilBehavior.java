package tfar.anvilrepair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class RepairAnvilBehavior extends OptionalDispenseItemBehavior {

    @Override
    protected ItemStack execute(BlockSource blockSource, ItemStack itemStack) {
        ServerLevel level = blockSource.getLevel();
        if (!level.isClientSide()) {
            BlockPos blockPos = blockSource.getPos().relative(blockSource.getBlockState().getValue(DispenserBlock.FACING));
            this.setSuccess(tryRepairAnvil(level, blockPos));
            if (this.isSuccess()) {
                itemStack.shrink(1);
            }
        }

        return itemStack;
    }

    private static boolean tryRepairAnvil(ServerLevel serverLevel, BlockPos blockPos) {
        BlockState blockState = serverLevel.getBlockState(blockPos);
        if (repairMap.get(blockState.getBlock()) != null) {
            serverLevel.setBlock(blockPos,repairMap.get(blockState.getBlock()).defaultBlockState().setValue(AnvilBlock.FACING,blockState.getValue(AnvilBlock.FACING)),2);
                return true;
        }
        return false;
    }

    private static final Map<Block, Block> repairMap = new HashMap<>();

    static {
        repairMap.put(Blocks.CHIPPED_ANVIL,Blocks.ANVIL);
        repairMap.put(Blocks.DAMAGED_ANVIL,Blocks.CHIPPED_ANVIL);
    }

    public static boolean canRepair(Block block) {
        return repairMap.containsKey(block);
    }

    public static Block getRepair(Block block) {
        return repairMap.get(block);
    }
}
