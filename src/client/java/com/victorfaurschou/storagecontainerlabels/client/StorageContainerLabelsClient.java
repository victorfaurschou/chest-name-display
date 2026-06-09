package com.victorfaurschou.storagecontainerlabels.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.victorfaurschou.StorageContainerLabels;
import com.victorfaurschou.StorageContainerLabelsConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.joml.Matrix4f;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StorageContainerLabelsClient implements ClientModInitializer {
	private record ChestLabel(double worldX, double worldY, double worldZ, Component name) {}

	private static volatile List<ChestLabel> chestLabels = List.of();
	private static int tickCount = 0;

	@Override
	public void onInitializeClient() {
		StorageContainerLabelsConfig.load();
		StorageContainerLabels.LOGGER.info("Storage Container Labels initialized");

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.level == null || client.player == null) {
				chestLabels = List.of();
				return;
			}

			if (++tickCount % 5 != 0) return;

			BlockPos playerPos = client.player.blockPosition();
			int range = StorageContainerLabelsConfig.renderDistance;
			Set<Long> seen = new HashSet<>();
			List<ChestLabel> labels = new ArrayList<>();

			BlockPos.betweenClosedStream(
				playerPos.offset(-range, -range, -range),
				playerPos.offset(range, range, range)
			).forEach(pos -> {
				if (!(client.level.getBlockEntity(pos) instanceof ChestBlockEntity chest) || !chest.hasCustomName()) return;

				var blockState = client.level.getBlockState(pos);
				Direction facing = blockState.getValue(ChestBlock.FACING);
				ChestType chestType = blockState.getValue(ChestBlock.TYPE);

				long myKey = pos.asLong();
				long canonKey = myKey;
				double centerX = pos.getX() + 0.5;
				double centerZ = pos.getZ() + 0.5;

				if (chestType != ChestType.SINGLE) {
					Direction companionDir = (chestType == ChestType.LEFT)
						? facing.getClockWise()
						: facing.getCounterClockWise();
					BlockPos companionPos = pos.relative(companionDir);
					if (client.level.getBlockEntity(companionPos) instanceof ChestBlockEntity) {
						canonKey = Math.min(myKey, companionPos.asLong());
						centerX = (pos.getX() + companionPos.getX()) / 2.0 + 0.5;
						centerZ = (pos.getZ() + companionPos.getZ()) / 2.0 + 0.5;
					}
				}

				if (!seen.add(canonKey)) return;

				double[] rotated = StorageContainerLabels.rotateOffset(
					StorageContainerLabelsConfig.offsetX,
					StorageContainerLabelsConfig.offsetY,
					StorageContainerLabelsConfig.offsetZ,
					facing
				);
				labels.add(new ChestLabel(
					centerX + rotated[0],
					pos.getY() + 1.25 + rotated[1],
					centerZ + rotated[2],
					chest.getDisplayName()
				));
			});

			chestLabels = labels;
		});

		LevelRenderEvents.END_MAIN.register(context -> {
			List<ChestLabel> labels = chestLabels;
			if (labels.isEmpty()) return;

			Minecraft client = Minecraft.getInstance();
			CameraRenderState camera = context.levelState().cameraRenderState;

			PoseStack poseStack = context.poseStack();
			MultiBufferSource.BufferSource bufferSource = context.bufferSource();

			int alpha = (int)(StorageContainerLabelsConfig.opacity * 255);
			int argb = (alpha << 24) | 0xFFFFFF;
			float scale = StorageContainerLabelsConfig.size * 0.025f;

			for (ChestLabel label : labels) {
				double dx = label.worldX() - camera.pos.x;
				double dy = label.worldY() - camera.pos.y;
				double dz = label.worldZ() - camera.pos.z;

				poseStack.pushPose();
				poseStack.translate(dx, dy, dz);
				poseStack.mulPose(camera.orientation);
				poseStack.scale(scale, -scale, scale);

				float textX = -client.font.width(label.name()) / 2.0f;
				client.font.drawInBatch(
					label.name(),
					textX, 0f,
					argb,
					false,
					new Matrix4f(poseStack.last().pose()),
					bufferSource,
					StorageContainerLabelsConfig.seeThrough ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL,
					0,
					LightCoordsUtil.FULL_BRIGHT
				);

				poseStack.popPose();
			}

			bufferSource.endBatch();
		});
	}
}
