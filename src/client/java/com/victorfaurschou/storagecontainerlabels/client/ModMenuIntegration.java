package com.victorfaurschou.storagecontainerlabels.client;

import com.victorfaurschou.StorageContainerLabelsConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ModMenuIntegration implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return ModMenuIntegration::buildConfigScreen;
	}

	private static Screen buildConfigScreen(Screen parent) {
		ConfigBuilder builder = ConfigBuilder.create()
			.setParentScreen(parent)
			.setTitle(Component.literal("Storage Container Labels"));

		ConfigEntryBuilder entryBuilder = builder.entryBuilder();

		var generalCategory = builder.getOrCreateCategory(Component.literal("General"));

		generalCategory
			.addEntry(entryBuilder.startIntSlider(
				Component.literal("Render Distance"),
				StorageContainerLabelsConfig.renderDistance,
				1,
				32
			)
			.setDefaultValue(6)
			.setTooltip(Component.literal("Minimum render distance to display labels."))
			.setSaveConsumer(value -> {
				StorageContainerLabelsConfig.renderDistance = value;
				StorageContainerLabelsConfig.save();
			})
			.build())
			.addEntry(entryBuilder.startIntSlider(
				Component.literal("Size"),
				(int)(StorageContainerLabelsConfig.size * 10),
				1,
				20
			)
			.setDefaultValue(6)
			.setTextGetter(value -> Component.literal(String.format("%.1f", value / 10.0f)))
			.setTooltip(Component.literal("Size of the label text."))
			.setSaveConsumer(value -> {
				StorageContainerLabelsConfig.size = value / 10.0f;
				StorageContainerLabelsConfig.save();
			})
			.build())
			.addEntry(entryBuilder.startIntSlider(
				Component.literal("Opacity"),
				(int)(StorageContainerLabelsConfig.opacity * 100),
				1,
				100
			)
			.setDefaultValue(80)
			.setTextGetter(value -> Component.literal(String.format("%d%%", value)))
			.setTooltip(Component.literal("Opacity of the label text."))
			.setSaveConsumer(value -> {
				StorageContainerLabelsConfig.opacity = value / 100.0f;
				StorageContainerLabelsConfig.save();
			})
			.build())
			.addEntry(entryBuilder.startIntSlider(
				Component.literal("Offset X"),
				(int)(StorageContainerLabelsConfig.offsetX * 10),
				-10,
				10
			)
			.setDefaultValue(0)
			.setTextGetter(value -> Component.literal(String.format("%.1f", value / 10.0f)))
			.setTooltip(Component.literal("Horizontal offset"))
			.setSaveConsumer(value -> {
				StorageContainerLabelsConfig.offsetX = value / 10.0f;
				StorageContainerLabelsConfig.save();
			})
			.build())
			.addEntry(entryBuilder.startIntSlider(
				Component.literal("Offset Y"),
				(int)(StorageContainerLabelsConfig.offsetY * 10),
				-10,
				10
			)
			.setDefaultValue(-8)
			.setTextGetter(value -> Component.literal(String.format("%.1f", value / 10.0f)))
			.setTooltip(Component.literal("Vertical offset"))
			.setSaveConsumer(value -> {
				StorageContainerLabelsConfig.offsetY = value / 10.0f;
				StorageContainerLabelsConfig.save();
			})
			.build())
			.addEntry(entryBuilder.startIntSlider(
				Component.literal("Offset Z"),
				(int)(StorageContainerLabelsConfig.offsetZ * 10),
				-10,
				10
			)
			.setDefaultValue(-7)
			.setTextGetter(value -> Component.literal(String.format("%.1f", value / 10.0f)))
			.setTooltip(Component.literal("Depth offset"))
			.setSaveConsumer(value -> {
				StorageContainerLabelsConfig.offsetZ = value / 10.0f;
				StorageContainerLabelsConfig.save();
			})
			.build())
			.addEntry(entryBuilder.startIntSlider(
				Component.literal("Distance Fade"),
				(int)(StorageContainerLabelsConfig.fade * 100),
				0,
				100
			)
			.setDefaultValue(15)
			.setTextGetter(value -> Component.literal(String.format("%d%%", value)))
			.setTooltip(Component.literal("How much of the render distance fades out. 0 = hard cutoff, 100 = fades from full distance."))
			.setSaveConsumer(value -> {
				StorageContainerLabelsConfig.fade = value / 100.0f;
				StorageContainerLabelsConfig.save();
			})
			.build())
			.addEntry(entryBuilder.startBooleanToggle(
				Component.literal("Visible Through Blocks"),
				StorageContainerLabelsConfig.seeThrough
			)
			.setDefaultValue(true)
			.setTooltip(Component.literal("When enabled, labels are visible through blocks."))
			.setSaveConsumer(value -> {
				StorageContainerLabelsConfig.seeThrough = value;
				StorageContainerLabelsConfig.save();
			})
			.build());

		return builder.build();
	}
}
