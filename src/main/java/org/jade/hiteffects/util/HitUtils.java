package org.jade.hiteffects.util;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class HitUtils {
	static Player player;

	public static void setPlayer(Player new_player) {
		player = new_player;
	}

	// called whenever player hits mob
	// this uses HORRIBLE logic to detect if you can crit I think
	public static boolean can_hit() {
		return player.getAttackStrengthScale(0.5F) > 0.9f && player.fallDistance > 0.0f && !player.onGround() && !player.onClimbable() && !player.isInWater() && !player.hasEffect(MobEffects.BLINDNESS) && !player.isPassenger();
	}
}
