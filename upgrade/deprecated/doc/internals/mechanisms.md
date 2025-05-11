# Mechanisms

> [!NOTE]
> 
> This might be a bit outdated, I'll check later.
> 

## Declaration vs. Creation

When talking about mechanism, there is a very important distinction between the **declaration** and the  **creation**
of a fragment.

* **declaration** is the place of the fragment source code
* **creation** is the place where the instance of the fragment is built

There may be only one **declaration**, but there may be many **creation** of a given fragment.

This distinction is very important in case of anonymous fragments, more about that later.

```kotlin
fun Z3.higherFun(builder : Z3.() -> Unit) {
    builder() // creation
    builder() // creation
}

fun Z2.test() {
    higherFun { // creation of `higherFun` and declaration of an anonymous component
        T0()
        T1(i)
    }
}
```

## Index

Each fragment has an `index`: the position of the rendering statement in the original function.

```kotlin
fun Z2.test() {
    // (for the implicit sequence) index: 0
    T0()  // index: 1
    T1(i) // index: 2
}

```

For anonymous fragments two indices exists: one for the **declaration** and one for **creation**. 

```kotlin
fun Z3.higherFun(builder : Z3.() -> Unit) {
    // (for the implicit sequence) index: 0
    T0()      // index: 1
    builder() // index: 2 (creation)
    builder() // index: 3 (creation)
}

fun Z2.test() {
    // (for `higherFun` call) index: 0 (creation)
    // (for the anonymous function) index: 1 (declaration)
    // (for implicit sequence in the anonymous function) index: 1 (creation)
    higherFun { 
        T0()      // index: 2
        T1(i)     // index: 3
    }
}
```

## Build

Fragments are **created** with the `AdaptiveFragment.build` method of the fragment they are **declared** in.

The method has an `index` parameter which is the index of the fragment.

For example:

```kotlin
fun Z2.test() {
    // (for the implicit sequence) index: 0
    T0()      // index: 1
    T1(i)     // index: 2
}
```

Build method of `test`:

```kotlin
override fun build(parent: AdaptiveFragment<BT>, declarationIndex: Int): AdaptiveFragment<BT> {
  
    val fragment = when (declarationIndex) {
        0 -> AdaptiveSequence(adapter, parent, declarationIndex)
        1 -> AdaptiveT0(adapter, parent, declarationIndex)
        2 -> AdaptiveT1(adapter, parent, declarationIndex)
        else -> invalidIndex(declarationIndex) // throws exception
    }
  
    fragment.create()
  
    return fragment
}
```

## Create

* computes the initial state of the component
* builds children

```kotlin
fun create() {

    patch()
  
    // build the contained fragment
    genBuild(this, 0)?.let { children.add(it) }
}
```

## Patch

Patch methods for each fragment:

* `patch` patches the fragment itself, calls `patchExternal` and `patchInternal`
* `patchExternal` patches the external state variables of the fragment, calls `createClosure.owner.genPatchDescendant`
* `patchInternal` patches the internal state variables of the fragment and calls `patch` of its children
* `genPatchDescendant` patches the external variables of a descendant fragment

Notes:

* `patchExternal` calls `genPatchDescendant` of the **creating** fragment this updates all external state variables.
* `genPatchDescendant`
  * is **call site dependent**, it uses the `index` of the fragment to identify the call site
  * always uses `createClosure` to calculate the value of state variables

```kotlin
fun patchInternal() {

    if (genPatchInternal()) {
        // at this point all variables that have been updated are set to dirty
        children.forEach { it.patch() }
    }

    // clear the state, the UI and the state should be in sync now
    dirtyMask = cleanStateMask
}
```

```kotlin
fun genPatchDescendant(fragment : AdaptiveFragment<BT>) {
    when (fragment.index) {
        1 -> {
            TODO("update external state variables of the fragment")
        }
    }
}
```

## Higher Order Arguments

When using higher order functions with the `@Adaptive` annotation, the plugin uses the `BoundFragmentFactory` 
class to pass information about the fragment passed in the parameter.

Higher order functions without `@Adaptive` annotation are state-access transformed, but passed as-is otherwise.

```kotlin
fun higherFun(callback: () -> Unit, @Adaptive builder : () -> Unit) {
    callback() // this is initialization
    // ----- boundary ----
    builder() // this is rendering 
}

fun test() {
    higherFun({ /*...*/ }) {
        /*...*/
    }
}
```

There are three ways to supply higher order arguments:

1. lambda
2. direct function reference
3. property function reference

These differ significantly as the lambda version:

