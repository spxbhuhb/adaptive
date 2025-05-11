# Call site name

This utility feature is intended mostly for logging, but there may be other useful uses as well.

Then the `@CallSiteName` annotation is present on a function:

- if the function has a `String` parameter called `callSiteName`
- **AND** the parameter value is not specified (using the default value)

The compiler plugin replaces the value of the parameter with fully qualified name
of the first named parent of the function call statement.

Example:

```kotlin
package somePackage.someSubPackage

@CallSiteName
fun test(callSiteName: String = "<unknown>") {
    println(callSiteName)
}

fun hello() {
    test() // prints out "somePackage.someSubPackage.hello"
    test("some other text") // prints out "some other text"
}
```

> [!NOTE]
>
> If the annotation is present, but there is no `callSiteName` parameter or the type of the
> parameter is not `String` the compilation fails.
> 