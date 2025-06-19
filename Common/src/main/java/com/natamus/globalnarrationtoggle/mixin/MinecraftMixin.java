package com.natamus.globalnarrationtoggle.mixin;

import com.natamus.globalnarrationtoggle.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Minecraft.class, priority = 1001)
public class MinecraftMixin {
	@Inject(method = "<init>(Lnet/minecraft/client/main/GameConfig;)V", at = @At(value = "TAIL"))
	public void Minecraft(GameConfig gameConfig, CallbackInfo ci) {
		if (Util.isGloballyToggledOff()) {
			Minecraft.getInstance().options.onboardAccessibility = false;
		}
	}
}
