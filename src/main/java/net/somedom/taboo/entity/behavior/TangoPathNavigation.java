package net.somedom.taboo.entity.behavior;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.*;
import net.somedom.taboo.block.Salt;
import net.somedom.taboo.entity.custom.TangoEntity;
import net.tslat.smartbrainlib.api.core.navigation.SmoothAmphibiousPathNavigation;

import javax.annotation.Nullable;

public class TangoPathNavigation extends SmoothAmphibiousPathNavigation {
    public TangoPathNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    @Override
    protected PathFinder createPathFinder(int maxVisitedNodes) {
        this.nodeEvaluator = new WalkNodeEvaluator() {
            @Override
            public PathType getPathTypeOfMob(PathfindingContext context, int x, int y, int z, Mob mob) {
                if (mob instanceof TangoEntity) {
                    BlockState state = context.level().getBlockState(new BlockPos(x, y, z));
                    if (state.getBlock() instanceof Salt) {
                        return PathType.BLOCKED;
                    }
                }
                return super.getPathTypeOfMob(context, x, y, z, mob);
            }

            @Override
            protected boolean isDiagonalValid(Node root, @Nullable Node xNode, @Nullable Node zNode) {
                if (isSaltNode(xNode) || isSaltNode(zNode)) {
                    return true; // let the diagonal node be evaluated on its own merits
                }
                return super.isDiagonalValid(root, xNode, zNode);
            }

            private boolean isSaltNode(@Nullable Node node) {
                if (node == null || node.costMalus >= 0) return false;
                BlockState state = this.currentContext.level().getBlockState(new BlockPos(node.x, node.y, node.z));
                return state.getBlock() instanceof Salt;
            }
        };
        return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
    }
}