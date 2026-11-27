package com.ritamx.butterfly;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.util.Util;
import net.minecraft.resources.Identifier;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.DeltaTracker;

/** Client-only animated folding-fan cosmetic prototype. */
public final class ButterflyCosmeticClient implements ClientModInitializer {
    private static final String MOD_ID = "butterflycosmetic";
    private static final KeyMapping.Category CATEGORY =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "cosmetics"));

    private static KeyMapping toggleKey;
    private static KeyMapping inspectKey;
    private static boolean enabled = false;
    private static long inspectStartedAt = 0L;

    @Override
    public void onInitializeClient() {
        toggleKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.butterflycosmetic.toggle_fan",
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_B,
                CATEGORY
        ));
        inspectKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.butterflycosmetic.inspect_fan",
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_V,
                CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.consumeClick()) {
                enabled = !enabled;
            }
            while (inspectKey.consumeClick()) {
                if (enabled) inspectStartedAt = Util.getMillis();
            }
        });

        HudElementRegistry.addLast(
                Identifier.fromNamespaceAndPath(MOD_ID, "folding_fan_hud"),
                ButterflyCosmeticClient::renderFan
        );
    }

    private static void renderFan(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        if (!enabled) return;

        long now = Util.getMillis();
        float idle = (float) Math.sin(now / 420.0) * 0.035f;
        float inspect = 0.0f;
        if (inspectStartedAt > 0L) {
            float progress = Math.min(1.0f, (now - inspectStartedAt) / 850.0f);
            inspect = (float) Math.sin(progress * Math.PI) * 0.55f;
            if (progress >= 1.0f) inspectStartedAt = 0L;
        }

        int x = graphics.guiWidth() - 118;
        int y = graphics.guiHeight() - 112;
        graphics.fill(x - 8, y - 22, x + 108, y + 8, 0x660A1020);
        graphics.text(net.minecraft.client.Minecraft.getInstance().font,
                "RXG FOLDING FAN", x, y - 17, 0xFFE8F4FF, false);

        var pose = graphics.pose();
        pose.pushMatrix();
        pose.translate(x + 54, y + 58);
        pose.rotate(idle + inspect);
        pose.scale(1.0f, 1.0f);

        // Decorative fan ribs and coloured panels, drawn as a compact HUD prop.
        final int ribs = 9;
        for (int i = 0; i < ribs; i++) {
            float t = (i - (ribs - 1) / 2.0f) / ((ribs - 1) / 2.0f);
            float spread = 0.30f + Math.abs(t) * 0.18f;
            pose.pushMatrix();
            pose.rotate(t * spread);
            int panelColor = (i % 2 == 0) ? 0xFF38BDF8 : 0xFF8B5CF6;
            graphics.fill(-3, -53, 3, -5, panelColor);
            graphics.fill(-2, -50, 2, -8, 0xFFDBF4FF);
            pose.popMatrix();
        }
        // Hinge and lower grip.
        graphics.fill(-7, -8, 7, 5, 0xFFE6B85C);
        graphics.fill(-3, 4, 3, 32, 0xFF273449);
        graphics.fill(-2, 6, 2, 29, 0xFF9ADCFB);
        pose.popMatrix();

        graphics.text(net.minecraft.client.Minecraft.getInstance().font,
                "B: Toggle  |  V: Inspect", x - 4, y + 93, 0xFFD6E7F5, false);
    }
}
