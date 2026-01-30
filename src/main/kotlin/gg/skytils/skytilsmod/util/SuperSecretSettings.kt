package gg.skytils.skytilsmod.util

import net.fabricmc.loader.api.FabricLoader
import org.apache.logging.log4j.LogManager
import kotlin.io.path.createFile
import kotlin.io.path.createParentDirectories
import kotlin.io.path.exists
import kotlin.io.path.readLines
import kotlin.io.path.writeLines

object SuperSecretSettings {
    private val LOGGER = LogManager.getLogger()
    private val saveLocation = FabricLoader.getInstance().configDir.resolve("skytils/supersecretsettings.txt")
    private var dirty = false
    private val settings = mutableSetOf<String>()

    init {
        if (!saveLocation.exists()) {
            try {
                saveLocation
                    .createParentDirectories()
                    .createFile()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        Runtime.getRuntime().addShutdownHook(Thread(::save, "Skytils-SuperSecretSave"))
    }

    fun getSetting(setting: String) =
        settings.contains(setting)

    fun addSetting(setting: String) {
        dirty = settings.add(setting);
    }

    fun removeSetting(setting: String) {
        dirty = settings.remove(setting)
    }

    fun clearSettings() {
        if (settings.isEmpty()) return
        settings.clear()
        dirty = true
    }

    fun load() {
        LOGGER.debug("Loading super secret settings")
        settings.clear()
        try {
            val lines = saveLocation.readLines(Charsets.UTF_8)
            settings.addAll(lines.mapNotNull { it.takeIf(String::isNotEmpty) })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun save() {
        LOGGER.debug("Saving super secret settings")
        if (!dirty) return
        dirty = false
        try {
            saveLocation.writeLines(settings, Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}