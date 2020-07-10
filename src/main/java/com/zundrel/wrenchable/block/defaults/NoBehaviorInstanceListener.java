package com.zundrel.wrenchable.block.defaults;

import com.zundrel.wrenchable.block.InstanceListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class NoBehaviorInstanceListener extends InstanceListener {
    public NoBehaviorInstanceListener(Class<?> block) {
        super(block);
    }

    @Override
    public void onWrenched(World world, PlayerEntity player, BlockHitResult result) {
        return;
    }
}
