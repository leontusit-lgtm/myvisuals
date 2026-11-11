package dev.myvisuals;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderTickCounter;

public final class HudRenderer {
    private static final int BG = 0xB0101018;
    private static final int KEY_OFF = 0x90101018;
    private static final int KEY_ON = 0xC0FFFFFF;

    private HudRenderer() {}

    public static void render(DrawContext ctx, RenderTickCounter tick) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.options.hudHidden || mc.getDebugHud().shouldShowDebugHud()) return;

        int sw = ctx.getScaledWindowWidth();
        int sh = ctx.getScaledWindowHeight();

        if (Config.vignette) vignette(ctx, sw, sh);
        if (Config.tint) ctx.fill(0, 0, sw, sh, 0x182060FF);

        TextRenderer tr = mc.textRenderer;
        var m = ctx.getMatrices();
        m.push();
        m.scale(Config.hudScale, Config.hudScale, 1f);
        int vw = sw / Config.hudScale;
        int vh = sh / Config.hudScale;

        if (Config.watermark) watermark(ctx, tr, mc);
        if (Config.info) info(ctx, tr, mc, vh);
        if (Config.keystrokes) keystrokes(ctx, tr, mc, vw, vh);
        m.pop();
    }

    private static void watermark(DrawContext ctx, TextRenderer tr, MinecraftClient mc) {
        String text = "MyVisuals  |  " + mc.getSession().getUsername();
        int w = tr.getWidth(text) + 12;
        Draw.rrect(ctx, 6, 6, w, 18, 5, BG);
        ctx.fill(10, 22, 6 + w - 4, 23, Draw.accent(0f));
        ctx.drawText(tr, text, 12, 11, 0xFFFFFFFF, true);
    }

    private static void info(DrawContext ctx, TextRenderer tr, MinecraftClient mc, int vh) {
        int ping = 0;
        if (mc.getNetworkHandler() != null) {
            PlayerListEntry e = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
            if (e != null) ping = e.getLatency();
        }
        String[] lines = {
            String.format("XYZ  %.1f  %.1f  %.1f", mc.player.getX(), mc.player.getY(), mc.player.getZ()),
            "FPS  " + mc.getCurrentFps() + "    Ping  " + ping + " ms"
        };
        int w = 0;
        for (String l : lines) w = Math.max(w, tr.getWidth(l));
        int h = lines.length * 11 + 6;
        int y = vh - h - 6;
        Draw.rrect(ctx, 6, y, w + 12, h, 5, BG);
        for (int i = 0; i < lines.length; i++) ctx.drawText(tr, lines[i], 12, y + 5 + i * 11, 0xFFE8E8F0, true);
    }

    private static void keystrokes(DrawContext ctx, TextRenderer tr, MinecraftClient mc, int vw, int vh) {
        int s = 22, g = 3;
        int x0 = vw - (s * 3 + g * 2) - 8;
        int y0 = vh - (s * 3 + g * 2) - 8;
        key(ctx, tr, "W", x0 + s + g, y0, s, s, mc.options.forwardKey);
        key(ctx, tr, "A", x0, y0 + s + g, s, s, mc.options.leftKey);
        key(ctx, tr, "S", x0 + s + g, y0 + s + g, s, s, mc.options.backKey);
        key(ctx, tr, "D", x0 + (s + g) * 2, y0 + s + g, s, s, mc.options.rightKey);
        int bw = (s * 3 + g * 2 - g) / 2;
        key(ctx, tr, "LMB", x0, y0 + (s + g) * 2, bw, s, mc.options.attackKey);
        key(ctx, tr, "RMB", x0 + bw + g, y0 + (s + g) * 2, bw, s, mc.options.useKey);
    }

    private static void key(DrawContext ctx, TextRenderer tr, String label, int x, int y, int w, int h, KeyBinding kb) {
        boolean on = kb.isPressed();
        Draw.rrect(ctx, x, y, w, h, 4, on ? KEY_ON : KEY_OFF);
        int tw = tr.getWidth(label);
        ctx.drawText(tr, label, x + (w - tw) / 2, y + (h - 8) / 2, on ? 0xFF101018 : 0xFFFFFFFF, false);
    }

    /** Cheap vignette using stacked gradient strips on each edge. */
    private static void vignette(DrawContext ctx, int w, int h) {
        int t = Math.max(24, h / 6);
        int top = 0x70000000;
        ctx.fillGradient(0, 0, w, t, top, 0x00000000);
        ctx.fillGradient(0, h - t, w, h, 0x00000000, top);
    }
}
