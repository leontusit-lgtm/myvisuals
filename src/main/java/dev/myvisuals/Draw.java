package dev.myvisuals;

import net.minecraft.client.gui.DrawContext;

public final class Draw {
    private Draw() {}

    /** Rounded rectangle built from 1px rows. */
    public static void rrect(DrawContext c, int x, int y, int w, int h, int r, int color) {
        r = Math.min(r, Math.min(w, h) / 2);
        c.fill(x, y + r, x + w, y + h - r, color);
        for (int i = 0; i < r; i++) {
            int dy = r - i;
            int d = r - (int) Math.round(Math.sqrt((double) r * r - (double) dy * dy));
            c.fill(x + d, y + i, x + w - d, y + i + 1, color);
            c.fill(x + d, y + h - i - 1, x + w - d, y + h - i, color);
        }
    }

    /** Animated rainbow-ish accent colour (ARGB). */
    public static int accent(float offset) {
        float hue = ((System.currentTimeMillis() % 6000L) / 6000f + offset) % 1f;
        return 0xFF000000 | (java.awt.Color.HSBtoRGB(hue, 0.55f, 1f) & 0xFFFFFF);
    }
}
