package org.jade.hiteffects.util;

import net.minecraft.world.entity.LivingEntity;
import org.jade.hiteffects.features.OnDeathEffect;

public class EntityUtils {
	LivingEntity entity;

	boolean already_dead = false;

	public EntityUtils(LivingEntity entity){
		this.entity = entity;
	}

	public void tick(){
		if (entity.getHealth() <= 0 && entity.deathTime < 2){
			//this.already_dead = true;
			onDeath();
		}
	}

	public boolean isValid(){
		return entity != null && !entity.isRemoved();
	}

	void onDeath(){
		OnDeathEffect.createEffect(entity);
	}

}
