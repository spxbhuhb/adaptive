# Canvas

# SVG Path

| Function           | Variant            | SVG                                                     |
|--------------------|--------------------|---------------------------------------------------------|
| Move To            | absolute           | M x y                                                   | 
| Move To            | relative           | m dx dx                                                 | 
| Line To            | absolute           | L x y                                                   | 
| Line To            | relative           | l dx dx                                                 | 
| Horizontal Line To | absolute           | H x y                                                   |
| Horizontal Line To | relative           | h dx dx                                                 |
| Vertical Line To   | absolute           | V x y                                                   |
| Vertical Line To   | relative           | v dx dx                                                 |
| Close path         |                    | Z                                                       |
| Close path         |                    | z                                                       |
| Cubic Bézier Curve | absolute           | C x1 y1, x2 y2, x y                                     |
| Cubic Bézier Curve | relative           | c dx1 dy1, dx2 dy2, dx dy                               |
| Cubic Bézier Curve | shortcut, absolute | S x2 y2, x y                                            |
| Cubic Bézier Curve | shortcut, relative | s dx2 dy2, dx dy                                        |
| Quadratic Curve    | absolute           | Q x1 y1, x y                                            |
| Quadratic Curve    | relative           | q dx1 dy1, dx dy                                        |
| Quadratic Curve    | shortcut, absolute | T x y                                                   |
| Quadratic Curve    | shortcut, relative | t dx dy                                                 |
| Arc                | absolute           | A rx ry x-axis-rotation large-arc-flag sweep-flag x y   |
| Arc                | relative           | a rx ry x-axis-rotation large-arc-flag sweep-flag dx dy |

Android:
* https://developer.android.com/reference/android/graphics/Canvas
* https://developer.android.com/reference/android/graphics/Path

Browser: 
* https://developer.mozilla.org/en-US/docs/Web/API/Canvas_API
* https://developer.mozilla.org/en-US/docs/Web/API/Path2D/Path2D

iOS: 
* https://developer.apple.com/documentation/swiftui/canvas
* https://developer.apple.com/documentation/swiftui/path/