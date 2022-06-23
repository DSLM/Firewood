package com.dslm.firewood.entity;

import com.dslm.firewood.block.entity.RemnantSoulBoundedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class RemnantSoulEntity extends AmbientCreature
{
    BlockPos blockPos;
    
    public RemnantSoulEntity(EntityType<? extends AmbientCreature> type, Level worldIn)
    {
        super(type, worldIn);
    }
    
    public BlockPos getBlockPos()
    {
        return blockPos;
    }
    
    public void setBlockPos(BlockPos blockPos)
    {
        this.blockPos = blockPos;
        this.setPosRaw(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }
    
    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
    
        if(tag.contains("blockPos"))
        {
            blockPos = NbtUtils.readBlockPos(tag.getCompound("blockPos"));
        }
    }
    
    @Override
    public boolean hurt(DamageSource source, float amount)
    {
        if(source == DamageSource.IN_WALL)
            return false;
        return super.hurt(source, amount);
    }
    
    @Override
    public boolean isInvulnerableTo(DamageSource pSource)
    {
        return this.isInvulnerable() && pSource != DamageSource.OUT_OF_WORLD && !pSource.isCreativePlayer();
    }
    
    @Override
    public boolean save(CompoundTag tag)
    {
        if(blockPos != null)
        {
            tag.put("blockPos", NbtUtils.writeBlockPos(blockPos));
        }
        
        return super.save(tag);
    }
    
    @Override
    public void tick()
    {
        super.tick();
        if(!level.isClientSide && tickCount % 5 == 0 && blockPos != null && !(level.getBlockEntity(blockPos) instanceof RemnantSoulBoundedBlockEntity))
        {
            this.setHealth(0);
        }
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource)
    {
        return null;
    }
    
    @Nullable
    @Override
    protected SoundEvent getDeathSound()
    {
        return null;
    }
    
    public static AttributeSupplier.Builder prepareAttributes()
    {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
                .add(Attributes.FOLLOW_RANGE, 0);
    }
}
