package `fun`.adaptive.graphics.canvas.platform

import `fun`.adaptive.graphics.canvas.instruction.*
import `fun`.adaptive.graphics.canvas.model.gradient.GradientStop
import `fun`.adaptive.graphics.canvas.model.gradient.LinearGradient
import `fun`.adaptive.graphics.canvas.model.path.Arc
import `fun`.adaptive.graphics.canvas.render.CanvasRenderData
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.render.model.TextRenderData
import `fun`.adaptive.ui.testing.DensityIndependentTestAdapter
import kotlin.math.PI
import kotlin.test.*

class ActualJvmCanvasTest {

    private lateinit var canvas: ActualJvmCanvas
    private lateinit var adapter: DensityIndependentTestAdapter
    
    @BeforeTest
    fun setup() {
        adapter = DensityIndependentTestAdapter()
        canvas = ActualJvmCanvas()
    }
    
    @Test
    fun testInitialization() {
        // Initially, width and height should be 0
        assertEquals(0.0, canvas.width)
        assertEquals(0.0, canvas.height)
        
        // Initially, shouldRedraw should be true
        assertTrue(canvas.shouldRedraw)
        
        // Initially, receiver and buffer should be null
        assertNull(canvas.receiver)
        assertNull(canvas.buffer)
    }
    
    @Test
    fun testSetSize() {
        val width = 100.0
        val height = 150.0
        
        canvas.setSize(width, height)
        
        // Width and height should be set correctly
        assertEquals(width, canvas.width)
        assertEquals(height, canvas.height)
        
        // Receiver and buffer should be initialized
        assertNotNull(canvas.receiver)
        assertNotNull(canvas.buffer)
        
        // Receiver and buffer should have the correct dimensions
        assertEquals(width.toInt(), canvas.receiver?.width)
        assertEquals(height.toInt(), canvas.receiver?.height)
        assertEquals(width.toInt(), canvas.buffer?.width)
        assertEquals(height.toInt(), canvas.buffer?.height)
    }
    
    @Test
    fun testRedrawNeeded() {
        // Initially, shouldRedraw should be true
        assertTrue(canvas.shouldRedraw)
        
        // Set shouldRedraw to false
        canvas.shouldRedraw = false
        assertFalse(canvas.shouldRedraw)
        
        // Call redrawNeeded
        canvas.redrawNeeded()
        
        // shouldRedraw should be true again
        assertTrue(canvas.shouldRedraw)
    }
    
    @Test
    fun testSaveAndRestore() {
        // Initialize canvas
        canvas.setSize(100.0, 100.0)
        
        // Save state
        val id = 1L
        canvas.save(id)
        
        // Modify state
        canvas.transform(Translate(10.0, 20.0))
        canvas.setFill(Color(0xFF0000u))
        
        // Restore state
        canvas.restore(id)
        
        // Draw something to verify state was restored
        // This is more of a visual test, but we can at least check that it doesn't throw an exception
        canvas.draw {
            canvas.fillRect(0.0, 0.0, 50.0, 50.0)
        }
    }
    
    @Test
    fun testDraw() {
        // Initialize canvas
        canvas.setSize(100.0, 100.0)
        
        // Draw something
        canvas.draw {
            canvas.fillRect(0.0, 0.0, 50.0, 50.0)
        }
        
        // Check that the receiver has been updated
        assertNotNull(canvas.receiver)
    }
    
    @Test
    fun testFillRect() {
        // Initialize canvas
        canvas.setSize(100.0, 100.0)
        
        // Draw a rectangle
        canvas.draw {
            canvas.fillRect(10.0, 20.0, 30.0, 40.0)
        }
    }
    
    @Test
    fun testLine() {
        // Initialize canvas
        canvas.setSize(100.0, 100.0)
        
        // Draw a line
        canvas.draw {
            canvas.line(10.0, 20.0, 30.0, 40.0)
        }
    }
    
    @Test
    fun testArc() {
        // Initialize canvas
        canvas.setSize(100.0, 100.0)
        
        // Draw an arc
        canvas.draw {
            canvas.arc(50.0, 50.0, 30.0, 0.0, PI / 2, false)
        }
    }
    
    @Test
    fun testFillText() {
        // Initialize canvas
        canvas.setSize(100.0, 100.0)
        
        // Draw text
        canvas.draw {
            canvas.setFont("Arial 12")
            canvas.fillText(10.0, 20.0, "Hello, World!")
        }
    }
    
    @Test
    fun testPathOperations() {
        // Initialize canvas
        canvas.setSize(100.0, 100.0)
        
        // Create a path
        val path = canvas.newPath()
        path.moveTo(10.0, 10.0)
        path.lineTo(50.0, 10.0)
        path.lineTo(50.0, 50.0)
        path.lineTo(10.0, 50.0)
        path.closePath(10.0, 50.0, 10.0, 10.0)
        
        // Fill the path
        canvas.draw {
            canvas.fill(path)
        }
        
        // Stroke the path
        canvas.draw {
            canvas.stroke(path)
        }
    }
    
