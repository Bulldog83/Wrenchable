package com.zundrel.wrenchable.block.defaults;

import com.zundrel.wrenchable.block.InstanceListener;
import com.zundrel.wrenchable.WrenchableUtilities;
import grondag.fermion.modkeys.impl.ModKeysAccess;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PistonInstanceListener extends InstanceListener {
    public PistonInstanceListener() {
        super(PistonBlock.class);
    }

    @Override
    public void onWrenched(World world, PlayerEntity player, BlockHitResult result) {
        BlockPos pos = result.getBlockPos();
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (!state.get(Properties.EXTENDED)) {
            if (ModKeysAccess.isControlPressed(player) && block == Blocks.STICKY_PISTON) {
                world.setBlockState(pos, Blocks.PISTON.getDefaultState().with(Properties.FACING, state.get(Properties.FACING)));
                world.playSound(null, pos, SoundEvents.ENTITY_SLIME_SQUISH, SoundCategory.BLOCKS, 1, 1F);
                if (!player.isCreative())
                    player.inventory.offerOrDrop(world, new ItemStack(Items.SLIME_BALL));

                return;
            }

            WrenchableUtilities.doFacingBehavior(world, player, result);
        }
    }
}
