package com.dslm.firewood.compat.jei;

import com.dslm.firewood.Register;
import com.dslm.firewood.subtype.FireEffectSubTypeBase;
import com.dslm.firewood.subtype.FireEffectSubTypeManager;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static com.dslm.firewood.datagen.LanguageUtil.JEI_SUB_INFO;
import static com.dslm.firewood.util.StaticValue.MOD_ID;

public class SubEffectInfoCategory implements IRecipeCategory<FireEffectSubTypeBase>
{
    public static final RecipeType<FireEffectSubTypeBase> TYPE = RecipeType.create(
            MOD_ID,
            FireEffectSubTypeManager.LOCATION,
            FireEffectSubTypeBase.class);
    
    protected final IDrawable icon;
    protected final ResourceLocation uid;
    protected final IDrawable backPic;
    protected final List<Item> tinderItems;
    public static final int WIDTH = 150;
    public static final int HEIGHT = 150;
    public static final TranslatableComponent TITLE = new TranslatableComponent(JEI_SUB_INFO + "title");
    
    public SubEffectInfoCategory(IGuiHelper guiHelper, List<Item> tinderItems)
    {
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Register.LANTERN_ITEM.get()));
        uid = new ResourceLocation(
                MOD_ID,
                FireEffectSubTypeManager.LOCATION);
        backPic = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
        this.tinderItems = tinderItems;
    }
    
    @Override
    public Component getTitle()
    {
        return TITLE;
    }
    
    @Override
    public IDrawable getBackground()
    {
        return backPic;
    }
    
    @Override
    public IDrawable getIcon()
    {
        return icon;
    }
    
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FireEffectSubTypeBase recipe, IFocusGroup focuses)
    {
        recipe.getJEIHandler().setRecipe(recipe, builder, focuses, tinderItems, WIDTH, HEIGHT);
    }
    
    @Override
    public void draw(FireEffectSubTypeBase recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY)
    {
        recipe.getJEIHandler().draw(recipe, recipeSlotsView, stack, mouseX, mouseY, WIDTH, HEIGHT);
    }
    
    @SuppressWarnings("removal")
    @Override
    public ResourceLocation getUid()
    {
        return uid;
    }
    
    @SuppressWarnings("removal")
    @Override
    public Class<? extends FireEffectSubTypeBase> getRecipeClass()
    {
        return FireEffectSubTypeBase.class;
    }
}
