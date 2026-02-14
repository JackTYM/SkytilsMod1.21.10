package gg.skytils.skytilsmod.mixins.events;

import gg.skytils.event.EventsKt;
import gg.skytils.skytilsmod._event.PacketReceiveEvent;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class MixinClientConnection {

    @Shadow
    @Final
    private NetworkSide side;

    @Shadow
    private volatile @Nullable PacketListener packetListener;

    @Inject(
            method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;handlePacket(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;)V"),
            cancellable = true
    )
    public void channelRead0(ChannelHandlerContext ctx, Packet<?> packet, CallbackInfo ci) {
        if (this.side == NetworkSide.CLIENTBOUND && this.packetListener instanceof ClientPlayNetworkHandler) {
            if (EventsKt.postCancellableSync(new PacketReceiveEvent(packet))) {
                ci.cancel();
            }
        }
    }
}
