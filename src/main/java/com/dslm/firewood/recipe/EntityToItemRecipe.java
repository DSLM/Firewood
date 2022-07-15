package com.dslm.firewood.recipe;

import com.dslm.firewood.Register;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;


public class EntityToItemRecipe implements Recipe<FakeEntityTransmuteContainer>, SubRecipeBase
{
    protected final ResourceLocation id;
    protected final String recipeType;
    protected final String recipeSubType;
    protected final boolean entitiesBlacklist;
    protected final List<String> entitiesList;
    protected final List<ItemStack> itemsList;
    
    public EntityToItemRecipe(ResourceLocation id, String recipeType, String recipeSubType, boolean entitiesBlacklist, List<String> entitiesList, List<ItemStack> itemsList)
    {
        this.id = id;
        this.recipeType = recipeType;
        this.recipeSubType = recipeSubType;
        this.entitiesBlacklist = entitiesBlacklist;
        this.entitiesList = entitiesList;
        this.itemsList = itemsList;
    }
    
    public EntityToItemRecipe(EntityToItemRecipe recipe)
    {
        this.id = recipe.id;
        this.recipeType = recipe.recipeType;
        this.recipeSubType = recipe.recipeSubType;
        this.entitiesBlacklist = recipe.entitiesBlacklist;
        this.entitiesList = recipe.entitiesList;
        this.itemsList = recipe.itemsList;
    }
    
    @Override
    public boolean matches(FakeEntityTransmuteContainer container, Level level)
    {
        return container.getLivingEntity() != null
                && container.getLivingEntity().getType().getRegistryName() != null
                && (entitiesList.contains(container.getLivingEntity().getType().getRegistryName().toString()) != entitiesBlacklist);
    }
    
    @Override
    public ItemStack assemble(FakeEntityTransmuteContainer container)
    {
        return null;
    }
    
    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight)
    {
        return true;
    }
    
    @Override
    public ItemStack getResultItem()
    {
        return null;
    }
    
    @Override
    public ResourceLocation getId()
    {
        return id;
    }
    
    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return Register.ENTITY_TO_ITEM_RECIPE_SERIALIZER.get();
    }
    
    @Override
    public RecipeType<?> getType()
    {
        return Register.ENTITY_TO_ITEM_RECIPE_TYPE.get();
    }
    
    public String getRecipeType()
    {
        return recipeType;
    }
    
    public String getRecipeSubType()
    {
        return recipeSubType;
    }
    
    public boolean isEntitiesBlacklist()
    {
        return entitiesBlacklist;
    }
    
    public List<String> getEntitiesList()
    {
        return entitiesList;
    }
    
    public List<ItemStack> getItemsList()
    {
        return itemsList;
    }
}