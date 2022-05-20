package com.dslm.firewood.fireEffectHelper;

import com.dslm.firewood.tooltip.MiddleComponent;
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
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static com.dslm.firewood.config.SpiritualFireBlockEffectConfig.TELEPORT_BASE_DAMAGE;
import static com.dslm.firewood.fireEffectHelper.FireEffectHelpers.colorfulText;

public class TeleportFireEffectHelper extends FireEffectHelperBase
{
    private static final ArrayList<FireEffectHelperInterface> instanceList = new ArrayList<>();
    
    private static final int color = 0x336666;
    public static final String dimTagId = "dim";
    public static final String xTagId = "posX";
    public static final String yTagId = "posY";
    public static final String zTagId = "posZ";
    
    public TeleportFireEffectHelper(String id)
    {
        super(new HashMap<>()
        {{
            put(dimTagId, "overworld");
            put(xTagId, "0");
            put(yTagId, "256");
            put(zTagId, "0");
        }}, id);
        instanceList.add(this);
    }
    
    @Override
    public int getColor(HashMap<String, String> data)
    {
        return color;
    }
    
    public static int getColor()
    {
        return color;
    }
    
    @Override
    public void triggerEffect(HashMap<String, String> data, BlockState state, Level level, BlockPos pos, LivingEntity entity)
    {
        try
        {
            Set<ResourceKey<Level>> levelList = level.getServer().levelKeys();
            for(ResourceKey<Level> levelKey : levelList)
            {
                if(entity.getServer().getLevel(levelKey).dimension().location().getPath().equals(data.get(dimTagId)))
                {
                    entity.changeDimension(entity.getServer().getLevel(levelKey), new ITeleporter()
                    {
                        @Override
                        public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity)
                        {
                            entity = repositionEntity.apply(false);
                            entity.teleportTo(Integer.parseInt(data.get(xTagId)) + 0.5, Integer.parseInt(data.get(yTagId)), Integer.parseInt(data.get(zTagId)) + 0.5);
                            return entity;
                        }
                    });
                    return;
                }
            }
        } catch(Exception e)
        {
            //LOGGER.error(e.toString());
        }
    }
    
    @Override
    public float getDamage()
    {
        return TELEPORT_BASE_DAMAGE.get().floatValue();
    }
    
    @Override
    public ArrayList<Component> getToolTips(HashMap<String, String> data, boolean extended)
    {
        ArrayList<Component> lines = new ArrayList<>();
        MiddleComponent mainLine = (MiddleComponent) colorfulText(
                new MiddleComponent("tooltip.firewood.tinder_item.major_effect." + data.get("type")), color);
        lines.add(mainLine);
        if(extended)
        {
            lines.add(colorfulText(
                    new TranslatableComponent("tooltip.firewood.tinder_item.major_effect." + data.get("type") + ".extend.1",
                            data.get(dimTagId), Integer.parseInt(data.get(xTagId)), Integer.parseInt(data.get(yTagId)), Integer.parseInt(data.get(zTagId))),
                    color));
        }
        mainLine.setDamage(getDamage());
        return lines;
    }
    
    @Override
    public CompoundTag saveToNBT(HashMap<String, String> data)
    {
        CompoundTag tags = new CompoundTag();
        tags.putString("type", id);
        tags.putString(dimTagId, data.get(dimTagId));
        tags.putString(xTagId, data.get(xTagId));
        tags.putString(yTagId, data.get(yTagId));
        tags.putString(zTagId, data.get(zTagId));
        return tags;
    }
    
    @Override
    public HashMap<String, String> readFromNBT(CompoundTag tags)
    {
        HashMap<String, String> data = new HashMap<>();
        data.put("type", id);
        data.put(dimTagId, tags.getString(dimTagId));
        data.put(xTagId, tags.getString(xTagId));
        data.put(yTagId, tags.getString(yTagId));
        data.put(zTagId, tags.getString(zTagId));
        return data;
    }
    
    @Override
    public void fillItemCategory(NonNullList<ItemStack> items, ItemStack item)
    {
        items.add(FireEffectHelpers.addMajorEffect(item.copy(), id, defaultData));
    }
    
    public static List<FireEffectHelperInterface> getInstanceList()
    {
        return instanceList;
    }
}
