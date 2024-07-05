package somePackage.someSubPackage

import hu.simplexion.adaptive.reflect.*

val names = mutableListOf<String>()

@CallSiteName
fun test(callSiteName: String = "<unknown>") {
    names += callSiteName
}

@CallSiteName
fun test2(someInt: Int, callSiteName: String = "<unknown>") {
    names += "$someInt $callSiteName"
}

fun hello() {
    test() // prints out "somePackage.someSubPackage.hello"
    test("some other text") // prints out "some other text"
}

class HelloWorld {
    fun wave(someInt: Int) {
        test2(someInt)
    }
}

fun box(): String {
    test()
    hello()
    test2(12)
    HelloWorld().wave(23)

    val pkg = "somePackage.someSubPackage"

    if (names[0] != "$pkg.box") return "Fail: 0 >${names[0]}<"
    if (names[1] != "$pkg.hello") return "Fail: 1"
    if (names[2] != "some other text") return "Fail: 2"
    if (names[3] != "12 $pkg.box") return "Fail: 3"
    if (names[4] != "23 $pkg.HelloWorld.wave") return "Fail: 4 >${names[4]}"

    return "OK"
}