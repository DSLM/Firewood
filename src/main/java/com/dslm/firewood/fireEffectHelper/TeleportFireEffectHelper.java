package com.dslm.firewood.fireEffectHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.ITeleporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Function;

import static com.dslm.firewood.config.SpiritualFireBlockEffectConfig.Teleport_BASE_DAMAGE;
import static com.dslm.firewood.fireEffectHelper.FireEffectHelpers.colorfulText;

public class TeleportFireEffectHelper extends FireEffectHelperBase
{
    public static final int color = 0x336666;
    
    public TeleportFireEffectHelper()
    {
        super(new HashMap<>()
        {{
            put("dim", "");
            put("posX", "0");
            put("posY", "256");
            put("posZ", "0");
        }});
    }
    
    @Override
    public int getColor(HashMap<String, String> data)
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
                if(entity.getServer().getLevel(levelKey).dimension().location().getPath().equals(data.get("dim")))
                {
                    entity.changeDimension(entity.getServer().getLevel(levelKey), new ITeleporter()
                    {
                        @Override
                        public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity)
                        {
                            entity = repositionEntity.apply(false);
                            entity.teleportTo(Integer.parseInt(data.get("posX")), Integer.parseInt(data.get("posY")), Integer.parseInt(data.get("posZ")));
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
        return Teleport_BASE_DAMAGE.get().floatValue();
    }
    
    @Override
    public ArrayList<Component> getToolTips(HashMap<String, String> data, boolean extend)
    {
        ArrayList<Component> lines = new ArrayList<>();
        lines.add(colorfulText(
                new TranslatableComponent("tooltip.firewood.tinder_item.major_effect." + data.get("type"),
                        data.get("dim"), data.get("posX"), data.get("posY"), data.get("posZ")),
                color));
        return lines;
    }
    
    @Override
    public boolean isSameNBT(CompoundTag first, CompoundTag second)
    {
        return first.getString("type").equals(second.getString("type"));
    }
    
    @Override
    public CompoundTag saveToNBT(HashMap<String, String> data)
    {
        CompoundTag tags = new CompoundTag();
        tags.putString("type", "teleport");
        tags.putString("dim", data.get("dim"));
        tags.putString("posX", data.get("posX"));
        tags.putString("posY", data.get("posY"));
        tags.putString("posZ", data.get("posZ"));
        return tags;
    }
    
    @Override
    public HashMap<String, String> readFromNBT(CompoundTag tags)
    {
        HashMap<String, String> data = new HashMap<>();
        data.put("type", "teleport");
        data.put("dim", tags.getString("dim"));
        data.put("posX", tags.getString("posX"));
        data.put("posY", tags.getString("posY"));
        data.put("posZ", tags.getString("posZ"));
        return data;
    }
}
