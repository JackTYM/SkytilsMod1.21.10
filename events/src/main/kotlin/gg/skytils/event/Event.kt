package gg.skytils.event

open class Event {
    internal open fun continuePropagation(): Boolean = true
}

open class CancellableEvent: Event() {
    var cancelled: Boolean = false

    override fun continuePropagation(): Boolean = !cancelled
}