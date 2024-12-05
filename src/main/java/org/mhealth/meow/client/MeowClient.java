package org.mhealth.meow.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import org.mhealth.meow.client.features.OnHitEffect;

public class MeowClient implements ClientModInitializer {

	public static Minecraft mc;
	/**
	 * Runs the mod initializer on the client environment.
	 */


	@Override
	public void onInitializeClient() {
		mc = Minecraft.getInstance();

		System.out.println("Meowmeow");


		OnHitEffect onHitEffect = new OnHitEffect();
		onHitEffect.init();
	}
}