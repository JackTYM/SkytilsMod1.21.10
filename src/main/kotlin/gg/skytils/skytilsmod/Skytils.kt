package gg.skytils.skytilsmod

import gg.skytils.skytilsmod.core.PersistentSave

object Skytils {
    fun init() {
        PersistentSave.loadData()
    }
}