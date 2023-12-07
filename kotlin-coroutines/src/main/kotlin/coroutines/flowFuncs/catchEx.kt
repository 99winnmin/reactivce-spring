package coroutines.flowFuncs

import kotlinx.coroutines.flow.*

class MyError : Throwable("My Error")

val flow = flow {
    emit(1)
    emit(2)
    throw MyError()
}

val flow2 = flow {
    emit("Message1")
    throw MyError()
}

val flow3 = flow {
    emit("Message1")
    emit("Message2")
}

suspend fun main(): Unit {
    // onEach 는 예외에 반응하지 않음, map, filter 와 같은 다른 함수에서도 마찬가지
    // 오직 onCompletion 핸들러만 예외가 발생했을 때 호출됨
    flow.onEach { println("Got $it") }
        .catch { println("Caught $it") }
        .collect {println("Collected $it") }

    flow2.catch { emit("Error") }
        .collect { println("Collected $it") }

    flowOf("Message1")
        .catch { emit("Error") }
        .onEach { throw Error(it) }
        .collect { println("Collected $it") }

    // collect 가 예외를 발생시킬 여지가 있다면 collect 의 연산은 onEach 로 옮기고 catch 이전에 두는 방법이 유용함
    flow3.onStart { println("Before") }
        .onEach { throw MyError() }
        .catch { println("Caught $it") }
        .collect()
}