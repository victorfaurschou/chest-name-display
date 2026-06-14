package com.victorfaurschou.storagecontainerlabels.client;

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
			.setTitle(Component.literal("Storage Container Labels"))
			.setSavingRunnable(StorageContainerLabelsConfig::save);

		ConfigEntryBuilder entryBuilder = builder.entryBuilder();

		builder.getOrCreateCategory(Component.literal("General"))
			.addEntry(entryBuilder.startIntSlider(
				Component.literal("Render Distance"),
				StorageContainerLabelsConfig.renderDistance,
				1, 32
			)
			.setDefaultValue(6)
			.setTooltip(Component.literal("Blocks radius to scan for labelled containers."))
			.setSaveConsumer(v -> StorageContainerLabelsConfig.renderDistance = v)
			.build())
			.addEntry(entryBuilder.startIntSlider(
				Component.literal("Wrap Width"),
				StorageContainerLabelsConfig.wrapWidth,
				20, 200
			)
			.setDefaultValue(110)
			.setTextGetter(v -> Component.literal(v + " px"))
			.setTooltip(Component.literal("Maximum label width in pixels before wrapping to a new line."))
			.setSaveConsumer(v -> StorageContainerLabelsConfig.wrapWidth = v)
			.build())
			.addEntry(entryBuilder.startBooleanToggle(
				Component.literal("Show Focused Only"),
				StorageContainerLabelsConfig.focusedOnly
			)
			.setDefaultValue(false)
			.setTooltip(Component.literal("Only show the label for the container you are looking at."))
			.setSaveConsumer(v -> StorageContainerLabelsConfig.focusedOnly = v)
			.build())
			.addEntry(entryBuilder.startBooleanToggle(
				Component.literal("Visible Through Blocks"),
				StorageContainerLabelsConfig.seeThrough
			)
			.setDefaultValue(false)
			.setTooltip(Component.literal("When enabled, labels are visible through blocks."))
			.setSaveConsumer(v -> StorageContainerLabelsConfig.seeThrough = v)
			.build())
			.addEntry(entryBuilder.startBooleanToggle(
				Component.literal("Show for Chests"),
				StorageContainerLabelsConfig.showForChests
			)
			.setDefaultValue(true)
			.setSaveConsumer(v -> StorageContainerLabelsConfig.showForChests = v)
			.build())
			.addEntry(entryBuilder.startBooleanToggle(
				Component.literal("Show for Chest Variants"),
				StorageContainerLabelsConfig.showForChestVariants
			)
			.setDefaultValue(true)
			.setTooltip(Component.literal("Trapped chests and other chest-type blocks."))
			.setSaveConsumer(v -> StorageContainerLabelsConfig.showForChestVariants = v)
			.build())
			.addEntry(entryBuilder.startBooleanToggle(
				Component.literal("Show for Barrels"),
				StorageContainerLabelsConfig.showForBarrels
			)
			.setDefaultValue(true)
			.setSaveConsumer(v -> StorageContainerLabelsConfig.showForBarrels = v)
			.build())
			.addEntry(entryBuilder.startBooleanToggle(
				Component.literal("Show for Shulker Boxes"),
				StorageContainerLabelsConfig.showForShulkerBoxes
			)
			.setDefaultValue(true)
			.setSaveConsumer(v -> StorageContainerLabelsConfig.showForShulkerBoxes = v)
			.build())
			.addEntry(entryBuilder.startBooleanToggle(
				Component.literal("Show for Hoppers"),
				StorageContainerLabelsConfig.showForHoppers
			)
			.setDefaultValue(false)
			.setSaveConsumer(v -> StorageContainerLabelsConfig.showForHoppers = v)
			.build())
			.addEntry(entryBuilder.startBooleanToggle(
				Component.literal("Show for Droppers"),
				StorageContainerLabelsConfig.showForDroppers
			)
			.setDefaultValue(false)
			.setSaveConsumer(v -> StorageContainerLabelsConfig.showForDroppers = v)
			.build())
			.addEntry(entryBuilder.startBooleanToggle(
				Component.literal("Show for Dispensers"),
				StorageContainerLabelsConfig.showForDispensers
			)
			.setDefaultValue(false)
			.setSaveConsumer(v -> StorageContainerLabelsConfig.showForDispensers = v)
			.build());

		builder.getOrCreateCategory(Component.literal("Appearance"))
			.addEntry(entryBuilder.startColorField(
				Component.literal("Label Color"),
				StorageContainerLabelsConfig.labelColor
			)
			.setDefaultValue(0xFFFFFF)
			.setTooltip(Component.literal("Default label color. Can be overridden per container with &X prefix (e.g. &cRed Chest)."))
			.setSaveConsumer(v -> StorageContainerLabelsConfig.labelColor = v)
			.build())
			.addEntry(entryBuilder.startIntSlider(
				Component.literal("Size"),
				(int)(StorageContainerLabelsConfig.size * 10),
				1, 20
			)
			.setDefaultValue(6)
			.setTextGetter(v -> Component.literal(String.format("%.1f", v / 10.0f)))
			.setTooltip(Component.literal("Size of the label text."))
			.setSaveConsumer(v -> StorageContainerLabelsConfig.size = v / 10.0f)
			.build())
			.addEntry(entryBuilder.startIntSlider(
				Component.literal("Offset X"),
				(int)(StorageContainerLabelsConfig.offsetX * 10),
				-10, 10
			)
			.setDefaultValue(0)
			.setTextGetter(v -> Component.literal(String.format("%.1f", v / 10.0f)))
			.setTooltip(Component.literal("Horizontal offset."))
			.setSaveConsumer(v -> StorageContainerLabelsConfig.offsetX = v / 10.0f)
			.build())
			.addEntry(entryBuilder.startIntSlider(
				Component.literal("Offset Y"),
				(int)(StorageContainerLabelsConfig.offsetY * 10),
				-10, 10
			)
			.setDefaultValue(-8)
			.setTextGetter(v -> Component.literal(String.format("%.1f", v / 10.0f)))
			.setTooltip(Component.literal("Vertical offset."))
			.setSaveConsumer(v -> StorageContainerLabelsConfig.offsetY = v / 10.0f)
			.build())
			.addEntry(entryBuilder.startIntSlider(
				Component.literal("Offset Z"),
				(int)(StorageContainerLabelsConfig.offsetZ * 10),
				-10, 10
			)
			.setDefaultValue(-7)
			.setTextGetter(v -> Component.literal(String.format("%.1f", v / 10.0f)))
			.setTooltip(Component.literal("Depth offset."))
			.setSaveConsumer(v -> StorageContainerLabelsConfig.offsetZ = v / 10.0f)
			.build())
			.addEntry(entryBuilder.startIntSlider(
				Component.literal("Distance Fade"),
				(int)(StorageContainerLabelsConfig.fade * 100),
				0, 100
			)
			.setDefaultValue(15)
			.setTextGetter(v -> Component.literal(String.format("%d%%", v)))
			.setTooltip(Component.literal("Portion of render distance over which labels fade. 0 = hard cutoff."))
			.setSaveConsumer(v -> StorageContainerLabelsConfig.fade = v / 100.0f)
			.build())
			.addEntry(entryBuilder.startIntSlider(
				Component.literal("Text Opacity"),
				(int)(StorageContainerLabelsConfig.opacity * 100),
				1, 100
			)
			.setDefaultValue(80)
			.setTextGetter(v -> Component.literal(String.format("%d%%", v)))
			.setTooltip(Component.literal("Opacity of the label text."))
			.setSaveConsumer(v -> StorageContainerLabelsConfig.opacity = v / 100.0f)
			.build())
			.addEntry(entryBuilder.startBooleanToggle(
				Component.literal("Show Background"),
				StorageContainerLabelsConfig.showBackground
			)
			.setDefaultValue(false)
			.setTooltip(Component.literal("Show a background behind label text."))
			.setSaveConsumer(v -> StorageContainerLabelsConfig.showBackground = v)
			.build())
			.addEntry(entryBuilder.startColorField(
				Component.literal("Background Color"),
				StorageContainerLabelsConfig.backgroundColor
			)
			.setDefaultValue(0x000000)
			.setTooltip(Component.literal("Color of the label background."))
			.setSaveConsumer(v -> StorageContainerLabelsConfig.backgroundColor = v)
			.build())
			.addEntry(entryBuilder.startIntSlider(
				Component.literal("Background Opacity"),
				(int)(StorageContainerLabelsConfig.backgroundOpacity * 100),
				1, 100
			)
			.setDefaultValue(25)
			.setTextGetter(v -> Component.literal(String.format("%d%%", v)))
			.setTooltip(Component.literal("Opacity of the label background."))
			.setSaveConsumer(v -> StorageContainerLabelsConfig.backgroundOpacity = v / 100.0f)
			.build());

		return builder.build();
	}
}
