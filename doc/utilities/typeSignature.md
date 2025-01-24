# Type signature

You can get the compile time WireFormat type signature of a type or a value with the `typeSignature` function:

```kotlin
1.typeSignature().print()
typeSignature<AdaptiveInstructionGroup>().println()

val a = listOf(1,2)
a.typeSignature().println()
```

Output:

```text
I
Lfun.adaptive.foundation.instruction.AdaptiveInstructionGroup;
Lkotlin.collections.List<I>;
```

Note, that this is **COMPILE TIME** signature and it **IS** influenced by smart casting.

* The following code prints out `LI;` and then `LB;`. 
* Type of `a` is `I` at the time of the first `typeSignature` call.
* It becomes `LB;` for the second call because of the smart cast.

```kotlin
interface I
class A : I
class B : I

fun main() {
    var a : I = A()

    a.typeSignature.println()

    a = B()
    
    a.typeSignature.println()
}
```
