# Layout system

[UI layouts](def://) in [Adaptive](def://) perform the layout calculations necessary to 
position [UI fragments](def://) with exact coordinates and sizes.

They are typically independent of the layouts provided by the underlying [actual UI](def://).

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

## Layout process overview

Layout calculations start with a [sizing proposal](def://), an instance of [SizingProposal](class://).

This proposal contains:

- [minWidth](property://SizingProposal)
- [maxWidth](property://SizingProposal)
- [minHeight](property://SizingProposal)
- [maxHeight](property://SizingProposal)

The proposal is passed to the [computeLayout](function://AbstractAuiFragment) function, which then calculates
the layout of the fragment.

[computeLayout](function://AbstractAuiFragment) saves the results of the calculations into the [render data](def://)
of the [ui fragment](def://), which is an instance of [AuiRenderData](class://).

The final result of the calculation is represented by four fields of [AuiRenderData](class://):

- [finalTop](property://AuiRenderData)
- [finalLeft](property://AuiRenderData)
- [finalWidth](property://AuiRenderData)
- [finalHeight](property://AuiRenderData)

Once calculated, the [UI rendering](def://) process applies this data to the [actual UI](def://).

## See also

- [built-in UI container fragments](guide://)
- [built-in layout instructions](guide://)