package coroutines.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.selects.select

suspend fun requestData1(): String {
    delay(10000)
    return "Data1"
}

suspend fun requestData2(): String {
    delay(100)
    return "Data2"
}

val scope = CoroutineScope(SupervisorJob())

suspend fun askMultipleForData(): String {
    val defData1 = scope.async { requestData1() }
    val defData2 = scope.async { requestData2() }
    return select {
        defData1.onAwait{ it }
        defData2.onAwait{ it }
    }
}

suspend fun askMultipleForData2(): String = coroutineScope {
     select {
        async { requestData1() }.onAwait { it }
        async { requestData2() }.onAwait { it }
    }
}

suspend fun askMultipleForData3(): String = coroutineScope {
    select {
        async { requestData1() }.onAwait { it }
        async { requestData2() }.onAwait { it }
    }.also { coroutineContext.cancelChildren() }
}

//suspend fun askMultipleForData4(): String = raceOf({
//    requestData1()
//}, {
//    requestData2()
//})

suspend fun main(): Unit = coroutineScope {
//    println(askMultipleForData())
//    println(askMultipleForData2())
    println(askMultipleForData3())
}