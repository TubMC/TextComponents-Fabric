package com.tubmc.text;

import java.awt.Color;
import java.util.Collection;
import java.util.Optional;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.text.KeybindTextContent;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.NbtTextContent;
import net.minecraft.text.ScoreTextContent;
import net.minecraft.text.SelectorTextContent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

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
final class FabricTextImplemenation extends AbstractImplementation {
	
	@Contract("null -> null")
	@Internal
	static @Nullable final IComponent wrapComponent(@Nullable final Text toWrapImmutable) {
		if (toWrapImmutable == null) return null;
		if (!(toWrapImmutable instanceof MutableText toWrap)) throw new IllegalArgumentException("Wrap type not mutable!: " + toWrapImmutable.getClass().getName());
		if (toWrap.getContent() instanceof KeybindTextContent) return new KeybindFabricComponent(toWrap);
		if (toWrap.getContent() instanceof LiteralTextContent) return new LiteralFabricComponent(toWrap);
		if (toWrap.getContent() instanceof ScoreTextContent) return new ScoreboardFabricComponent(toWrap);
		if (toWrap.getContent() instanceof SelectorTextContent) return new SelectorFabricComponent(toWrap);
		if (toWrap.getContent() instanceof TranslatableTextContent) return new TranslatableFabricComponent(toWrap);
		if (toWrap.getContent() instanceof NbtTextContent nbt) {
			try {
				return new LiteralFabricComponent(nbt.parse(TextComponents.SERVER.getCommandSource(), null, 0));
			} catch (CommandSyntaxException e) {
				return ILiteralComponent.builder(nbt.getPath()).color(Color.RED).tooltip("Failed to read nbt!").build();
			}
		}
		throw new IllegalArgumentException("Unexpected wrap type: " + toWrap.getClass().getName());
	}
	
	@Contract("null -> null")
	@Internal
	static @Nullable final Text unwrapComponent(@Nullable final IComponent toUnwrap) {
		if (toUnwrap == null) return null;
		if (toUnwrap instanceof AbstractBaseFabricComponent component) return component.internal;
		throw new IllegalArgumentException("Unexpected unwrap type: " + toUnwrap.getClass().getName());
	}
	
	@Override
	public final @NotNull IKeybindComponent createKeybind(@NotNull final String keybind) {
		return new KeybindFabricComponent(MutableText.of(new KeybindTextContent(keybind)));
	}

	@Override
	public final @NotNull ILiteralComponent createLiteral(@NotNull final String text) {
		return new LiteralFabricComponent(MutableText.of(new LiteralTextContent(text)));
	}

	@Override
	public final @NotNull IScoreboardComponent createScoreboard(@NotNull final String selector, @NotNull final String objective, @Nullable final String value) {
		final ScoreboardFabricComponent component = new ScoreboardFabricComponent(MutableText.of(new ScoreTextContent("*", objective)));
		component.setSelector(selector);
		component.setValue(value);
		return component;
	}

	@Override
	public final @NotNull ISelectedComponent createSelector(@NotNull final String selector, @Nullable final IComponent seperator) {
		return new SelectorFabricComponent(MutableText.of(new SelectorTextContent(selector, Optional.ofNullable(FabricTextImplemenation.unwrapComponent(seperator)))));
	}

	@Override
	public final @NotNull ITranslatableComponent createTranslatable(@NotNull final String translationKey, @Nullable final String fallback, @Nullable final Collection<@NotNull IComponent> insertions) {
		return new TranslatableFabricComponent(MutableText.of(new TranslatableTextContent(translationKey, fallback, insertions.stream().map(FabricTextImplemenation::unwrapComponent).toArray())));
	}
	
}
