package com.natamus.globalnarrationtoggle.forge.mixin;

import com.natamus.globalnarrationtoggle.util.Util;
import net.minecraft.client.NarratorStatus;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(value = Options.class, priority = 1001)
public class OptionsMixin {
	@Shadow private @Final OptionInstance<NarratorStatus> narrator;

	@Inject(method = "save()V", at = @At(value = "HEAD"))
	public void save(CallbackInfo ci) {
		try {
			Util.initGlobalSync(narrator.get().getId());
		}
		catch (IOException ignored) {}
	}
}
