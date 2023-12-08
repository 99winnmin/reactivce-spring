package coroutines.dispatcher

import kotlinx.coroutines.*
import kotlin.random.Random

suspend fun main() = coroutineScope {
    repeat(1000) {
        launch {
            List(1000) { Random.nextLong()}.maxOrNull()
            val threadName = Thread.currentThread().name
            println("Running in thread: $threadName")
        }
    }
}