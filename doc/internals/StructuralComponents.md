# Structural Components

## Sequence

A sequence of fragments.

* one state variable:
  * an array that contains the indices of sequence items
* `create` calls `build` of the closure's owner for each item
* `patch` calls `patch` of each item (changing items is not supported)

## Select

One fragment selected from a list of options based on criteria. 

* one state variable: 
  * the index of the fragment to show or -1 if no fragment should be shown
* `create` calls `build` of the closure's owner the fragment to show
* `patch` calls `patch` of the shown fragment or if replaces it with another

## Loop

A list of fragments created with an iterator.

* two state variables:
  * an `AdaptiveIteratorFactory` to create the iterator
  * an `AdaptiveFragmentFactory` to create the items of the list
* `create` uses the iterator to create the items of the list
* `patch`
  * rebuilds the list if the iterator is dirty
  * calls `patch` of the items if the iterator is not dirty

## Examples and Considerations

### Basic

* The sequence is at `0`.
* All children are made by calling `build` of the sequence's declaring component (`test`).
* Closure is the same for all fragments.

```kotlin
fun Adaptive.test() { // index: 0
    T1(1) // index: 1
    T1(2) // index: 2
}
```

### Lower order call

* the `test.lowerFun` external state variable will be an `AdaptiveFragmentFactory`
* `test.build()` uses `lowerFun.build()` to create the fragment at index `2`
* `createClosure` of `2` is the closure of `test`
* `declarationClosure` of `2` is `AdaptiveFragmentFactory.closure`

```kotlin
fun Adaptive.test(lowerFun : Adaptive.() -> Unit) { // index: 0
    T1(1) // index:  1
    lowerFun() // index:  2
    T1(2) // index:  3
}
```

### Higher order call

* `higherFun` is a higher order component with a state variable that is an `AdaptiveFragmentFactory`
* `higherFun` uses the fragment factory to create components when the lower order function is called
* `test.build`:
  * creates an instance of `AdaptiveHigherFun`
  * calls `create` of the created instance, `create` 
      * calls `externalPatch` - sets the fragment factory state variable
      * calls `internalPatch`
      * calls `create` of the contained fragment

```kotlin
fun Adaptive.test() {
    T1(1)
    higherFun {
        T1(2)
        T1(3)
    }
    T1(4)
}
```


