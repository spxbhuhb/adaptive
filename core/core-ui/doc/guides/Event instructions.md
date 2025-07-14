# Event instructions

```text
onClick(handler: UiEventHandler)

onClick(
    feedbackText: String? = null,
    feedbackIcon: GraphicsResourceSet? = null,
    handler: UiEventHandler
)

onDoubleClick(handler: UiEventHandler)

onMove(handler: UiEventHandler)
onLeave(handler: UiEventHandler)

onPrimaryDown(handler: UiEventHandler)
onPrimaryUp(handler: UiEventHandler)

onSecondaryDown(handler: UiEventHandler)
onSecondaryUp(handler: UiEventHandler)

onKeydown(handler: UiEventHandler)

onEnter(
    feedbackText: String? = null,
    feedbackIcon: GraphicsResourceSet? = null,
    handler: UiEventHandler
)


noPointerEvents
enablePointerEvents
```