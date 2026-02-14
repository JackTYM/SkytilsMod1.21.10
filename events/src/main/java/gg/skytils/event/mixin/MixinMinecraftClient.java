package gg.skytils.event.mixin;

import gg.skytils.event.EventsKt;
import gg.skytils.event.impl.TickEvent;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {

    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(CallbackInfo ci) {
        EventsKt.postSync(new TickEvent());
    }
}
