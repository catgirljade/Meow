package org.jade.hiteffects.util;

import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

public class ParticleUtils {
	static List<Callable<Boolean>> current = new ArrayList<>();

	public static void create_arc(CleaveAnimation func, double radius, int startingDegrees, int endingDegrees, float angle, float pitch, float yaw, double spacing, int arcInc, int rings) {
		double radiusInc = (Math.PI / (endingDegrees - startingDegrees));

		double mPI = 0;

		Vec3 vec;
		for (double d = startingDegrees; d < (double) startingDegrees + arcInc; d += spacing * 90 / radius) {

			for (int i = 0; i < rings; i++) {
				double radian1 = Math.toRadians(d);
				double radiusSpacing = Math.sin(mPI) * i;
				vec = new Vec3(Math.cos(radian1) * (radius + radiusSpacing), 0, Math.sin(radian1) * (radius + radiusSpacing));
				vec = vec.zRot(angle);
				vec = vec.xRot(-pitch);
				vec = vec.yRot(-yaw);

				func.spawn_particle(vec, radius);
				mPI += radiusInc * 2.5;
			}
			mPI = 0;
		}
	}

	public static void drawParticleLineSlash(LineSlashAnimation animation, float length, float angle, float pitch, float yaw, double spacing, int duration) {
		List<Vec3> points = new ArrayList<>();
		Vec3 vec = new Vec3(0, 0, 1);
		vec = vec.zRot(angle);
		vec = vec.xRot(pitch);
		vec = vec.yRot(yaw);
		vec = vec.normalize();

		for (double ln = -length; ln < length; ln += spacing) {
			Vec3 point = vec.scale(ln);
			points.add(point);
		}

		if (duration <= 0) {
			System.out.println();
			boolean midReached = false;
			for (int i = 0; i < points.size(); i++) {
				Vec3 point = points.get(i);
				boolean middle = !midReached && i == points.size() / 2;
				if (middle) {
					midReached = true;
					System.out.println(point);
				}
				animation.lineSlashAnimation(point,
							1D - (point.distanceTo(Vec3.ZERO) / length), (double) (i + 1) / points.size(), middle);
			}
		} else {
			current.add(new Callable<>() {

				final int mPointsPerTick = (int) (points.size() * (1D / duration));
				int mT = 0;
				boolean mMidReached = false;

				@Override
				public Boolean call() {
					for (int i = mPointsPerTick * mT; i < Math.min(points.size(), mPointsPerTick * (mT + 1)); i++) {
						Vec3 point = points.get(i);
						boolean middle = !mMidReached && i == points.size() / 2;
						if (middle) {
							mMidReached = true;
						}
						animation.lineSlashAnimation(point,
									1D - (point.distanceTo(Vec3.ZERO) / length), (double) (i + 1) / points.size(), middle);
					}
					mT++;

					if (mT >= duration) {
						return Boolean.TRUE;
					}
					return Boolean.FALSE;
				}
			});
		}
	}

	public static void tick() {
		for (Iterator<Callable<Boolean>> callableIterator = current.iterator(); callableIterator.hasNext(); ) {
			Callable<Boolean> callable = callableIterator.next();
			try {
				boolean result = callable.call();
				if (result){
					callableIterator.remove();
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	@FunctionalInterface
	public interface CleaveAnimation {
		void spawn_particle(Vec3 vec, double radius);
	}


	@FunctionalInterface
	public interface LineSlashAnimation {

		void lineSlashAnimation(Vec3 center, double middleProgress, double endProgress, boolean middle);
	}


}
