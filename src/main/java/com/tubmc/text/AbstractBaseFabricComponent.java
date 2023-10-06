package com.tubmc.text;

import java.awt.Color;
import java.util.Collection;
import java.util.List;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import com.tubmc.text.interaction.ClickInteraction;
import com.tubmc.text.interaction.ClickType;
import com.tubmc.text.interaction.EntityHoverData;
import com.tubmc.text.interaction.HoverInteraction;
import com.tubmc.text.interaction.HoverType;
import com.tubmc.text.interaction.ItemHoverData;

import fun.bb1.objects.defineables.ITypedProxy;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.HoverEvent.EntityContent;
import net.minecraft.text.HoverEvent.ItemStackContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

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
abstract class AbstractBaseFabricComponent implements ImplementationComponentBase, ITypedProxy<Text> {
	
	protected final @NotNull MutableText internal;
	
	protected AbstractBaseFabricComponent(final @NotNull MutableText internal) {
		this.internal = internal;
		if (this.internal.getStyle() == null) {
			this.internal.setStyle(Style.EMPTY);
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final @Nullable Collection<@NotNull IComponent> getChildren() {
		if (this.internal.getSiblings() == null || this.internal.getSiblings().isEmpty()) {
			return List.of();
		}
		return this.internal.getSiblings().stream().map(FabricTextImplemenation::wrapComponent).toList();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setChildren(@NotNull final Collection<@NotNull IComponent> newChildrenComponents) {
		this.internal.getSiblings().clear();
		if (newChildrenComponents == null || newChildrenComponents.isEmpty()) {
			return;
		}
		this.internal.getSiblings().addAll(newChildrenComponents.stream().map(FabricTextImplemenation::unwrapComponent).toList());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final @Nullable Color getColor() {
		return new Color(this.internal.getStyle().getColor().getRgb());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setColor(@Nullable final Color newColor) {
		if (newColor == null) {
			this.internal.setStyle(this.internal.getStyle().withColor((Formatting)null));
			return;
		}
		this.internal.setStyle(this.internal.getStyle().withColor(newColor.getRGB()));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isBold() {
		return this.internal.getStyle().isBold();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setBold(final boolean isBold) {
		this.internal.setStyle(this.internal.getStyle().withBold(isBold));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isItalic() {
		return this.internal.getStyle().isItalic();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setItalic(final boolean isItalic) {
		this.internal.setStyle(this.internal.getStyle().withItalic(isItalic));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isUnderlined() {
		return this.internal.getStyle().isUnderlined();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setUnderlined(final boolean isUnderlined) {
		this.internal.setStyle(this.internal.getStyle().withUnderline(isUnderlined));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isStrikedThrough() {
		return this.internal.getStyle().isStrikethrough();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setStrikeThrough(final boolean isStrikedThrough) {
		this.internal.setStyle(this.internal.getStyle().withStrikethrough(isStrikedThrough));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isObfuscated() {
		return this.internal.getStyle().isObfuscated();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setObfuscated(final boolean isObfuscated) {
		this.internal.setStyle(this.internal.getStyle().withObfuscated(isObfuscated));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final @Nullable String getFont() {
		return this.internal.getStyle().getFont().toString();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setFont(@Nullable final String newFont) {
		this.internal.setStyle(this.internal.getStyle().withFont(Identifier.tryParse(newFont)));
	}
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("deprecation")
	@Override
	public final @Nullable ClickInteraction getClicked() {
		if (this.internal.getStyle().getClickEvent() == null) return null;
		final ClickEvent old = this.internal.getStyle().getClickEvent();
		return new ClickInteraction(switch(old.getAction()) {
			case CHANGE_PAGE -> ClickType.GOTO_PAGE;
			case COPY_TO_CLIPBOARD -> ClickType.CLIPBOARD;
			case OPEN_FILE -> ClickType.OPEN_FILE;
			case OPEN_URL -> ClickType.OPEN_URL;
			case RUN_COMMAND -> ClickType.EXECUTE;
			default -> ClickType.SUGGEST;
		}, old.getValue());
	}
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("deprecation")
	@Override
	public final void setClicked(@Nullable final ClickInteraction interaction) {
		if (interaction == null) {
			this.internal.setStyle(this.internal.getStyle().withClickEvent(null));
			return;
		}
		this.internal.setStyle(this.internal.getStyle().withClickEvent(new ClickEvent(switch (interaction.type()) {
			case CLIPBOARD -> ClickEvent.Action.COPY_TO_CLIPBOARD;
			case GOTO_PAGE -> ClickEvent.Action.CHANGE_PAGE;
			case OPEN_FILE -> ClickEvent.Action.OPEN_FILE;
			case OPEN_URL -> ClickEvent.Action.OPEN_URL;
			case EXECUTE -> ClickEvent.Action.RUN_COMMAND;
			default -> ClickEvent.Action.SUGGEST_COMMAND;
		}, interaction.data())));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final @Nullable HoverInteraction<?> getHovering() {
		if (this.internal.getStyle().getHoverEvent() == null) return null;
		final HoverEvent old = this.internal.getStyle().getHoverEvent();
		if (old.getAction().equals(HoverEvent.Action.SHOW_ENTITY)) {
			final EntityContent context = old.getValue(HoverEvent.Action.SHOW_ENTITY);
			return new HoverInteraction<EntityHoverData>(HoverType.ENTITY, new EntityHoverData(FabricTextImplemenation.wrapComponent(context.name), Registries.ENTITY_TYPE.getId(context.entityType).toString(), context.uuid));
		}
		if (old.getAction().equals(HoverEvent.Action.SHOW_ITEM)) {
			final ItemStackContent context = old.getValue(HoverEvent.Action.SHOW_ITEM);
			return new HoverInteraction<ItemHoverData>(HoverType.ITEM, new ItemHoverData(Registries.ITEM.getKey(context.asStack().getItem()).toString(), context.asStack().getCount(), context.asStack().getNbt() == null ? null : new Gson().toJson(NbtCompound.CODEC.encode(context.asStack().getNbt(), JsonOps.INSTANCE, JsonOps.INSTANCE.empty()))));
		}
		return new HoverInteraction<IComponent>(HoverType.TEXT, FabricTextImplemenation.wrapComponent(old.getValue(HoverEvent.Action.SHOW_TEXT)));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setHovering(@Nullable final HoverInteraction<?> interaction) {
		if (interaction == null) {
			this.internal.setStyle(this.internal.getStyle().withHoverEvent(null));
			return;
		}
		if (interaction.data() instanceof EntityHoverData entityHover) {
			this.internal.setStyle(this.internal.getStyle().withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new EntityContent(Registries.ENTITY_TYPE.get(Identifier.tryParse(entityHover.typeIdentifier())), entityHover.entityUUID(), FabricTextImplemenation.unwrapComponent(entityHover.entityName())))));
			return;
		}
		if (interaction.data() instanceof ItemHoverData itemHover) {
			final ItemStack stack = Registries.ITEM.get(Identifier.tryParse(itemHover.itemIdentifier())).getDefaultStack();
			stack.setCount(itemHover.count());
			if (itemHover.nbtTagAsString() != null) {
				stack.setNbt(NbtCompound.CODEC.decode(JsonOps.INSTANCE, JsonParser.parseString(itemHover.nbtTagAsString())).get().orThrow().getFirst());
			}
			this.internal.setStyle(this.internal.getStyle().withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ItemStackContent(stack))));
			return;
		}
		this.internal.setStyle(this.internal.getStyle().withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, FabricTextImplemenation.unwrapComponent((IComponent)interaction.data()))));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final @NotNull Text getProxiedObject() {
		return this.internal;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract @NotNull AbstractBaseFabricComponent clone();
}