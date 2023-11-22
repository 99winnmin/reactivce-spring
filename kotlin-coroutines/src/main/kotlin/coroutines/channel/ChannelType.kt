package coroutines.channel

import kotlinx.coroutines.channels.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay


suspend fun main() {
//    unlimited()
//    buffered()
//    rendezvous()
    conflated()
}

suspend fun unlimited(): Unit = coroutineScope {
    val channel = produce(capacity = Channel.UNLIMITED) {
        repeat(5) {
            delay(100)
            println("Producing next one : " + it * 2)
            send(it * 2)
        }
    }

    delay(1000)
    channel.consumeEach {
        println("Consuming next one : " + it)
    }
}

suspend fun buffered(): Unit = coroutineScope {
    val channel = produce(capacity = 3) {
        repeat(5) {
            send(it * 2)
            delay(100)
            println("Sent")
        }
    }

    delay(1000)
    channel.consumeEach {
        println("Consuming next one : " + it)
        delay(1000)
    }
}

suspend fun rendezvous(): Unit = coroutineScope {
//    val channel = produce { // 이 방식도 동일
    val channel = produce(capacity = Channel.RENDEZVOUS) {
        repeat(5) {
            send(it * 2)
            delay(100)
            println("Sent")
        }
    }

    delay(1000)
    channel.consumeEach {
        println("Consuming next one : " + it)
        delay(1000)
    }
}

suspend fun conflated(): Unit = coroutineScope {
    val channel = produce(capacity = Channel.CONFLATED) {
        repeat(5) {
            send(it * 2)
            delay(100)
            println("Sent")
        }
    }

    delay(1000)
    channel.consumeEach {
        println("Consuming next one : " + it)
        delay(1000)
    }
}