package org.jade.hiteffects;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import org.apache.commons.compress.harmony.pack200.NewAttributeBands;
import org.jade.hiteffects.features.OnHitEffect;
import org.jade.hiteffects.util.CallableManager;
import org.jade.hiteffects.util.ModConfig;
import org.jade.hiteffects.util.ParticleUtils;

public class HitEffectClient implements ClientModInitializer {

	public static Minecraft mc;
	/**
	 * Runs the mod initializer on the client environment.
	 */

	OnHitEffect onHitEffect;

	@Override
	public void onInitializeClient() {
		System.out.println("Meow meow!");

		AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);

		mc = Minecraft.getInstance();

		load();
	}

	private void load() {
		ClientTickEvents.END_CLIENT_TICK.register(mc -> {
			CallableManager.tick();
		});
		OnHitEffect.init();
	}
}