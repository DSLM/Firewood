package com.dslm.firewood.util;

import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.mojang.math.Transformation;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;
import oshi.util.tuples.Quartet;

import java.util.List;

// copy from https://github.com/McJty/TutorialV3
public class ClientTools
{
    
    private static void putVertex(BakedQuadBuilder builder, Vector3f normal, Vector4f vector,
                                  float u, float v, TextureAtlasSprite sprite)
    {
        
        var elements = builder.getVertexFormat().getElements().asList();
        for(int j = 0; j < elements.size(); j++)
        {
            var e = elements.get(j);
            switch(e.getUsage())
            {
                case POSITION -> builder.put(j, vector.x(), vector.y(), vector.z(), 1.0f);
                case COLOR -> builder.put(j, 1.0f, 1.0f, 1.0f, 1.0f);
                case UV -> putVertexUV(builder, u, v, sprite, j, e);
                case NORMAL -> builder.put(j, normal.x(), normal.y(), normal.z());
                default -> builder.put(j);
            }
        }
    }
    
    private static void putVertexUV(BakedQuadBuilder builder, float u, float v, TextureAtlasSprite sprite, int j, VertexFormatElement e)
    {
        switch(e.getIndex())
        {
            case 0 -> builder.put(j, sprite.getU(u), sprite.getV(v));
            case 2 -> builder.put(j, (short) 0, (short) 0);
            default -> builder.put(j);
        }
    }
    
    
    public static BakedQuad createQuad(Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, Transformation rotation, TextureAtlasSprite sprite, Quartet<Float, Float, Float, Float> spriteSize, int tint)
    {
        return createQuad(v1, v2, v3, v4, rotation, sprite, new Octagon(
                spriteSize.getB(), spriteSize.getA(),
                spriteSize.getD(), spriteSize.getA(),
                spriteSize.getD(), spriteSize.getC(),
                spriteSize.getB(), spriteSize.getC()), tint);
    }
    
    public static BakedQuad createReverseQuad(Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, Transformation rotation, TextureAtlasSprite sprite, Quartet<Float, Float, Float, Float> spriteSize, int tint)
    {
        return createQuad(v1, v2, v3, v4, rotation, sprite, new Octagon(
                spriteSize.getB(), spriteSize.getA(),
                spriteSize.getB(), spriteSize.getC(),
                spriteSize.getD(), spriteSize.getC(),
                spriteSize.getD(), spriteSize.getA()), tint);
    }
    
    public static BakedQuad createQuad(Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, Transformation rotation, TextureAtlasSprite sprite, Octagon spriteSize, int tint)
    {
        // Quartet: float spriteWStart, float spriteHStart, float spriteWEnd, float spriteHEnd
        Vector3f normal = v3.copy();
        normal.sub(v2);
        Vector3f temp = v1.copy();
        temp.sub(v2);
        normal.cross(temp);
        normal.normalize();
        
        int tw = sprite.getWidth();
        int th = sprite.getHeight();
        
        rotation = rotation.blockCenterToCorner();
        rotation.transformNormal(normal);
        
        Vector4f vv1 = new Vector4f(v1);
        rotation.transformPosition(vv1);
        Vector4f vv2 = new Vector4f(v2);
        rotation.transformPosition(vv2);
        Vector4f vv3 = new Vector4f(v3);
        rotation.transformPosition(vv3);
        Vector4f vv4 = new Vector4f(v4);
        rotation.transformPosition(vv4);
        
        var builder = new BakedQuadBuilder(sprite);
        builder.setQuadTint(tint);
        builder.setQuadOrientation(Direction.getNearest(normal.x(), normal.y(), normal.z()));
        putVertex(builder, normal, vv1, spriteSize.A() * tw, spriteSize.B() * th, sprite);
        putVertex(builder, normal, vv2, spriteSize.C() * tw, spriteSize.D() * th, sprite);
        putVertex(builder, normal, vv3, spriteSize.E() * tw, spriteSize.F() * th, sprite);
        putVertex(builder, normal, vv4, spriteSize.G() * tw, spriteSize.H() * th, sprite);
        return builder.build();
    }
    
    public static BakedQuad createCenterRectBottomQuad(float a, float b, float h, Transformation rotation, TextureAtlasSprite sprite, int tint)
    {
        return createRectBottomQuad(a, b, a, b, h, rotation, sprite, tint);
    }
    
