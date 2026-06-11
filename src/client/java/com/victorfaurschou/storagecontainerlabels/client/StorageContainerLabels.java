package com.victorfaurschou.storagecontainerlabels.client;

import net.minecraft.core.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StorageContainerLabels {
	public static final String MOD_ID = "storage-container-labels";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static double[] rotateOffset(double offsetX, double offsetY, double offsetZ, Direction facing) {
		return switch (facing) {
			case NORTH -> new double[]{offsetX, offsetY, offsetZ};
			case SOUTH -> new double[]{-offsetX, offsetY, -offsetZ};
			case EAST -> new double[]{-offsetZ, offsetY, -offsetX};
			case WEST -> new double[]{offsetZ, offsetY, offsetX};
			default -> throw new IllegalArgumentException("rotateOffset called with vertical direction: " + facing);
		};
	}
}
