package gg.skytils.skytilsmod.commands

import gg.skytils.skytilsmod.commands.impl.SkytilsCommand
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.fabric.FabricClientCommandManager

object SkytilsCommands {
    val commandManager = FabricClientCommandManager.createNative(ExecutionCoordinator.simpleCoordinator())
    val annotationParser = AnnotationParser(
        commandManager,
        FabricClientCommandSource::class.java
    )

    init {
        runCatching {
            annotationParser.parse(
                SkytilsCommand
            )
        }.onFailure { t ->
            t.printStackTrace()
        }
    }
}