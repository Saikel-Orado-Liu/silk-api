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

package pers.saikel0rado1iu.silk.util.config;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.moandjiezana.toml.TomlWriter;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static pers.saikel0rado1iu.silk.util.config.ConfigData.CHARSET;
import static pers.saikel0rado1iu.silk.util.config.ConfigData.CONFIG_PATH;

/**
 * <p><b style="color:FFC800"><font size="+1">用于配置数据写入</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
public final class ConfigWriter {
	private static final ScheduledExecutorService CONFIG_STORE_POOL = new ScheduledThreadPoolExecutor(0,
			new BasicThreadFactory.Builder().daemon(true).build());
	private final ConfigData configData;
	
	ConfigWriter(ConfigData configData) {
		this.configData = configData;
	}
	
	/**
	 * 设置保存了配置数据的 {@link LinkedProperties}
	 */
	static void setPropertiesConfigs(LinkedHashMap<?, ?> data, LinkedProperties ppt, String keyPrefix) {
		for (Object key : data.keySet()) {
			String fullKey = keyPrefix + key;
			if (data.get(key) instanceof LinkedHashMap<?, ?> map) setPropertiesConfigs(map, ppt, keyPrefix + key + ".");
			else ppt.setProperty(fullKey, String.valueOf(data.get(key)));
		}
	}
	
	/**
	 * 设置保存了数据的 XML 文件
	 */
	static void setXmlConfigs(LinkedHashMap<?, ?> data, TransformerHandler handler, AttributesImpl attr) throws SAXException {
		for (Object key : data.keySet()) {
			handler.startElement("", "", (String) key, attr);
			Object obj = data.get(key);
			if (obj instanceof LinkedHashMap<?, ?> map) setXmlConfigs(map, handler, attr);
			else handler.characters(obj.toString().toCharArray(), 0, obj.toString().length());
			handler.endElement("", "", (String) key);
		}
	}
	
	/**
	 * 获取保存配置应保存信息
	 */
	static LinkedHashMap<String, Object> getSaveConfigs(ConfigData configData) {
		LinkedHashMap<String, Object> saveConfigs = Maps.newLinkedHashMapWithExpectedSize(10);
		configData.configs.forEach((s, object) -> {
			if (object instanceof ConfigData data) saveConfigs.put(s, getSaveConfigs(data));
			else if (object instanceof List<?> list) saveConfigs.put(s, list.get(2));
			else saveConfigs.put(s, object);
		});
		return saveConfigs;
	}
	
	/**
	 * 用于添加附加信息
	 */
	static List<String> getAdditionalInfo(ConfigData configData) {
		List<String> list = new ArrayList<>(8);
		list.add("This configuration file is generated by '" + configData.mod.getName() + "' calling Silk API.");
		String separator = File.separator.contains("\\") ? "\\\\" : File.separator;
		String[] paths = configData.mod.getMod().getOrigin().getPaths().get(0).toString().split(separator);
		list.add("Mod Jar:      " + paths[paths.length - 1]);
		list.add("Mod Name:     " + configData.mod.getName());
		list.add("Mod ID:       " + configData.mod.getId());
		list.add("Mod Version:  " + configData.mod.getVersion());
		list.add("Mod Authors:  " + String.join(", ", configData.mod.getAuthors()));
		list.add("Mod Licenses: " + String.join(", ", configData.mod.getLicenses()));
		if (configData.mod.getLink(ModBasicData.LinkType.HOMEPAGE).isPresent())
			list.add("Mod HomePage: " + configData.mod.getLink(ModBasicData.LinkType.HOMEPAGE).get());
		list.add("Stored in " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd-HH:mm:ss")));
		return list;
	}
	
	/**
	 * 将以模组 ID 为名的配置文件保存在默认路径中
	 */
	@SilkApi
	public void save() {
		save(CONFIG_PATH);
	}
	
	/**
	 * 将以模组 ID 为名的配置文件保存在自定义保存路径中
	 */
	@SilkApi
	public void save(Path customPath) {
		switch (configData.mode) {
			case PROPERTIES -> save(customPath, configData.mod.getId() + ".properties");
			case XML -> save(customPath, configData.mod.getId() + ".xml");
			case JSON -> save(customPath, configData.mod.getId() + ".json");
			case TOML -> save(customPath, configData.mod.getId() + ".toml");
		}
	}
	
	/**
	 * 将特定名称的配置文件保存在自定义保存路径中
	 */
	@SilkApi
	public void save(Path customPath, String fileName) {
		CONFIG_STORE_POOL.schedule(new ConfigStoreThread(configData, customPath, fileName), 0, TimeUnit.SECONDS);
	}
	
	/**
	 * 将此配置列表输出到模组日志中，对调试有极大帮助
	 */
	@SilkApi
	public void debug() {
		debug(configData.mod);
	}
	
	/**
	 * 将此配置列表输出到指定的模组日志中，对调试有极大帮助
	 */
	@SilkApi
	public void debug(ModBasicData mod) {
		mod.logger().info("-- configs debug --");
		switch (configData.mode) {
			case PROPERTIES -> {
				LinkedProperties ppt = new LinkedProperties();
				setPropertiesConfigs(getSaveConfigs(configData), ppt, "");
				List<String> info = getAdditionalInfo(configData);
				info.replaceAll(s -> "# " + s);
				info.add("");
				info.forEach(s -> mod.logger().info(s));
				for (String key : ppt.linkedSet) mod.logger().info(key + " = " + ppt.getProperty(key));
			}
			case XML -> {
				try {
					SAXTransformerFactory xtf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
					TransformerHandler handler = xtf.newTransformerHandler();
					Transformer transformer = handler.getTransformer();
					transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
					transformer.setOutputProperty(OutputKeys.INDENT, "yes");
					transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					Result result = new StreamResult(baos);
					handler.setResult(result);
					AttributesImpl attr = new AttributesImpl();
					StringBuilder info = new StringBuilder("\n");
					for (String s : getAdditionalInfo(configData)) info.append(s).append("\n");
					handler.startDocument();
					handler.comment(info.toString().toCharArray(), 0, info.length());
					handler.startElement("", "", configData.mod.getId(), attr);
					setXmlConfigs(getSaveConfigs(configData), handler, attr);
					handler.endElement("", "", configData.mod.getId());
					handler.endDocument();
					List<String> baseData = Arrays.asList(baos.toString().split("><"));
					baseData.replaceAll(s -> s.toCharArray()[0] != '<' ? "<" + s : s);
					baseData.replaceAll(s -> (s.toCharArray()[s.length() - 1] != '>' && s.toCharArray()[s.length() - 1] != '\n') ? s + ">" : s);
					List<String> data = new ArrayList<>(8);
					baseData.forEach(s -> data.addAll(Arrays.asList(s.split("\n"))));
					data.forEach(s -> mod.logger().info(s));
				} catch (TransformerConfigurationException | SAXException e) {
					Silk.DATA.logger().error(e.getLocalizedMessage());
				}
			}
			case JSON -> {
				JsonObject jsonObject = JsonParser.parseString(new Gson().toJson(getSaveConfigs(configData))).getAsJsonObject();
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				List<String> info = getAdditionalInfo(configData);
				for (int count = 0; count < info.size(); count++) info.set(count, "  \"//" + count + "\": \"" + info.get(count) + "\",");
				info.add("  ");
				List<String> data = new ArrayList<>(List.of(gson.toJson(jsonObject).split("\n")));
				data.addAll(1, info);
				data.forEach(s -> mod.logger().info(s));
			}
			case TOML -> {
				TomlWriter tomlWriter = new TomlWriter.Builder().indentValuesBy(2).build();
				List<String> info = getAdditionalInfo(configData);
				info.replaceAll(s -> "# " + s);
				info.add("");
				info.forEach(s -> mod.logger().info(s));
				Arrays.asList(tomlWriter.write(getSaveConfigs(configData)).split("\n")).forEach(s -> mod.logger().info(s));
			}
		}
	}
	
	/**
	 * 有序的 {@link Properties}
	 */
	static final class LinkedProperties extends Properties {
		final LinkedHashSet<String> linkedSet = Sets.newLinkedHashSetWithExpectedSize(8);
		
		@Override
		public synchronized Object put(Object key, Object value) {
			linkedSet.add((String) key);
			return super.put(key, value);
		}
	}
	
	/**
	 * 单独开辟线程减少主线程损耗
	 */
	private static class ConfigStoreThread extends Thread {
		private final ConfigData configData;
		private final Path customPath;
		private final String fileName;
		
		private ConfigStoreThread(ConfigData configData, Path customPath, String fileName) {
			this.configData = configData;
			this.customPath = customPath;
			this.fileName = fileName;
		}
		
		@Override
		public void run() {
			try {
				Path file = Paths.get(customPath.toString(), fileName);
				switch (configData.mode) {
					case PROPERTIES -> {
						LinkedProperties ppt = new LinkedProperties();
						setPropertiesConfigs(getSaveConfigs(configData), ppt, "");
						List<String> info = getAdditionalInfo(configData);
						info.replaceAll(s -> "# " + s);
						info.add("");
						List<String> data = new ArrayList<>(ppt.linkedSet.size());
						for (String key : ppt.linkedSet) data.add(key + " = " + ppt.getProperty(key));
						Files.write(file, info, CHARSET);
						Files.write(file, data, CHARSET, StandardOpenOption.APPEND);
					}
					case XML -> {
						try {
							SAXTransformerFactory xtf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
							TransformerHandler handler = xtf.newTransformerHandler();
							Transformer transformer = handler.getTransformer();
							transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
							transformer.setOutputProperty(OutputKeys.INDENT, "yes");
							transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							Result result = new StreamResult(baos);
							handler.setResult(result);
							AttributesImpl attr = new AttributesImpl();
							StringBuilder info = new StringBuilder("\n");
							for (String s : getAdditionalInfo(configData)) info.append(s).append("\n");
							handler.startDocument();
							handler.comment(info.toString().toCharArray(), 0, info.length());
							handler.startElement("", "", configData.mod.getId(), attr);
							setXmlConfigs(getSaveConfigs(configData), handler, attr);
							handler.endElement("", "", configData.mod.getId());
							handler.endDocument();
							List<String> data = Arrays.asList(baos.toString().split("><"));
							data.replaceAll(s -> s.toCharArray()[0] != '<' ? "<" + s : s);
							data.replaceAll(s -> (s.toCharArray()[s.length() - 1] != '>' && s.toCharArray()[s.length() - 1] != '\n') ? s + ">" : s);
							Files.write(file, data, CHARSET);
						} catch (TransformerConfigurationException | SAXException e) {
							Silk.DATA.logger().error(e.getLocalizedMessage());
						}
					}
					case JSON -> {
						JsonObject jsonObject = JsonParser.parseString(new Gson().toJson(getSaveConfigs(configData))).getAsJsonObject();
						Gson gson = new GsonBuilder().setPrettyPrinting().create();
						List<String> info = getAdditionalInfo(configData);
						for (int count = 0; count < info.size(); count++) info.set(count, "  \"//" + count + "\": \"" + info.get(count) + "\",");
						info.add("  ");
						List<String> data = new ArrayList<>(List.of(gson.toJson(jsonObject).split("\n")));
						data.addAll(1, info);
						Files.write(file, data, CHARSET);
					}
					case TOML -> {
						TomlWriter tomlWriter = new TomlWriter.Builder().indentValuesBy(2).build();
						tomlWriter.write(getSaveConfigs(configData), file.toFile());
						List<String> info = getAdditionalInfo(configData);
						info.replaceAll(s -> "# " + s);
						info.add("");
						List<String> data = Files.readAllLines(file, CHARSET);
						Files.write(file, info, CHARSET);
						Files.write(file, data, CHARSET, StandardOpenOption.APPEND);
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
