# Basic Fragments

## Canvas

Creates a canvas for drawing. For more detailed information check: [Graphics](../graphics)

```kotlin
canvas {
    
}
```

## Image

Displays an image with the given size:

### Instructions

```kotlin
image("/background.jpg", Size(92f, 92f))
```

## SVG

Creates a canvas and adds a `canvas:svg` fragment to it. Check [Graphics](../graphics) for details.

```kotlin
svg(R.drawable.thermometer)
```

You can perform customizations on the SVG, check [Graphics](../graphics) for details.
An example (switched the color of the icon to blue):

```kotlin
svg(R.drawable.thermometer, blue)
```

## Text

Displays the string representation (`toString()`) of the first argument:

```kotlin
text("Hello World")
text(12)
```

### Instructions

```kotlin
text("a", fontName("Courier New"))
text("a", fontSize(12))
text("a", lightFont)
text("a", letterSpacing(- 2))
text("a", noTextWrap)
```