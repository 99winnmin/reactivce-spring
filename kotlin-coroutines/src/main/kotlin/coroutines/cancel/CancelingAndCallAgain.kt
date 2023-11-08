package coroutines.cancel

import kotlinx.coroutines.*

suspend fun main() {
//    cancelTest1()
    cancelTest2()
}

suspend fun cancelTest1(): Unit = coroutineScope {
    val job = Job()
    launch(job) {
        try {
            delay(2000)
            println("Job is done")
        } finally {
            println("Finally")
            launch {// 코루틴이 이미 cancelling 상태가 되었기 때문에 무시됨
                println("1. Will not be printed")
            }
            delay(1000) // 예외 발생
            println("2. Will not be printed")
        }
    }

    delay(1100L)
    job.cancelAndJoin()
    println("main: Now I can quit.")
}

suspend fun cancelTest2(): Unit = coroutineScope {
    val job = Job()
    launch(job) {
        try {
            delay(200)
            println("Coroutine finished")
        } finally {
            println("Finally")
            withContext(NonCancellable) {
                delay(1000)
                println("Cleanup done")
            }
        }
    }

    delay(100)
    job.cancelAndJoin()
    println("Done")
}