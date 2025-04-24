package com.fantasy.components.tools

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

object  SharedFlowBusKey {
    const val test = "test"

    const val MealDelete = "MealDelete"
    const val MealUpdate = "MealUpdate"
    const val MealSavedNew = "MealSavedNew"
    const val FolderUpdate = "FolderUpdate"
    const val FolderDelete = "FolderDelete"
    const val CollectionUpdate = "CollectionUpdate"
    const val CollectionDelete = "CollectionDelete"

    const val CardExchange = "CardExchange"

}

object SharedFlowBus {
    private var events = ConcurrentHashMap<String, MutableSharedFlow<Any>>()
    private var stickyEvents = ConcurrentHashMap<String, MutableSharedFlow<Any>>()

    fun <T> with(objectKey: String): MutableSharedFlow<T> {
        if (!events.containsKey(objectKey)) {
            events[objectKey] = MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)
        }
        return events[objectKey] as MutableSharedFlow<T>
    }

    fun <T> on(objectKey: String): SharedFlow<T> {
        return with(objectKey)
    }

    private fun using() {
        MainScope().launch {
            with<String>(SharedFlowBusKey.test).emit("123")

            on<String>(SharedFlowBusKey.test).collect {

            }
        }
    }

    fun <T> withSticky(objectKey: String): MutableSharedFlow<T> {
        if (!stickyEvents.containsKey(objectKey)) {
            stickyEvents[objectKey] = MutableSharedFlow(1, 1, BufferOverflow.DROP_OLDEST)
        }
        return stickyEvents[objectKey] as MutableSharedFlow<T>
    }

    fun <T> onSticky(objectKey: String): SharedFlow<T> {
        return withSticky(objectKey)
    }
}
