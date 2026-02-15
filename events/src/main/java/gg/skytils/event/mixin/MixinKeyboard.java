package gg.skytils.event.mixin;

import gg.skytils.event.EventsKt;
import gg.skytils.event.impl.KeyboardInputEvent;
import net.minecraft.client.Keyboard;
import net.minecraft.client.input.KeyInput;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class MixinKeyboard {

    @Inject(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;getHandle()J", shift = At.Shift.AFTER), cancellable = true)
    private void onKey(long window, int action, KeyInput input, CallbackInfo ci) {
        if (action != GLFW.GLFW_PRESS) return;

        if (EventsKt.postCancellableSync(new KeyboardInputEvent(input.getKeycode()))) {
            ci.cancel();
        }
    }
}
