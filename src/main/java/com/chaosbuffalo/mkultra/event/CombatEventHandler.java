package com.chaosbuffalo.mkultra.event;

import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.effects.SpellTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber
public class CombatEventHandler {

    private static float MAX_CRIT_MESSAGE_DISTANCE = 50.0f;


    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        EntityLivingBase livingTarget = event.getEntityLiving();
        if (livingTarget.world.isRemote)
            return;

        DamageSource source = event.getSource();
        Entity trueSource = source.getTrueSource();
        if (source == DamageSource.FALL) { // TODO: maybe just use LivingFallEvent?
            SpellTriggers.FALL.onLivingFall(event, source, livingTarget);
        }

        // Player is the source
        if (trueSource instanceof EntityPlayerMP) {
            EntityPlayerMP playerSource = (EntityPlayerMP) trueSource;
            IPlayerData sourceData = MKUPlayerData.get(playerSource);
            if (sourceData == null) {
                return;
            }
            SpellTriggers.PLAYER_HURT_ENTITY.onPlayerHurtEntity(event, source, livingTarget, playerSource, sourceData);
        }

        // Player is the victim
        if (livingTarget instanceof EntityPlayerMP) {
            IPlayerData targetData = MKUPlayerData.get((EntityPlayerMP) livingTarget);
            if (targetData == null) {
                return;
            }

            SpellTriggers.ENTITY_HURT_PLAYER.onEntityHurtPlayer(event, source, (EntityPlayer) livingTarget, targetData);
        }
    }

    @SubscribeEvent
    public static void onLivingAttackEvent(LivingAttackEvent event) {
        Entity target = event.getEntity();
        if (target.world.isRemote)
            return;

        Entity source = event.getSource().getTrueSource();
        if (source instanceof EntityLivingBase) {
            SpellTriggers.ATTACK_ENTITY.onAttackEntity((EntityLivingBase) source, target);
        }
    }


    @SubscribeEvent
    public static void onAttackEntityEvent(AttackEntityEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        if (player.world.isRemote)
            return;
        Entity target = event.getTarget();

        SpellTriggers.ATTACK_ENTITY.onAttackEntity(player, target);
    }

}
