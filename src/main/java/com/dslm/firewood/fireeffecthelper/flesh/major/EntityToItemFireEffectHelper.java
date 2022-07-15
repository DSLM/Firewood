package com.dslm.firewood.fireeffecthelper.flesh.major;

import com.dslm.firewood.Register;
import com.dslm.firewood.fireeffecthelper.flesh.base.SubMajorFireEffectHelperBase;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTDataInterface;
import com.dslm.firewood.recipe.EntityToItemRecipe;
import com.dslm.firewood.recipe.FakeEntityTransmuteContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;

public class EntityToItemFireEffectHelper extends SubMajorFireEffectHelperBase
{
    
    public EntityToItemFireEffectHelper(String id)
    {
        super(id, TargetType.LIVING_ENTITY);
    }
    
    @Override
    public void transmuteEntity(FireEffectNBTDataInterface data, Level level, LivingEntity livingEntity, LivingEntity source)
    {
        for(EntityToItemRecipe entityToItemRecipe : level.getRecipeManager().getAllRecipesFor(Register.ENTITY_TO_ITEM_RECIPE_TYPE.get()))
        {
            if(entityToItemRecipe.getRecipeSubType().equals(data.getSubType()))
            {
                FakeEntityTransmuteContainer container = new FakeEntityTransmuteContainer(livingEntity, level);
                if(entityToItemRecipe.matches(container, level))
                {
                    entityToItemRecipe.getItemsList().forEach(itemStack -> {
                        ItemEntity item = new ItemEntity(level,
                                livingEntity.position().x,
                                livingEntity.position().y,
                                livingEntity.position().z,
                                itemStack.copy(),
                                0,
                                0,
                                0);
                        level.addFreshEntity(item);
                    });
                    
                    break;
                }
            }
        }
    }
}
