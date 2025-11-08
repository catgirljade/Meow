package org.jade.hiteffects.util;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class HitUtils {
	// this uses HORRIBLE logic to detect if you can crit I think
	public static boolean canHit(Player player) {
		return player.getAttackStrengthScale(0.5F) > 0.9f && player.fallDistance > 0.0f && !player.onGround() && !player.onClimbable() && !player.isInWater() && !player.hasEffect(MobEffects.BLINDNESS) && !player.isPassenger();
	}
}
