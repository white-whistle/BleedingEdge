package com.whitewhistle.bleedingedge.items.impl;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.whitewhistle.bleedingedge.entity.ModEntityAttributes;
import com.whitewhistle.bleedingedge.items.IHasModel;
import com.whitewhistle.bleedingedge.nbt.ModComponents;
import com.whitewhistle.bleedingedge.util.UuidUtil;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.client.Model;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class BreachHammer extends PickaxeItem implements IHasModel {

    private static final UUID CHARGED_BREACH_HAMMER_THREAT_MODIFIER = UuidUtil.from("charged_breach_hammer_modifier");
    private static final int CHARGED_THREAT = ModTrinketItem.MAJOR_THREAT;
    private final float chargedAttackDamage;
    private final Multimap<EntityAttribute, EntityAttributeModifier> chargedAttributeModifiers;

    private boolean charged = false;
    private boolean loaded = false;

    public BreachHammer(ToolMaterial material, int attackDamage, float chargedAttackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);

        this.chargedAttackDamage = chargedAttackDamage + material.getAttackDamage();
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(
                EntityAttributes.GENERIC_ATTACK_DAMAGE,
                new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Charged Tool modifier", (double) this.chargedAttackDamage, EntityAttributeModifier.Operation.ADDITION)
        );
        builder.put(
                EntityAttributes.GENERIC_ATTACK_SPEED,
                new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Charged Tool modifier", (double) attackSpeed, EntityAttributeModifier.Operation.ADDITION)
        );
        builder.put(ModEntityAttributes.THREAT, new EntityAttributeModifier(CHARGED_BREACH_HAMMER_THREAT_MODIFIER, "Charged Tool modifier", CHARGED_THREAT, EntityAttributeModifier.Operation.ADDITION));
        this.chargedAttributeModifiers = builder.build();
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        if (ModComponents.CHARGED.get(stack)) {
            tooltip.add(Text.translatable("tooltip.bleeding-edge.breach_hammer.description.charged").styled(s -> s.withColor(Formatting.GRAY)));
        } else {
            tooltip.add(Text.translatable("tooltip.bleeding-edge.breach_hammer.description.charge").styled(s -> s.withColor(Formatting.GRAY)));
        }
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (ModComponents.CHARGED.get(stack)) {
            attacker.playSound(SoundEvents.ITEM_SHIELD_BREAK, 1f, 1f);

            ModComponents.CHARGED.set(stack, false);
        }

        return super.postHit(stack, target, attacker);
    }

    private SoundEvent getQuickChargeSound(int stage) {
        switch (stage) {
            case 1:
                return SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_1;
            case 2:
                return SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_2;
            case 3:
                return SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_3;
            default:
                return SoundEvents.ITEM_CROSSBOW_LOADING_START;
        }
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!world.isClient) {
            int i = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
            SoundEvent soundEvent = this.getQuickChargeSound(i);
            SoundEvent soundEvent2 = i == 0 ? SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE : null;
            float f = (float)(stack.getMaxUseTime() - remainingUseTicks) / CrossbowItem.getPullTime(stack);
            if (f < 0.2F) {
                this.charged = false;
                this.loaded = false;
            }

            if (f >= 0.2F && !this.charged) {
                this.charged = true;
                world.playSound(null, user.getX(), user.getY(), user.getZ(), soundEvent, SoundCategory.PLAYERS, 0.5F, 1.0F);
            }

            if (f >= 0.5F && soundEvent2 != null && !this.loaded) {
                this.loaded = true;
                world.playSound(null, user.getX(), user.getY(), user.getZ(), soundEvent2, SoundCategory.PLAYERS, 0.5F, 1.0F);
            }
        }
    }

    public float getChargedAttackDamage() {
        return chargedAttackDamage;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        var charged = ModComponents.CHARGED.get(stack);

        if (slot != EquipmentSlot.MAINHAND) return ImmutableMultimap.of();

        if (charged) {
            return chargedAttributeModifiers;
        }

        return super.getAttributeModifiers(stack, slot);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ModComponents.CHARGED.set(stack, true);

        user.playSound(SoundEvents.ITEM_CROSSBOW_LOADING_END, 1f, 1f);

        return super.finishUsing(stack, world, user);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return CrossbowItem.getPullTime(stack);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.CROSSBOW;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (ModComponents.CHARGED.get(itemStack)) {
            return TypedActionResult.fail(itemStack);
        } else {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
    }

    @Override
    public Model getBaseModel() {
        return null;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }
}
