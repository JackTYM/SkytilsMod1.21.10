package gg.skytils.skytilsmod.util

import gg.essential.elementa.unstable.state.v2.MutableState
import gg.essential.elementa.unstable.state.v2.mutableStateOf
import gg.essential.elementa.unstable.state.v2.State
import gg.essential.elementa.unstable.state.v2.memo
import gg.skytils.event.EventSubscriber
import gg.skytils.event.impl.ClientDisconnectEvent
import gg.skytils.event.register
import gg.skytils.skytilsmod._event.HypixelPacketReceiveEvent
import gg.skytils.skytilsmod._event.MainThreadPacketReceiveEvent
import net.fabricmc.loader.api.FabricLoader
import net.hypixel.data.type.ServerType
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket
import net.minecraft.network.packet.BrandCustomPayload
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket
import net.minecraft.network.packet.s2c.play.ScoreboardDisplayS2CPacket
import net.minecraft.scoreboard.ScoreboardDisplaySlot

object SBInfo : EventSubscriber {

    private val _hypixelState: MutableState<Boolean> = mutableStateOf(false)
    private val _skyblockState: MutableState<Boolean> = mutableStateOf(false)
    private val _locationState: MutableState<String> = mutableStateOf("")
    private val _modeState: MutableState<String> = mutableStateOf("")
    private val _serverIdState: MutableState<String> = mutableStateOf("")
    private val _serverTypeState: MutableState<ServerType?> = mutableStateOf(null)

    val hypixelState: State<Boolean> = {
        _hypixelState() || FabricLoader.getInstance().isDevelopmentEnvironment
    } // TODO: Add support for dev toggles
    val skyblockState: State<Boolean> = {
        _skyblockState() || FabricLoader.getInstance().isDevelopmentEnvironment
    } // same as above
    val dungeonsState: State<Boolean> = memo { _modeState() == "dungeon" } // same as above
    val locationState: State<String> = _locationState
    val modeState: State<String> = _modeState
    val serverIdState: State<String> = _serverIdState
    val serverTypeState: State<ServerType?> = _serverTypeState

    fun onDisconnect(event: ClientDisconnectEvent) {
        _hypixelState.set(false)
        _skyblockState.set(false)
        _modeState.set("")
        _serverIdState.set("")
        _serverTypeState.set(null)
        _locationState.set("")
    }

    fun onHypixelPacket(event: HypixelPacketReceiveEvent) {
        if (event.packet is ClientboundLocationPacket) {
            checkThreadAndQueue {
                _hypixelState.set(true)
                _modeState.set(event.packet.mode.orElse(""))
                _serverIdState.set(event.packet.serverName)
                _serverTypeState.set(event.packet.serverType.orElse(null))
                _locationState.set("")
            }
        }
    }

    fun onPacket(event: MainThreadPacketReceiveEvent<*>) {
        if (!_hypixelState.getUntracked() && event.packet is CustomPayloadS2CPacket) {
            _hypixelState.set((event.packet.payload as? BrandCustomPayload)?.brand?.contains("hypixel") == true)
        }
        if (!_skyblockState.getUntracked() && _hypixelState.getUntracked() && event.packet is ScoreboardDisplayS2CPacket && event.packet.slot == ScoreboardDisplaySlot.SIDEBAR) {
            _skyblockState.set(event.packet.name == "SBScoreboard")
        }
    }

    override fun setup() {
        register(::onDisconnect)
        register(::onHypixelPacket)
        register(::onPacket)
    }
}