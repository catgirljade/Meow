package org.jade.hiteffects.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.world.entity.LivingEntity;
import org.jade.hiteffects.HitEffectClient;

public class EntityUtilManager {
	static Int2ObjectOpenHashMap<EntityUtils> entityUtilHashMap = new Int2ObjectOpenHashMap<>();

	public static void addEntity(LivingEntity entity) {
		// check if not in already
		entityUtilHashMap.compute(entity.getId(), (key, value) -> {
			if (Math.abs(entity.getX() - HitEffectClient.mc.player.position().x) > HitEffectClient.mc.options.renderDistance().get() * 16 ||
						Math.abs(entity.getZ() - HitEffectClient.mc.player.position().z) > HitEffectClient.mc.options.renderDistance().get() * 16) {
				return null;
			}
			if (entity.equals(HitEffectClient.mc.player)){
				return null;
			}
			EntityUtils newEntityUtils = new EntityUtils(entity);
			return newEntityUtils;
		});
	}

	public static void tick() {
		ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
		if (!HitEffectClient.mc.isPaused() && HitEffectClient.mc.level != null) {
			for (ObjectIterator<EntityUtils> it = entityUtilHashMap.values().iterator(); it.hasNext(); ) {
				EntityUtils entityUtils = it.next();
				if (entityUtils.isValid() && config.kill_effect) {
					entityUtils.tick();
					continue;
				}
				it.remove();
			}
		}
	}
}
