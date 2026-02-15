package gg.skytils.skytilsmod._event

import gg.skytils.event.Event
import net.hypixel.modapi.packet.ClientboundHypixelPacket

class HypixelPacketReceiveEvent(val packet: ClientboundHypixelPacket) : Event()