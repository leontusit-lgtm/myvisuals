package dev.myvisuals;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class SettingsScreen extends Screen {
    private final Screen parent;

    public SettingsScreen(Screen parent) {
        super(Text.literal("My Visuals"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int w = 200, h = 20, x = width / 2 - w / 2, y = height / 2 - 90, gap = 24;
        toggle(x, y, w, h, "Watermark", () -> Config.watermark, v -> Config.watermark = v);
        toggle(x, y + gap, w, h, "Coords / FPS / Ping", () -> Config.info, v -> Config.info = v);
        toggle(x, y + gap * 2, w, h, "Keystrokes", () -> Config.keystrokes, v -> Config.keystrokes = v);
        toggle(x, y + gap * 3, w, h, "Vignette", () -> Config.vignette, v -> Config.vignette = v);
        toggle(x, y + gap * 4, w, h, "Blue tint", () -> Config.tint, v -> Config.tint = v);
        toggle(x, y + gap * 5, w, h, "Title screen overlay", () -> Config.titleOverlay, v -> Config.titleOverlay = v);

        addDrawableChild(ButtonWidget.builder(label("HUD scale", "x" + Config.hudScale), b -> {
            Config.hudScale = Config.hudScale % 3 + 1;
            b.setMessage(label("HUD scale", "x" + Config.hudScale));
        }).dimensions(x, y + gap * 6, w, h).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Done"), b -> close())
                .dimensions(x, y + gap * 7 + 6, w, h).build());
    }

    private void toggle(int x, int y, int w, int h, String name, BooleanSupplier get, Consumer<Boolean> set) {
        addDrawableChild(ButtonWidget.builder(label(name, get.getAsBoolean() ? "ON" : "OFF"), b -> {
            set.accept(!get.getAsBoolean());
            b.setMessage(label(name, get.getAsBoolean() ? "ON" : "OFF"));
        }).dimensions(x, y, w, h).build());
    }

    private static Text label(String name, String value) {
        return Text.literal(name + ": " + value);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);
        ctx.drawCenteredTextWithShadow(textRenderer, "My Visuals", width / 2, height / 2 - 116, Draw.accent(0f));
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }
}
