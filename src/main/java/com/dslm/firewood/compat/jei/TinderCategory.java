package com.dslm.firewood.compat.jei;

import com.dslm.firewood.Firewood;
import com.dslm.firewood.Register;
import com.dslm.firewood.menu.SpiritualCampfireBlockMenu;
import com.dslm.firewood.recipe.TinderRecipe;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class TinderCategory implements IRecipeCategory<TinderRecipe>
{
    public static final RecipeType<TinderRecipe> TYPE = RecipeType.create(
            Register.TINDER_RECIPE_SERIALIZER.getId().getNamespace(),
            Register.TINDER_RECIPE_SERIALIZER.getId().getPath(),
            TinderRecipe.class);
    private final ResourceLocation backPic = new ResourceLocation(Firewood.MOD_ID, "textures/gui/spiritual_campfire_block_jei_gui.png");
    private final IDrawable back;
    private final IDrawable icon;
    public final Pair<Integer, Integer> startPoint = Pair.of(6, 6);
    
    protected TinderCategory(IGuiHelper guiHelper)
    {
        back = guiHelper.createDrawable(backPic, 0, 0, 178, 136);
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Register.SPIRITUAL_CAMPFIRE_ITEM.get()));
    }
    
    @Override
    public RecipeType<TinderRecipe> getRecipeType()
    {
        return TYPE;
    }
    
    @Override
    public Component getTitle()
    {
        return new TranslatableComponent("block.firewood.spiritual_campfire_block");
    }
    
    @Override
    public IDrawable getBackground()
    {
        return back;
    }
    
    @Override
    public IDrawable getIcon()
    {
        return icon;
    }
    
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, TinderRecipe recipe, IFocusGroup focuses)
    {
        //Inputs
        List<Either<List<ItemStack>, Ingredient>> inputs = recipe.getJEIInputs();
        for(int i = 0; i < inputs.size(); i++)
        {
            IRecipeSlotBuilder slot = builder.addSlot(RecipeIngredientRole.INPUT,
                    startPoint.getLeft() + SpiritualCampfireBlockMenu.slotsPos.get(i).getLeft(),
                    startPoint.getRight() + SpiritualCampfireBlockMenu.slotsPos.get(i).getRight());
            if(inputs.get(i).left().isPresent())
            {
                slot.addItemStacks(inputs.get(i).left().get());
            }
            else
            {
                slot.addIngredients(inputs.get(i).right().get());
            }
            
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT,
                startPoint.getLeft() + SpiritualCampfireBlockMenu.slotsPos.get(0).getLeft() + 101,
                startPoint.getRight() + SpiritualCampfireBlockMenu.slotsPos.get(0).getRight())
                .addItemStacks(recipe.getJEIResult());
        
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addIngredients(recipe.getTinder());
    }
    
    public static Boolean isInArrow(double x, double y)
    {
        return x >= 137 && x <= 160 && y >= 60 && y <= 77;
    }
    
    @Override
    public void draw(TinderRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY)
    {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
    }
    
    @Override
    public List<Component> getTooltipStrings(TinderRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY)
    {
        if(isInArrow(mouseX, mouseY))
        {
            return Arrays.asList(new TranslatableComponent("tooltip.firewood.recipe.progress", recipe.getProcess()),
                    new TranslatableComponent("tooltip.firewood.recipe.chance", recipe.getChance()),
                    new TranslatableComponent("tooltip.firewood.recipe.damage", recipe.getDamage()),
                    new TranslatableComponent("tooltip.firewood.recipe.cooldown", recipe.getCooldown() / 20, recipe.getCooldown()),
                    new TranslatableComponent("tooltip.firewood.recipe.minhealth", recipe.getMinHealth()));
        }
        return IRecipeCategory.super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
    }
    
    @Override
    public boolean handleInput(TinderRecipe recipe, double mouseX, double mouseY, InputConstants.Key input)
    {
        return IRecipeCategory.super.handleInput(recipe, mouseX, mouseY, input);
    }
    
    @Override
    public boolean isHandled(TinderRecipe recipe)
    {
        return IRecipeCategory.super.isHandled(recipe);
    }
    
    @Override
    public @Nullable ResourceLocation getRegistryName(TinderRecipe recipe)
    {
        return recipe.getId();
    }
    
    @SuppressWarnings("removal")
    @Override
    public ResourceLocation getUid()
    {
        return new ResourceLocation(
                Register.TINDER_RECIPE_SERIALIZER.getId().getNamespace(),
                Register.TINDER_RECIPE_SERIALIZER.getId().getPath());
    }
    
    @SuppressWarnings("removal")
    @Override
    public Class<? extends TinderRecipe> getRecipeClass()
    {
        return TinderRecipe.class;
    }
}
