package gg.skytils.event.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import gg.skytils.event.EventsKt;
import gg.skytils.event.impl.MouseInputEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.input.MouseInput;
import net.minecraft.client.util.Window;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MixinMouse {

    @Shadow @Final
    private MinecraftClient client;

    @Shadow public abstract double getScaledX(Window window);

    @Shadow
    public abstract double getScaledY(Window window);

    @Inject(method = "onMouseButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;getHandle()J", shift = At.Shift.AFTER, ordinal = 0), cancellable = true)
    private void onMouseButton(long window, MouseInput input, int action, CallbackInfo ci, @Local Window windowObj) {
        if (action != GLFW.GLFW_PRESS) return;

        double scaledX = this.getScaledX(windowObj);
        double scaledY = this.getScaledY(windowObj);

        if (EventsKt.postCancellableSync(new MouseInputEvent((int) scaledX, (int) scaledY, input.button()))) {
            ci.cancel();
        }
    }
}