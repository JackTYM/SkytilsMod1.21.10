package gg.skytils.skytilsmod.gui

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.Window
import gg.essential.elementa.components.input.UITextInput
import gg.essential.elementa.dsl.effect
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.unstable.layoutdsl.Alignment
import gg.essential.elementa.unstable.layoutdsl.Arrangement
import gg.essential.elementa.unstable.layoutdsl.Modifier
import gg.essential.elementa.unstable.layoutdsl.alignVertical
import gg.essential.elementa.unstable.layoutdsl.color
import gg.essential.elementa.unstable.layoutdsl.column
import gg.essential.elementa.unstable.layoutdsl.fillHeight
import gg.essential.elementa.unstable.layoutdsl.fillRemainingHeight
import gg.essential.elementa.unstable.layoutdsl.fillRemainingWidth
import gg.essential.elementa.unstable.layoutdsl.fillWidth
import gg.essential.elementa.unstable.layoutdsl.layoutAsBox
import gg.essential.elementa.unstable.layoutdsl.onLeftClick
import gg.essential.elementa.unstable.layoutdsl.row
import gg.essential.elementa.unstable.layoutdsl.scrollable
import gg.essential.elementa.unstable.state.v2.add
import gg.essential.elementa.unstable.state.v2.mutableListStateOf
import gg.essential.elementa.unstable.state.v2.remove
import gg.essential.elementa.unstable.state.v2.set
import gg.essential.elementa.unstable.state.v2.stateOf
import gg.essential.universal.UKeyboard
import gg.essential.vigilance.gui.settings.CheckboxComponent
import gg.skytils.skytilsmod.core.PersistentSave
import gg.skytils.skytilsmod.features.impl.handlers.KeyShortcuts
import gg.skytils.skytilsmod.gui.layout.button
import gg.skytils.skytilsmod.gui.layout.text
import net.minecraft.client.input.KeyInput
import net.minecraft.client.option.KeyBinding
import net.minecraft.util.StringHelper
import java.awt.Color

class KeyShortcutsScreen : WindowScreen(ElementaVersion.V10) {
    private val shortcuts = mutableListStateOf(*KeyShortcuts.shortcuts.toTypedArray())
    private var currentlyEditingKeybind: KeyShortcuts.KeybindShortcut? = null

    init {
        window.layoutAsBox {
            column(Modifier.fillHeight(padding = 20f).fillWidth(), horizontalAlignment = Alignment.Center) {
                text("Key Shortcuts", Modifier.alignVertical(Alignment.Start(20f)))
                //scroller
                scrollable(Modifier.fillRemainingHeight().fillWidth(), vertical = true) {
                    column(Modifier.fillWidth(), verticalArrangement = Arrangement.spacedBy(5f)) {
                        forEach(shortcuts) { shortcut ->
                            val index = shortcuts.getUntracked().indexOf(shortcut)
                            if (index < 0 ) return@forEach
                            row(Modifier.fillWidth(0.8f), horizontalArrangement = Arrangement.spacedBy(5f)) {
                                CheckboxComponent(shortcut.enabled)().onValueChange { newValue ->
                                    shortcuts.set(index, shortcut.copy(enabled = newValue!! as Boolean))
                                }
                                UITextInput("Executed Command")(Modifier.fillRemainingWidth().onLeftClick {
                                    if (currentlyEditingKeybind == null) grabWindowFocus()
                                }).also { input ->
                                    // Setting text directly is too early as the parent isn't initialized yet
                                    Window.enqueueRenderOperation {
                                        input.setText(shortcut.message)
                                    }
                                    input.onKeyType { _, _ ->
                                        val filtered = input.getText().filter { StringHelper.isValidChar(it.code) }.take(256)
                                        input.setText(filtered)
                                        shortcuts.set(index, shortcut.copy(message = filtered))
                                    }
                                }
                                val keyName = getKeyName(shortcut.keyCode)
                                val reused = shortcut.keyCode != 0
                                        && (client?.options?.allKeys?.any { it.matchesKey(KeyInput(shortcut.keyCode, -1, shortcut.modifiers)) } == true
                                        || shortcuts.getUntracked().any { it.keyCode != 0 && it !== shortcut && it.keyCode == shortcut.keyCode })
                                button({
                                    if (currentlyEditingKeybind == shortcut)
                                        "> §e${keyName}§f <"
                                    else if (reused)
                                        "§c$keyName"
                                    else keyName
                                }) {
                                    currentlyEditingKeybind = shortcut
                                }
                                button(stateOf("Remove")) {
                                    shortcuts.remove(shortcut)
                                }
                            }.effect(OutlineEffect(Color(0xa0a0a0), 1f))
                        }
                    }
                }
                row(horizontalArrangement = Arrangement.spacedBy(5f)) {
                    button(stateOf("Save and Exit")) {
                        close()
                    }
                    button(stateOf("Add Shortcut")) {
                        shortcuts.add(KeyShortcuts.KeybindShortcut("", 0))
                    }
                }
            }
        }
    }

    override fun onScreenClose() {
        super.onScreenClose()

        KeyShortcuts.shortcuts.run {
            clear()
            addAll(shortcuts.getUntracked().filter { it.keyCode != 0 && it.message.isNotBlank() })
        }

        PersistentSave.markDirty<KeyShortcuts>()
    }

    override fun onKeyPressed(keyCode: Int, typedChar: Char, modifiers: UKeyboard.Modifiers?) {
        currentlyEditingKeybind?.let { editingKeybind ->
            val index = shortcuts.getUntracked().indexOf(editingKeybind)
            if (index < 0) return@let
            val modifiersBitfield = modifiers?.let(KeyShortcuts.Modifiers::fromUCraftBitfield)
                ?: KeyShortcuts.Modifiers.getPressed().let(KeyShortcuts.Modifiers::getBitfield)
            when {
                keyCode == UKeyboard.KEY_ESCAPE -> {
                    shortcuts.set(index, editingKeybind.copy(keyCode = 0, modifiers = 0))
                }
                keyCode != 0 -> {
                    shortcuts.set(index, editingKeybind.copy(keyCode = keyCode, modifiers = modifiersBitfield))
                }
                typedChar.code > 0 -> {
                    shortcuts.set(index, editingKeybind.copy(keyCode = typedChar.uppercaseChar().code, modifiers = modifiersBitfield))
                }
            }
            currentlyEditingKeybind = null
        } ?: super.onKeyPressed(keyCode, typedChar, modifiers)
    }

    override fun onMouseClicked(mouseX: Double, mouseY: Double, mouseButton: Int) {
        currentlyEditingKeybind?.let { editingKeybind ->
            val index = shortcuts.getUntracked().indexOf(editingKeybind)
            if (index < 0) return@let
            shortcuts.set(index, editingKeybind.copy(keyCode = mouseButton - 100, modifiers = KeyShortcuts.Modifiers.getPressed().let(KeyShortcuts.Modifiers::getBitfield)))
            currentlyEditingKeybind = null
        } ?: super.onMouseClicked(mouseX, mouseY, mouseButton)
    }

    private fun getKeyName(keyCode: Int) =
        if (keyCode < 0) {
            "Button ${keyCode + 101}"
        } else {
            @Suppress("DEPRECATION")
            UKeyboard.getKeyName(keyCode) ?:
            if (keyCode == 0) "None" else "Key $keyCode"
        }
}