package `fun`.adaptive.graphics.canvas.platform

import `fun`.adaptive.graphics.canvas.instruction.*
import `fun`.adaptive.graphics.canvas.model.gradient.Gradient
import `fun`.adaptive.graphics.canvas.model.gradient.LinearGradient
import `fun`.adaptive.graphics.canvas.render.CanvasRenderData
import `fun`.adaptive.ui.fragment.layout.RawTextMeasurement
import `fun`.adaptive.ui.instruction.decoration.Color
import java.awt.*
import java.awt.geom.AffineTransform
import java.awt.geom.Arc2D
import java.awt.image.BufferedImage
import kotlin.math.PI
import kotlin.math.ceil
import kotlin.math.tan

class ActualJvmCanvas : ActualCanvas {

    // For JVM, we'll use BufferedImage as our "canvas" and Graphics2D as its "context"
    var receiver: BufferedImage? = null
    var receiverContext: Graphics2D? = null

    var buffer: BufferedImage? = null
    var bufferContext: Graphics2D? = null

    // Stack for saving/restoring Graphics2D states
    private val transformStack = mutableListOf<AffineTransform>()
    private val paintStack = mutableListOf<java.awt.Paint>()
    private val strokeStack = mutableListOf<java.awt.Stroke>()
    private val fontStack = mutableListOf<Font>()
    private val compositeStack = mutableListOf<AlphaComposite>()
    private val clipStack = mutableListOf<java.awt.Shape?>()

    override var shouldRedraw: Boolean = true

    override val width: Double
        get() = currentWidth

    override val height: Double
        get() = currentHeight

    var currentWidth = 0.0
    var currentHeight = 0.0

    override fun redrawNeeded() {
        shouldRedraw = true
    }

    private fun newBufferedImage(width: Int, height: Int): BufferedImage {
        // TYPE_INT_ARGB is a good choice for images with alpha channels
        val img = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val g2d = img.createGraphics()
        // Set rendering hints for better quality (e.g., anti-aliasing for text and shapes)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE)
        return img
    }

    override fun setSize(width: Double, height: Double) {
        if (width == currentWidth && height == currentHeight) return

        currentWidth = width
        currentHeight = height

        // No devicePixelRatio for JVM, unless explicitly managed for high-DPI outputs
        val scaledWidth = width.toInt()
        val scaledHeight = height.toInt()

        receiver = newBufferedImage(scaledWidth, scaledHeight)
        receiverContext = receiver?.createGraphics()

        buffer = newBufferedImage(scaledWidth, scaledHeight)
        bufferContext = buffer?.createGraphics()

        // Initial setup for the buffer context if necessary, like applying base transformations
        // bufferContext.scale(scale, scale) // If you introduce manual scaling
    }

    override fun save(id: Long) {
        bufferContext?.let { g2d ->
            transformStack.add(g2d.transform)
            paintStack.add(g2d.paint)
            strokeStack.add(g2d.stroke)
            fontStack.add(g2d.font)
            compositeStack.add(g2d.composite as AlphaComposite)
            clipStack.add(g2d.clip)
        }
    }

    override fun restore(id: Long) {
        bufferContext?.let { g2d ->
            if (transformStack.isNotEmpty()) g2d.transform = transformStack.removeLast()
            if (paintStack.isNotEmpty()) g2d.paint = paintStack.removeLast()
            if (strokeStack.isNotEmpty()) g2d.stroke = strokeStack.removeLast()
            if (fontStack.isNotEmpty()) g2d.font = fontStack.removeLast()
            if (compositeStack.isNotEmpty()) g2d.composite = compositeStack.removeLast()
            if (clipStack.isNotEmpty()) g2d.clip = clipStack.removeLast()
        }
    }

    override fun draw(drawFun: () -> Unit) {
        // In JVM, we don't have requestAnimationFrame.
        // Call drawFun directly to render to the buffer.
        clear() // Clear the buffer before drawing
        drawFun()

        // Copy buffer to receiver. In an off-screen context,
        // you'd typically just use the 'buffer' image as your final output.
        // For a true "receiver" concept (like displaying on screen), you'd draw the buffer
        // onto a visible component's Graphics.
        receiverContext?.clearRect(0, 0, receiver!!.width, receiver!!.height)
        receiverContext?.drawImage(buffer, 0, 0, null)
    }

