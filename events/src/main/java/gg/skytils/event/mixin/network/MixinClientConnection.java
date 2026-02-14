package gg.skytils.event.mixin.network;

import gg.skytils.event.EventsKt;
import gg.skytils.event.impl.ClientConnectEvent;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class MixinClientConnection {

    @Shadow
    private volatile @Nullable PacketListener packetListener;

    @Inject(method = "channelInactive", at = @At("HEAD"), remap = false)
    public void channelInactive(CallbackInfo ci) {
        if (this.packetListener instanceof ClientPlayNetworkHandler) {
            MinecraftClient.getInstance().submit(() -> {
                EventsKt.postSync(new ClientConnectEvent());
            });
        }
    }

    @Inject(method = "handleDisconnection", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/listener/PacketListener;onDisconnected(Lnet/minecraft/network/DisconnectionInfo;)V"))
    public void onDisconnect(CallbackInfo ci) {
        if (this.packetListener instanceof ClientPlayNetworkHandler) {
            MinecraftClient.getInstance().submit(() -> {
                EventsKt.postSync(new ClientConnectEvent());
            });
        }
    }
}
