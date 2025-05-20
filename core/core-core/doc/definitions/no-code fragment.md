# No-code fragment

A no-code fragment is a [fragment](def://) described purely with data. This data is
interpreted by [Adaptive](def://) to create the [fragment](def://) when needed.

No-code fragments have high adaptability as they can be changed any time by changing
the data that describes a fragment. They can also reduce application code size and
be loaded dynamically, on-demand.

Adding state management logic to no-code fragments is much harder than for
[hard-coded fragments](def://) as the data cannot easily contain Kotlin code.
There are workarounds for this problem, but in general, no-code fragments 
are more rigid about fragment-specific logic.

## See also

- [fragment](def://)
- [hard-coded fragment](def://)