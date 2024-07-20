# Instructions

- `ArmStatVariable.isInstrutions` is true when the state variable is an instruction array
- `ArmClassBuilder` checks if there is any state variable with `isInstructions` on true and passes the index of that variable in the constructor

## Instruction lowering

Inner and outer instructions are lowered by in `IrFunction2ArmClass` after state definition transform
and before rendering transform.

```kotlin
import hu.simplexion.adaptive.foundation.Adaptive

@Adaptive
fun someFun() {
    grid {
        rowTemplate(260.dp, 1.fr, 100.dp, 100.dp)
        colTemplate()
        
        text("Hello World!", white)
    } .. traceLayout
}
```

For inner instructions:

- the called function has to have an `instructions` parameter
- the called function has to have an `@Adaptive` function parameter
- transformer is `InnerInstructionLowering`

For outer instructions:

- the called function has to have an `instructions` parameter
- transformer is `OuterInstructionLowering`