    @Test
    fun testPathWithArc() {
        // Initialize canvas
        canvas.setSize(100.0, 100.0)
        
        // Create a path with an arc
        val path = canvas.newPath()
        path.moveTo(10.0, 10.0)
        path.lineTo(50.0, 10.0)
        
        val arc = Arc(
            x1 = 50.0,
            y1 = 10.0,
            x2 = 50.0,
            y2 = 50.0,
            rx = 20.0,
            ry = 20.0,
            xAxisRotation = 0.0,
            largeArcFlag = 0,
            sweepFlag = 1
        )
        path.arcTo(arc)
        
        path.lineTo(10.0, 50.0)
        path.closePath(10.0, 50.0, 10.0, 10.0)
        
        // Fill the path
        canvas.draw {
            canvas.fill(path)
        }
    }
    
    @Test
    fun testTransformations() {
        // Initialize canvas
        canvas.setSize(100.0, 100.0)
        
        // Test translate
        canvas.draw {
            canvas.transform(Translate(10.0, 20.0))
            canvas.fillRect(0.0, 0.0, 30.0, 40.0)
        }
        
        // Test rotate
        canvas.draw {
            canvas.transform(Rotate(PI / 4, 50.0, 50.0))
            canvas.fillRect(30.0, 30.0, 40.0, 40.0)
        }
        
        // Test scale
        canvas.draw {
            canvas.transform(Scale(2.0, 2.0))
            canvas.fillRect(10.0, 10.0, 20.0, 20.0)
        }
        
        // Test matrix
        canvas.draw {
            canvas.transform(Matrix(1.0, 0.0, 0.0, 1.0, 10.0, 10.0))
            canvas.fillRect(0.0, 0.0, 30.0, 30.0)
        }
        
        // Test skewX
        canvas.draw {
            canvas.transform(SkewX(30.0))
            canvas.fillRect(10.0, 10.0, 20.0, 20.0)
        }
        
        // Test skewY
        canvas.draw {
            canvas.transform(SkewY(30.0))
            canvas.fillRect(10.0, 10.0, 20.0, 20.0)
        }
    }
    
    @Test
    fun testColorsAndGradients() {
        // Initialize canvas
        canvas.setSize(100.0, 100.0)
        
        // Test solid color
        canvas.draw {
            canvas.setFill(Color(0xFF0000u))
            canvas.fillRect(10.0, 10.0, 30.0, 30.0)
        }
        
        // Test gradient
        canvas.draw {
            val gradient = LinearGradient(
                x0 = 10.0,
                y0 = 10.0,
                x1 = 40.0,
                y1 = 40.0,
                stops = listOf(
                    GradientStop(0.0, Color(0xFF0000u)),
                    GradientStop(1.0, Color(0x0000FFu))
                )
            )
            canvas.setFill(gradient)
            canvas.fillRect(10.0, 10.0, 30.0, 30.0)
        }
    }
    
    @Test
    fun testClear() {
        // Initialize canvas
        canvas.setSize(100.0, 100.0)
        
        // Draw something
        canvas.draw {
            canvas.fillRect(10.0, 10.0, 30.0, 30.0)
        }
        
        // Clear the canvas
        canvas.clear()
    }
    
    @Test
    fun testImage() {
        // Initialize canvas
        canvas.setSize(100.0, 100.0)
        
        // Draw an image
        canvas.draw {
            canvas.image(10.0, 10.0, 30.0, 30.0) { setPixel ->
                // Set all pixels to red
                for (i in 0 until 30 * 30) {
                    setPixel(i, 0xFF0000FF.toInt()) // ARGB: opaque red
                }
            }
        }
    }
    
    @Test
    fun testMeasureText() {
        // Initialize canvas
        canvas.setSize(100.0, 100.0)
        
        // Create a render data
        val renderData = CanvasRenderData(adapter)
        renderData.text = TextRenderData()
        renderData.text?.fontName = "Arial"
        renderData.text?.fontSize = 12.0.sp
        
        // Measure text
        val measurement = canvas.measureText(renderData, "Hello, World!")
        
        // Check that the measurement is reasonable
        assertTrue(measurement.width > 0)
        assertTrue(measurement.height > 0)
        assertTrue(measurement.baseline > 0)
    }
    
    @Test
    fun testGetRenderedImage() {
        // Initialize canvas
        canvas.setSize(100.0, 100.0)
        
        // Draw something
        canvas.draw {
            canvas.fillRect(10.0, 10.0, 30.0, 30.0)
        }
        
        // Get the rendered image
        val image = canvas.getRenderedImage()
        
        // Check that the image is not null and has the correct dimensions
        assertNotNull(image)
        assertEquals(100, image.width)
        assertEquals(100, image.height)
    }
}