1) has access to the declaring fragment closure and all intermediate lamba closures
2) has build and patching included in the declaring fragment

For reference versions the referenced function has no information about the declaring closure,
it's closure consists solely of its own external state variables.

```kotlin
@Adaptive
fun higherFun(@Adaptive a: () -> Unit) {
    a()
}

@Adaptive
fun b() { }

@Adaptive
val c : () -> Unit

@Adaptive
fun someFun() {
    higherFun {  } // lambda
    higherFun(::a) // direct function reference
    higherFun(c)   // property function reference
```

## Closures

### Named components

Named components have two closures:

* `createClosure`
* `thisClosure`

`createClosure` is:

* the closure at the point where the component is **created**
* provides the data to compute the external state variables of the component

`thisClosure` is 

* a new closure created by the component
* contains the given component in `components`
* provides access to the state variables of the component
* passed to all components built by the component as `createClosure`



It is important that lambda has access to the closure

### Anonymous components

Anonymous components have two different modes: "reference" and "lambda", depending on the way
the higher order argument is supplied

Lambda mode uses the declaration closure, reference mode defines its own closure.

### Lambda anonymous components

Anonymous components have three closures:

* `createClosure`
* `declarationClosure`
* `thisClosure`

`createClosure` is:

* the closure at the point where the anonymous component is **created** in the higher order component
* provides the data to compute the external state variables of the anonymous component

`declarationClosure` is:

* the closure at the point where the anonymous function is **declared**
* found by finding the closest parent that has the same declaring component instance 
* used to create `thisClosure`

`thisClosure` is:

* a new closure created by the anonymous component
* created by calling `AdaptiveClosure.extend`:
  * create a new `AdaptiveClosure` instance
  * copy all data from `declarationClosure` into the new instance
  * add the anonymous component to `components` of the new instance
* used by the child components of the anonymous component to access the state where the anonymous function is **declared**

```kotlin
fun Z2.test() {
    higherFun(12) { lowerFunI1 ->
        higherFun(lowerFunI1) { lowerFunI2 ->
            T1(lowerFunI1 + lowerFunI2)
        }
    }
}

fun Z3.higherFun(higherI : Int, lowerFun : Z3.(lowerFunI : Int) -> Unit) {
    higherFunInner(higherI*2) { lowerFunInnerI -> 
        lowerFun(higherI + lowerFunInnerI)
    }
}

fun Z3.higherFunInner(innerI : Int, lowerFunInner : Z3.(lowerFunInnerI : Int) -> Unit) {
    lowerFunInner(innerI + 1) // Anonymous 1, Anonymous 3
}
```

```text
Fragment                                              Create Closure                                     This Closure                                         State (without factories)

Test 1                                                ?                                                  Test 1                                               []
  + HigherFun 1                                       Test 1                                             HigherFun 1                                          [ higherI= 12 ]
    + HigherFunInner 1                                HigherFun 1                                        HigherFunInner 1                                     [ innerI= 24 (createClosure.higherI * 2) ]
      + Anonymous 1 (from lowerFunInner)              HigherFunInner 1                                   Anonymous 1 extends HigherFun1                       [ higherI= 12 lowerFunInnerI=25 (createClosure.innerI + 1) ]
        + Anonymous 2 (from lowerFun)                 Anonymous 1 extends HigherFun1                     Anonymous 2 extends Test 1                           [ lowerFunI1 = 37 (createClosure.higherI + createClosure.lowerFunInnerI) ]
          + HigherFun 2                               Anonymous 2 extends Test 1                         HigherFun 2                                          [ higherI= 37 (createClosure.lowerFunI1) ]
            + HigherFunInner 2                        HigherFun 2                                        HigherFunInner 2                                     [ innerI= 74 (createClosure.higherI * 2) ]
              + Anonymous 3 (from lowerFunInner)      HigherFunInner 2                                   Anonymous 3 extends HigherFun2                       [ higherI= 37 (thisClosure.higherI) lowerFunInnerI=75 (createClosure.innerI + 1) ]
                + Anonymous 4 (from lowerFun)         Anonymous 3 extends HigherFun2                     Anonymous 4 extends Anonymous 2 extends Test 1       [ lowerFunI1 = 37 (thisClosure.lowerFun1) lowerFunI2 = 112 (createClosure.higherI + createClosure.lowerFunInnerI) ]
                  + T1 1                              Anonymous 4 extends Anonymous 2 extends Test 1     T1                                                   [ p0 = 149 (createClosure.lowerFunI1 + createClosure.lowerFunI2) ]
```

