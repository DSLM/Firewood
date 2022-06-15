package com.dslm.firewood.render;

import com.dslm.firewood.block.entity.LanternBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Transformation;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import static com.dslm.firewood.util.ClientTools.*;

public class LanternBakedModel implements IDynamicBakedModel
{
    private final ModelState modelState;
    private final Function<Material, TextureAtlasSprite> spriteGetter;
    private final List<BakedQuad> quadCache;
    private final List<BakedQuad> quadCacheDown;
    private final List<BakedQuad> quadGlass;
    private final ItemOverrides overrides;
    private final ItemTransforms itemTransforms;
    private final Quaternion FIXED_ROTATION = Vector3f.YP.rotationDegrees(90);
    
    public LanternBakedModel(ModelState modelState, Function<Material, TextureAtlasSprite> spriteGetter, ItemOverrides overrides, ItemTransforms itemTransforms, boolean translucent)
    {
        this.modelState = modelState;
        this.spriteGetter = spriteGetter;
        this.overrides = overrides;
        this.itemTransforms = itemTransforms;
        quadCache = generateQuads();
        quadCacheDown = generateQuadsDown();
        quadGlass = translucent ? generateTransGlass() : generateGlass();
    }
    
    @Override
    public boolean usesBlockLight()
    {
        return true;
    }
    
    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData)
    {
        if(side != null)
        {
            return switch(side)
                    {
                        case DOWN -> quadCacheDown;
                        case UP, NORTH, SOUTH, WEST, EAST -> Collections.emptyList();
                    };
        }
        
        
        Integer tempColor = extraData.getData(LanternBlockEntity.COLOR);
        int color = tempColor != null ? tempColor : 0;
        
        ArrayList quads = new ArrayList<>(quadCache);
        
        quads.addAll(quadGlass);
        
        return quads;
    }
    
    @NotNull
    private List<BakedQuad> generateGlass()
    {
        var quads = new ArrayList<BakedQuad>();
        
        Transformation rotation = modelState.getRotation();
        TextureAtlasSprite textureShellGlass = spriteGetter.apply(LanternModelLoader.MATERIAL_SHELL_GLASS);
        
        //glass
        float a = 4f / 16f;
        float b = 12f / 16f;
        float c = 5f / 16f;
        float d = 11f / 16f;
        float s = 4f / 16f;
        float h = 13f / 16f;
        quads.addAll(createCenterTrapezoidSideQuad(a, b, c, d, s, h, rotation, textureShellGlass, 1));
    
        return quads;
    }
    
    @NotNull
    private List<BakedQuad> generateTransGlass()
    {
        var quads = new ArrayList<BakedQuad>();
        
        Transformation rotation = modelState.getRotation();
        TextureAtlasSprite textureShellGlass = spriteGetter.apply(LanternModelLoader.MATERIAL_SHELL_TRANS_GLASS);
        
        //glass
        float a = 4f / 16f;
        float b = 12f / 16f;
        float c = 5f / 16f;
        float d = 11f / 16f;
        float s = 4f / 16f;
        float h = 13f / 16f;
        quads.addAll(createCenterTrapezoidSideQuad(a, b, c, d, s, h, rotation, textureShellGlass, 1));
        
        return quads;
    }
    
    @NotNull
    private List<BakedQuad> generateQuadsDown()
    {
        var quads = new ArrayList<BakedQuad>();
        
        Transformation rotation = modelState.getRotation();
        TextureAtlasSprite textureShellBottom = spriteGetter.apply(LanternModelLoader.MATERIAL_SHELL_BOTTOM);
        
        float a = 5f / 16f;
        float b = 11f / 16f;
        float h = 0;
        quads.add(createCenterRectBottomQuad(a, b, h, rotation, textureShellBottom, -1));
        
        return quads;
    }
    
    @SuppressWarnings("ConstantExpression")
    @NotNull
    private List<BakedQuad> generateQuads()
    {
        var quads = new ArrayList<BakedQuad>();
        
        Transformation rotation = modelState.getRotation();
        
        TextureAtlasSprite textureShellBottom = spriteGetter.apply(LanternModelLoader.MATERIAL_SHELL_BOTTOM);
        TextureAtlasSprite textureShellSide = spriteGetter.apply(LanternModelLoader.MATERIAL_SHELL_SIDE);
        
        float a;
        float a1;
        float b;
        float b1;
        float c;
        float c1;
        float d;
        float d1;
        float s;
        float h;
        
        
        //bottom 1
        a = 5f / 16f;
        b = 11f / 16f;
        s = 0;
        h = 1f / 16f;
        quads.addAll(createCenterRectSideQuad(a, b, s, h, rotation, textureShellSide, -1));
        
        //bottom 2
        a = 3f / 16f;
        b = 13f / 16f;
        s = h;
        h = 2f / 16f;
        quads.addAll(createCenterRectSideQuad(a, b, s, h, rotation, textureShellSide, -1));
        quads.add(createCenterRectBottomQuad(a, b, s, rotation, textureShellBottom, -1));
        quads.add(createCenterRectTopQuad(a, b, h, rotation, textureShellBottom, -1));
        
        //bottom 3
        a = 4f / 16f;
        b = 12f / 16f;
        s = h;
        h = 4f / 16f;
        quads.addAll(createCenterRectSideQuad(a, b, s, h, rotation, textureShellSide, -1));
        quads.add(createCenterRectTopQuad(a, b, h, rotation, textureShellBottom, -1));
        
        
        //side bar 1
        a = 7f / 16f;
        a1 = 9f / 16f;
        b = 2f / 16f;
        b1 = 4f / 16f;
        s = 2f / 16f;
        h = 15f / 16f;
        quads.add(createRectBottomQuad(a, a1, b, b1, s, rotation, textureShellBottom, -1));
        quads.add(createRectTopQuad(a, a1, b, b1, h, rotation, textureShellBottom, -1));
        quads.addAll(createRectSideQuad(a, a1, b, b1, s, h, rotation, textureShellSide, -1));
        
        //side bar 2
        b = 12f / 16f;
        b1 = 14f / 16f;
        quads.add(createRectBottomQuad(a, a1, b, b1, s, rotation, textureShellBottom, -1));
        quads.add(createRectTopQuad(a, a1, b, b1, h, rotation, textureShellBottom, -1));
        quads.addAll(createRectSideQuad(a, a1, b, b1, s, h, rotation, textureShellSide, -1));
        
        
        //top 1
        a = 5f / 16f;
        b = 11f / 16f;
        s = 13f / 16f;
        h = 16f / 16f;
        quads.add(createCenterRectBottomQuad(a, b, s, rotation, textureShellBottom, -1));
        quads.add(createCenterRectTopQuad(a, b, h, rotation, textureShellBottom, -1));
        
        //top 2
        a = 4f / 16f;
        b = 12f / 16f;
        s = 14f / 16f;
        h = 15f / 16f;
        quads.addAll(createCenterRectSideQuad(a, b, s, h, rotation, textureShellSide, -1));
        
        //top side 1
        a = 5f / 16f;
        b = 11f / 16f;
        c = 4f / 16f;
        d = 12f / 16f;
        s = 13f / 16f;
        h = 14f / 16f;
        quads.addAll(createCenterTrapezoidSideQuad(a, b, c, d, s, h, rotation, textureShellSide, -1));
        
        //top side 2
        a = 4f / 16f;
        b = 12f / 16f;
        c = 5f / 16f;
        d = 11f / 16f;
        s = 15f / 16f;
        h = 16f / 16f;
        quads.addAll(createCenterTrapezoidSideQuad(a, b, c, d, s, h, rotation, textureShellSide, -1));
        
        return quads;
    }
    
    @Override
    public BakedModel handlePerspective(ItemTransforms.TransformType cameraTransformType, PoseStack poseStack)
    {
        switch(cameraTransformType)
        {
            case THIRD_PERSON_RIGHT_HAND, THIRD_PERSON_LEFT_HAND -> poseStack.translate(0, -0.2, -0.2);
            case FIRST_PERSON_RIGHT_HAND, FIRST_PERSON_LEFT_HAND -> {
                float size = 1.5f;
                poseStack.scale(size, size, size);
                poseStack.translate(0, 0.15, 0);
            }
            case GROUND -> poseStack.scale(2, 2, 2);
            case FIXED -> {
                poseStack.scale(2, 2, 2);
                poseStack.mulPose(FIXED_ROTATION);
            }
        }
//        poseStack.translate(10, 10, 10);
//        poseStack.scale(10, 10, 10);
        return IDynamicBakedModel.super.handlePerspective(cameraTransformType, poseStack);
    }
    
    @Override
    public boolean useAmbientOcclusion()
    {
        return true;
    }
    
    @Override
    public boolean isGui3d()
    {
        return false;
    }
    
    @Override
    public boolean isCustomRenderer()
    {
        return false;
    }
    
    @Override
    public TextureAtlasSprite getParticleIcon()
    {
        return spriteGetter.apply(LanternModelLoader.MATERIAL_SHELL_SIDE);
    }
    
    @Override
    public ItemOverrides getOverrides()
    {
        return overrides;
    }
    
    @Override
    public ItemTransforms getTransforms()
    {
        return itemTransforms;
    }
}
