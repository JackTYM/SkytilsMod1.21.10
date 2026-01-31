package gg.skytils.event

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun <T : Event> post(event: T) =
    EventPriority.Highest.post(event)

fun <T : Event> postSync(event: T) =
    runBlocking {
        post(event)
    }

suspend fun <T : CancellableEvent> postCancellable(event: T) : Boolean {
    EventPriority.Highest.post(event)
    return event.cancelled
}

fun <T : CancellableEvent> postCancellableSync(event: T) =
    runBlocking {
        postCancellable(event)
    }

inline fun <reified T : Event> on(priority: EventPriority = EventPriority.Normal, noinline block: suspend (T) -> Unit) =
    priority.subscribe<T>(block)

suspend inline fun <reified T : Event> await(priority: EventPriority = EventPriority.Normal) =
    suspendCancellableCoroutine { coroutine ->
        var deregister: () -> Boolean = { true }
        deregister = priority.subscribe<T> { event ->
            if (coroutine.isActive) {
                deregister()
                coroutine.resume(event)
            }
        }
        coroutine.invokeOnCancellation {
            deregister()
        }
    }

suspend inline fun <reified T : Event> await(repetitions: Int, priority: EventPriority = EventPriority.Normal) = run {
    assert(repetitions >= 1) { "Expected repetitions to be at least 1 but received $repetitions" }
    repeat(repetitions) {
        await<T>(priority)
    }
}