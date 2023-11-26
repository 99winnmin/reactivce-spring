package coroutines.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext

fun getList(): List<String> = List(3) {
    Thread.sleep(1000)
    "User$it"
}

fun getSequence(): Sequence<String> = sequence {
    repeat(3) {
        Thread.sleep(1000)
        yield("User$it")
    }
}

fun main() {
    val list = getList()
    println("Function started")
    list.forEach{ println(it) }

}

suspend fun test() {
    withContext(newSingleThreadContext("test")) {
        launch {
            repeat(3) {
                delay(100)
                println("코루틴이 돌고 싶어요")
            }
        }
    }

    val list = getSequence()
    list.forEach { println(it) }
}

