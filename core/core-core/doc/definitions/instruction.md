# Instruction

An instruction is an element carried by [fragments](def://) that can influence their styling,
layout, or behavior.

By definition, the first state variable of all fragments is the list of instructions
passed to that fragment.

Instructions are very frequently used in [UI fragments](def://) but are not limited to them.
They are part of the [fragment state](def://) and are fully reactive; when an instruction
changes, the [fragment](def://) is patched, allowing it to react to the change.

## See also

- [fragment](def://)
- [fragment state](def://)