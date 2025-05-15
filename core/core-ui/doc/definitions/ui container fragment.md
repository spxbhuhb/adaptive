# UI Container fragment

A UI container fragment is a type of [ui fragment](def://) specifically designed to
arrange and position other [ui fragments](def://) within itself.

Each UI container fragment applies a specific, inherent [layout](def://) mechanism. For example,
a [row](fragment://) fragment typically places its child [ui fragments](def://) horizontally
side-by-side, while a [column](fragment://) fragment stacks them vertically.

While the fundamental direction or structure of the layout is hard-coded into 
the container fragment type, the specific behavior and parameters
of this layout are controlled and modified by applying [instructions](def://) to the
container fragment. Instructions allow you to customize aspects like alignment,
spacing, sizing rules, and other layout-related properties.

## See also

- [ui fragment](def://)
- [layout](def://)
- [instruction](def://)