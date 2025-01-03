package org.jade.hiteffects.mixin;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jade.hiteffects.features.OnDeathEffect;
import org.jade.hiteffects.util.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
	public LivingEntityMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}
	@Inject(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;level()Lnet/minecraft/world/level/Level;", ordinal = 1))
	public void onDie(DamageSource damageSource, CallbackInfo ci){
		if (level().isClientSide && config.kill_effect) {
			OnDeathEffect.createEffect((LivingEntity) (Object) this);
		}
	}
}
