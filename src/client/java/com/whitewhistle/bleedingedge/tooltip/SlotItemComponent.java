package com.whitewhistle.bleedingedge.tooltip;

import com.whitewhistle.bleedingedge.items.impl.SlotItem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class SlotItemComponent implements TooltipComponent {
    public static final Identifier TEXTURE = new Identifier("textures/gui/container/bundle.png");

    private final ItemStack stack;

    public SlotItemComponent(SlotItem.SlotItemTooltipData data) {
        this.stack = data.stack();
    }

    @Override
    public int getHeight() {
        return this.getRows() * 20 + 2 + 4;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return this.getColumns() * 18 + 2;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        int i = this.getColumns();
        int j = this.getRows();
        int k = 0;

        for (int l = 0; l < j; l++) {
            for (int m = 0; m < i; m++) {
                int n = x + m * 18 + 1;
                int o = y + l * 20 + 1;
                this.drawSlot(n, o, k++, context, textRenderer);
            }
        }

        this.drawOutline(x, y, i, j, context);
    }

    private void drawSlot(int x, int y, int index, DrawContext context, TextRenderer textRenderer) {
        if (index >= 1) {
            this.draw(context, x, y, Sprite.SLOT);
        } else {
            ItemStack itemStack = this.stack;
            this.draw(context, x, y, Sprite.SLOT);
            context.drawItem(itemStack, x + 1, y + 1, index);
            context.drawItemInSlot(textRenderer, itemStack, x + 1, y + 1);
            if (index == 0) {
                HandledScreen.drawSlotHighlight(context, x + 1, y + 1, 0);
            }
        }
    }

    private void drawOutline(int x, int y, int columns, int rows, DrawContext context) {
        this.draw(context, x, y, Sprite.BORDER_CORNER_TOP);
        this.draw(context, x + columns * 18 + 1, y, Sprite.BORDER_CORNER_TOP);

        for (int i = 0; i < columns; i++) {
            this.draw(context, x + 1 + i * 18, y, Sprite.BORDER_HORIZONTAL_TOP);
            this.draw(context, x + 1 + i * 18, y + rows * 20, Sprite.BORDER_HORIZONTAL_BOTTOM);
        }

        for (int i = 0; i < rows; i++) {
            this.draw(context, x, y + i * 20 + 1, Sprite.BORDER_VERTICAL);
            this.draw(context, x + columns * 18 + 1, y + i * 20 + 1, Sprite.BORDER_VERTICAL);
        }

        this.draw(context, x, y + rows * 20, Sprite.BORDER_CORNER_BOTTOM);
        this.draw(context, x + columns * 18 + 1, y + rows * 20, Sprite.BORDER_CORNER_BOTTOM);
    }

    private void draw(DrawContext context, int x, int y, Sprite sprite) {
        context.drawTexture(TEXTURE, x, y, 0, sprite.u, sprite.v, sprite.width, sprite.height, 128, 128);
    }

    private int getColumns() {
        return 1;
    }

    private int getRows() {
        return 1;
    }

    enum Sprite {
        SLOT(0, 0, 18, 20),
        BLOCKED_SLOT(0, 40, 18, 20),
        BORDER_VERTICAL(0, 18, 1, 20),
        BORDER_HORIZONTAL_TOP(0, 20, 18, 1),
        BORDER_HORIZONTAL_BOTTOM(0, 60, 18, 1),
        BORDER_CORNER_TOP(0, 20, 1, 1),
        BORDER_CORNER_BOTTOM(0, 60, 1, 1);

        public final int u;
        public final int v;
        public final int width;
        public final int height;

        Sprite(int u, int v, int width, int height) {
            this.u = u;
            this.v = v;
            this.width = width;
            this.height = height;
        }
    }
}
