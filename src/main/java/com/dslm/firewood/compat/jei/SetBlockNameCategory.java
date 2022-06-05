package com.dslm.firewood.compat.jei;

import com.dslm.firewood.Register;
import com.dslm.firewood.subType.SetBlockNameSubType.SetBlockNameFakeRecipe;
import com.dslm.firewood.util.StaticValue;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraftforge.registries.RegistryObject;

public class SetBlockNameCategory extends SubEffectCategoryBase<SetBlockNameFakeRecipe>
{
    protected final RecipeType<SetBlockNameFakeRecipe> recipeType;
    protected final IDrawable back;
    static int w = 70;
    static int h = 50;
    
    public SetBlockNameCategory(IGuiHelper guiHelper, String subType)
    {
        super(guiHelper, "set_block_name", subType);
        back = guiHelper.createDrawable(backPic, 128 - w - 24 / 2, 0, w * 2 + 24, h * 2 + 17);
        recipeType = RecipeType.create(
                StaticValue.MOD_ID,
                "set_block_name." + subType,
                SetBlockNameFakeRecipe.class);
    }
    
    @Override
    public RecipeType<SetBlockNameFakeRecipe> getRecipeType()
    {
        return recipeType;
    }
    
    @Override
    public IDrawable getBackground()
    {
        return back;
    }
    
    public static Boolean isInArrow(double x, double y)
    {
        return x >= w && x <= w + 24 && y >= 0 && y <= 17;
    }
    
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SetBlockNameFakeRecipe recipe, IFocusGroup focuses)
    {
        builder.addSlot(RecipeIngredientRole.INPUT,
                back.getWidth() / 4 - 9,
                0)
                .addItemStacks(recipe.getJEIInputs());
        
        
        builder.addSlot(RecipeIngredientRole.OUTPUT,
                back.getWidth() / 4 * 3 - 9,
                0)
                .addItemStacks(recipe.getJEIOutputs());
    }
    
    @Override
    public void draw(SetBlockNameFakeRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY)
    {
        Minecraft minecraft = Minecraft.getInstance();
        int textH = 18;
        minecraft.font.draw(stack, recipe.getOrderKey().toString(), 0, textH, 4210752);
        textH += minecraft.font.lineHeight;
        minecraft.font.draw(stack, recipe.getNext().getOrderKey().toString(), 0, textH, 4210752);
        textH += minecraft.font.lineHeight;
//        renderBlock(poseStack, getFarmland(), 30, 30, -10, 20F, 20F);
//        renderBlock(poseStack, getBlockState(), 30, 12, 10, 20F, 20F);
//        mcjty.theoneprobe.rendering.RenderHelper.java.renderEntity
    }
    
    @Override
    public RegistryObject getSerializer()
    {
        return Register.BLOCK_TO_BLOCK_RECIPE_SERIALIZER;
    }
    
    @Override
    public Class<? extends SetBlockNameFakeRecipe> getRecipeClass()
    {
        return SetBlockNameFakeRecipe.class;
    }
}
