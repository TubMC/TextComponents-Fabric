package com.tubmc.text;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;

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
final class LiteralFabricComponent extends AbstractBaseFabricComponent implements ILiteralComponent {

	@Internal
	LiteralFabricComponent(@NotNull final MutableText internal) {
		super(internal);
	}
	
	private final @NotNull LiteralTextContent getContent() {
		return (LiteralTextContent) this.internal.getContent();
	}

	@Override
	public final @Nullable String getMessage() {
		return this.getContent().string();
	}

	@Override
	public final void setMessage(@Nullable final String newMessage) {
		this.internal.content = new LiteralTextContent(newMessage == null ? "" : newMessage);
	}
	
	@Override
	public final @NotNull AbstractBaseFabricComponent clone() {
		return new LiteralFabricComponent(this.internal.copy());
	}
}
