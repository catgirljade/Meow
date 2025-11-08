package org.jade.hiteffects.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.world.entity.LivingEntity;
import org.jade.hiteffects.HitEffectClient;

public class EntityUtilManager {
	static Int2ObjectOpenHashMap<EntityTracker> entityTrackers = new Int2ObjectOpenHashMap<>();

	public static void handleEntity(LivingEntity entity) {
		// check if not in already
		entityTrackers.compute(entity.getId(), (key, value) -> {
			if (HitEffectClient.mc.player != null) {
				return null;
			}

			return value == null ? new EntityTracker(entity) : value;
		});
	}

	public static void tick() {
		ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
		if (!HitEffectClient.mc.isPaused() && HitEffectClient.mc.level != null && config.kill_effect) {
			for (EntityTracker entityTracker : entityTrackers.values()) {
				entityTracker.tick();
			}
		}
	}
}
