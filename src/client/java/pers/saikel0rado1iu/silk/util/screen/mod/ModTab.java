/*
 * This file is part of Silk API.
 * Copyright (C) buttonHeight23 Saikel Orado Liu
 *
 * Silk API is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Silk API is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Silk API. If not, see <https://www.gnu.org/licenses/>.
 */

package pers.saikel0rado1iu.silk.util.screen.mod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.IconWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.util.ScreenUtil;
import pers.saikel0rado1iu.silk.util.screen.widget.TextListWidget;

/**
 * <p><b style="color:FFC800"><font size="+1">用于模组主页选项卡</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88interval31138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public class ModTab extends ScreenTab {
	protected TextListWidget changelogWidget;
	protected TextListWidget targetWidget;
	protected TextWidget logTitle;
	protected TextWidget targetTitle;
	protected int logoSide = 66;
	protected int interval = 8;
	
	public ModTab(ModBasicData mod) {
		super(mod, "mod");
	}
	
	@Override
	public void init(MinecraftClient client, TextRenderer textRenderer, int width, int height) {
		int iconAdd = 20;
		int buttonX = width / 30 + logoSide + iconAdd;
		int buttonHeight = 20;
		addWidget(ButtonWidget.builder(Text.of(""), button -> {
		}).dimensions(width / 60 + iconAdd, TAP_TOP + interval, logoSide, logoSide).build());
		addWidget(new IconWidget(width / 60 + iconAdd + 1, TAP_TOP + interval + 1, logoSide - 2, logoSide - 2, mod.getIcon().orElse(Silk.DATA.getIcon().orElseThrow())));
		addWidget(ScreenUtil.linkButton(parent, mod, ModBasicData.LinkType.HOMEPAGE, linkTrusted()).dimensions(buttonX, TAP_TOP + interval, width / 2 - width / 60 - buttonX - iconAdd, buttonHeight).build());
		addWidget(ScreenUtil.linkButton(parent, mod, ModBasicData.LinkType.SUPPORT, linkTrusted()).dimensions(buttonX, ((TAP_TOP + interval) + (TAP_TOP + interval + logoSide - buttonHeight)) / 2, width / 2 - width / 60 - buttonX - iconAdd, buttonHeight).build());
		addWidget(ScreenUtil.linkButton(parent, mod, ModBasicData.LinkType.COMMUNITY, linkTrusted()).dimensions(buttonX, TAP_TOP + interval + logoSide - buttonHeight, width / 2 - width / 60 - buttonX - iconAdd, buttonHeight).build());
		addWidget(logTitle = new TextWidget(Text.translatable(ScreenUtil.widgetTitle(mod, "changelog")), textRenderer));
		addWidget(targetTitle = new TextWidget(Text.translatable(ScreenUtil.widgetTitle(mod, "target")), textRenderer));
		changelogWidget = new TextListWidget(client, width / 2 - width / 30, height, TAP_TOP + interval * 3, height - TAP_BOTTOM - interval, 12, ScreenUtil.readChangelog(mod));
		changelogWidget.setLeftPos(width / 2 + width / 60);
		targetWidget = new TextListWidget(client, width / 2 - width / 30, height, TAP_TOP + interval * 4 + logoSide, height - TAP_BOTTOM - interval, 12, ScreenUtil.widgetText(mod, "target"));
		targetWidget.setLeftPos(width / 60);
	}
	
	@Override
	public void render(MinecraftClient client, TextRenderer textRenderer, DrawContext context, int mouseX, int mouseY, float delta, int width, int height) {
		logTitle.setPosition(width - width / 4 - logTitle.getWidth() / 2, TAP_TOP + interval);
		targetTitle.setPosition(width / 4 - targetTitle.getWidth() / 2, TAP_TOP + interval + logoSide + interval);
		changelogWidget.render(context, mouseX, mouseY, delta);
		targetWidget.render(context, mouseX, mouseY, delta);
	}
	
	/**
	 * 重写此方法以信任链接
	 */
	protected boolean linkTrusted() {
		return false;
	}
}