    override fun arc(
        cx: Double,
        cy: Double,
        radius: Double,
        startAngle: Double,
        endAngle: Double,
        anticlockwise: Boolean
    ) {
        bufferContext?.let { g2d ->
            // Arc2D takes start angle and extent (sweep) in degrees.
            // 0 degrees is at 3 o'clock, positive is counter-clockwise.
            // Convert radians to degrees.
            val startAngleDegrees = Math.toDegrees(startAngle)
            var endAngleDegrees = Math.toDegrees(endAngle)

            // Normalize angles to be within [0, 360) for consistent sweep calculation
            fun normalizeAngle(angle: Double) = (angle % 360 + 360) % 360
            val normalizedStart = normalizeAngle(startAngleDegrees)
            val normalizedEnd = normalizeAngle(endAngleDegrees)

            var sweepAngleDegrees: Double
            if (anticlockwise) {
                if (normalizedEnd >= normalizedStart) {
                    sweepAngleDegrees = normalizedEnd - normalizedStart
                } else {
                    sweepAngleDegrees = (360 - normalizedStart) + normalizedEnd
                }
            } else { // Clockwise
                if (normalizedEnd <= normalizedStart) {
                    sweepAngleDegrees = normalizedEnd - normalizedStart
                } else {
                    sweepAngleDegrees = normalizedEnd - (normalizedStart + 360)
                }
            }

            // Create an Arc2D
            val arc = Arc2D.Double(
                cx - radius, cy - radius, // x, y of the top-left corner of the arc's bounding box
                radius * 2, radius * 2,  // width, height of the arc's bounding box
                startAngleDegrees, // Start angle
                sweepAngleDegrees, // Angular extent (sweep)
                Arc2D.OPEN // Type of arc (OPEN for just the curve, CHORD or PIE for closed shapes)
            )
            g2d.fill(arc)
        }
    }

    override fun newPath(): ActualJvmPath {
        return ActualJvmPath()
    }

    override fun fill(path: ActualPath) {
        path as ActualJvmPath
        bufferContext?.fill(path.receiver)
    }

    override fun stroke(path: ActualPath) {
        path as ActualJvmPath
        bufferContext?.draw(path.receiver)
    }

    override fun fill() {
        // This fill applies to the current path in the Graphics2D context.
        // Graphics2D does not implicitly maintain a path like browser canvas.
        // This method needs to operate on a currently defined path if it's meant to be similar.
        // For now, it will apply to whatever shape was last set or will be set.
        // To make this work like browser `fill()`, you might need to manage a 'current path' on the canvas.
        // For simplicity, if this is called without a specific path, it might not have an effect.
        // A common pattern is `g2d.fill(new Path2D.Double().apply { moveTo(...); lineTo(...); ... })`
        // or a path passed directly to fill(path).
        // If this `fill()` is meant to fill the *last drawn* path, that path needs to be stored.
        // The browser canvas implicitly keeps a current path.
        // Let's assume for this port that `fill(path)` is preferred, or that a path is built and then filled.
    }

    override fun fillText(x: Double, y: Double, text: String) {
        bufferContext?.drawString(text, x.toFloat(), y.toFloat())
    }

    override fun fillRect(x: Double, y: Double, width: Double, height: Double) {
        bufferContext?.fillRect(x.toInt(), y.toInt(), width.toInt(), height.toInt())
    }

    override fun line(x1: Double, y1: Double, x2: Double, y2: Double) {
        bufferContext?.drawLine(x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt())
    }

    override fun image(x: Double, y: Double, width: Double, height: Double, drawFun: ((Int, Int) -> Unit) -> Unit) {
        val img = BufferedImage(width.toInt(), height.toInt(), BufferedImage.TYPE_INT_ARGB)
        val pixelData = IntArray(width.toInt() * height.toInt())

        // The drawFun provides (index, value) where value is a byte (0-255)
        // For BufferedImage, we need ARGB integers.
        // The original `drawFun` likely expects a Uint8ClampedArray.
        // We'll need to adapt this. Assuming the drawFun expects (index, ARGB_value_as_int).
        drawFun { index, value -> pixelData[index] = value }

        img.setRGB(0, 0, width.toInt(), height.toInt(), pixelData, 0, width.toInt())
        bufferContext?.drawImage(img, x.toInt(), y.toInt(), null)
    }

    override fun transform(t: CanvasTransformInstruction) {
        bufferContext?.let { g2d ->
            when (t) {
                is Translate -> g2d.translate(t.tx, t.ty)
                is Rotate -> {
                    // Java2D rotate rotates around (0,0). To rotate around a center point (cx, cy),
                    // translate to (cx, cy), rotate, then translate back.
                    g2d.translate(t.cx, t.cy)
                    g2d.rotate(t.rotateAngle)
                    g2d.translate(-t.cx, -t.cy)
                }
                is Scale -> g2d.scale(t.sx, t.sy)
                is Matrix -> g2d.transform(AffineTransform(t.a, t.b, t.c, t.d, t.e, t.f))
                is SkewX -> {
                    val rad = t.skewAngle * PI / 180
                    val currentTransform = g2d.transform
                    currentTransform.shear(tan(rad), 0.0)
                    g2d.transform = currentTransform
                }
                is SkewY -> {
                    val rad = t.skewAngle * PI / 180
                    val currentTransform = g2d.transform
                    currentTransform.shear(0.0, tan(rad))
                    g2d.transform = currentTransform
                }
            }
        }
    }

