package com.tubmc.text.mixin;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.brigadier.StringReader;
import com.tubmc.text.definables.IReversableEntitySelector;

import net.minecraft.command.EntitySelector;
import net.minecraft.command.EntitySelectorReader;

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
@Mixin(EntitySelectorReader.class)
public class EntitySelectorReaderMixin {
	
	@Shadow
	private final StringReader reader = null;
	
	@Inject(method = "Lnet/minecraft/command/EntitySelectorReader;build()Lnet/minecraft/command/EntitySelector;", at = @At("RETURN"))
	public void onBuild$TextComponents(@NotNull final CallbackInfoReturnable<EntitySelector> returnable) {
		final EntitySelector entitySelector = returnable.getReturnValue();
		if (entitySelector instanceof IReversableEntitySelector reverse) {
			reverse.setOriginalString$TextComponents(this.reader.getString());
		}
		returnable.setReturnValue(entitySelector);
	}
	
}
