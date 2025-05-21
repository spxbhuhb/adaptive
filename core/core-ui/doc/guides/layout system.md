# Layout system

[UI layouts](def://) in [Adaptive](def://) are typically independent of the layouts provided by the underlying
[actual UI](def://). They perform all layout calculations by themselves and position [ui fragments](def://)
with exact coordinates and sizes.

## Layout process overview

Layout calculations start with a [sizing proposal](def://), an instance of the [SizingProposal](class://) class.

This proposal contains:

- [minWidth](property://SizingProposal)
- [maxWidth](property://SizingProposal)
- [minHeight](property://SizingProposal)
- [maxHeight](property://SizingProposal)

This proposal is passed to the [computeLayout](function://AbstractAuiFragment) function, which then calculates
the layout of the fragment.

[computeLayout](function://AbstractAuiFragment) saves the results of the calculations into the [render data](def://) 
of the [ui fragment](def://), which is an instance of [RenderData](class://).

The final result of the calculation is represented by four fields of [RenderData](class://):

- [finalTop](property://RenderData)
- [finalLeft](property://RenderData)
- [finalWidth](property://RenderData)
- [finalHeight](property://renderData)

Once calculated, the [ui rendering](def://) process applies this data to the [actual UI](def://).

## Layout surrounding

Each [ui fragment](def://) has a [layout surrounding](def://) defined by:

- [margins](def://)
- [padding](def://)
- [border](def://)

The surrounding reduces the space the fragment uses for its content.

In contrast with other layout systems, **margin is counted into the size** of a fragment.

