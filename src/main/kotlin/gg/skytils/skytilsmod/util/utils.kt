package gg.skytils.skytilsmod.util

import net.minecraft.client.MinecraftClient

fun checkThreadAndQueue(block: () -> Unit) {
    val mc = MinecraftClient.getInstance()
    if (mc.isOnThread) {
        block()
    } else {
        mc.submit(block)
    }
}