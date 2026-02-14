package gg.skytils.event.impl

import gg.skytils.event.CancellableEvent

class MouseInputEvent(val x: Int, val y: Int, val button: Int) : CancellableEvent()

class KeyboardInputEvent(val keyCode: Int) : CancellableEvent()