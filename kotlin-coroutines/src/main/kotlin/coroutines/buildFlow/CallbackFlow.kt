//package coroutines.buildFlow
//
//import kotlinx.coroutines.CancellationException
//import kotlinx.coroutines.channels.awaitClose
//import kotlinx.coroutines.channels.trySendBlocking
//import kotlinx.coroutines.flow.callbackFlow
//
//fun flowFrom(api: CallbackBasedApi): Flow<T> = callbackFlow {
//    val callback = object : Callback {
//        override fun onNextValue(value: T) {
//            trySendBlocking(value)
//        }
//        override fun onApiError(cause: Throwable) {
//            cancel(CancellationException("API Error", cause))
//        }
//        override fun onCompleleted() = channel.close()
//    }
//    api.register(callback)
//    awaitClose{api.unregister(callback)}
//}