package coroutines.flowDetail

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.*
import kotlin.map
import kotlin.random.Random

suspend fun main(): Unit {
//    makeCertainValue()
//    makeParallelValue()
    makeNotSyncValue()
}


fun Flow<*>.counter1() = flow<Int> {
    var counter = 0
    collect{
        counter++
        List(100) { Random.nextLong()}.shuffled().sorted()
        emit(counter)
    }
}

suspend fun makeCertainValue(): Unit = coroutineScope {
    val f1 = List(1000) { "$it" }.asFlow()
    val f2 = List(1000) { "$it" }.asFlow().counter1()

    launch { println(f1.counter1().last()) }
    launch { println(f1.counter1().last()) }
    launch { println(f2.last()) }
    launch { println(f2.last()) }
}

fun Flow<*>.counter2(): Flow<Int> {
    var counter = 0
    return this.map {
        counter++
        List(100) { Random.nextLong()}.shuffled().sorted()
        counter
    }
}

suspend fun makeParallelValue(): Unit = coroutineScope {
    val f1 = List(1000) { "$it" }.asFlow()
    val f2 = List(1000) { "$it" }.asFlow().counter2()

    launch { println(f1.counter2().last()) }
    launch { println(f1.counter2().last()) }
    delay(2000)
    // 하위 2개의 플로우가 병렬적으로
    launch { println(f2.last()) }
    launch { println(f2.last()) }
}

var counter = 0
fun Flow<*>.counter3(): Flow<Int> = this.map {
    counter++
    List(100) { Random.nextLong()}.shuffled().sorted()
    counter
}

suspend fun makeNotSyncValue(): Unit = coroutineScope {
    val f1 = List(1000) { "$it" }.asFlow()
    val f2 = List(1000) { "$it" }.asFlow().counter3()
    // 4개의 코루틴이 전부 병렬적으로 숫자를 셈
    launch { println(f1.counter3().last()) }
    launch { println(f1.counter3().last()) }
    launch { println(f2.last()) }
    launch { println(f2.last()) }
}



