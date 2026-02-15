package gg.skytils.skytilsmod.core

import gg.skytils.event.postSync
import gg.skytils.skytilsmod._event.HypixelPacketReceiveEvent
import net.hypixel.modapi.HypixelModAPI
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket

object HypixelApi {
    fun setup() {
        HypixelModAPI.getInstance().createHandler(ClientboundLocationPacket::class.java) { packet ->
            postSync(HypixelPacketReceiveEvent(packet))
        }
    }

}