    override fun setFont(font: String) {
        bufferContext?.font = Font.decode(font) // Font.decode can parse CSS-like font strings to some extent
    }

    fun Color.toAwtColor() = java.awt.Color(
        (value.toInt() shl 24) and 0xff,
        (value.toInt() shl 24) and 0xff,
        (value.toInt() shl 24) and 0xff,
        (opacity * 255.0).toInt()
    )

    override fun setStroke(color: Color) {
        bufferContext?.color = color.toAwtColor()
    }

    override fun setFill(color: Color) {
        bufferContext?.paint = color.toAwtColor()
    }

    override fun setFill(gradient: Gradient) {
        bufferContext?.let { g2d ->
            when (gradient) {
                is LinearGradient -> {
                    val fractions = gradient.stops.map { it.position.toFloat() }.toFloatArray()
                    val colors = gradient.stops.map { it.color.toAwtColor() }.toTypedArray()

                    val paint = LinearGradientPaint(
                        gradient.x0.toFloat(), gradient.y0.toFloat(),
                        gradient.x1.toFloat(), gradient.y1.toFloat(),
                        fractions, colors
                    )
                    g2d.paint = paint
                }
            }
        }
    }

    override fun clear() {
        bufferContext?.let { g2d ->
            // Save current paint
            val originalPaint = g2d.paint
            val originalComposite = g2d.composite

            // Set composite to clear pixels (useful if there's an alpha channel)
            g2d.composite = AlphaComposite.Clear

            // Fill the whole buffer with transparent pixels
            g2d.fillRect(0, 0, buffer!!.width, buffer!!.height)

            // Restore original paint and composite
            g2d.composite = originalComposite
            g2d.paint = originalPaint
        }
    }

    override fun measureText(renderData: CanvasRenderData, text: String): RawTextMeasurement {
        if (text.isEmpty()) {
            return RawTextMeasurement.ZERO
        }

        bufferContext?.let { g2d ->
            val textRenderData = renderData.text
            var font: Font? = null

            if (textRenderData != null) {
                // You'll need to parse your CSS-like font string into a Java AWT Font.
                // This is a simplified conversion. A robust solution would parse font family, size, weight etc.
                // For now, assuming `toCssString(null)` provides something `Font.decode` can handle (e.g., "12px sans-serif").
                // If it's just "fontSize family", you can construct it.
                val fontSize = textRenderData.fontSize?.value?.toInt() ?: 12 // Default font size
                val fontFamily = textRenderData.fontName ?: Font.SANS_SERIF
                font = Font(fontFamily, Font.PLAIN, fontSize) // Simplified font creation
                g2d.font = font
            } else {
                font = g2d.font // Use current font if no textRenderData
            }

            val metrics = g2d.getFontMetrics(font)

            val width = ceil(metrics.stringWidth(text).toDouble()) + 0.05
            val height = ceil(
                textRenderData?.lineHeight
                    ?: textRenderData?.fontSize?.value?.let { it * 1.5 }
                    ?: (metrics.ascent + metrics.descent).toDouble()
            )

            // Ideographic baseline is tricky in AWT. ascent is typically from baseline to top.
            // AWT FontMetrics `getAscent()` gives distance from baseline to top of most characters.
            // `getDescent()` gives distance from baseline to bottom.
            // The browser's `actualBoundingBoxAscent` and `actualBoundingBoxDescent` are more precise.
            // For a basic port, `metrics.ascent` can be used as a proxy for baseline-to-top.
            val ideographicBaseline = metrics.ascent.toDouble()

            return RawTextMeasurement(width, height, ideographicBaseline)
        }
        return RawTextMeasurement.ZERO // Should not happen if bufferContext is initialized
    }

    /**
     * Helper function to retrieve the rendered BufferedImage.
     * After calling `draw` with your drawing logic, you can get the final image.
     */
    fun getRenderedImage(): BufferedImage? {
        // You might want to draw the buffer to the receiver if you use the receiver for final output
        // receiverContext?.clearRect(0, 0, receiver!!.width, receiver!!.height)
        // receiverContext?.drawImage(buffer, 0, 0, null)
        return receiver // Or buffer, depending on which one you consider the "final" output.
    }
}