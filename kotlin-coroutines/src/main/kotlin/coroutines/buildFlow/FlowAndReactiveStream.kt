package coroutines.buildFlow

// 하위 코드들은 kotlinx-coroutines-reactive, kotlinx-coroutines-rx3 라이브러리 필요

//suspend fun main() = coroutineScope {
//    Flux.range(1,5).asFlow()
//        .collect{ print(it)}
//    Flowable.range(1,5).asFlow()
//        .collect{ print(it)}
//    Observable.range(1,5).asFlow()
//        .collect{ print(it)}
//}

//suspend fun main(): Unit = coroutineScope {
//    val flow = flowOf(1,2,3,4,5)
//    flow.asFlux()
//        .doOnNext{ print(it)}
//        .subscribe()
//
//    flow.asFlowable()
//        .subscribe{ print(it)}
//
//    flow.asObservable()
//        .subscribe{ print(it)}
//}