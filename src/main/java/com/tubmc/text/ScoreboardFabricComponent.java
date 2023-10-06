package com.tubmc.text;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.tubmc.text.definables.IForceableScoreTextContent;
import com.tubmc.text.definables.IReversableEntitySelector;

import net.minecraft.command.EntitySelector;
import net.minecraft.command.EntitySelectorReader;
import net.minecraft.text.MutableText;
import net.minecraft.text.ScoreTextContent;

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
@Internal
final class ScoreboardFabricComponent extends AbstractBaseFabricComponent implements IScoreboardComponent {

	@Internal
	ScoreboardFabricComponent(@NotNull final MutableText internal) {
		super(internal);
	}
	
	private final @NotNull ScoreTextContent getContent() {
		return (ScoreTextContent) this.internal.getContent();
	}

	@Override
	public final @NotNull String getSelector() {
		if (this.getContent().getSelector() != null && this.getContent().getSelector() instanceof IReversableEntitySelector reverse) {
			return reverse.getOriginalString$TextComponents() == null ? this.getContent().getName() : reverse.getOriginalString$TextComponents();
		}
		return this.getContent().getName();
	}

	@Override
	public final void setSelector(@NotNull final String newSelector) {
		try {
			final EntitySelector selector = new EntitySelectorReader(new StringReader(newSelector)).read();
			final @Nullable String value = this.getValue();
			this.internal.content = new ScoreTextContent(selector.getEntity(TextComponents.SERVER.getCommandSource()).getEntityName(), this.getObjective());
			this.setValue(value);
			this.getContent().selector = selector;
		} catch (final CommandSyntaxException e) {
			throw new RuntimeException("Failed to apply selector", e);
		}
	}

	@Override
	public final @NotNull String getObjective() {
		return this.getContent().getObjective();
	}

	@Override
	public final void setObjective(@NotNull final String newObjective) {
		final EntitySelector selector = this.getContent().getSelector();
		final @Nullable String value = this.getValue();
		this.internal.content = new ScoreTextContent(this.getContent().getName(), newObjective);
		this.getContent().selector = selector;
		this.setValue(value);
	}

	@Override
	public final @Nullable String getValue() {
		if (this.getContent() instanceof IForceableScoreTextContent forceable) {
			return forceable.getForcedValue$TextComponents();
		}
		return null;
	}

	@Override
	public final void setValue(@Nullable final String newValue) {
		if (this.getContent() instanceof IForceableScoreTextContent forceable) {
			forceable.setForcedValue$TextComponents(newValue);
		}
	}

	@Override
	public final @NotNull AbstractBaseFabricComponent clone() {
		return new ScoreboardFabricComponent(this.internal.copy());
	}

}