    public static BakedQuad createRectBottomQuad(float a, float a1, float b, float b1, float h, Transformation rotation, TextureAtlasSprite sprite, int tint)
    {
        return createReverseQuad(v(a, h, b), v(a1, h, b), v(a1, h, b1), v(a, h, b1), rotation, sprite, q(a, b, a1, b1), tint);
    }
    
    public static BakedQuad createCenterRectTopQuad(float a, float b, float h, Transformation rotation, TextureAtlasSprite sprite, int tint)
    {
        return createRectTopQuad(a, b, a, b, h, rotation, sprite, tint);
    }
    
    public static BakedQuad createRectTopQuad(float a, float a1, float b, float b1, float h, Transformation rotation, TextureAtlasSprite sprite, int tint)
    {
        return createQuad(v(a, h, b), v(a, h, b1), v(a1, h, b1), v(a1, h, b), rotation, sprite, q(a, b, a1, b1), tint);
    }
    
    public static List<BakedQuad> createCenterRectSideQuad(float a, float b, float s, float h, Transformation rotation, TextureAtlasSprite sprite, int tint)
    {
        return createRectSideQuad(a, b, a, b, s, h, rotation, sprite, tint);
    }
    
    public static List<BakedQuad> createRectSideQuad(float a, float a1, float b, float b1, float s, float h, Transformation rotation, TextureAtlasSprite sprite, int tint)
    {
        return List.of(
                createQuad(v(a, s, b), v(a, s, b1), v(a, h, b1), v(a, h, b), rotation, sprite, q(s, b, h, b1), tint),
                createQuad(v(a, s, b1), v(a1, s, b1), v(a1, h, b1), v(a, h, b1), rotation, sprite, q(s, a, h, a1), tint),
                createReverseQuad(v(a1, s, b), v(a1, h, b), v(a1, h, b1), v(a1, s, b1), rotation, sprite, q(s, 1 - b, h, 1 - b1), tint),
                createReverseQuad(v(a, s, b), v(a, h, b), v(a1, h, b), v(a1, s, b), rotation, sprite, q(s, 1 - a, h, 1 - a1), tint)
        );
    }
    
    
    public static List<BakedQuad> createCenterTrapezoidSideQuad(float a, float b, float c, float d, float s, float h, Transformation rotation, TextureAtlasSprite sprite, int tint)
    {
        return createTrapezoidSideQuad(a, b, a, b, c, d, c, d, s, h, rotation, sprite, tint);
    }
    
    public static List<BakedQuad> createTrapezoidSideQuad(float a, float a1, float b, float b1, float c, float c1, float d, float d1, float s, float h, Transformation rotation, TextureAtlasSprite sprite, int tint)
    {
        return List.of(
                createQuad(v(a, s, b), v(a, s, b1), v(c, h, d1), v(c, h, d), rotation, sprite, new Octagon(
                        b, 1 - s,
                        b1, 1 - s,
                        d1, 1 - h,
                        d, 1 - h), tint),
                createQuad(v(a, s, b1), v(a1, s, b1), v(c1, h, d1), v(c, h, d1), rotation, sprite, new Octagon(
                        a, 1 - s,
                        a1, 1 - s,
                        c1, 1 - h,
                        c, 1 - h), tint),
                createQuad(v(a1, s, b), v(c1, h, d), v(c1, h, d1), v(a1, s, b1), rotation, sprite, new Octagon(
                        1 - b, 1 - s,
                        1 - d, 1 - h,
                        1 - d1, 1 - h,
                        1 - b1, 1 - s), tint),
                createQuad(v(a, s, b), v(c, h, d), v(c1, h, d), v(a1, s, b), rotation, sprite, new Octagon(
                        1 - a, 1 - s,
                        1 - c, 1 - h,
                        1 - c1, 1 - h,
                        1 - a1, 1 - s), tint)
        );
    }
    
    public static Vector3f v(float x, float y, float z)
    {
        return new Vector3f(x, y, z);
    }
    
    public static Quartet<Float, Float, Float, Float> q(float a, float b, float c, float d)
    {
        return new Quartet<>(1 - a, b, 1 - c, d);
    }
    
    public record Octagon(float A, float B, float C, float D, float E, float F, float G, float H)
    {
    }
}
