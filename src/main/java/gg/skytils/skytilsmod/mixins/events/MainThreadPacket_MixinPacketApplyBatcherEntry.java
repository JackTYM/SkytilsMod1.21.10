package gg.skytils.skytilsmod.mixins.events;

import gg.skytils.event.EventsKt;
import gg.skytils.skytilsmod._event.MainThreadPacketReceiveEvent;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.network.PacketApplyBatcher$Entry")
public class MainThreadPacket_MixinPacketApplyBatcherEntry {
    @Shadow
    @Final
    private Packet<?> packet;

    @Inject(method = "apply", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/Packet;apply(Lnet/minecraft/network/listener/PacketListener;)V"), cancellable = true)
    public void apply(CallbackInfo ci) {
        if (EventsKt.postCancellableSync(new MainThreadPacketReceiveEvent(this.packet))) {
            ci.cancel();
        }
    }
}
