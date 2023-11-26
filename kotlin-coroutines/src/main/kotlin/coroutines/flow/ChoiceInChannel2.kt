package coroutines.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.selects.select

suspend fun main(): Unit = runBlocking {
    val c1 = Channel<Char>(capacity = 2)
    val c2 = Channel<Char>(capacity = 2)

    // 값을 보냄
    launch {
        for(c in 'A'..'H'){
            delay(400)
            select<Unit> {
                c1.onSend(c) { println("Sent $c to 1")}
                c2.onSend(c) { println("Sent $c to 2")}
            }
        }
    }

    // 값을 받음
    launch {
        while(true) {
            delay(1000)
            val c = select<String> {
                c1.onReceive { "$it from 1" }
                c2.onReceive { "$it from 2" }
            }
            println("Received $c")
        }
    }
}