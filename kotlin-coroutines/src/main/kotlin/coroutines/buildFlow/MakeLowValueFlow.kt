package coroutines.buildFlow

import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

suspend fun main() {
    flowOf(1,2,3,4,5)
        .collect{ print(it) }
    emptyFlow<Int>()
        .collect{ print(it) }
}