package org.jade.hiteffects.util;

import net.minecraft.world.entity.LivingEntity;
import org.jade.hiteffects.features.OnDeathEffect;

public class EntityUtils {
	LivingEntity entity;

	EntityUtils(LivingEntity entity){
		this.entity = entity;
	}

	public void tick(){
		if (entity != null && !entity.isRemoved() && entity.getHealth() <= 0){
			onDeath();
			entity = null;
		}
	}

	void onDeath(){
		OnDeathEffect.createEffect(entity);
	}
}
