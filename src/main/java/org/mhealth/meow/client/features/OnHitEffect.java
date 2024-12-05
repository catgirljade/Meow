package org.mhealth.meow.client.features;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.mhealth.meow.client.util.ParticleUtils;

public class OnHitEffect {
	private final double STEP = Math.toRadians(30);
	private final double PLAYER_HEIGHT = 1.8;
	private static final float[] TRIDENT_PITCH = {0.75f, 1f, 0.5f};
	private static final float[] SWEEP_PITCH = {0.75f, 0.9f, 0.55f};

	private final Vector3f COLOUR = new Vector3f(0.2F, 0, 0.9F);

	Player player;
	Level world;
	InteractionHand hand;
	Entity entity;
	private int combo = 0;

	public void init() {
		AttackEntityCallback.EVENT.register((mplayer, mworld, mhand, mentity, mhitResult) -> {
			System.out.println();
			entity = mentity;
			if (entity instanceof LivingEntity && entity.isAlive()) {
				player = mplayer;
				world = mworld;
				hand = mhand;


				dark_combos();
			}
			return InteractionResult.PASS;
		});
	}

	void dark_combos() {
		double attack_weak = 1 - player.getAttackStrengthScale(0.0F);
		BlockPos pos = new BlockPos( new Vec3i((int) player.getX(), (int) player.getY(), (int) player.getZ()));
		world.playSound(player, pos, SoundEvents.CHAIN_FALL, SoundSource.PLAYERS, 0.8f, 1.6f);
		world.playSound(player, pos, SoundEvents.RESPAWN_ANCHOR_DEPLETE.value(), SoundSource.PLAYERS, 0.3f, TRIDENT_PITCH[combo]);
		world.playSound(player, pos, SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1, SWEEP_PITCH[combo]);
		world.playSound(player, pos, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 0.4f, TRIDENT_PITCH[combo]);
		world.playSound(player, pos, SoundEvents.TRIDENT_HIT, SoundSource.PLAYERS, 0.7f, 1.8f);
		world.playSound(player, pos, SoundEvents.TRIDENT_RETURN, SoundSource.PLAYERS, 1, 1.0f);
		world.playSound(player, pos, SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 0.4f, 1.5f);
		world.playSound(player, pos, SoundEvents.TRIDENT_RIPTIDE_3, SoundSource.PLAYERS, 0.5f, 2.0f);
		Vector3d eye_pos = new Vector3d(player.getX(), player.getY() + PLAYER_HEIGHT, player.getZ());
		ParticleUtils.create_arc((radius, center, vec) -> {
			world.addParticle(new DustColorTransitionOptions(new Vector3f(0.7F, 0, 0.1F * (float) radius),
									new Vector3f(0, 0, 0), 1),
						center.x - vec.x, center.y - vec.y, center.z - vec.z, 0, 0, 0);
		}, eye_pos, Math.max(player.position().distanceTo(entity.position().lerp(entity.getEyePosition(), 0.5)), 1.0f),
					(int) (-180 + attack_weak * 90), (int) (90 * attack_weak), (combo - 1) * STEP + Math.PI / 2,
					Math.toRadians(player.getXRot()), Math.toRadians(player.getYRot()), 8, (int) (180 * (1 - attack_weak)), 3);

		combo++;
		combo %= 3;
	}

}



