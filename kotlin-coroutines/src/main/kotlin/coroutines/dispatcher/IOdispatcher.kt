package coroutines.dispatcher

import kotlinx.coroutines.*
import java.util.concurrent.Executors
import kotlin.random.Random
import kotlin.system.measureTimeMillis

suspend fun main() = coroutineScope {
    val time = measureTimeMillis {
        coroutineScope {
            repeat(50){
                launch(Dispatchers.IO) {
                    Thread.sleep(1000)
                }
            }
        }
    }
    println("Took $time ms")
    main2()
}

suspend fun main2() = coroutineScope {
    repeat(1000) {
        launch(Dispatchers.IO) {
            Thread.sleep(200)
            val threadName = Thread.currentThread().name
            println("Running in thread: $threadName")
        }
    }

    val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
}