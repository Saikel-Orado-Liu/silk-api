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

package pers.saikel0rado1iu.silk.api.client.pattern.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

import java.util.Optional;

/**
 * <h2 style="color:FFC800">文本列表控件</h2>
 * 用于显示列表的文本控件
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public class TextListWidget extends ScrollableWidget implements CustomBackground {
	private final Contents contents;
	
	public TextListWidget(int x, int y, int width, int height, TextRenderer textRenderer, String text) {
		super(x, y, width, height, Text.empty());
		ContentsBuilder contentsBuilder = new ContentsBuilder(getGridWidth());
		contentsBuilder.appendText(textRenderer, Text.literal(text));
		this.contents = contentsBuilder.build();
	}
	
	@Override
	protected int getContentsHeight() {
		return contents.grid().getHeight();
	}
	
	@Override
	protected double getDeltaYPerScroll() {
		return 9;
	}
	
	@Override
	protected void renderContents(DrawContext context, int mouseX, int mouseY, float delta) {
		context.getMatrices().push();
		context.getMatrices().translate(getX() + getPadding(), getY() + getPadding(), 0);
		contents.grid().forEachChild(widget -> widget.render(context, mouseX, mouseY, delta));
		context.getMatrices().pop();
	}
	
	@Override
	protected void drawBox(DrawContext context, int x, int y, int width, int height) {
		final int gradientHeight = 12;
		context.fillGradient(x, y, x + width, y + gradientHeight, ColorHelper.Argb.withAlpha(128, 0x000000), ColorHelper.Argb.withAlpha(0, 0x000000));
		context.fillGradient(x, y + height - gradientHeight, x + width, y + height, ColorHelper.Argb.withAlpha(0, 0x000000), ColorHelper.Argb.withAlpha(128, 0x000000));
		if (isFocused()) context.drawBorder(x, y, width, height, ColorHelper.Argb.fullAlpha(0xFFFFFF));
		else context.drawBorder(x, y, width, height, ColorHelper.Argb.fullAlpha(0x666666));
	}
	
	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {
		builder.put(NarrationPart.TITLE, contents.narration());
	}
	
	private int getGridWidth() {
		return width - getPaddingDoubled();
	}
	
	@Override
	public Optional<Identifier> background() {
		return Optional.empty();
	}
	
	@Environment(EnvType.CLIENT)
	record Contents(LayoutWidget grid, Text narration) {
	}
	
	@Environment(EnvType.CLIENT)
	static class ContentsBuilder {
		private final int gridWidth;
		private final DirectionalLayoutWidget layout;
		private final MutableText narration = Text.empty();
		
		public ContentsBuilder(int gridWidth) {
			this.gridWidth = gridWidth;
			this.layout = DirectionalLayoutWidget.vertical();
			this.layout.getMainPositioner().alignLeft();
			this.layout.add(EmptyWidget.ofWidth(gridWidth));
		}
		
		public void appendText(TextRenderer textRenderer, Text text) {
			layout.add(new MultilineTextWidget(text, textRenderer).setMaxWidth(gridWidth));
			narration.append(text).append("\n");
		}
		
		public Contents build() {
			layout.refreshPositions();
			return new Contents(layout, narration);
		}
	}
}
