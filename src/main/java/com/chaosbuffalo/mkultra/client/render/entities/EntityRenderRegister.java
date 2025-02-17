package com.chaosbuffalo.mkultra.client.render.entities;

import com.chaosbuffalo.mkultra.entities.EntityMKAreaEffect;
import com.chaosbuffalo.mkultra.entities.projectiles.*;
import com.chaosbuffalo.mkultra.init.ModItems;
import net.minecraft.client.renderer.entity.Render;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

/**
 * Created by Jacob on 7/15/2016.
 */
public class EntityRenderRegister {

    public static void registerEntityRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityMKAreaEffect.class, new RenderMKAreaEffect());
        RenderingRegistry.registerEntityRenderingHandler(EntityDrownProjectile.class,
                new RenderProjectile(ModItems.drownProjectile, 1.0f));
        RenderingRegistry.registerEntityRenderingHandler(EntityGeyserProjectile.class,
                new RenderProjectile(ModItems.geyserProjectile, 1.0f));
        RenderingRegistry.registerEntityRenderingHandler(EntityBallLightningProjectile.class,
                new RenderProjectile(ModItems.ballLightning, 1.0f));
        RenderingRegistry.registerEntityRenderingHandler(EntityDualityRuneProjectile.class,
                new RenderProjectile(ModItems.duality_rune_projectile, 1.0f));
        RenderingRegistry.registerEntityRenderingHandler(EntityWhirlpoolProjectile.class,
                new RenderProjectile(ModItems.whirlpool_projectile, 1.0f));
        RenderingRegistry.registerEntityRenderingHandler(EntityFlameBladeProjectile.class,
                new RenderProjectile(ModItems.flame_blade_projectile, 1.0f));
        RenderingRegistry.registerEntityRenderingHandler(EntityFairyFireProjectile.class,
                new RenderProjectile(ModItems.fairy_fire_projectile, 1.0f));
        RenderingRegistry.registerEntityRenderingHandler(EntityCleansingSeedProjectile.class,
                new RenderProjectile(ModItems.cleansing_seed_projectile, 1.0f));
        RenderingRegistry.registerEntityRenderingHandler(EntitySpiritBombProjectile.class,
                new RenderProjectile(ModItems.spirit_bomb_projectile, 1.0f));
        RenderingRegistry.registerEntityRenderingHandler(EntityMobFireballProjectile.class,
                new RenderProjectile(ModItems.mob_fireball_projectile, 1.0f));
        RenderingRegistry.registerEntityRenderingHandler(EntityGraspingRootsProjectile.class,
                new RenderProjectile(ModItems.grasping_roots_projectile, 1.0f));
    }
}
