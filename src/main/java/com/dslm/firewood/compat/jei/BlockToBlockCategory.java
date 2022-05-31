package com.dslm.firewood.compat.jei;

import com.dslm.firewood.Register;
import com.dslm.firewood.recipe.BlockToBlockRecipe;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

public class BlockToBlockCategory extends SubEffectCategoryBase<BlockToBlockRecipe>
{
    protected final RecipeType<BlockToBlockRecipe> recipeType;
    protected final IDrawable back;
    static int w = 100;
    static int h = 50;
    
    public BlockToBlockCategory(IGuiHelper guiHelper, String subType)
    {
        super(guiHelper, "block_to_block", subType);
        back = guiHelper.createDrawable(backPic, 128 - w - 24 / 2, 0, w * 2 + 24, h * 2 + 17);
        recipeType = RecipeType.create(
                Register.BLOCK_TO_BLOCK_RECIPE_SERIALIZER.getId().getNamespace(),
                Register.BLOCK_TO_BLOCK_RECIPE_SERIALIZER.getId().getPath() + ".block_to_block." + subType,
                BlockToBlockRecipe.class);
    }
    
    @Override
    public RecipeType<BlockToBlockRecipe> getRecipeType()
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
    public void setRecipe(IRecipeLayoutBuilder builder, BlockToBlockRecipe recipe, IFocusGroup focuses)
    {
        builder.addSlot(RecipeIngredientRole.INPUT,
                back.getWidth() / 4 - 9,
                0)
                .addItemStacks(recipe.getJEIInputs());
        
        
        builder.addSlot(RecipeIngredientRole.OUTPUT,
                back.getWidth() / 4 * 3 - 9,
                0)
                .addItemStack(new ItemStack(recipe.getTargetBlock().getBlock().asItem()));
    }
    
    @Override
    public void draw(BlockToBlockRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY)
    {
        drawBlockState(recipe.getIngBlock().getProperties(), stack, 18, 0);
        var targetNBT = NbtUtils.writeBlockState(recipe.getTargetBlock());
        if(targetNBT.contains("Properties"))
        {
            drawBlockState(targetNBT.getCompound("Properties"), stack, 18, w + 24 / 2);
        }
    }
    
    @Override
    public RegistryObject getSerializer()
    {
        return Register.BLOCK_TO_BLOCK_RECIPE_SERIALIZER;
    }
    
    @Override
    public Class<? extends BlockToBlockRecipe> getRecipeClass()
    {
        return BlockToBlockRecipe.class;
    }
}
