package com.tubmc.text;

import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.text.MutableText;
import net.minecraft.text.SelectorTextContent;

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
final class SelectorFabricComponent extends AbstractBaseFabricComponent implements ISelectedComponent {

	@Internal
	SelectorFabricComponent(@NotNull final MutableText internal) {
		super(internal);
	}
	
	private final @NotNull SelectorTextContent getContent() {
		return (SelectorTextContent) this.internal.getContent();
	}

	@Override
	public final @NotNull String getSelector() {
		return this.getContent().getPattern();
	}

	@Override
	public final void setSelector(@NotNull final String newSelector) {
		this.internal.content = new SelectorTextContent(newSelector, this.getContent().getSeparator());
	}
	
	@Override
	public final @Nullable IComponent getSeperator() {
		return FabricTextImplemenation.wrapComponent(this.getContent().getSeparator().orElse(null));
	}

	@Override
	public final void setSeperator(@Nullable final IComponent newSeperator) {
		this.internal.content = new SelectorTextContent(this.getContent().getPattern(), Optional.ofNullable(FabricTextImplemenation.unwrapComponent(newSeperator)));
	}

	@Override
	public final @NotNull AbstractBaseFabricComponent clone() {
		return new SelectorFabricComponent(this.internal.copy());
	}

}
