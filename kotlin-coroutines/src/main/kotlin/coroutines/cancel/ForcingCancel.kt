package coroutines.cancel

import kotlinx.coroutines.*

suspend fun main(): Unit {
//    cannotStop()
//    forcingStop()
//    trackJobIsActive()
    catchingException()
}

suspend fun cannotStop(): Unit = coroutineScope {
    val job = Job()
    launch(job) {
        repeat(1_000) { i ->
            // 복잡한 연산이나 I/O가 큰 작업이 있다고 가정
            // delay 같은 중단점이 없어서 취소가 안됨
            Thread.sleep(200)
            println("job: I'm sleeping $i ...")
        }
    }
    delay(1000)
    job.cancelAndJoin()
    println("main: Now I can quit.")
    delay(1000)
}

suspend fun forcingStop(): Unit = coroutineScope {
    val job = Job()
    launch(job) {
        repeat(1_000) { i ->
            Thread.sleep(200)
            // 중단 가능하지 않으면서 cpu 집약적이거나 시간 집약적인 연산들이 중단 함수에 있다면
            // 각 연산들 사이에 yield 를 넣어주는 것이 좋음
            // BUT 전형적인 최상위 중단 함수이고 중단하고 재개하는 일을 하기 때문에 스레드 풀을 가진 디스패처를 사용하면
            // 스레드가 바뀌는 문제가 생길 수 있음
            yield() // -> CPU 사용량이 크거나 스레드를 블로킹하는 중단 함수에서 자주 사용
            println("job: I'm sleeping $i ...")
        }
    }
    delay(1100)
    job.cancelAndJoin()
    println("main: Now I can quit.")
}

suspend fun trackJobIsActive(): Unit = coroutineScope {
    val job = Job()
    launch(job) {
        do {
            Thread.sleep(200)
            println("Printing")
        } while(isActive) // isActive 는 잡이 액티브한지 확인할 수 있는 코루틴 라이브러리 제공 함수
    }
    delay(1100)
    job.cancelAndJoin()
    println("main: Now I can quit.")
}

suspend fun catchingException(): Unit = coroutineScope {
    val job = Job()
    launch(job) {
        repeat(1_000) { num ->
            Thread.sleep(200)
            // isActive 와 달리 CoroutineScope 에서 호출되어야함, 함수가 하는 일은 잡이 active 하지 않으면 예외 던짐
            // ensureActive 가 좀 더 가벼워서 선호되고 있음
            ensureActive()
            println("Printing $num")
        }
    }
    delay(1100)
    job.cancelAndJoin()
    println("main: Now I can quit.")
}