package gg.skytils.event.mixin.network;

import gg.skytils.event.EventsKt;
import gg.skytils.event.impl.ClientConnectEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {

    @Inject(method = "<init>", at = @At("RETURN"))
    public void onConnect(CallbackInfo ci) {
        MinecraftClient.getInstance().submit(() -> {
            EventsKt.postSync(new ClientConnectEvent());
        });
    }
}
