package org.jade.hiteffects.util;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class HitUtils {
	Player player;

	public void setPlayer(Player player) {
		this.player = player;
	}

	// called whenever player hits mob
	// this uses HORRIBLE logic to detect if you can crit I think
	public boolean can_hit() {
		return player.getAttackStrengthScale(0.5F) > 0.9f && player.fallDistance > 0.0f && !player.onGround() && !player.onClimbable() && !player.isInWater() && !player.hasEffect(MobEffects.BLINDNESS) && !player.isPassenger();
	}
}
