package org.jade.hiteffects.features;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jade.hiteffects.HitEffectClient;
import org.jade.hiteffects.util.HitUtils;
import org.jade.hiteffects.util.ModConfig;
import org.jade.hiteffects.util.ParticleUtils;
import org.jetbrains.annotations.Nullable;

public class OnHitEffect {
	private static final float[] TRIDENT_PITCH = {0.75f, 1f, 0.5f};
	private static final float[] SWEEP_PITCH = {0.75f, 0.9f, 0.55f};
	private final double STEP = Math.toRadians(30);

	private int combo = 0;

	public OnHitEffect() {
		AttackEntityCallback.EVENT.register(this::interact);
	}

	void slashEffect(Level world, Player player, Entity entity) {
		boolean crit = HitUtils.canHit(player);
		double attack_weak = 1 - player.getAttackStrengthScale(0.0F);
		BlockPos pos = new BlockPos(new Vec3i((int) entity.getX(), (int) entity.getY(), (int) entity.getZ()));
		ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

		world.playSound(player, pos, SoundEvents.CHAIN_FALL, SoundSource.PLAYERS, 0.8f * config.hit_effect_volume, 1.6f);
		world.playSound(player, pos, SoundEvents.RESPAWN_ANCHOR_DEPLETE.value(), SoundSource.PLAYERS, 0.3f, TRIDENT_PITCH[combo]);
		world.playSound(player, pos, SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1 * config.hit_effect_volume, SWEEP_PITCH[combo]);
		world.playSound(player, pos, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 0.4f * config.hit_effect_volume, TRIDENT_PITCH[combo]);
		world.playSound(player, pos, SoundEvents.TRIDENT_HIT, SoundSource.PLAYERS, 0.7f * config.hit_effect_volume, 1.8f);
		world.playSound(player, pos, SoundEvents.TRIDENT_RETURN, SoundSource.PLAYERS, 1 * config.hit_effect_volume, 1.0f);
		if (crit) {
			world.playSound(player, pos, SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 0.4f * config.hit_effect_volume, 1.5f);
			world.playSound(player, pos, SoundEvents.TRIDENT_RIPTIDE_3, SoundSource.PLAYERS, 0.5f * config.hit_effect_volume, 2.0f);
		}
		Vec3 eye_pos = player.getEyePosition();

		ParticleUtils.create_arc((vec, radius) -> {
				world.addParticle(
					new DustColorTransitionOptions(
						config.colourToVec(config.hit_colours_initial),
						config.colourToVec(config.hit_colours_final),
						1),
					eye_pos.x + vec.x, eye_pos.y + vec.y, eye_pos.z + vec.z, 0, 0, 0
				);
			},
			Math.max(eye_pos.distanceTo(entity.position().add(new Vec3(0, entity.getEyeHeight() / 2, 0))), 1.0f),
			(int) (90 * attack_weak),
			(int) (180 - attack_weak * 90),
			((combo - 1) * STEP + Math.PI / 2),
			Math.toRadians(player.getXRot()),
			Math.toRadians(player.getYRot()),
			config.spacing,
			(int) (180 * (1 - attack_weak)),
			3
		);

		combo++;
		combo %= 3;
	}

	private InteractionResult interact(Player player, Level world, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
		ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
		if (!config.hit_effect) {
			return InteractionResult.PASS;
		}
		if (entity instanceof LivingEntity && entity.isAlive() && !entity.isRemoved()) {
			this.slashEffect(world, player, entity);
		}
		return InteractionResult.PASS;
	}
}



