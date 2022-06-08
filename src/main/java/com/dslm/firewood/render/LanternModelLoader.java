package com.dslm.firewood.render;

import com.dslm.firewood.util.StaticValue;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.geometry.IModelGeometry;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class LanternModelLoader implements IModelLoader<LanternModelLoader.LanternModelGeometry>
{
    public static final ResourceLocation LANTERN_LOADER = new ResourceLocation(StaticValue.MOD_ID, "lantern_loader");
    
    public static final ResourceLocation GENERATOR_SHELL_SIDE = new ResourceLocation(StaticValue.MOD_ID, "block/lantern_shell");
    public static final ResourceLocation GENERATOR_SHELL_BOTTOM = new ResourceLocation(StaticValue.MOD_ID, "block/lantern_shell");
    public static final ResourceLocation GENERATOR_SHELL_GLASS = new ResourceLocation(StaticValue.MOD_ID, "block/lantern_glass");
    
    public static final Material MATERIAL_SHELL_SIDE = ForgeHooksClient.getBlockMaterial(GENERATOR_SHELL_SIDE);
    public static final Material MATERIAL_SHELL_BOTTOM = ForgeHooksClient.getBlockMaterial(GENERATOR_SHELL_BOTTOM);
    public static final Material MATERIAL_SHELL_GLASS = ForgeHooksClient.getBlockMaterial(GENERATOR_SHELL_GLASS);
    
    @Override
    public LanternModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents)
    {
        return new LanternModelGeometry();
    }
    
    @Override
    public void onResourceManagerReload(ResourceManager pResourceManager)
    {
    
    }
    
    
    public static class LanternModelGeometry implements IModelGeometry<LanternModelGeometry>
    {
        
        @Override
        public BakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation)
        {
            return new LanternBakedModel(modelTransform, spriteGetter, overrides, owner.getCameraTransforms());
        }
        
        @Override
        public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors)
        {
            return List.of(MATERIAL_SHELL_SIDE, MATERIAL_SHELL_BOTTOM, MATERIAL_SHELL_GLASS);
        }
    }
}
