---
status: todo
---

# Text

Displays the string representation (`toString()`) of the first argument:

```kotlin
text("Hello World")
text(12)
```

### Instructions

```text
fontName(fontName: String)
fontName(fontName: () -> String)

fontSize(fontSize: SPixel)
fontSize(fontSize: () -> SPixel)

fontWeight(weight: Int) = FontWeight(weight)
fontWeight(fontWeight: () -> Int)

lineHeight(height: DPixel)
lineHeight(height: () -> DPixel)

fun letterSpacing(value: Double)

fun textColor(value: Int)
fun textColor(value: UInt)
fun textColor(value: Color)

thinFont
extraLightFont
lightFont
normalFont
mediumFont
semiBoldFont
boldFont
extraBoldFont
blackFont

noSelect
noTextWrap
textWrap
underline
smallCaps
```
