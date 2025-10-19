package com.whitewhistle.bleedingedge.mixin;

import com.whitewhistle.bleedingedge.ability.IHasAbilities;
import com.whitewhistle.bleedingedge.common.CommonBridge;
import com.whitewhistle.bleedingedge.effects.ModStatusEffects;
import com.whitewhistle.bleedingedge.items.AppliedItem;
import com.whitewhistle.bleedingedge.items.ElectricToggledItem;
import com.whitewhistle.bleedingedge.items.GlobalItemClick;
import com.whitewhistle.bleedingedge.items.ToggledItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract Item getItem();

    @Inject(at = @At("HEAD"), method = "onClicked", cancellable = true)
    private void onClicked(ItemStack _stack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference, CallbackInfoReturnable<Boolean> cir) {
        var stack = (ItemStack) (Object) this;
        var optionalResult = GlobalItemClick.onClicked(stack, slot, clickType, player, cursorStackReference);

        optionalResult.ifPresent(cir::setReturnValue);
    }

    @Inject(at = @At("RETURN"), method = "getTooltip")
    private void addGlobalTooltips(@Nullable PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir) {
        if (player == null) return;

        var stack = (ItemStack)(Object)this;
        var tooltip = cir.getReturnValue();
        var item = stack.getItem();

        // generic append tooltip onto applied items
        if (item instanceof ToggledItem toggledItem) {
            var enabled = toggledItem.isEnabled(player, stack);
            var statusMsg = enabled ? Text.translatable("tooltip.bleeding-edge.toggled-item.on").styled(s -> s.withColor(Formatting.GREEN)) : Text.translatable("tooltip.bleeding-edge.toggled-item.off").styled(s -> s.withColor(Formatting.GRAY));

            tooltip.add(Text.empty());

            if (item instanceof ElectricToggledItem) {
                if (player.hasStatusEffect(ModStatusEffects.EMP)) {
                    statusMsg = Text.translatable("tooltip.bleeding-edge.toggled-item.err").styled(s -> s.withColor(Formatting.RED));
                }

                tooltip.add(Text.translatable("tooltip.bleeding-edge.toggled-item.description", statusMsg).styled(s -> s.withColor(Formatting.GRAY)));
                tooltip.add(Text.translatable("tooltip.bleeding-edge.toggled-item.emp.disclaimer").styled(s -> s.withColor(Formatting.GRAY)));
            } else {
                tooltip.add(Text.translatable("tooltip.bleeding-edge.toggled-item.description", statusMsg).styled(s -> s.withColor(Formatting.GRAY)));
            }
        }

        if (item instanceof IHasAbilities) {
            tooltip.add(Text.empty());
            CommonBridge.INSTANCE.addHotkeyTooltip(stack, tooltip);
        }

        if (item instanceof AppliedItem appliedItem) {
            tooltip.add(Text.empty());
            tooltip.add(appliedItem.getTooltip(stack));
        }
    }

}
