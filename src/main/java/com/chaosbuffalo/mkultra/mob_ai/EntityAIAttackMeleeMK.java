package com.chaosbuffalo.mkultra.mob_ai;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import com.chaosbuffalo.mkultra.event.ItemRestrictionHandler;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIAttackMeleeMK extends EntityAIBase {
    World world;
    protected EntityCreature attacker;
    protected int attackTick;
    double speedTowardsTarget;
    boolean longMemory;
    Path path;
    private int delayCounter;
    private double targetX;
    private double targetY;
    private double targetZ;
    protected final int attackInterval = 20;
    private int failedPathFindingPenalty = 0;
    private boolean canPenalize = false;

    public EntityAIAttackMeleeMK(EntityCreature entityIn, double moveSpeed, boolean longMemory) {
        this.attacker = entityIn;
        this.world = entityIn.world;
        this.speedTowardsTarget = moveSpeed;
        this.longMemory = longMemory;
        this.setMutexBits(3);
    }

    public boolean shouldExecute() {
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        if (entitylivingbase == null) {
            return false;
        } else if (!entitylivingbase.isEntityAlive()) {
            return false;
        } else if (this.canPenalize) {
            if (--this.delayCounter <= 0) {
                this.path = this.attacker.getNavigator().getPathToEntityLiving(entitylivingbase);
                this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
                return this.path != null;
            } else {
                return true;
            }
        } else {
            this.path = this.attacker.getNavigator().getPathToEntityLiving(entitylivingbase);
            if (this.path != null) {
                return true;
            } else {
                return this.getAttackReachSqr(entitylivingbase) >= this.attacker.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
            }
        }
    }

    public boolean shouldContinueExecuting() {
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        if (entitylivingbase == null) {
            return false;
        } else if (!entitylivingbase.isEntityAlive()) {
            return false;
        } else if (!this.longMemory) {
            return !this.attacker.getNavigator().noPath();
        } else if (!this.attacker.isWithinHomeDistanceFromPosition(new BlockPos(entitylivingbase))) {
            return false;
        } else {
            return !(entitylivingbase instanceof EntityPlayer) || !((EntityPlayer)entitylivingbase).isSpectator() && !((EntityPlayer)entitylivingbase).isCreative();
        }
    }

    public void startExecuting() {
//        Log.info("Start Executing Attack Melee: %s", attacker.toString());
        this.attacker.getNavigator().setPath(this.path, this.speedTowardsTarget);
        this.delayCounter = 0;
    }

    public void resetTask() {
//        Log.info("Resetting Attack Melee: %s", attacker.toString());
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        if (entitylivingbase instanceof EntityPlayer && (((EntityPlayer)entitylivingbase).isSpectator() ||
                ((EntityPlayer)entitylivingbase).isCreative())) {
            this.attacker.setAttackTarget((EntityLivingBase)null);
        }

        this.attacker.getNavigator().clearPath();
    }

    public void updateTask() {
//        Log.info("Updating Attack Melee: %s", attacker.toString());
        EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        if (entitylivingbase == null){
            return;
        }
        this.attacker.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F,
                30.0F);
        double d0 = this.attacker.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY,
                entitylivingbase.posZ);
        --this.delayCounter;
        if ((this.longMemory || this.attacker.getEntitySenses().canSee(entitylivingbase)) &&
                this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D
                && this.targetZ == 0.0D ||
                entitylivingbase.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D ||
                this.attacker.getRNG().nextFloat() < 0.05F)) {
            this.targetX = entitylivingbase.posX;
            this.targetY = entitylivingbase.getEntityBoundingBox().minY;
            this.targetZ = entitylivingbase.posZ;
            this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
            if (this.canPenalize) {
                this.delayCounter += this.failedPathFindingPenalty;
                if (this.attacker.getNavigator().getPath() != null) {
                    PathPoint finalPathPoint = this.attacker.getNavigator().getPath().getFinalPathPoint();
                    if (finalPathPoint != null && entitylivingbase.getDistanceSq((
                            double)finalPathPoint.x, (double)finalPathPoint.y, (double)finalPathPoint.z) < 1.0D) {
                        this.failedPathFindingPenalty = 0;
                    } else {
                        this.failedPathFindingPenalty += 10;
                    }
                } else {
                    this.failedPathFindingPenalty += 10;
                }
            }

            if (d0 > 1024.0D) {
                this.delayCounter += 10;
            } else if (d0 > 256.0D) {
                this.delayCounter += 5;
            }

            if (!this.attacker.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.speedTowardsTarget)) {
                this.delayCounter += 15;
            }
        }

        this.attackTick = Math.max(this.attackTick - 1, 0);
        this.checkAndPerformAttack(entitylivingbase, d0);
    }

    protected void checkAndPerformAttack(EntityLivingBase attackTarget, double distance) {
        double d0 = this.getAttackReachSqr(attackTarget);
        if (distance <= d0 && this.attackTick <= 0) {
            double attackSpeed = 4.0;
            ItemStack inMainhand = attacker.getHeldItemMainhand();
            if (inMainhand != ItemStack.EMPTY) {
                Multimap<String, AttributeModifier> attrs = inMainhand.getAttributeModifiers(
                        EntityEquipmentSlot.MAINHAND);
                if (attrs.containsKey(SharedMonsterAttributes.ATTACK_SPEED.getName())){
                    for (AttributeModifier mod : attrs.get(SharedMonsterAttributes.ATTACK_SPEED.getName())){
                        switch (mod.getOperation()){
                            case (PlayerAttributes.OP_INCREMENT):
                                attackSpeed += mod.getAmount();
                                break;
                            case (PlayerAttributes.OP_SCALE_ADDITIVE):
                                attackSpeed += attackSpeed * mod.getAmount();
                                break;
                            case (PlayerAttributes.OP_SCALE_MULTIPLICATIVE):
                                attackSpeed *= (1 + mod.getAmount());
                                break;
                        }
                    }
                }
            }
            int attackTicks = (int)(GameConstants.TICKS_PER_SECOND / attackSpeed);
            this.attackTick = attackTicks;
            this.attacker.swingArm(EnumHand.MAIN_HAND);
            this.attacker.attackEntityAsMob(attackTarget);
        }

    }

    protected double getAttackReachSqr(EntityLivingBase attackTarget) {
        ItemStack inMainhand = attacker.getHeldItemMainhand();
        double attackRange = this.attacker.width * 2.0;
        if (inMainhand != ItemStack.EMPTY){
            Item item = inMainhand.getItem();
            if (ItemRestrictionHandler.isNoShieldItem(item)){
                attackRange *= 2.0;
            }
        }
        return (double)(attackRange + attackTarget.width);
    }
}
