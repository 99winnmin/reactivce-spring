package coroutines.flow

fun m(i: Int): Int {
    print("m$i ")
    return i*i
}

fun f(i: Int): Boolean {
    print("f$i ")
    return i >= 10
}

fun main() {
    listOf(1,2,3,4,5,6,7,8,9,10)
        .map { m(it) }
        .find { f(it) }
        .let { print(it) }
    // m1 m2 m3 m4 m5 m6 m7 m8 m9 m10 f1 f4 f9 f16 16

    println()

    sequenceOf(1,2,3,4,5,6,7,8,9,10)
        .map { m(it) }
        .find { f(it) }
        .let { print(it) }
    // m1 f1 m2 f4 m3 f9 m4 f16 16

    println()

    val l = listOf(1,2,3,4,5,6,7,8,9,10)
        .map { m(it) }
    print(l) // [1, 4, 9, 16, 25, 36, 49, 64, 81, 100]
    println(l.find { it > 10 }) // 16
    println(l.find { it > 10 }) // 16
    println(l.find { it > 10 }) // 16

    val s = sequenceOf(1,2,3,4,5,6,7,8,9,10)
        .map { m(it) }
    println(s.toList()) // [1, 4, 9, 16, 25, 36, 49, 64, 81, 100]
    println(s.find { it > 10 }) // m1 m2 m3 m4 16
    println(s.find { it > 10 }) // // m1 m2 m3 m4 16
    println(s.find { it > 10 }) // // m1 m2 m3 m4 16
}