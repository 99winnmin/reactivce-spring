package coroutines.flow

import kotlinx.coroutines.flow.*

suspend fun main() {
    flow { emit("Message 1") } // 플로우 빌더
        // 중간 연산
        .onEach { println(it) }
        .onStart { println("Do something before") }
        .onCompletion { println("Do something after") }
        .catch { emit("Error") }
        // 최종 연산
        .collect { println("Collected $it") }
}