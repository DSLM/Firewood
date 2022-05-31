package com.dslm.firewood.compat.jei;

import com.dslm.firewood.Firewood;
import com.dslm.firewood.Register;
import com.dslm.firewood.fireEffectHelper.flesh.FireEffectHelpers;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

abstract class SubEffectCategoryBase<T extends Recipe<?>> implements IRecipeCategory<T>
{
    protected final IDrawable icon;
    protected final String type;
    protected final String subType;
    protected final ResourceLocation uid;
    protected static final ResourceLocation backPic = new ResourceLocation(Firewood.MOD_ID, "textures/gui/transmute_block_jei_gui.png");
    
    public SubEffectCategoryBase(IGuiHelper guiHelper, String type, String subType)
    {
        this.type = type;
        this.subType = subType;
        FireEffectNBTData defaultData = FireEffectHelpers.getMajorHelperByType(type).getDefaultData();
        defaultData.put("subType", subType);
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                FireEffectHelpers.addMajorEffects(
                        new ItemStack(Register.TINDER_ITEM.get()), Arrays.asList(defaultData)));
        uid = new ResourceLocation(
                getSerializer().getId().getNamespace(),
                "%1$s.%2$s.%3$s".formatted(
                        getSerializer().getId().getPath(),
                        type,
                        subType));
    }
    
    @Override
    public Component getTitle()
    {
        return new TranslatableComponent("tooltip.firewood.recipe.title.fire_effect_sub",
                new TranslatableComponent("tooltip.firewood.tinder_item.major_effect.%s".formatted(type)),
                new TranslatableComponent("tooltip.firewood.tinder_item.major_effect.%1$s.%2$s".formatted(type, subType)));
    }
    
    @Override
    public IDrawable getIcon()
    {
        return icon;
    }
    
    @SuppressWarnings("removal")
    @Override
    public ResourceLocation getUid()
    {
        return uid;
    }
    
    abstract public RegistryObject getSerializer();
    
    @Override
    public IDrawable getBackground()
    {
        return null;
    }
    
    @SuppressWarnings("removal")
    @Override
    abstract public Class<? extends T> getRecipeClass();
    
    @Override
    public RecipeType<T> getRecipeType()
    {
        return IRecipeCategory.super.getRecipeType();
    }
    
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses)
    {
        IRecipeCategory.super.setRecipe(builder, recipe, focuses);
    }
    
    @Override
    public void draw(T recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY)
    {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
    }
    
    @Override
    public List<Component> getTooltipStrings(T recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY)
    {
        return IRecipeCategory.super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
    }
    
    @Override
    public boolean handleInput(T recipe, double mouseX, double mouseY, InputConstants.Key input)
    {
        return IRecipeCategory.super.handleInput(recipe, mouseX, mouseY, input);
    }
    
    @Override
    public boolean isHandled(T recipe)
    {
        return IRecipeCategory.super.isHandled(recipe);
    }
    
    @Override
    public @Nullable ResourceLocation getRegistryName(T recipe)
    {
        return IRecipeCategory.super.getRegistryName(recipe);
    }
    
    
    public void drawBlockState(CompoundTag properties, PoseStack stack, int x, int y)
    {
        Minecraft minecraft = Minecraft.getInstance();
        int textH = x;
        if(properties.size() > 0)
        {
            minecraft.font.draw(stack, new TranslatableComponent("tooltip.firewood.recipe.block_state"), y, textH, 4210752);
            textH += minecraft.font.lineHeight;
            for(String key : properties.getAllKeys())
            {
                minecraft.font.draw(stack, new TranslatableComponent("tooltip.firewood.recipe.block_state_line",
                        key, properties.get(key).getAsString()), y, textH, 4210752);
                textH += minecraft.font.lineHeight;
            }
        }
    }
}
