## Higher Order Functions

Handling of higher order functions is rather complex because of the scopes involved. Unfortunately we can't
build on the actual Kotlin scopes as we transform a temporary runtime memory stack into a persistent object instance
tree.

The following example shows the complexity:

```kotlin
@Adaptive
fun ho(ph: Int, @Adaptive func: (pc: Int) -> Unit) {
    val vv = randomInt()
    func(ph * 2 + vv)
    func(ph * 3 + vv)
}

@Adaptive
fun test(p0: Int) {
    ho(p0) { p1 ->
        ho(p1) { p2 ->
            ho(p2) { p3 ->
                T1(p0 + p1 + p2 + p3)
            }
        }
    }
}
```

### Scopes

*declaration scope* The state defined by a *named original function*.

*anonymous scope* The state defined by an *anonymous original function*. Consists of the function parameters.

*closure* The state defined as the union of the *declaration scope* and all *anonymous scopes* up until the given *call site*.

Example:

```kotlin
fun start(s1: Int) {       // declaration scope
    var s2: Int
    ho(s1) { p1 ->         // anonymous scope 1
        ho(s2) { p2 ->     // anonymous scope 2
            t1(p1 + p2)
        }
    }
}

fun ho(ho1: Int, @Adaptive func: (pc: Int) -> Unit) {
    var ho2: Int
}
```

State variables in the closures:

```
declaration closure    anonymous closure 1    anonymous closure 2

       s1                      s1                   s1
       s2                      s2                   s2
                               p1                   p1
                                                    p2
```

### Considerations

1. Higher-order components may perform calculations needed to properly patch the components deeper in the tree.
2. Components lower in the tree may access state variables higher in the tree.
3. A higher-order component may add the *anonymous component* more than once.

### Anonymous Components

Parameter functions implicitly define components with the *closure* as the component state.
For each parameter function call an instance of `AdaptiveAnonymous` is created. This class contains
the state of the implicitly defined component.

`AdaptiveAnonymous` stores the state in `adaptiveState` which is an array of `Any?`. The plugin takes care
of casting the stored data into the appropriate data type.

The `adaptiveScopeComponents` is an array in `AdaptiveAnonymous` that contains the all the components that store
state variables of the given scope.

### Patching

Similar to other components `AdaptiveAnonymous` is patched in two steps: calling `adaptiveExternalPatch` and calling `adaptivePatch`.

However, the scope in which these functions are called are not the same. `adaptiveExternalPatch` has to be called in the
*call site scope* while `adaptivePatch` has to be called in the *declaration scope*.

External patch depends on the actual call site and the function is defined in the class created for the higher order 
function. The implicit component may have only external state variable (stored in `AdaptiveAnonymous.adaptiveState`),
external patch updates these variables based on the state of the higher order function.

`adaptivePatch` of `AdaptiveAnonymous` calls `adaptiveExternalPatch` of `containedFragment` and then `adaptivePatch` of the contained
fragment. In this case `adaptiveExternalPatch` is defined in the *original function* where the implicit component is declared.














Effective patching have to decide which components may be ignored. External patch passed to implicit fragments have to
know the dirty mask of their parent scopes to make these decisions possible.

This code has a limitation of 64 state variables, but I think we can live with that.

```kotlin
fun adaptiveEp123(it: AdaptiveFragment, scopeMask: Long): Long {
    it as AdaptiveImplicit
    if ((scopeMask and it.callSiteDependencyMask) == 0) return 0
    // do external patch stuff, updates it.adaptiveDirty0
    return scopeMask or (it.adaptiveDirty0 lsh numberOfStateVariables)
}

fun adaptivePatch(scopeMask: Long) {
    val extendedScopeMask = fragment.adaptiveEp123(scopeMask)
    if (extendedScopeMask != 0) fragment.patch(extendedScopeMask)
}
```

`callSiteDependencyMask` bits belong to the state variables of the *end scope*. Any given call site will probably use
a subset of the state variables. We do not have to patch components that do not depend on changed variables.

`numberOfStateVariables` is the number of state variables in the parent scope.

For normal components (not higher order) `scopeMask` is simply the `adaptiveDirty0` of the start scope.

### Recognizing

On the call site we cannot see if the argument is annotated with `Adaptive` or not. Therefore, we have to fetch the
definition of the component called to see if this is actually a higher order function or not.

Compiler version: 1.8.21

```kotlin
@Adaptive
fun a(@Adaptive stuff: () -> Unit) {
    stuff()
}

@Adaptive
fun b() {
    a { T0() }
}
```

```text
MODULE_FRAGMENT name:<main>
  FILE fqName:hu.simplexion.adaptive.kotlin.plugin.adhoc fileName:/Users/tiz/src/adaptive/adaptive-kotlin-plugin/src/test/kotlin/hu/simplexion/adaptive/kotlin/plugin/adhoc/Adhoc.kt
    FUN name:a visibility:public modality:FINAL <> (stuff:kotlin.Function0<kotlin.Unit>) returnType:kotlin.Unit
      annotations:
        Adaptive
      VALUE_PARAMETER name:stuff index:0 type:kotlin.Function0<kotlin.Unit>
        annotations:
          Adaptive
      BLOCK_BODY
        CALL 'public abstract fun invoke (): R of kotlin.Function0 [operator] declared in kotlin.Function0' type=kotlin.Unit origin=INVOKE
          $this: GET_VAR 'stuff: kotlin.Function0<kotlin.Unit> declared in hu.simplexion.adaptive.kotlin.plugin.adhoc.a' type=kotlin.Function0<kotlin.Unit> origin=VARIABLE_AS_FUNCTION
    FUN name:b visibility:public modality:FINAL <> () returnType:kotlin.Unit
      annotations:
        Adaptive
      BLOCK_BODY
        CALL 'public final fun a (stuff: kotlin.Function0<kotlin.Unit>): kotlin.Unit declared in hu.simplexion.adaptive.kotlin.plugin.adhoc' type=kotlin.Unit origin=null
          stuff: FUN_EXPR type=kotlin.Function0<kotlin.Unit> origin=LAMBDA
            FUN LOCAL_FUNCTION_FOR_LAMBDA name:<anonymous> visibility:local modality:FINAL <> () returnType:kotlin.Unit
              BLOCK_BODY
                CALL 'public final fun T0 (): kotlin.Unit declared in hu.simplexion.adaptive.runtime.testing.FragmentsKt' type=kotlin.Unit origin=null
```