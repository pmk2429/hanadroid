package com.example.hanadroid.util

class InteractionQueue<T> {
    private val queue = ArrayDeque<T>()

    fun enqueue(item: T) {
        queue.add(item)
    }

    fun dequeue(): T? {
        return if (queue.isNotEmpty()) {
            queue.removeFirst()
        } else {
            null
        }
    }

    fun isEmpty(): Boolean {
        return queue.isEmpty()
    }
}
