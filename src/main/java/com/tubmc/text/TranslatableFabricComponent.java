package com.tubmc.text;

import java.util.Collection;
import java.util.stream.Stream;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.text.MutableText;
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
final class TranslatableFabricComponent extends AbstractBaseFabricComponent implements ITranslatableComponent {

	@Internal
	TranslatableFabricComponent(@NotNull final MutableText internal) {
		super(internal);
	}
	
	private final @NotNull TranslatableTextContent getContent() {
		return (TranslatableTextContent) this.internal.getContent();
	}

	@Override
	public final @NotNull String getTranslationKey() {
		return this.getContent().getKey();
	}

	@Override
	public final void setTranslationKey(@NotNull final String newTranslationKey) {
		this.internal.content = new TranslatableTextContent(newTranslationKey, this.getTranslationFallback(), this.getContent().getArgs());
	}

	@Override
	public final @Nullable String getTranslationFallback() {
		return this.getContent().getFallback();
	}

	@Override
	public final void setTranslationFallback(@Nullable final String newFallback) {
		this.internal.content = new TranslatableTextContent(this.getTranslationKey(), newFallback, this.getContent().getArgs());
	}

	@Override
	public final @Nullable Collection<@NotNull IComponent> getInsertions() {
		return Stream.of(this.getContent().getArgs()).map(o -> o instanceof Text text ? FabricTextImplemenation.wrapComponent(text) : AbstractImplementation.IMPLEMENTATION.createLiteral(o.toString())).toList();
	}

	@Override
	public void setInsertions(@NotNull final Collection<@NotNull IComponent> newInsertions) {
		this.internal.content = new TranslatableTextContent(this.getTranslationKey(), this.getTranslationFallback(), newInsertions.stream().map(FabricTextImplemenation::unwrapComponent).toArray());
	}

	@Override
	public final @NotNull AbstractBaseFabricComponent clone() {
		return new TranslatableFabricComponent(this.internal.copy());
	}

}
