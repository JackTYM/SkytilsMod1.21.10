package gg.skytils.skytilsmod.gui.layout

import gg.essential.elementa.components.UIText
import gg.essential.elementa.unstable.layoutdsl.LayoutScope
import gg.essential.elementa.unstable.layoutdsl.Modifier
import gg.essential.elementa.unstable.state.v2.State
import gg.essential.elementa.unstable.state.v2.effect

fun LayoutScope.text(textState: State<String>, modifier: Modifier = Modifier) =
    UIText()(modifier).also { component ->
        effect(component) {
            component.setText(textState())
        }
    }

fun LayoutScope.text(text: String, modifier: Modifier = Modifier) =
    UIText(text)(modifier)