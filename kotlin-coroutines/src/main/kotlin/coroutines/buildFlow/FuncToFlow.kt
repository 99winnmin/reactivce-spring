package coroutines.buildFlow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow

suspend fun main() {
    val function = suspend {
        // 중단함수를 람다식으로 만듬
        delay(1000)
        "Username"
    }
    function.asFlow()
        .collect{ print(it)}

    ::getUsername
        .asFlow()
        .collect{ print(it)}
}

suspend fun getUsername(): String {
    delay(1000)
    return "UserName"
}