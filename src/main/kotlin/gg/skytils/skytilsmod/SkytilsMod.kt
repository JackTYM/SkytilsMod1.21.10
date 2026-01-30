package gg.skytils.skytilsmod

import gg.skytils.skytilsmod.util.SuperSecretSettings
import net.fabricmc.api.ClientModInitializer

object SkytilsMod : ClientModInitializer {
    override fun onInitializeClient() {

    }

    fun preLaunch() {
        SuperSecretSettings.load()
    }
}