package com.dslm.firewood.fireeffecthelper.flesh.major;

import com.dslm.firewood.Firewood;
import com.dslm.firewood.entity.RemnantSoulEntity;
import com.dslm.firewood.fireeffecthelper.flesh.FireEffectHelpers;
import com.dslm.firewood.fireeffecthelper.flesh.base.SubMajorFireEffectHelperBase;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTData;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTDataInterface;
import com.dslm.firewood.subtype.FireEffectSubTypeBase;
import com.dslm.firewood.subtype.FireEffectSubTypeManager;
import com.dslm.firewood.subtype.TeleportSubType;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.ITeleporter;

import java.util.ArrayList;
import java.util.Set;
import java.util.function.Function;

import static com.dslm.firewood.util.StaticValue.colorfulText;

public class TeleportFireEffectHelper extends SubMajorFireEffectHelperBase
{
    public static final String DIM_TAG_ID = "dim";
    public static final String X_TAG_ID = "posX";
    public static final String Y_TAG_ID = "posY";
    public static final String Z_TAG_ID = "posZ";
    
    public TeleportFireEffectHelper(String id)
    {
        super(new FireEffectNBTData()
        {{
            setType(id);
            setSubType("");
            setProcess(0);
            set(DIM_TAG_ID, "minecraft:overworld");
            set(X_TAG_ID, "0");
            set(Y_TAG_ID, "256");
            set(Z_TAG_ID, "0");
        }}, id, TargetType.LIVING_ENTITY);
    }
    
    @Override
    public void transmuteEntity(FireEffectNBTDataInterface data, Level level, LivingEntity livingEntity, LivingEntity source)
    {
        FireEffectSubTypeBase subType = FireEffectSubTypeManager.getEffectsMap().get("teleport").get(data.getSubType());
        if(livingEntity instanceof RemnantSoulEntity)
        {
            return;
        }
        if(subType instanceof TeleportSubType teleportSubType)
        {
            if(teleportSubType.allowFromDim(level.dimension().location().toString()) && teleportSubType.allowToDim(data.get(DIM_TAG_ID)))
            {
            }
            else
            {
                return;
            }
        }
        else
        {
            return;
        }
        try
        {
            Set<ResourceKey<Level>> levelList = level.getServer().levelKeys();
            for(ResourceKey<Level> levelKey : levelList)
            {
                if(livingEntity.getServer().getLevel(levelKey).dimension().location().toString().equals(data.get(DIM_TAG_ID)))
                {
                    livingEntity.changeDimension(livingEntity.getServer().getLevel(levelKey), new ITeleporter()
                    {
                        @Override
                        public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity)
                        {
                            entity = repositionEntity.apply(false);
                            entity.teleportTo(Integer.parseInt(data.get(X_TAG_ID)) + 0.5, Integer.parseInt(data.get(Y_TAG_ID)), Integer.parseInt(data.get(Z_TAG_ID)) + 0.5);
                            return entity;
                        }
                    
                        @Override
                        public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld)
                        {
                            return false;
                        }
                    });
                    return;
                }
            }
        } catch(Exception e)
        {
            Firewood.LOGGER.error("TeleportFireEffectHelper.triggerEffect Exception: ", e);
        }
    }
    
    @Override
    public ArrayList<Component> getToolTips(FireEffectNBTDataInterface data, boolean extended)
    {
        ArrayList<Component> lines = new ArrayList<>();
        lines.add(getMainToolTips(data, extended));
        if(extended)
        {
            lines.add(colorfulText(
                    new TranslatableComponent("tooltip.firewood.tinder_item.major_effect." + data.getType() + ".extend.1",
                            data.get(DIM_TAG_ID), Integer.parseInt(data.get(X_TAG_ID)), Integer.parseInt(data.get(Y_TAG_ID)), Integer.parseInt(data.get(Z_TAG_ID))),
                    getColor(data)));
            lines.addAll(getExtraToolTips(data));
        }
        return lines;
    }
    
    @Override
    public CompoundTag saveToNBT(FireEffectNBTDataInterface data)
    {
        CompoundTag tags = super.saveToNBT(data);
        
        tags.putString(DIM_TAG_ID, data.get(DIM_TAG_ID));
        tags.putString(X_TAG_ID, data.get(X_TAG_ID));
        tags.putString(Y_TAG_ID, data.get(Y_TAG_ID));
        tags.putString(Z_TAG_ID, data.get(Z_TAG_ID));
        return tags;
    }
    
    @Override
    public FireEffectNBTDataInterface readFromNBT(CompoundTag tags)
    {
        FireEffectNBTDataInterface data = super.readFromNBT(tags);
        data.set(DIM_TAG_ID, tags.getString(DIM_TAG_ID));
        data.set(X_TAG_ID, tags.getString(X_TAG_ID));
        data.set(Y_TAG_ID, tags.getString(Y_TAG_ID));
        data.set(Z_TAG_ID, tags.getString(Z_TAG_ID));
        return data;
    }
    
    @Override
    public void fillItemCategory(NonNullList<ItemStack> items, ItemStack item)
    {
        for(String s : getSubIdList())
        {
            FireEffectNBTDataInterface defaultData = getDefaultData();
            defaultData.setSubType(s);
    
            ItemStack stack = FireEffectHelpers.addMajorEffect(item.copy(), ID, defaultData);
    
            if(!stack.isEmpty())
                items.add(stack);
        }
    }
}
