# Instructions

- `ArmStatVariable.isInstrutions` is true when the state variable is an instruction array
- `ArmClassBuilder` checks if there is any state variable with `isInstructions` on true and passes the index of that variable in the constructor

## Instruction lowering

Inner and outer instructions are lowered by in `IrFunction2ArmClass` after state definition transform
and before rendering transform.

```kotlin
import `fun`.adaptive.foundation.Adaptive

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

## Internal handling

- we can pass instructions to fragments in these ways:
  - as function parameters
  - as inner instructions
  - as outer instructions

All these are semantically equivalent but syntactically different.

The compiler plugin:

- moves inner and outer instructions into the `instructions` vararg parameter value,
- sets the state variable of instructions to `AdaptiveInstructionGroup(instructions)`

## Flat vs. Structured

There are two choices about storing instructions in fragments:

1. flat - one simple list
2. tree - a tree of instructions

The flat version:

- faster when looking for instructions
- slower when composing instructions from groups

The tree version:

- may contain structural information
- instructions that are grouped together can be inspected and handled as a unit

For example, we can see if a padding came from the theme or if it 
is manually specified. We cannot replace padding that came from the
theme, we can only override it with a local instruction. Manually specified
padding should be replaced if we want to change it.

I've decided to go with the tree version. This adds some overhead, but keeps the instruction structure.