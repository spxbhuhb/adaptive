---
status: outdated
---

# Layout

[ui layout](def://?inline)

Adaptive layouts are typically independent of the layouts provided by the underlying [actual UI](def://).

One goal of the layout system is to make sure that [applications](def://) written with [Adaptive](def://)
have the very same look and feel on all [platforms](def://).

## Box model and position

The fragment box consists of four parts:

- [margins](def://)
- [border](def://)
- [padding](def://)
- content

```text
    Top
Left┌────────────────────────────────────────────┐   ┬ 
    │                  MARGIN                    │   │
    │  ┌──────────────────────────────────────┐  │   │
    │  │               BORDER                 │  │   │
    │  │  ┌───────────────────────────────┐   │  │   │
    │  │  │           PADDING             │   │  │   │  Height
    │  │  │  ┌─────────────────────────┐  │   │  │   │ 
    │  │  │  │        CONTENT          │  │   │  │   │
    │  │  │  └─────────────────────────┘  │   │  │   │
    │  │  └───────────────────────────────┘   │  │   │
    │  └──────────────────────────────────────┘  │   │
    └────────────────────────────────────────────┘   ┴
    
    ├────────────────── Width ───────────────────┤
```

The first three [margin](def://), [border](def://) and [padding](def://) together are
called the [layout surrounding](def://).

Width and height together define the size of the fragment.

In contrast with other layout systems, **margin is counted into the size** of a fragment.

Each [UI fragment](def://) has a position that is relative to the top-left corner of 
the [UI container fragment](def://) that contains said [UI fragment](def://).

As the picture above shows, the top-left means the top-left corner of the [layout surrounding](def://),
margins included.

For example, in the code below the top and left position of `text` is 3.dp as the surroundings
sum to 3.dp, so the children positioning start from there.

```kotlin
box {
    margin { 1.dp } .. borders.outline .. padding { 1.dp }
    text("Hello World!")
}
```

## Layout process

The layout process is quite complex as it has to handle many situations while offering
good performance.

The actual layout process is decoupled from fragment [patching](def://).

1. When a fragment recognizes that the layout should be updated, it calls [scheduleUpdate](function://AbstractAuiFragment).
2. [scheduleUpdate](function://AbstractAuiFragment) adds the fragment to the [updateBatch](property://AbstractAuiAdapter) of the adapter.
3. When all fragments have been patched, the fragment that initiated [patching](def://) calls [closePatchBatch](function://AuiAdapter).
4. [closePatchBatch](function://AuiAdapter) calls [updateLayout](function://AbstractAuiFragment) for each fragment in [updateBatch](property://AbstractAuiAdapter)

[updateLayout](function://AbstractAuiFragment)

1. checks if the fragment is able to update the layout by itself
   1. if so, calls [computeLayout](function://AbstractAuiFragment) and [placeLayout](function://AbstractAuiFragment)
   2. if not, calls [updateLayout](function://AbstractAuiFragment) of the [ui container fragment](def://)
   
[ui container fragments](def://) typically call [computeLayout](function://AbstractAuiFragment) and [placeLayout](function://AbstractAuiFragment)
of the fragments they contain.

[computeLayout](function://AbstractAuiFragment) gets a [sizing proposal](def://), an instance of [SizingProposal](class://).

This proposal contains:

- [minWidth](property://SizingProposal)
- [maxWidth](property://SizingProposal)
- [minHeight](property://SizingProposal)
- [maxHeight](property://SizingProposal)


[computeLayout](function://AbstractAuiFragment) then calculates the layout and saves the results of the calculations into
the [render data](def://) of the [ui fragment](def://), which is an instance of [AuiRenderData](class://).

The final result of the calculation is represented by four fields of [AuiRenderData](class://):

- [finalTop](property://AuiRenderData)
- [finalLeft](property://AuiRenderData)
- [finalWidth](property://AuiRenderData)
- [finalHeight](property://AuiRenderData)

Once calculated, the [UI rendering](def://) process applies this data to the [actual UI](def://).

### Special cases

#### boxWithProposal

The [boxWithProposal](fragment://) is a special case from the layout point of view because it cannot create 
the child fragments until the proposed sizes are known.

Also, when the proposed dimensions change, it has to patch the children, which is problematic as it is in
the middle of the layout update.

The fragment resolves this conflict by postponing creation and patching of the children until after the
layout update is done, using [addLayoutTask](function://AbstractAuiFragment).

At the time these tasks are called, all layout updates are applied. Most cases the task generates another
layout update round, but that cannot be avoided. (Browsers also do double layout when you call `getBoundingClientRect`).

## See also

- [layout instructions](guide://)
- [Box](guide://)
- [Column](guide://)
- [Flow box](guide://)
- [Grid](guide://)
- [Manual](guide://)
- [Multi-pane workspace](guide://)
- [Root box](guide://)
- [Row](guide://)
- [Split pane](guide://)
- [Wrap](guide://)