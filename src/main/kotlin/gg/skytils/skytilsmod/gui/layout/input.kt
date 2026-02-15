package gg.skytils.skytilsmod.gui.layout

import gg.essential.elementa.UIComponent
import gg.essential.elementa.unstable.layoutdsl.LayoutScope
import gg.essential.elementa.unstable.layoutdsl.Modifier
import gg.essential.elementa.unstable.layoutdsl.animateColor
import gg.essential.elementa.unstable.layoutdsl.childBasedHeight
import gg.essential.elementa.unstable.layoutdsl.childBasedWidth
import gg.essential.elementa.unstable.layoutdsl.column
import gg.essential.elementa.unstable.layoutdsl.hoverColor
import gg.essential.elementa.unstable.layoutdsl.hoverScope
import gg.essential.elementa.unstable.layoutdsl.inheritHoverScope
import gg.essential.elementa.unstable.layoutdsl.row
import gg.essential.elementa.unstable.state.v2.State
import gg.essential.universal.USound
import java.awt.Color

fun LayoutScope.button(text: State<String>, modifier: Modifier = Modifier, onClick: () -> Unit) {
    row(Modifier.hoverScope().animateColor(Color(0, 0, 0, 80)).hoverColor(Color(255, 255, 255, 80)).childBasedWidth(40f).childBasedHeight(10f).then(modifier)) {
        column {
            text(text, Modifier.inheritHoverScope().animateColor(Color(0xe0e0e0)).hoverColor(Color(0xffffa0)))
        }
    }.onMouseClick { event ->
        if (event.mouseButton != 0) return@onMouseClick
        USound.playButtonPress()
        onClick()
    }
}