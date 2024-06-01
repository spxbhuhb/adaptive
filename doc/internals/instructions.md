# Instructions

- `ArmStatVariable.isInstrutions` is true when the state variable is an instruction array
- `ArmClassBuilder` checks if there is any state variable with `isInstructions` on true and passes the index of that variable in the constructor

## Inner instructions

```kotlin
import hu.simplexion.adaptive.foundation.Adaptive

@Adaptive
fun someFun() {
    grid {
        RowTemplate(260.dp, 1.fr, 100.dp, 100.dp)
        colTemplate()
        traceLayout
        
        text("Hello World!", white)
    }
}
```

For this to work:

- the called function has to have an `instructions` parameter
- the called function has to have an `@Adaptive` function parameter

Processing:

- happens in `IrFunction2ArmClass`
- `transformStatement` may encounter
  - call to a function that returns with an instruction, handled by `transformCall`
    - normal function call that returns with an AdaptiveInstruction
    - getter with type of `AdaptiveInstruction`
    - getter with type of `Array<AdaptiveInstruction>`
  - call to an instruction constructor, handled by `transformInstructionConstructor`
- all of these cases should check if th

