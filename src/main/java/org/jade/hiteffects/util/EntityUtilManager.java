package org.jade.hiteffects.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.world.entity.LivingEntity;
import org.jade.hiteffects.HitEffectClient;

public class EntityUtilManager {
	static Int2ObjectOpenHashMap<EntityUtils> entityUtilHashMap = new Int2ObjectOpenHashMap<>();

	public static void handleEntity(LivingEntity entity) {
		// check if not in already
		entityUtilHashMap.compute(entity.getId(), (key, value) -> {
			if (Math.abs(entity.getX() - HitEffectClient.mc.player.position().x) > HitEffectClient.mc.options.renderDistance().get() * 16 ||
						Math.abs(entity.getZ() - HitEffectClient.mc.player.position().z) > HitEffectClient.mc.options.renderDistance().get() * 16) {
				return null;
			}
			if (entity.equals(HitEffectClient.mc.player)){
				return null;
			}
			if (value == null && entity.getHealth() <= 0.0 || value != null && value.died){
				return null;
			}
			return value == null ? new EntityUtils(entity) : value;
		});
	}

	public static void tick() {
		System.out.println(entityUtilHashMap.size());
		ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
		if (!HitEffectClient.mc.isPaused() && HitEffectClient.mc.level != null && config.kill_effect) {
			for (EntityUtils entityUtils : entityUtilHashMap.values()) {
				entityUtils.tick();
			}
		}
	}
}
