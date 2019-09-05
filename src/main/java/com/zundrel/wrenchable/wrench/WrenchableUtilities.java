package com.zundrel.wrenchable.wrench;

import com.zundrel.wrenchable.WrenchableRegistry;
import grondag.fermion.modkeys.api.ModKeys;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SkullBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WrenchableUtilities {
    /**
     * This method causes the default Properties.FACING behavior to run.
     * @param world An instance of the world where this block was wrenched.
     * @param player The player who wrenched this block.
     * @param result Information about the block that was wrenched.
     * @author Zundrel
     */
    public static void doFacingBehavior(World world, PlayerEntity player, BlockHitResult result) {
        BlockPos pos = result.getBlockPos();
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        Direction direction = state.get(Properties.FACING);

        if (ModKeys.isAltPressed(player)) {
            world.setBlockState(pos, state.with(Properties.FACING, result.getSide()));
            world.updateNeighbor(pos, block, pos);
            return;
        }

        if (direction == Direction.UP || direction == Direction.DOWN) {
            world.setBlockState(pos, state.with(Properties.FACING, direction.getOpposite()));
            world.updateNeighbor(pos, block, pos);

            return;
        }

        rotateState(world, player, state, pos);
    }

    /**
     * This method causes the default Properties.HORIZONTAL_FACING behavior to run.
     * @param world An instance of the world where this block was wrenched.
     * @param player The player who wrenched this block.
     * @param result Information about the block that was wrenched.
     * @author Zundrel
     */
    public static void doHorizontalFacingBehavior(World world, PlayerEntity player, BlockHitResult result) {
        BlockPos pos = result.getBlockPos();
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (ModKeys.isAltPressed(player)) {
            if (!state.with(Properties.HORIZONTAL_FACING, player.getHorizontalFacing().getOpposite()).canPlaceAt(world, pos))
                return;

            world.setBlockState(pos, state.with(Properties.HORIZONTAL_FACING, player.getHorizontalFacing().getOpposite()));
            world.updateNeighbor(pos, block, pos);
            return;
        }

        rotateState(world, player, state, pos);
    }

    /**
     * This method causes basic rotation behavior for a BlockState to occur.
     * I don't recommend using this, instead use doFacingBehavior or doHorizontalFacingBehavior.
     * @param world An instance of the world where this block was wrenched.
     * @param player The player who wrenched this block.
     * @param state The BlockState that was wrenched.
     * @param pos The BlockPos that was wrenched.
     * @author Zundrel
     */
    public static void rotateState(World world, PlayerEntity player, BlockState state, BlockPos pos) {
        Block block = state.getBlock();

        if (player.isSneaking()) {
            if (!state.rotate(BlockRotation.COUNTERCLOCKWISE_90).canPlaceAt(world, pos))
                return;

            world.setBlockState(pos, state.rotate(BlockRotation.COUNTERCLOCKWISE_90));
            world.updateNeighbor(pos, block, pos);
        } else {
            if (!state.rotate(BlockRotation.CLOCKWISE_90).canPlaceAt(world, pos))
                return;

            world.setBlockState(pos, state.rotate(BlockRotation.CLOCKWISE_90));
            world.updateNeighbor(pos, block, pos);
        }
    }

    public static void doRotationBehavior(World world, PlayerEntity player, BlockHitResult result) {
        BlockPos pos = result.getBlockPos();
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (ModKeys.isAltPressed(player)) {
            if (block instanceof SkullBlock)
                world.setBlockState(pos, state.with(Properties.ROTATION, MathHelper.floor((double)((player.yaw) * 16.0F / 360.0F) + 0.5D) & 15));
            else
                world.setBlockState(pos, state.with(Properties.ROTATION, MathHelper.floor((double)((180.0F + player.yaw) * 16.0F / 360.0F) + 0.5D) & 15));
            world.updateNeighbor(pos, block, pos);
            return;
        }

        if (player.isSneaking()) {
            if (state.get(Properties.ROTATION) > 0) {
                world.setBlockState(pos, state.with(Properties.ROTATION, state.get(Properties.ROTATION) - 1));
            } else {
                world.setBlockState(pos, state.with(Properties.ROTATION, 15));
            }

            world.updateNeighbor(pos, block, pos);
            return;
        } else {
            if (state.get(Properties.ROTATION) < 15) {
                world.setBlockState(pos, state.with(Properties.ROTATION, state.get(Properties.ROTATION) + 1));
            } else {
                world.setBlockState(pos, state.with(Properties.ROTATION, 0));
            }

            world.updateNeighbor(pos, block, pos);
            return;
        }
    }

    public static Wrench getWrench(Item item) {
        if (item instanceof Wrench)
            return (Wrench) item;
        else if (WrenchableRegistry.isWrench(item))
            return WrenchableRegistry.getWrench(item);

        return null;
    }

    public static boolean isWrench(Item item) {
        return item instanceof Wrench || WrenchableRegistry.isWrench(item);
    }
}