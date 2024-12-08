package org.jade.hiteffects.features;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jade.hiteffects.HitEffectClient;
import org.jade.hiteffects.util.ModConfig;
import org.jade.hiteffects.util.ParticleUtils;
import org.joml.Vector3f;

public class OnDeathEffect {
	final static int angles[][]= {
		{70, 45}, {70, -45}, {-70, -45}, {-70, 45}
	};
	// probably SHOULD NOT USE entity IN ANY COROUTINE/CALLABLE! (I want it to actually get deleted meow)
	public static void createEffect(LivingEntity entity) {
		Vec3 location = entity.position();
		BlockPos loc = new BlockPos(new Vec3i((int) location.x, (int) location.y, (int) location.z));
		HitEffectClient.mc.level.playSound(HitEffectClient.mc.player, loc, SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 0.5f, 0.75f);
		HitEffectClient.mc.level.playSound(HitEffectClient.mc.player, loc, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 0.9f, 1.5f);
		HitEffectClient.mc.level.playSound(HitEffectClient.mc.player, loc, SoundEvents.TRIDENT_RIPTIDE_3, SoundSource.PLAYERS, 0.75f, 1.25f);

		ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

		ParticleUtils.create_arc((vec, radius) -> {
						HitEffectClient.mc.level.addParticle(new DustColorTransitionOptions(config.colourToVec(config.kill_colours_initial), config.colourToVec(config.kill_colours_final), 1),
									location.x + vec.x, location.y + vec.y, location.z + vec.z, 0, 0, 0);
					},
					1.5, 0, 360, (float) Math.PI, 0.0F, (float) 0, 0.1, 360, 6);

		for (int i = 0; i < 4; ++i) {
			// assume eye height it the top of hit-box (IT Isn't bUT!!)
			ParticleUtils.drawParticleLineSlash((vec, middleProgress, end, middle) -> {
				HitEffectClient.mc.level.addParticle(new DustColorTransitionOptions(config.colourToVec(config.hit_colours_initial), config.colourToVec(config.hit_colours_final), 1),
							location.x + vec.x, location.y + vec.y + (entity.getEyeY() - location.y) / 2, location.z + vec.z, 0, 0, 0
				);
			}, 2, 0, (float) Math.toRadians(angles[i][0] - 90), (float) Math.toRadians(angles[i][1] - entity.getYHeadRot()) , 0.1, 4);
		}
	}
}
