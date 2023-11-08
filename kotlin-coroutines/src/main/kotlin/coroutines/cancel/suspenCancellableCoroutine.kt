package coroutines.cancel

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun main() {
    val result = waitForCondition()
    println("Result is $result")
}

suspend fun waitForCondition(): Boolean {
    return suspendCancellableCoroutine { continuation ->
        // 외부에서 조건을 충족시키는 작업을 수행
        val conditionSatisfied = checkCondition()

        if (conditionSatisfied) {
            // 조건이 이미 충족된 경우 결과를 반환하고 코루틴 종료
            continuation.resume(true)
        } else {
            // 조건이 충족되지 않은 경우, 외부에서 조건이 충족되면 콜백을 호출하여 결과 반환
            val callback: (Boolean) -> Unit = { result ->
                continuation.resume(result)
            }

            // 외부에서 조건 충족 시 콜백을 호출할 수 있는 예시 함수 (가정)
            addConditionListener(callback)

            // 취소 처리
            continuation.invokeOnCancellation {
                // 취소 시에 콜백 등록 해제 또는 필요한 작업 수행
                removeConditionListener(callback)
            }
        }
    }
}

fun checkCondition(): Boolean {
    // 조건을 확인하고 조건이 충족되었는지 여부를 반환하는 예시 함수 (가정)
    // 실제로는 외부 상태를 체크하고 결과를 반환하는 로직이 들어갑니다.
    return true
}

fun addConditionListener(callback: (Boolean) -> Unit) {
    // 외부에서 조건 충족 시 호출할 콜백을 등록하는 예시 함수 (가정)
    // 실제로는 외부 이벤트나 비동기 작업이 발생하면 해당 콜백을 호출합니다.
}

fun removeConditionListener(callback: (Boolean) -> Unit) {
    // 조건 충족 시 호출할 콜백 등록을 해제하는 예시 함수 (가정)
    // 실제로는 콜백 등록 해제 또는 관련 리소스 정리 작업이 들어갑니다.
}
