package com.dslm.firewood.fireEffectHelper.flesh;

import com.dslm.firewood.Firewood;
import com.dslm.firewood.fireEffectHelper.flesh.base.FireEffectHelperInterface;
import com.dslm.firewood.fireEffectHelper.flesh.base.MajorFireEffectHelperBase;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;
import com.dslm.firewood.tooltip.MiddleComponent;
import com.dslm.firewood.util.StaticValue;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.ITeleporter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static com.dslm.firewood.config.SpiritualFireBlockEffectConfig.TELEPORT_BASE_DAMAGE;
import static com.dslm.firewood.fireEffectHelper.flesh.FireEffectHelpers.colorfulText;

public class TeleportFireEffectHelper extends MajorFireEffectHelperBase
{
    private static final ArrayList<FireEffectHelperInterface> instanceList = new ArrayList<>();
    
    private static final int COLOR = 0x336666;
    public static final String DIM_TAG_ID = "dim";
    public static final String X_TAG_ID = "posX";
    public static final String Y_TAG_ID = "posY";
    public static final String Z_TAG_ID = "posZ";
    
    public TeleportFireEffectHelper(String id)
    {
        super(new FireEffectNBTData()
        {{
            put(DIM_TAG_ID, "overworld");
            put(X_TAG_ID, "0");
            put(Y_TAG_ID, "256");
            put(Z_TAG_ID, "0");
        }}, id);
        instanceList.add(this);
    }
    
    @Override
    public int getColor(FireEffectNBTData data)
    {
        return COLOR;
    }
    
    public static int getColor()
    {
        return COLOR;
    }
    
    @Override
    public FireEffectNBTData triggerEffect(FireEffectNBTData data, BlockState state, Level level, BlockPos pos, LivingEntity entity)
    {
        try
        {
            Set<ResourceKey<Level>> levelList = level.getServer().levelKeys();
            for(ResourceKey<Level> levelKey : levelList)
            {
                if(entity.getServer().getLevel(levelKey).dimension().location().getPath().equals(data.get(DIM_TAG_ID)))
                {
                    entity.changeDimension(entity.getServer().getLevel(levelKey), new ITeleporter()
                    {
                        @Override
                        public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity)
                        {
                            entity = repositionEntity.apply(false);
                            entity.teleportTo(Integer.parseInt(data.get(X_TAG_ID)) + 0.5, Integer.parseInt(data.get(Y_TAG_ID)), Integer.parseInt(data.get(Z_TAG_ID)) + 0.5);
                            return entity;
                        }
                    });
                    return data;
                }
            }
        } catch(Exception e)
        {
            Firewood.LOGGER.error("TeleportFireEffectHelper.triggerEffect Exception: ", e);
        }
        return data;
    }
    
    @Override
    public float getDamage(FireEffectNBTData data)
    {
        return TELEPORT_BASE_DAMAGE.get().floatValue();
    }
    
    @Override
    public ArrayList<Component> getToolTips(FireEffectNBTData data, boolean extended)
    {
        ArrayList<Component> lines = new ArrayList<>();
        MiddleComponent mainLine = (MiddleComponent) colorfulText(
                new MiddleComponent("tooltip.firewood.tinder_item.major_effect." + data.getType()), COLOR);
        lines.add(mainLine);
        if(extended)
        {
            lines.add(colorfulText(
                    new TranslatableComponent("tooltip.firewood.tinder_item.major_effect." + data.getType() + ".extend.1",
                            data.get(DIM_TAG_ID), Integer.parseInt(data.get(X_TAG_ID)), Integer.parseInt(data.get(Y_TAG_ID)), Integer.parseInt(data.get(Z_TAG_ID))),
                    COLOR));
        }
        mainLine.setDamage(getDamage(data));
        mainLine.setMinHealth(getMinHealth(data));
        return lines;
    }
    
    @Override
    public CompoundTag saveToNBT(FireEffectNBTData data)
    {
        CompoundTag tags = new CompoundTag();
        tags.putString(StaticValue.TYPE, ID);
        tags.putString(DIM_TAG_ID, data.get(DIM_TAG_ID));
        tags.putString(X_TAG_ID, data.get(X_TAG_ID));
        tags.putString(Y_TAG_ID, data.get(Y_TAG_ID));
        tags.putString(Z_TAG_ID, data.get(Z_TAG_ID));
        return tags;
    }
    
    @Override
    public FireEffectNBTData readFromNBT(CompoundTag tags)
    {
        FireEffectNBTData data = new FireEffectNBTData();
        data.put(StaticValue.TYPE, ID);
        data.put(DIM_TAG_ID, tags.getString(DIM_TAG_ID));
        data.put(X_TAG_ID, tags.getString(X_TAG_ID));
        data.put(Y_TAG_ID, tags.getString(Y_TAG_ID));
        data.put(Z_TAG_ID, tags.getString(Z_TAG_ID));
        return data;
    }
    
    @Override
    public void fillItemCategory(NonNullList<ItemStack> items, ItemStack item)
    {
        items.add(FireEffectHelpers.addMajorEffect(item.copy(), ID, DEFAULT_DATA));
    }
    
    public static List<FireEffectHelperInterface> getInstanceList()
    {
        return instanceList;
    }
}
