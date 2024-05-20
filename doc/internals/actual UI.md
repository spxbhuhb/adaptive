# Actual UI

Actual UI is the UI the given platform offers. Any given platform may have more than one UI
implementation (like Swing, JavaFX and others on JVM), these are different actual UIs.

## Views and Containers

In any actual UIs there are typically _leaf_ types and _container_ types, the two may be the same:

| Target      | Leaf Type | Container Type | Add                 | Remove                       |
|-------------|-----------|----------------|---------------------|------------------------------|
| jsMain      | `Node`    | `Node`         | `Node.appendChild`  | `Node.removeChild`           |
| androidMain | `View`    | `ViewGroup`    | `ViewGroup.addView` | `ViewGroup.removeView`       |
| iosMain     | `UIView`  | `UIView`       | `UIView.addSubview` | `UIView.removeFromSuperview` | 

## Fragment Kind

There are different kinds of UI fragments:

* independent - can be used with any actual UI
* expect - can be used with actual UI it has implementation for
* dependent - can be used only with a specific actual UI

A fragment is independent when it uses only independent and expect fragments.

Most fragments are independent fragments that use the expect fragments 
provided by the library.

Expect fragments are explicitly marked with the `@AdaptiveExpect` annotation and they
are manually implemented.

```kotlin
@AdaptiveExpect
fun text(content : String) {
    manualImplementation(content)
}
```

The actual implementation of expect fragments are typically dependent fragments.

Dependent fragments usually have a property that stores a _leaf_ or a _container_ instance
of the actual UI.

## Root Container

`AdaptiveAdapter.rootContainer` contains the upmost _container_ of an adaptive UI tree:

* the type of the root container depends on the actual UI
* the root container is typically passed to the adapter by the entry function

## Mount and Unmount

`AdaptiveFragment.mount` and `AdaptiveFragment.unmount` functions connect the fragment tree and the actual UI.

`AdaptiveFragment.addActual` and `AdaptiveFragment.removeActual` functions add/remove actual UI elements (_leaf_ or
_container_) to an actual UI _container_.

**dependent fragments**
  * `mount`/`unmount` 
    * calls `addActual`/`removeActual` of the parent fragment
    * calls `mount`/`unmount` of the child fragments
  * `addActual`/`removeActual`
    * _leaf_ fragments
      * throws IllegalStateException
    * _container fragments
      * checks if the child supports the given actual UI, if not, throws IllegalStateException
      * adds the child to the actual UI _container_

**independent fragments**
  * `mount`/`unmount` calls `mount`/`unmount` of the child fragments
  * `addActual`/`removeActual` passes the call to the parent fragment


```text
fun implicit() {
                        // implicit AdaptiveSequence, independent, container       
    text("C")           // expect, leaf
    independent()       // independent
    independentIf()     // independent
}

fun independent() {     
    grid {              // expect, container
        grid {          // expect, container
                        // implicit AdaptiveSequence, independent, container
            text("A")   // expect, leaf
            text("B")   // expect, leaf
        }
    }
}

fun independentIf(i : Int) {
   if (i % 2 == 0) {    // AdaptiveSelect, independent, container
       text("even")     // expect, leaf
   } else 
       text("odd")      // expect, leaf
   }
}
```

## Ordered Layouts

Ordered layouts depend on the order their children are added. When they have a descendant
using `if` and `for` the number of items to layout may change and/or the items in the 
layout may be replaced.

These layouts have to have the information where to apply the changes in the actual UI.

`addAnchor` and `removeAnchor` functions of `AdaptiveFragment` may be used to add
and remove anchors to and from the actual UI. Non-ordered layouts may simply ignore
these calls, ordered layouts have to perform their own anchor management to keep
tha actual UI in-line with the fragment tree.

Also, `addActual` and `removeActual` has an anchor parameter that tells the layout
to which anchor the fragment belongs to.

`if` and `for`

- calls `addAnchor`/`removeAnchor` in `beforeMount`/`afterUnmount`
- when forwarding `addActual`/`removeActual` 
  - checks if there is an anchor specified
    - if so, forwards the call with the specified anchor
    - if not, forwards the call with itself (the `if` or `for`) as anchor
- when forwarding `addAnchor`/`removeAnchor`
  - checks if there is a higher anchor specified
      - if so, forwards the call with the specified anchor
      - if not, forwards the call with itself (the `if` or `for`) as the higher anchor
