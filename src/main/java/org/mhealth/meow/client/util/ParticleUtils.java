package org.mhealth.meow.client.util;

import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.world.level.Level;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class ParticleUtils {
	@FunctionalInterface
	public interface spawnParticle{
		void spawn_particle(double radius, Vector3d center, Vector3d vec);
	}
	public static void create_arc(spawnParticle func, Vector3d center, double radius, int startingDegrees, int endingDegrees, double angle, double pitch, double yaw, int spacing, int arcInc, int rings) {
		double radiusInc = (Math.PI / (endingDegrees - startingDegrees));

		double mDegrees = startingDegrees;
		double mPI = 0;

		Vector3d vec;
		for (int i = 0; i < rings; i++) {
			for (double d = mDegrees; d < mDegrees + arcInc; d += spacing / radius) {
				double radian1 = Math.toRadians(d);
				double radiusSpacing = Math.sin(mPI) * i;
				vec = new Vector3d(Math.cos(radian1) * (radius + radiusSpacing), 0, Math.sin(radian1) * (radius + radiusSpacing));
				vec.rotateZ(angle);
				vec.rotateX(pitch);
				vec.rotateY(-yaw);

				func.spawn_particle(radius, center, vec);
			}

			mPI += radiusInc * 2.5;
		}

	}
}
