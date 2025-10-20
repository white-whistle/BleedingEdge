package com.whitewhistle.bleedingedge.items.impl;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.whitewhistle.bleedingedge.effects.ModStatusEffects;
import com.whitewhistle.bleedingedge.entity.ModEntityAttributes;
import com.whitewhistle.bleedingedge.items.IHasModel;
import com.whitewhistle.bleedingedge.particles.ModParticleTypes;
import com.whitewhistle.bleedingedge.util.UuidUtil;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.client.Model;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.whitewhistle.bleedingedge.items.impl.ModTrinketItem.MAJOR_THREAT;
import static com.whitewhistle.bleedingedge.util.RandomUtil.r0;

public class StormBenderItem extends BowItem implements IHasModel {
    public static final double RANGE = 256;
    public static final int SELF_DAMAGE = 10;
    public static final int TARGET_DAMAGE = 16;
    public static final int FAIL_DAMAGE = 4;
    public static final int BLAST_DAMAGE = 8;
    public static final int SELF_EMP_TICKS = 20 * 5;
    public static final int TARGET_EMP_TICKS = 20 * 15;
    public static final int BLAST_EMP_TICKS = 20 * 5;
    public static final int FAIL_EMP_TICKS = 20 * 3;

    private static final ImmutableMultimap<EntityAttribute, EntityAttributeModifier> HELD_MODIFIERS = ImmutableMultimap.<EntityAttribute, EntityAttributeModifier>builder().put(
            ModEntityAttributes.THREAT,
            new EntityAttributeModifier(UuidUtil.from("storm_bender_held"), "Storm bender threat", MAJOR_THREAT, EntityAttributeModifier.Operation.ADDITION)
    ).build();

    public StormBenderItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);

        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {

        int i = this.getMaxUseTime(stack) - remainingUseTicks;
        float f = getPullProgress(i);

        if (f == 1) {
            fireSonicBoom(world, user);

            if (!world.isClient) {
                var lightning = EntityType.LIGHTNING_BOLT.create(world);
                if (lightning != null) {
                    lightning.refreshPositionAfterTeleport(user.getX(), user.getY(), user.getZ());
                    lightning.setCosmetic(true);
                    world.spawnEntity(lightning);
                }
            }

            user.damage(world.getDamageSources().lightningBolt(), SELF_DAMAGE);
            user.addStatusEffect(new StatusEffectInstance(ModStatusEffects.EMP, SELF_EMP_TICKS));

            user.playSound(SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, 3, r0());

        } else if (f > 0.5f) {
            // half charge punish

            TeslaPackItem.spawnTeslaBlast(world, null, user.getX(), user.getY(), user.getZ(), 5, 5, FAIL_DAMAGE, FAIL_EMP_TICKS, 64);
        }


    }

    private EntityHitResult getEntityHitResult(World world, PlayerEntity player, Vec3d start, Vec3d end) {
        return ProjectileUtil.raycast(
                player,
                start,
                end,
                player.getBoundingBox().stretch(player.getRotationVec(1.0F).multiply(RANGE)).expand(1.0D),
                (entity) -> !entity.isSpectator() && entity.canHit() && entity instanceof LivingEntity,
                30.0D
        );
    }

    private void sonicBoomRay(ServerWorld serverWorld, PlayerEntity player) {

        Vec3d cameraPos = player.getCameraPosVec(1.0F);
        Vec3d rotation = player.getRotationVec(1.0F);
        Vec3d targetPos = cameraPos.add(rotation.multiply(RANGE));

        EntityHitResult entityHit = getEntityHitResult(serverWorld, player, cameraPos, targetPos);

        if (entityHit != null) {
            Entity target = entityHit.getEntity();

            var tPos = target.getPos();

            spawnSonicBoomParticles(serverWorld, cameraPos, tPos);

            TeslaPackItem.spawnTeslaBlast(serverWorld, target, tPos.x, tPos.y, tPos.z, 5, 5, BLAST_DAMAGE, BLAST_EMP_TICKS, 128);

            target.damage(serverWorld.getDamageSources().lightningBolt(), TARGET_DAMAGE);
            if (target instanceof LivingEntity livingEntity) {
                livingEntity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.EMP, TARGET_EMP_TICKS));

                var res = livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
                var d = 0.5 * (1.0 - res);
                var e = 2.5 * (1.0 - res);

                var dir = target.getPos().subtract(cameraPos).normalize();

                livingEntity.addVelocity(dir.getX() * e, dir.getY() * d, dir.getZ() * e);
            }

            return;

        }

        BlockHitResult blockHit = serverWorld.raycast(new RaycastContext(
                cameraPos,
                targetPos,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                player
        ));

        if (blockHit.getType() != HitResult.Type.MISS) {
            BlockPos blockPos = blockHit.getBlockPos();
            var bPos = blockPos.toCenterPos();
            spawnSonicBoomParticles(serverWorld, cameraPos, bPos);
            TeslaPackItem.spawnTeslaBlast(serverWorld, null, bPos.x, bPos.y, bPos.z, 5, 5, BLAST_DAMAGE, BLAST_EMP_TICKS, 128);

            return;
        }

        spawnSonicBoomParticles(serverWorld, player.getPos(), targetPos);

    }

    private void spawnSonicBoomParticles(ServerWorld serverWorld, Vec3d startPos, Vec3d endPos) {
        var diff = endPos.subtract(startPos);
        var direction = diff.normalize();

        for (int i = 1; i < MathHelper.floor(diff.length()); i++) {
            Vec3d vec3d4 = startPos.add(direction.multiply(i));
            serverWorld.spawnParticles(ModParticleTypes.STORM_BEAM, vec3d4.x, vec3d4.y, vec3d4.z, 1, 0.0, 0.0, 0.0, 0.0);
        }
    }

    private void fireSonicBoom(World world, LivingEntity user) {
        if (!(user instanceof PlayerEntity player)) return;

        if (world instanceof ServerWorld serverWorld) {
            this.sonicBoomRay(serverWorld, player);
        }

        user.playSound(SoundEvents.ENTITY_WARDEN_SONIC_BOOM, 3.0F, 1.0F);

    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(Text.translatable("tooltip.bleeding-edge.storm_bender.description1").styled(s -> s.withColor(Formatting.GRAY)));
        tooltip.add(Text.translatable("tooltip.bleeding-edge.storm_bender.description2").styled(s -> s.withColor(Formatting.GRAY)));
    }

    @Override
    public Model getBaseModel() {
        return null;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            return HELD_MODIFIERS;
        }

        return super.getAttributeModifiers(stack, slot);
    }
}
