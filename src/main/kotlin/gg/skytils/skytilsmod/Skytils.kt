package gg.skytils.skytilsmod

import gg.skytils.event.EventSubscriber
import gg.skytils.skytilsmod.core.HypixelApi
import gg.skytils.skytilsmod.core.PersistentSave
import gg.skytils.skytilsmod.util.SBInfo

object Skytils {
    fun init() {
        listOf(
            SBInfo,
        ).forEach(EventSubscriber::setup)

        HypixelApi.setup()
        PersistentSave.loadData()
    }
}