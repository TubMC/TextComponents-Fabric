package com.tubmc.text;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.text.KeybindTextContent;
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
final class KeybindFabricComponent extends AbstractBaseFabricComponent implements IKeybindComponent {

	@Internal
	KeybindFabricComponent(@NotNull final MutableText internal) {
		super(internal);
	}
	
	private final @NotNull KeybindTextContent getContent() {
		return (KeybindTextContent) this.internal.getContent();
	}

	@Override
	public final @Nullable String getKeybind() {
		return this.getContent().getKey();
	}

	@Override
	public final void setKeybind(@Nullable final String newKeybind) {
		this.internal.content = new KeybindTextContent(newKeybind == null ? "" : newKeybind);
	}
	
	@Override
	public final @NotNull AbstractBaseFabricComponent clone() {
		return new KeybindFabricComponent(this.internal.copy());
	}

}
