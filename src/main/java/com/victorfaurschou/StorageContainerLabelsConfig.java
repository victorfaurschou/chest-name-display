package com.victorfaurschou;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class StorageContainerLabelsConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final File CONFIG_FILE = new File("config/storage-container-labels.json");

	public static int renderDistance = 6;
	public static float size = 0.6f;
	public static float offsetX = 0.0f;
	public static float offsetY = -0.8f;
	public static float offsetZ = -0.7f;
	public static float opacity = 0.8f;

	public static void save() {
		try {
			CONFIG_FILE.getParentFile().mkdirs();
			try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
				GSON.toJson(new ConfigData(renderDistance, size, offsetX, offsetY, offsetZ, opacity), writer);
			}
		} catch (IOException e) {
			StorageContainerLabels.LOGGER.warn("Failed to save config", e);
		}
	}

	public static void load() {
		try {
			if (CONFIG_FILE.exists()) {
				try (FileReader reader = new FileReader(CONFIG_FILE)) {
					ConfigData data = GSON.fromJson(reader, ConfigData.class);
					if (data != null) {
						renderDistance = data.minimumDistance;
						size = data.size;
						offsetX = data.offsetX;
						offsetY = data.offsetY;
						offsetZ = data.offsetZ;
						opacity = data.opacity > 0 ? data.opacity : 0.8f;
					}
				}
			}
		} catch (IOException e) {
			StorageContainerLabels.LOGGER.warn("Failed to load config", e);
		}
	}

	static class ConfigData {
		int minimumDistance;
		float size;
		float offsetX;
		float offsetY;
		float offsetZ;
		float opacity;

		ConfigData(int minimumDistance, float size, float offsetX, float offsetY, float offsetZ, float opacity) {
			this.minimumDistance = minimumDistance;
			this.size = size;
			this.offsetX = offsetX;
			this.offsetY = offsetY;
			this.offsetZ = offsetZ;
			this.opacity = opacity;
		}
	}
}
