package com.victorfaurschou.storagecontainerlabels.mixin;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockEntity.class)
public class BlockEntityMixin {
	@Inject(method = "getUpdateTag", at = @At("RETURN"))
	private void includeContainerCustomName(HolderLookup.Provider registries, CallbackInfoReturnable<CompoundTag> cir) {
		if (!((Object)this instanceof BaseContainerBlockEntity container)) return;
		if (container.getCustomName() == null) return;
		Tag nameTag = ComponentSerialization.CODEC
			.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), container.getCustomName())
			.result()
			.orElse(null);
		if (nameTag == null) return;
		cir.getReturnValue().put("CustomName", nameTag);
	}

	@Inject(method = "getUpdatePacket", at = @At("RETURN"), cancellable = true)
	private void provideNamedContainerUpdatePacket(CallbackInfoReturnable<Packet<?>> cir) {
		if (cir.getReturnValue() != null) return;
		if (!((Object)this instanceof BaseContainerBlockEntity container)) return;
		if (container.getCustomName() == null) return;
		cir.setReturnValue(ClientboundBlockEntityDataPacket.create((BlockEntity)(Object)this));
	}
}
