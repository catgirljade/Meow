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
	private final double PLAYER_HEIGHT = 1.8;

	Player player;
	Level world;
	InteractionHand hand;
	Entity entity;

	HitUtils hitUtils;
	private int combo = 0;

	public OnHitEffect() {
		this.player = HitEffectClient.mc.player;
		this.hitUtils = new HitUtils();
		AttackEntityCallback.EVENT.register(this::interact);
	}

	void slashEffect() {
		hitUtils.setPlayer(player);
		boolean crit = hitUtils.can_hit();
		double attack_weak = 1 - player.getAttackStrengthScale(0.0F);
		BlockPos pos = new BlockPos(new Vec3i((int) entity.getX(), (int) entity.getY(), (int) entity.getZ()));

		world.playSound(player, pos, SoundEvents.CHAIN_FALL, SoundSource.PLAYERS, 0.8f, 1.6f);
		world.playSound(player, pos, SoundEvents.RESPAWN_ANCHOR_DEPLETE.value(), SoundSource.PLAYERS, 0.3f, TRIDENT_PITCH[combo]);
		world.playSound(player, pos, SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1, SWEEP_PITCH[combo]);
		world.playSound(player, pos, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 0.4f, TRIDENT_PITCH[combo]);
		world.playSound(player, pos, SoundEvents.TRIDENT_HIT, SoundSource.PLAYERS, 0.7f, 1.8f);
		world.playSound(player, pos, SoundEvents.TRIDENT_RETURN, SoundSource.PLAYERS, 1, 1.0f);
		if (crit) {
			world.playSound(player, pos, SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 0.4f, 1.5f);
			world.playSound(player, pos, SoundEvents.TRIDENT_RIPTIDE_3, SoundSource.PLAYERS, 0.5f, 2.0f);
		}
		Vec3 eye_pos = player.position().add(new Vec3(0, PLAYER_HEIGHT, 0));

		ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

		ParticleUtils.create_arc((vec, radius) -> {
						world.addParticle(new DustColorTransitionOptions(config.colourToVec(config.hit_colours_initial), config.colourToVec(config.hit_colours_final), 1),
									eye_pos.x + vec.x, eye_pos.y + vec.y, eye_pos.z + vec.z, 0, 0, 0);
					}, Math.max(player.position().distanceTo(entity.position().lerp(entity.getEyePosition(), 0.5)), 1.0f),
					(int) (90 * attack_weak), (int) (180 - attack_weak * 90), (float) ((combo - 1) * STEP + Math.PI / 2),
					(float) Math.toRadians(player.getXRot()), (float) Math.toRadians(player.getYRot()), 0.1, (int) (180 * (1 - attack_weak)), 3);

		combo++;
		combo %= 3;
	}

	private InteractionResult interact(Player mplayer, Level mworld, InteractionHand mhand, Entity mentity, @Nullable EntityHitResult mhitResult) {
		ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
		if (!config.hit_effect){
			return InteractionResult.PASS;
		}
		this.entity = mentity;
		if (this.entity instanceof LivingEntity && this.entity.isAlive()) {
			this.player = mplayer;
			this.world = mworld;
			this.hand = mhand;


			this.slashEffect();
		}
		return InteractionResult.PASS;
	}
}



