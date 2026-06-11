package com.victorfaurschou.storagecontainerlabels.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import net.fabricmc.loader.api.FabricLoader;

public class StorageContainerLabelsConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(StorageContainerLabels.MOD_ID + ".json").toFile();

	public static int renderDistance = 6;
	public static float size = 0.6f;
	public static float offsetX = 0.0f;
	public static float offsetY = -0.8f;
	public static float offsetZ = -0.7f;
	public static float opacity = 0.8f;
	public static boolean seeThrough = false;
	public static float fade = 0.15f;
	public static boolean showForChests = true;
	public static boolean showForChestVariants = true;
	public static boolean showForBarrels = true;
	public static boolean showForShulkerBoxes = true;
	public static boolean showForHoppers = false;
	public static boolean showForDroppers = false;
	public static boolean showForDispensers = false;
	public static int labelColor = 0xFFFFFF;

	public static void save() {
		try {
			CONFIG_FILE.getParentFile().mkdirs();
			try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
				GSON.toJson(new ConfigData(
					renderDistance, size, offsetX, offsetY, offsetZ, opacity, seeThrough, fade,
					showForChests, showForChestVariants, showForBarrels, showForShulkerBoxes,
					showForHoppers, showForDroppers, showForDispensers, labelColor
				), writer);
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
						renderDistance = data.renderDistance != null ? data.renderDistance : 6;
						size = data.size != null ? data.size : 0.6f;
						offsetX = data.offsetX != null ? data.offsetX : 0.0f;
						offsetY = data.offsetY != null ? data.offsetY : -0.8f;
						offsetZ = data.offsetZ != null ? data.offsetZ : -0.7f;
						opacity = data.opacity != null ? data.opacity : 0.8f;
						seeThrough = data.seeThrough != null ? data.seeThrough : false;
						fade = data.fade != null ? data.fade : 0.15f;
						showForChests = data.showForChests != null ? data.showForChests : true;
						showForChestVariants = data.showForChestVariants != null ? data.showForChestVariants : true;
						showForBarrels = data.showForBarrels != null ? data.showForBarrels : true;
						showForShulkerBoxes = data.showForShulkerBoxes != null ? data.showForShulkerBoxes : true;
						showForHoppers = data.showForHoppers != null ? data.showForHoppers : false;
						showForDroppers = data.showForDroppers != null ? data.showForDroppers : false;
						showForDispensers = data.showForDispensers != null ? data.showForDispensers : false;
						labelColor = data.labelColor != null ? data.labelColor : 0xFFFFFF;
					}
				}
			}
		} catch (IOException e) {
			StorageContainerLabels.LOGGER.warn("Failed to load config", e);
		}
	}

	static class ConfigData {
		Integer renderDistance;
		Float size;
		Float offsetX;
		Float offsetY;
		Float offsetZ;
		Float opacity;
		Boolean seeThrough;
		Float fade;
		Boolean showForChests;
		Boolean showForChestVariants;
		Boolean showForBarrels;
		Boolean showForShulkerBoxes;
		Boolean showForHoppers;
		Boolean showForDroppers;
		Boolean showForDispensers;
		Integer labelColor;

		ConfigData(int renderDistance, float size, float offsetX, float offsetY, float offsetZ,
				float opacity, boolean seeThrough, float fade,
				boolean showForChests, boolean showForChestVariants, boolean showForBarrels,
				boolean showForShulkerBoxes, boolean showForHoppers, boolean showForDroppers,
				boolean showForDispensers, int labelColor) {
			this.renderDistance = renderDistance;
			this.size = size;
			this.offsetX = offsetX;
			this.offsetY = offsetY;
			this.offsetZ = offsetZ;
			this.opacity = opacity;
			this.seeThrough = seeThrough;
			this.fade = fade;
			this.showForChests = showForChests;
			this.showForChestVariants = showForChestVariants;
			this.showForBarrels = showForBarrels;
			this.showForShulkerBoxes = showForShulkerBoxes;
			this.showForHoppers = showForHoppers;
			this.showForDroppers = showForDroppers;
			this.showForDispensers = showForDispensers;
			this.labelColor = labelColor;
		}
	}
}
