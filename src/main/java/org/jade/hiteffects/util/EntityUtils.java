package org.jade.hiteffects.util;

import net.minecraft.world.entity.LivingEntity;
import org.jade.hiteffects.features.OnDeathEffect;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class EntityUtils {
	WeakReference<LivingEntity> entity;

	EntityUtils(LivingEntity entity){
		this.entity = new WeakReference<>(entity);
	}

	public void tick(){
		if (entity != null && !entity.get().isRemoved() && entity.get().getHealth() <= 0){
			onDeath();
		}
	}

	void onDeath(){
		OnDeathEffect.createEffect(Objects.requireNonNull(entity.get()));
	}
}
