package gg.skytils.skytilsmod.commands.impl

import gg.skytils.skytilsmod.Skytils
import gg.skytils.skytilsmod.gui.KeyShortcutsScreen
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Commands

@Commands
object SkytilsCommand {

    @Command("skytils|st")
    fun main(sender: FabricClientCommandSource) {
        sender.sendFeedback(Text.literal("§9§lSkytils §8» §rHello!"))
    }

    @Command("skytils|st keyshortcuts|shortcuts")
    fun openShortcuts() {
        Skytils.displayScreen = KeyShortcutsScreen()
    }
}