# Rendering Transformation

* Original functions are split into an initialization and a rendering part.
* The rendering part consists of rendering statements.
* Types of rendering statements:
  * Simple call
  * Sequence
  * Select
  * Loop
  * Higher order call
  * Anonymous function
  * Function reference call
* Each rendering statement is translated into two functions in the RUI component class:
  * builder
  * external patch

## Builder Functions

For each rendering statement there is exactly one builder function that builds the RUI component tree
that belongs to the statement.

The general format of builder functions is shown below. `NNN` is the call site offset of the rendering
statement.

```kotlin
fun ruiBuilderNNN(parentScope : RuiFragment<BT>, vararg arguments : Any?) : RuiFragment<BT> {
    // dispatchReceiver = startScope
    return RuiX(adapter, parentScope, this::ruiExternalPatchNNN, arguments)
}
```

## Simple Call

```kotlin
T1(i*2)
```

```kotlin
class RuiTest() {
    var i: Int = 12
  
    fun ruiBuilder123(parentScope: RuiFragment<BT>): RuiFragment<BT> {
        return RuiT1(adapter, parentScope, this::ruiExternalPatch123, i*2)
    }
  
    fun ruiExternalPatch123(it : RuiFragment<BT>, scopeMask : Long) : Long {
        if ((scopeMask and it.callSiteDependencyMask) == 0) return 0

        it as RuiT1
        if (scopeMask and 1L == 0) { // 1L is the mask for `i`
            it.p0 = this.i * 2
            it.ruiInvalidate(1) // 1 is the mask of `T1.p0`
        }
      
        return scopeMask or (it.ruiDirty0 lsh numberOfStateVariables)
    }
}
```

## Sequence

```kotlin
T0()
T1(i*2)
```

```kotlin
class RuiTest() {
    var i: Int = 12

    fun ruiBuilder123(parentScope: RuiFragment<BT>): RuiFragment<BT> {
        return RuiSequence(
          adapter,
          parentScope,
          this::ruiExternalPatch123, 
          this::ruiBuilder456, // builder for T0()
          this::ruiBuilder789 // builder for T1(i*2)
        )
    }
  
  fun ruiExternalPatch123(it : RuiFragment<BT>, scopeMask : Long) : Long {
      
  }
}
```

| Statement Type          | Builder Mechanics                                                                                                                                             |
|-------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Simple Call             |                                                                           |
| Sequence                | Create an instance of `RuiSequence`. <br/>Arguments: builder functions of the sequence items.                                                                 |
| Select                  | Create an instance of `RuiSelect`.<br/>Arguments: selector function and builder functions of the options.                                                     |
| Loop                    | Create an instance of `RuiLoop`.<br/>Arguments: iterator function and the builder function of the loop body.                                                  |
| Higher Order Call       | Create an instance of the given component.<br/>Arguments: parameters of the call, the function parameter replaced with the builder of the parameter function. |
| Anonymous Function      | Create an instance of `RuiAnonymous`.<br/>Arguments: parameters of the anonymous function.                                                                    |
| Function Reference Call | Call the builder of the anonymous function.<br/>Arguments: parameters of the anonymous function.                                                              |
