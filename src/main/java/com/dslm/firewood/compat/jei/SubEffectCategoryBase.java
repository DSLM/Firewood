package com.dslm.firewood.compat.jei;

import com.dslm.firewood.Register;
import com.dslm.firewood.fireeffecthelper.flesh.FireEffectHelpers;
import com.dslm.firewood.fireeffecthelper.flesh.data.FireEffectNBTDataInterface;
import com.dslm.firewood.recipe.SubRecipeBase;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collections;
import java.util.List;

import static com.dslm.firewood.util.StaticValue.MOD_ID;

abstract class SubEffectCategoryBase<T extends SubRecipeBase> implements IRecipeCategory<T>
{
    protected final IDrawable icon;
    protected final String type;
    protected final String subType;
    protected final ResourceLocation uid;
    protected static final ResourceLocation backPic = new ResourceLocation(MOD_ID, "textures/gui/transmute_block_jei_gui.png");
    
    public SubEffectCategoryBase(IGuiHelper guiHelper, String type, String subType)
    {
        this.type = type;
        this.subType = subType;
        FireEffectNBTDataInterface defaultData = FireEffectHelpers.getMajorHelperByType(type).getDefaultData();
        defaultData.setSubType(subType);
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                FireEffectHelpers.addMajorEffects(
                        new ItemStack(Register.TINDER_ITEM.get()), Collections.singletonList(defaultData)));
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
    
    public static void renderBlock(PoseStack poseStack, BlockState block, float x, float y, float z, float rotate, float scale)
    {
        Minecraft mc = Minecraft.getInstance();
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        poseStack.scale(-scale, -scale, -scale);
        poseStack.translate(-0.5F, -0.5F, 0);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(-30F));
        poseStack.translate(0.5F, 0, -0.5F);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(rotate));
        poseStack.translate(-0.5F, 0, 0.5F);
    
        poseStack.pushPose();
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        poseStack.translate(0, 0, -1);
    
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        mc.getBlockRenderer().renderSingleBlock(block, poseStack, bufferSource, 15728880, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
        bufferSource.endBatch();
        poseStack.popPose();
    
        poseStack.popPose();
    }
    
    public static void drawBlockState(CompoundTag properties, PoseStack stack, int x, int y)
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
    
    @Override
    public ResourceLocation getRegistryName(T recipe)
    {
        return IRecipeCategory.super.getRegistryName(recipe);
    }
}
