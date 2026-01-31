package gg.skytils.event

fun interface EventHandler <T : Event> {
    suspend fun onEvent(event: T)
}

interface EventSubscriber{
    fun setup()
}

@Suppress("UnusedReceiverParameter")
inline fun <reified T: Event> EventSubscriber.register(
    subscriber: EventHandler<T>,
    priority: EventPriority = EventPriority.Normal
) =
    on<T>(priority, subscriber::onEvent)