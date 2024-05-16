# Mechanisms

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
* builds `containedFragment`

```kotlin
fun create() {

    patch()
  
    // build the contained fragment
    containedFragment = build(this, 0)
}
```

## Patch

There are four patch methods for each fragment:

* `patch` patches the fragment itself, calls `patchExternal` and `patchInternal`
* `patchExternal` patches the external state variables of the fragment, calls `createClosure.owner.patchDescendant`
* `patchInternal` patches the internal state variables of the fragment and calls `patch` of its `containedFragment`
* `patchDescendant` patches the external variables of a descendant fragment

Notes:

* `patchExternal` calls `patchDescendant` of the **creating** fragment this updates all external state variables.
* `patchDescendant`
  * is **call site dependent**, it uses the `index` of the fragment to identify the call site
  * always uses `createClosure` to calculate the value of state variables
* `patchInternal`
  * always uses `thisClosure` to calculate the value of state variables

```kotlin
fun patchInternal() {

    TODO("update internal state variables if necessary")

    // at this point all variables that have been updated are set to dirty
    containedFragment?.patch()
  
    // clear the state, the UI and the state should be in sync now
    thisClosure.clear()
}
```

```kotlin
fun patchDescendant(fragment : AdaptiveFragment<BT>) {
    when (fragment.index) {
        1 -> {
            TODO("update external state variables of the fragment")
        }
    }
}
```

## Higher Order Arguments

When using higher order functions the plugin uses the `AdaptiveFragmentFactory` and `AdaptiveSupportFunction` classes.

```kotlin
fun Z3.higherFun(callback: () -> Unit, builder : Z3.() -> Unit) {
    callback() // this is initialization
    // ----- boundary ----
    builder() // this is rendering 
}

fun Z3.test() {
    higherFun({ /*...*/ }) {
        /*...*/
    }
}
```

```kotlin
class AdaptiveHigherFun<BT> : AdaptiveFragment<BT> {
    val state = arrayOfNulls(2)

    fun create() {
        // calls AdaptiveTest.patch which sets the state variables
        // the state variables store the callback argument and the builder argument
        createClosure.owner.patch(this)

        state[0].execute(this) // executes `callback` 

        containedFragment = build(thisClosure, this, 0) // 0 is the index of the `builder` call
    }

    fun build(declarationClosure: AdaptiveClosure<BT>, parent: AdaptiveFragment<BT>, declarationIndex: Int): AdaptiveFragment<BT> {
        
        val fragment = when (declarationIndex) {
            0 -> state[1].build(declarationClosure, parent, declarationIndex)
            else -> invalidIndex(declarationIndex) // throws exception
        }

        fragment.create()

        return fragment
    }

}

class AdaptiveTest<BT> : AdaptiveFragment<BT>  {
    
    fun patch(descendant : AdaptiveFragment<BT>) {
        when (descendant.index) {
            0 -> { // set the state variables of higherFun
                descendant.state[0] = AdaptiveSupportFunction(descendant, index = 0) // index of the support function
                descendant.state[1] = AdaptiveFragmentFactory(descendant, index = 1) // declaration index for the builder
            }
        }
    }
  
}
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

### Anonymous components

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