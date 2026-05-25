package dev.polaris_light.nekoration.entity;

import dev.polaris_light.nekoration.block.furniture.SeatBlock;
import dev.polaris_light.nekoration.init.EntityTypeRegistry;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class SeatEntity extends Entity {
    private BlockPos seatPos = BlockPos.ZERO;

    public SeatEntity(EntityType<? extends SeatEntity> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    public static SeatEntity create(Level level, BlockPos seatPos, double yOffset) {
        SeatEntity seat = EntityTypeRegistry.SEAT.get().create(level);
        if (seat == null) {
            return null;
        }
        seat.seatPos = seatPos;
        seat.setPos(seatPos.getX() + 0.5D, seatPos.getY() + yOffset, seatPos.getZ() + 0.5D);
        return seat;
    }

    public static InteractionResult seatPlayer(Level level, BlockPos pos, double yOffset, Player player) {
        if (player.isShiftKeyDown() || player.isPassenger()) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        SeatEntity seat = findSeat(level, pos);
        if (seat == null) {
            seat = create(level, pos, yOffset);
            if (seat == null) {
                return InteractionResult.PASS;
            }
            level.addFreshEntity(seat);
        }

        if (!seat.getPassengers().isEmpty()) {
            return InteractionResult.PASS;
        }

        player.startRiding(seat, false);
        return InteractionResult.CONSUME;
    }

    private static SeatEntity findSeat(Level level, BlockPos pos) {
        AABB area = new AABB(
            pos.getX(),
            pos.getY(),
            pos.getZ(),
            pos.getX() + 1.0D,
            pos.getY() + 1.0D,
            pos.getZ() + 1.0D
        );
        List<SeatEntity> seats = level.getEntitiesOfClass(SeatEntity.class, area, entity -> entity.seatPos.equals(pos));
        return seats.isEmpty() ? null : seats.get(0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("SeatPos")) {
            this.seatPos = BlockPos.of(tag.getLong("SeatPos"));
            this.setPos(this.seatPos.getX() + 0.5D, this.getY(), this.seatPos.getZ() + 0.5D);
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putLong("SeatPos", this.seatPos.asLong());
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            boolean validChair = this.level().getBlockState(this.seatPos).getBlock() instanceof SeatBlock;
            if (!this.isVehicle() || !validChair) {
                this.discard();
            }
        }
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().isEmpty();
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }
}
