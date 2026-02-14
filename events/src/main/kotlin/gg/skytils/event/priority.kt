package gg.skytils.event

import org.apache.logging.log4j.LogManager
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

private typealias Handler<T> = suspend (T) -> Unit

enum class EventPriority {
    Lowest {
        override val next: EventPriority? = null
    },
    Low {
        override val next: EventPriority = Lowest
    },
    Normal {
        override val next: EventPriority = Low
    },
    High {
        override val next: EventPriority = Normal
    },
    Highest {
        override val next: EventPriority = High
    };

    private val handlers: MutableMap<Class<out Event>, MutableList<Handler<*>>>
            = ConcurrentHashMap<Class<out Event>, MutableList<Handler<*>>>()
    internal abstract val next: EventPriority?

    @PublishedApi
    internal fun <T : Event> subscribe(eventClass: Class<T>, block: Handler<T>) =
        handlers.getOrPut(eventClass, ::CopyOnWriteArrayList).run {
            add(block)
            return@run {
                remove(block)
            }
        }

    @PublishedApi
    internal inline fun <reified T : Event> subscribe(noinline block: Handler<T>) =
        subscribe(T::class.java, block)

    internal open suspend fun <T : Event> post(event: T) {
        logger.trace("Posting ${event.javaClass.name} at priority ${this.name}")
        invokeHandlers(event)
        if (!event.continuePropagation()) return
        next?.post(event)
    }

    internal suspend fun <T : Event> invokeHandlers(event: T) {
        handlers[event.javaClass]?.forEach { handler ->
            runCatching {
                @Suppress("UNCHECKED_CAST")
                (handler as Handler<T>).invoke(event)
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    companion object {
        private val logger = LogManager.getLogger()
    }
}