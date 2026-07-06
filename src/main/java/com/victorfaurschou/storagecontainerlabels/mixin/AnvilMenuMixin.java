package com.victorfaurschou.storagecontainerlabels.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {

	@Shadow @Final private DataSlot cost;
	@Shadow private int repairItemCountCost;
	@Shadow private boolean onlyRenaming;

	@Unique
	private boolean freeContainerRename = false;

	private AnvilMenuMixin(@Nullable MenuType<?> menuType, int containerId, Inventory inventory,
			ContainerLevelAccess access, ItemCombinerMenuSlotDefinition itemInputSlots) {
		super(menuType, containerId, inventory, access, itemInputSlots);
	}

	@Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
	private void nameContainerWithNameTag(CallbackInfo ci) {
		ItemStack input = this.inputSlots.getItem(0);
		if (!isNameableContainer(input)) {
			this.freeContainerRename = false;
			return;
		}

		ItemStack addition = this.inputSlots.getItem(1);
		this.onlyRenaming = false;
		this.cost.set(0);

		if (addition.is(Items.NAME_TAG) && addition.has(DataComponents.CUSTOM_NAME)) {
			ItemStack result = input.copy();
			result.set(DataComponents.CUSTOM_NAME, addition.get(DataComponents.CUSTOM_NAME));
			this.repairItemCountCost = 1;
			this.freeContainerRename = true;
			this.resultSlots.setItem(0, result);
			this.broadcastChanges();
		} else {
			this.repairItemCountCost = 0;
			this.freeContainerRename = false;
			this.resultSlots.setItem(0, ItemStack.EMPTY);
		}

		ci.cancel();
	}

	@Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true)
	private void allowFreeContainerRenamePickup(Player player, boolean hasItem, CallbackInfoReturnable<Boolean> cir) {
		if (this.freeContainerRename && hasItem) {
			cir.setReturnValue(true);
		}
	}

	@Unique
	private static boolean isNameableContainer(ItemStack stack) {
		if (stack.isEmpty()) return false;
		Block block = Block.byItem(stack.getItem());
		return block instanceof ChestBlock
			|| block instanceof BarrelBlock
			|| block instanceof ShulkerBoxBlock
			|| block instanceof HopperBlock
			|| block instanceof DispenserBlock;
	}
}
