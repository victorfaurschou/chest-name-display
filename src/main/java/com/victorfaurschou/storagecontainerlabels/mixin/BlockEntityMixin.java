package com.victorfaurschou.storagecontainerlabels.mixin;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntity.class)
public class BlockEntityMixin {
	@Inject(method = "getUpdateTag", at = @At("HEAD"), cancellable = true)
	private void includeContainerCustomName(HolderLookup.Provider registries, CallbackInfoReturnable<CompoundTag> cir) {
		if (!((Object)this instanceof BaseContainerBlockEntity container)) return;
		if (container.getCustomName() == null) return;
		Tag nameTag = ((BlockEntity)(Object)this).saveCustomOnly(registries).get("CustomName");
		if (nameTag == null) return;
		CompoundTag tag = new CompoundTag();
		tag.put("CustomName", nameTag);
		cir.setReturnValue(tag);
	}
}
