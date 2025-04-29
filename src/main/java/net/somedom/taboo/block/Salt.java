package net.somedom.taboo.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class Salt extends Block {
    public static final EnumProperty<RedstoneSide> NORTH = RedStoneWireBlock.NORTH;
    public static final EnumProperty<RedstoneSide> EAST = RedStoneWireBlock.EAST;
    public static final EnumProperty<RedstoneSide> SOUTH = RedStoneWireBlock.SOUTH;
    public static final EnumProperty<RedstoneSide> WEST = RedStoneWireBlock.WEST;

    private static final VoxelShape DOT = Block.box(3, 0, 3, 13, 1, 13);
    private static final VoxelShape NORTH_LINE = Block.box(3, 0, 0, 13, 1, 3);
    private static final VoxelShape EAST_LINE = Block.box(13, 0, 3, 16, 1, 13);
    private static final VoxelShape SOUTH_LINE = Block.box(3, 0, 13, 13, 1, 16);
    private static final VoxelShape WEST_LINE = Block.box(0, 0, 3, 3, 1, 13);

    public Salt() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_WIRE).sound(SoundType.SAND));
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(NORTH, RedstoneSide.NONE)
                .setValue(EAST, RedstoneSide.NONE)
                .setValue(SOUTH, RedstoneSide.NONE)
                .setValue(WEST, RedstoneSide.NONE)
        );
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = DOT;

        shape = state.getValue(NORTH) != RedstoneSide.NONE ? Shapes.or(shape, NORTH_LINE) : shape;
        shape = state.getValue(EAST)  != RedstoneSide.NONE ? Shapes.or(shape, EAST_LINE)  : shape;
        shape = state.getValue(SOUTH) != RedstoneSide.NONE ? Shapes.or(shape, SOUTH_LINE) : shape;
        shape = state.getValue(WEST)  != RedstoneSide.NONE ? Shapes.or(shape, WEST_LINE)  : shape;

        return shape;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH,EAST,SOUTH,WEST);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston)
    {
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
            return;
        }

        BlockState bs = state;

        bs = bs.setValue(NORTH, (level.getBlockState(pos.north()).is(ModBlocks.SALT)) ? RedstoneSide.SIDE : RedstoneSide.NONE);
        bs = bs.setValue(EAST, (level.getBlockState(pos.east()).is(ModBlocks.SALT)) ? RedstoneSide.SIDE : RedstoneSide.NONE);
        bs = bs.setValue(SOUTH, (level.getBlockState(pos.south()).is(ModBlocks.SALT)) ? RedstoneSide.SIDE : RedstoneSide.NONE);
        bs = bs.setValue(WEST, (level.getBlockState(pos.west()).is(ModBlocks.SALT)) ? RedstoneSide.SIDE : RedstoneSide.NONE);

        level.setBlockAndUpdate(pos, bs);

        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState bs = ModBlocks.SALT.get().defaultBlockState();

        bs = bs.setValue(NORTH, (context.getLevel().getBlockState(context.getClickedPos().north()).is(ModBlocks.SALT)) ? RedstoneSide.SIDE : RedstoneSide.NONE);
        bs = bs.setValue(EAST, (context.getLevel().getBlockState(context.getClickedPos().east()).is(ModBlocks.SALT)) ? RedstoneSide.SIDE : RedstoneSide.NONE);
        bs = bs.setValue(SOUTH, (context.getLevel().getBlockState(context.getClickedPos().south()).is(ModBlocks.SALT)) ? RedstoneSide.SIDE : RedstoneSide.NONE);
        bs = bs.setValue(WEST, (context.getLevel().getBlockState(context.getClickedPos().west()).is(ModBlocks.SALT)) ? RedstoneSide.SIDE : RedstoneSide.NONE);

        return bs;
    }
}