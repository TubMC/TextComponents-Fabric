package com.tubmc.text;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

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
public class TextComponents implements ModInitializer {
	
    public static final @NotNull Logger LOGGER = LoggerFactory.getLogger("textcomponents");
    public static @Nullable MinecraftServer SERVER;

	@Override
	public void onInitialize() {
		if (AbstractImplementation.IMPLEMENTATION == null) {
			ServerLifecycleEvents.SERVER_STARTED.register((mc) -> {
				TextComponents.SERVER = mc;
			});
			new FabricTextImplemenation();
		}
	}
}