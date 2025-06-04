# Development tools

## Dumping the layout

Use [dumpLayoutButton](fragment://) to add a button that dumps a [fragment tree](def://) with [UI layout](def://) information.

For the button to work, you need to add a [local context](def://) that contains the [LayoutTraceContext](class://)
object.

## Tracing layout computations

Uncomment the body of trace functions in [AbstractAuiFragment](class://).