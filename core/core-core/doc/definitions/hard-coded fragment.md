# Hard-coded fragment

A hard-coded fragment is a [fragment](def://) defined by an [original function](def://)
written in Kotlin.

The source code defines the rendering and logic of these fragments, their
adaptability depends on how flexible the code itself is.

Changing a hard-coded fragment is expensive as it involves recompiling the fragment
and possibly the whole application.

The [compiler plugin](def://) transforms the [original function](def://) into a 
stateful class that can be added to a [fragment tree](def://).

## See also

- [fragment](def://)
- [original function](def://)
- [no-code fragment](def://)
- [compiler plugin](def://)