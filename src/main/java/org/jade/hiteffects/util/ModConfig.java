package org.jade.hiteffects.util;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import org.joml.Vector3f;

@Config(name = "hiteffects")
public class ModConfig implements ConfigData {
	public boolean hit_effect = true;
	public boolean kill_effect = true;
	@ConfigEntry.Category("hit_effect_colour")
	@ConfigEntry.Gui.Tooltip()
	@ConfigEntry.Gui.CollapsibleObject
	public Colour hit_colours_initial = new Colour();
	@ConfigEntry.Category("hit_effect_colour")
	@ConfigEntry.Gui.Tooltip()
	@ConfigEntry.Gui.CollapsibleObject
	public Colour hit_colours_final = new Colour();
	@ConfigEntry.Category("kill_effect_colour")
	@ConfigEntry.Gui.Tooltip()
	@ConfigEntry.Gui.CollapsibleObject
	public Colour kill_colours_initial = new Colour();

	@ConfigEntry.Category("kill_effect_colour")
	@ConfigEntry.Gui.Tooltip()
	@ConfigEntry.Gui.CollapsibleObject
	public Colour kill_colours_final = new Colour();

	static float clamp(float val, float min, float max) {
		return Math.max(min, Math.min(max, val));
	}

	@Override
	public void validatePostLoad() {
		for (Colour colour : new Colour[]{hit_colours_initial, hit_colours_final, kill_colours_initial, kill_colours_final}) {
			colour.r = clamp(colour.r, 0, 1);
			colour.g = clamp(colour.g, 0, 1);
			colour.b = clamp(colour.b, 0, 1);
		}
	}

	public Vector3f colourToVec(Colour colour) {
		return new Vector3f(colour.r, colour.g, colour.b);
	}

	static class Colour {
		float r = 1;
		float g = 1;
		float b = 1;

		boolean isValid() {
			return (r <= 1 && g <= 1 && b <= 1) && (r >= 0 && g >= 0 && b >= 0);
		}


		void fixBroken() {
			r = clamp(r, 0, 1);
			g = clamp(g, 0, 1);
			b = clamp(b, 0, 1);

		}
	}

}
