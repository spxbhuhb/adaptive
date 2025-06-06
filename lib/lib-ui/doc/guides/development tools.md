# Development tools

## Tracing fragment events

To switch on trace for all descendants of at a given point.

> [!NOTE]
> 
> This is quite expensive as each fragment will try to find the trace context.
> 

[traceWithContextExample](example://)

To switch on trace for one specific fragment:

```kotlin
// this might crash sometimes, I have to investigate
// AdaptiveFragment.jsonTrace = true
fragment().trace = true
fragment().tracePatterns = traceAll.patterns
```

## Dumping the layout

Use [dumpLayoutButton](fragment://) to add a button that dumps a [fragment tree](def://) with [UI layout](def://) information.

For the button to work, you need to add a [local context](def://) that contains the [LayoutTraceContext](class://)
object.

## Tracing layout computations

Uncomment the body of trace functions in [AbstractAuiFragment](class://).