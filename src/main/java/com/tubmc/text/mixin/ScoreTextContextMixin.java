package com.tubmc.text.mixin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.tubmc.text.definables.IForceableScoreTextContent;

import net.minecraft.command.EntitySelector;
import net.minecraft.server.command.ServerCommandSource;

/**
 *    Copyright 2023 TubMC.com
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
@Mixin(EntitySelector.class)
public class ScoreTextContextMixin implements IForceableScoreTextContent {
	
	@Unique
	private @Nullable String _value = null;

	@Override
	public final void setForcedValue$TextComponents(@Nullable final String value) {
		this._value = value;
	}

	@Override
	public final @Nullable String getForcedValue$TextComponents() {
		return this._value;
	}
	
	@Inject(method = "Lnet/minecraft/text/ScoreTextContent;getScore(Ljava/lang/String;Lnet/minecraft/server/command/ServerCommandSource;)Ljava/lang/String;", at = @At("HEAD"), cancellable = true)
	public final void getScore$TextComponents(final String _1, final ServerCommandSource _2, @NotNull final CallbackInfoReturnable<String> returnable) {
		if (this._value != null) {
			returnable.setReturnValue(this._value);
			returnable.cancel();
		}
	}
	
}
