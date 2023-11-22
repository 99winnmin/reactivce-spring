package coroutines.channel

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce

suspend fun main() {
//    test1()
    test2()
}

suspend fun test1(): Unit = coroutineScope {
    val channel = Channel<Int>()
    launch {
        repeat(5) {
            delay(100)
            println("Producing next one : " + it * 2)
            channel.send(it * 2)
        }
    }
    launch {
        repeat(5) {
            println("Consuming next one : " + channel.receive())
        }
    }
}

suspend fun test2(): Unit = coroutineScope {
    val channel = Channel<Int>()
    launch {
        repeat(5) {
            delay(100)
            println("Producing next one : " + it * 2)
            channel.send(it * 2)
        }
//        channel.close()
    }
    launch {
        channel.consumeEach {
            println("Consuming next one : " + it)
        }
//        또는 아래 방식 사용
//        for (i in channel) {
//            println("Consuming next one : " + i)
//        }
    }
}

suspend fun test3(): Unit = coroutineScope {
    val channel = produce {
        repeat(5) {
            delay(1000)
            println("Producing next one : " + it * 2)
            channel.send(it * 2)
        }
    }
    launch {
        for (i in channel) {
            println("Consuming next one : " + i)
        }
    }
}