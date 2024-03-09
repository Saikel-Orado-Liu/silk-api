/*
 * This file is part of Silk API.
 * Copyright (C) 2023 Saikel Orado Liu
 *
 * Silk API is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Silk API is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Silk API. If not, see <https://www.gnu.org/licenses/>.
 */

package pers.saikel0rado1iu.silk.util.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

import static net.minecraft.client.gui.screen.world.CreateWorldScreen.HEADER_SEPARATOR_TEXTURE;
import static pers.saikel0rado1iu.silk.util.screen.mod.ScreenTab.TAP_BOTTOM;

/**
 * <h2 style="color:FFC800">用于自定义背景的列表控件</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public class ConfigListWidget extends OptionListWidget implements CustomBackground {
	protected Identifier background;
	
	public ConfigListWidget(MinecraftClient minecraftClient, int width, int height, GameOptionsScreen optionsScreen) {
		super(minecraftClient, width, height, optionsScreen);
	}
	
	@Override
	public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		int scrollbarPositionX = getScrollbarPositionX();
		int posX = scrollbarPositionX + 6;
		RenderSystem.setShaderColor(0.25F, 0.25F, 0.25F, 1.0F);
		if (getBackground() != null) {
			RenderSystem.setShaderTexture(0, getBackground());
			context.drawTexture(getBackground(), 0, 0, 0, 0, 0, width, height - TAP_BOTTOM, width, height);
		}
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		
		int rowLeft = getRowLeft();
		int scrollAmount = getY() + 4 - (int) getScrollAmount();
		enableScissor(context);
		
		renderHeader(context, rowLeft, scrollAmount);
		
		renderList(context, mouseX, mouseY, delta);
		context.disableScissor();
		RenderSystem.setShaderTexture(0, HEADER_SEPARATOR_TEXTURE);
		context.drawTexture(HEADER_SEPARATOR_TEXTURE, getX(), 0, 0.0F, 0.0F, width, getY(), 32, 32);
		context.fillGradient(getX(), getY(), getRight(), getY() + 4, -16777216, 0);
		
		int maxScroll = getMaxScroll();
		if (maxScroll > 0) {
			int pos = (int) ((float) ((getBottom() - getY()) * (getBottom() - getY())) / (float) getMaxPosition());
			pos = MathHelper.clamp(pos, 32, getBottom() - getY() - 8);
			int o = Math.max(getY(), (int) getScrollAmount() * (getBottom() - getY() - pos) / maxScroll + getY());
			
			context.fill(scrollbarPositionX, getY(), posX, getBottom(), -16777216);
			context.fill(scrollbarPositionX, o, posX, o + pos, -8355712);
			context.fill(scrollbarPositionX, o, posX - 1, o + pos - 1, -4144960);
		}
		
		renderDecorations(context, mouseX, mouseY);
		RenderSystem.disableBlend();
	}
	
	@Override
	public @Nullable Identifier getBackground() {
		return background;
	}
	
	@Override
	public void setBackground(Identifier background) {
		this.background = background;
	}
}
