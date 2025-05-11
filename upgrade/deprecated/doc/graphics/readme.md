# Graphics

> [!CAUTION]
> 
> Support for canvas and svg is very basic, I only add what I need as I go on.
> 

The `canvas` and `svg` namespaces can be used to draw graphics.

The `common` namespace provides fragments to create a canvas or an SVG:

```kotlin
@Adaptive
fun stuff() {
    
    canvas { 
        circle()
    }
    
    svg(Res.drawable.thermometer)
    
}
```

[Canvas Fragments]
* [canvas](./canvas-fragments.md#canvas)
* [circle](./canvas-fragments.md#circle)

[SVG fragments]
* [svg](./svg-fragments.md#svg)

## Platform connection

The `canvas` namespace provides access to the platform-specific canvas through the `ActualCanvas` interface.
This interface is the bridge between `canvas` fragments and the actual platform canvas:

| Platform |                                                                 |
|----------|-----------------------------------------------------------------|
| android  | https://developer.android.com/reference/android/graphics/Canvas |
| browser  | https://developer.mozilla.org/en-US/docs/Web/API/Canvas_API     | 
| ios      | https://developer.apple.com/documentation/swiftui/canvas        |

The `canvas` namespace also provides access to the platform-specific paths through the `ActualPath` interface.

| Platform |                                                                |
|----------|----------------------------------------------------------------|
| android  | https://developer.android.com/reference/android/graphics/Path  |
| browser  | https://developer.mozilla.org/en-US/docs/Web/API/Path2D/Path2D | 
| ios      | https://developer.apple.com/documentation/swiftui/path/        |

## SVG

With the `svg` namespace you can load and render SVG files.

The namespace is based on SVG 1.1: https://www.w3.org/TR/SVG11

> [!NOTE]
>
> The `svg` namespace uses `canvas` to draw SVG-s, but - as of now - it **does not** use direct SVG support.
> 
> There are a few reasons for this:
>
> * SVG-s are turned into a proper fragment tree, this makes tree/instruction operations possible
> * I want to use SVG-s on backends for PDF/image generation
> * Android converts SVG-s to `VectorDrawable`, that needs the Vector Asset Studio and seems to be quite static
> * iOS has "some" SVG support in xCode, but it is really not what I want, it is very static, generates PNG-s
> 
> I'll probably add some optimization, most notably caching the generated images.
>

### SVG Path

Reference: https://www.w3.org/TR/SVG11/paths.html

Test tool: [SvgPathEditor](https://yqnn.github.io/svg-path-editor/)

| Function           | Variant            | Implemented | SVG                                                     |
|--------------------|--------------------|-------------|---------------------------------------------------------|
| Move To            | absolute           | yes         | M x y                                                   | 
| Move To            | relative           | yes         | m dx dx                                                 | 
| Line To            | absolute           | yes         | L x y                                                   | 
| Line To            | relative           | yes         | l dx dx                                                 | 
| Horizontal Line To | absolute           | yes         | H x y                                                   |
| Horizontal Line To | relative           | yes         | h dx dx                                                 |
| Vertical Line To   | absolute           | yes         | V x y                                                   |
| Vertical Line To   | relative           | yes         | v dx dx                                                 |
| Close path         |                    | yes         | Z                                                       |
| Close path         |                    | yes         | z                                                       |
| Cubic Bézier Curve | absolute           | -           | C x1 y1, x2 y2, x y                                     |
| Cubic Bézier Curve | relative           | -           | c dx1 dy1, dx2 dy2, dx dy                               |
| Cubic Bézier Curve | shortcut, absolute | -           | S x2 y2, x y                                            |
| Cubic Bézier Curve | shortcut, relative | -           | s dx2 dy2, dx dy                                        |
| Quadratic Curve    | absolute           | -           | Q x1 y1, x y                                            |
| Quadratic Curve    | relative           | -           | q dx1 dy1, dx dy                                        |
| Quadratic Curve    | shortcut, absolute | -           | T x y                                                   |
| Quadratic Curve    | shortcut, relative | -           | t dx dy                                                 |
| Arc                | absolute           | yes         | A rx ry x-axis-rotation large-arc-flag sweep-flag x y   |
| Arc                | relative           | yes         | a rx ry x-axis-rotation large-arc-flag sweep-flag dx dy |

