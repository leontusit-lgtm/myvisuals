package dev.myvisuals;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class VisualsClient implements ClientModInitializer {
    public static KeyBinding openMenu;

    @Override
    public void onInitializeClient() {
        openMenu = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.myvisuals.menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "key.category.myvisuals"));

        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            while (openMenu.wasPressed()) {
                if (mc.currentScreen == null) mc.setScreen(new SettingsScreen(null));
            }
        });

        HudRenderCallback.EVENT.register(HudRenderer::render);

        // Title screen: animated accent bar + gradient + button to open settings
        ScreenEvents.AFTER_INIT.register((mc, screen, w, h) -> {
            if (!(screen instanceof TitleScreen)) return;
            net.fabricmc.fabric.api.client.screen.v1.Screens.getButtons(screen).add(
                    net.minecraft.client.gui.widget.ButtonWidget.builder(
                            net.minecraft.text.Text.literal("Visuals"),
                            b -> MinecraftClient.getInstance().setScreen(new SettingsScreen(screen)))
                            .dimensions(6, 6, 60, 20).build());

            ScreenEvents.afterRender(screen).register((s, ctx, mx, my, delta) -> {
                if (!Config.titleOverlay) return;
                int sw = ctx.getScaledWindowWidth(), sh = ctx.getScaledWindowHeight();
                ctx.fillGradient(0, sh - sh / 3, sw, sh, 0x00000000, 0xA0101030);
                int seg = Math.max(1, sw / 64);
                for (int i = 0; i < 64; i++) {
                    ctx.fill(i * seg, sh - 3, (i + 1) * seg, sh, Draw.accent(i / 64f));
                }
            });
        });
    }
}
