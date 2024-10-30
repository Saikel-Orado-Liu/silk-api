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

package pers.saikel0rado1iu.silk.api.client.pattern.screen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.TabNavigationWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.api.client.pattern.tab.ScreenTab;
import pers.saikel0rado1iu.silk.api.client.pattern.widget.ButtonHelper;

import java.util.ArrayList;

import static com.mojang.blaze3d.systems.RenderSystem.setShaderColor;
import static com.mojang.blaze3d.systems.RenderSystem.setShaderTexture;

/**
 * <h2 style="color:FFC800">模组屏幕</h2>
 * 用于创建一个统一原版风格的模组主页
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public class ModScreen extends GameOptionsScreen {
	protected final ImmutableList<ScreenTab> tabs;
	private final TabManager tabManager = new TabManager(this::addDrawableChild, this::remove);
	private final int mainTabIndex;
	private final Identifier background;
	private GridWidget grid;
	private TabNavigationWidget tabNavigation;
	private TextWidget verWidget;
	private TextWidget licenseWidget;
	private int tempIndex = -1;
	private int tempSize = -1;
	
	/**
	 * @param parent 父屏幕
	 * @param tab    选项卡
	 * @param tabs   后续选项卡
	 */
	public ModScreen(@Nullable Screen parent, ScreenTab tab, ScreenTab... tabs) {
		this(parent, 0, tab, tabs);
	}
	
	/**
	 * @param parent       父屏幕
	 * @param mainTabIndex 主选项卡索引
	 * @param tab          选项卡
	 * @param tabs         后续选项卡
	 */
	public ModScreen(@Nullable Screen parent, int mainTabIndex, ScreenTab tab, ScreenTab... tabs) {
		this(parent, null, mainTabIndex, tab, tabs);
	}
	
	/**
	 * @param parent       父屏幕
	 * @param background   屏幕背景
	 * @param mainTabIndex 主选项卡索引
	 * @param tab          选项卡
	 * @param tabs         后续选项卡
	 */
	public ModScreen(@Nullable Screen parent, @Nullable Identifier background, int mainTabIndex, ScreenTab tab, ScreenTab... tabs) {
		super(parent, null, Text.of(""));
		this.background = background;
		this.mainTabIndex = mainTabIndex;
		this.tabs = ImmutableList.copyOf(Lists.asList(tab, tabs));
	}
	
	/**
	 * 重置屏幕以便重新初始化选项，某些时候很有用
	 */
	public void reset() {
		tempIndex = -1;
	}
	
	protected void tabWidgetReset(ScreenTab tab) {
		tab.getDrawableWidgetList().forEach(this::remove);
		tab.getSelectableWidgetList().forEach(this::remove);
		tab.getDrawableWidgetList().clear();
		tab.getSelectableWidgetList().clear();
		tab.init(client, textRenderer, width, height);
		tab.getDrawableWidgetList().forEach(this::addDrawableChild);
		tab.getSelectableWidgetList().forEach(this::addSelectableChild);
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context, mouseX, mouseY, delta);
		context.drawTexture(FOOTER_SEPARATOR_TEXTURE, 0, MathHelper.roundUpToMultiple(height - 36 - 2, 2), 0, 0, width, 2, 32, 2);
		super.render(context, mouseX, mouseY, delta);
		for (int count = 0; count < tabs.size(); count++) {
			if (tabNavigation.getFocused() == null) {
				if (count == mainTabIndex) {
					if (tempIndex != count || tempSize != width * height) tabWidgetReset(tabs.get(count));
					tabs.get(count).render(client, textRenderer, context, mouseX, mouseY, delta, width, height);
					tempIndex = count;
					tempSize = width * height;
				} else {
					tabs.get(count).getDrawableWidgetList().forEach(this::remove);
					tabs.get(count).getSelectableWidgetList().forEach(this::remove);
				}
			} else {
				if (tabNavigation.children().get(count).equals(tabNavigation.getFocused())) {
					if (tempIndex != count || tempSize != width * height) tabWidgetReset(tabs.get(count));
					tabs.get(count).render(client, textRenderer, context, mouseX, mouseY, delta, width, height);
					tempIndex = count;
					tempSize = width * height;
				} else {
					tabs.get(count).getDrawableWidgetList().forEach(this::remove);
					tabs.get(count).getSelectableWidgetList().forEach(this::remove);
				}
			}
		}
		tabNavigation.render(context, mouseX, mouseY, delta);
		String modLicense = new ArrayList<>(tabs.getFirst().modData().licenses()).isEmpty() ? "ARR" : new ArrayList<>(tabs.getFirst().modData().licenses()).getFirst();
		verWidget.setPosition(0, height - 12);
		licenseWidget.setPosition(width - textRenderer.getWidth(modLicense), height - 12);
	}
	
	@Override
	protected void renderPanoramaBackground(DrawContext context, float delta) {
		if (null == background) {
			super.renderPanoramaBackground(context, delta);
		} else {
			setShaderTexture(0, background);
			context.drawTexture(background, 0, 0, 0, 0, 0, width, height, width, height);
			setShaderColor(1, 1, 1, 1);
		}
	}
	
	@Override
	public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
		super.renderBackground(context, mouseX, mouseY, delta);
		RenderSystem.enableBlend();
		context.drawTexture(FOOTER_SEPARATOR_TEXTURE, 0, height - layout.getFooterHeight() - 2, 0, 0, width, 2, 32, 2);
		RenderSystem.disableBlend();
		if (null == background) {
			super.renderPanoramaBackground(context, delta);
		} else {
			setShaderTexture(0, background);
			setShaderColor(0.5F, 0.5F, 0.5F, 1.0F);
			context.drawTexture(background, 0, 0, 0, 0, 0, width, height - layout.getFooterHeight() - 2, width, height);
			setShaderColor(1, 1, 1, 1);
		}
	}
	
	@Override
	protected void init() {
		tabs.forEach(tab -> {
			tab.setParent(this);
			tabWidgetReset(tab);
		});
		tabNavigation = TabNavigationWidget.builder(tabManager, width).tabs(tabs.toArray(new Tab[0])).build();
		addDrawableChild(tabNavigation);
		grid = new GridWidget().setColumnSpacing(10);
		GridWidget.Adder adder = grid.createAdder(1);
		adder.add(ButtonHelper.done(this).build());
		grid.forEachChild(child -> {
			child.setNavigationOrder(1);
			addDrawableChild(child);
		});
		tabNavigation.selectTab(mainTabIndex, false);
		initTabNavigation();
		String modVerString = tabs.getFirst().modData().version();
		addDrawableChild(verWidget = new TextWidget(0, height - 12, textRenderer.getWidth(modVerString), textRenderer.fontHeight, Text.of(modVerString), textRenderer));
		String modLicense = new ArrayList<>(tabs.getFirst().modData().licenses()).isEmpty() ? "ARR" : new ArrayList<>(tabs.getFirst().modData().licenses()).getFirst();
		addDrawableChild(licenseWidget = new TextWidget(width - textRenderer.getWidth(modLicense), height - 12, textRenderer.getWidth(modLicense), textRenderer.fontHeight, Text.of(modLicense), textRenderer));
	}
	
	@Override
	protected void initTabNavigation() {
		if (tabNavigation == null || grid == null) return;
		tabNavigation.setWidth(width);
		tabNavigation.init();
		grid.refreshPositions();
		SimplePositioningWidget.setPos(grid, 0, height - 36, width, 36);
		int otherAxis = tabNavigation.getNavigationFocus().getBottom();
		ScreenRect screenRect = new ScreenRect(0, otherAxis, width, grid.getY() - otherAxis);
		tabManager.setTabArea(screenRect);
	}
	
	/**
	 * 以公开方法访问游戏设置
	 *
	 * @return {@link ModScreen#gameOptions}
	 */
	public GameOptions gameOptions() {
		return gameOptions;
	}
}
