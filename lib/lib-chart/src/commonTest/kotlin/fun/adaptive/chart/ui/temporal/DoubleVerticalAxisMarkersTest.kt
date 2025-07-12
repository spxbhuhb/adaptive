package `fun`.adaptive.chart.ui.temporal

import kotlin.test.Test
import kotlin.test.assertEquals

class DoubleVerticalAxisMarkersTest {

    @Test
    fun testDetermineDecimalPlaces() {
        // Test with NaN values
        assertEquals(1, determineDecimalPlaces(Double.NaN, 10.0))
        assertEquals(1, determineDecimalPlaces(5.0, Double.NaN))
        
        // Test with infinite values
        assertEquals(1, determineDecimalPlaces(Double.POSITIVE_INFINITY, 10.0))
        assertEquals(1, determineDecimalPlaces(5.0, Double.NEGATIVE_INFINITY))
        
        // Test with zero range
        assertEquals(1, determineDecimalPlaces(5.0, 5.0))
        
        // Test with large ranges
        assertEquals(0, determineDecimalPlaces(0.0, 100.0))
        assertEquals(0, determineDecimalPlaces(-50.0, 150.0))
        
        // Test with medium ranges
        assertEquals(1, determineDecimalPlaces(0.0, 10.0))
        assertEquals(1, determineDecimalPlaces(5.0, 15.0))
        
        // Test with smaller ranges
        assertEquals(2, determineDecimalPlaces(0.0, 1.0))
        assertEquals(2, determineDecimalPlaces(1.5, 2.5))
        
        // Test with very small ranges
        assertEquals(3, determineDecimalPlaces(0.0, 0.1))
        assertEquals(3, determineDecimalPlaces(0.15, 0.25))
        
        // Test with tiny ranges
        assertEquals(4, determineDecimalPlaces(0.0, 0.01))
        assertEquals(4, determineDecimalPlaces(0.015, 0.025))
        
        // Test with extremely small ranges
        assertEquals(5, determineDecimalPlaces(0.0, 0.001))
        assertEquals(5, determineDecimalPlaces(0.0015, 0.0025))
        
        // Test with microscopic ranges
        assertEquals(6, determineDecimalPlaces(0.0, 0.0001))
        assertEquals(6, determineDecimalPlaces(0.00015, 0.00025))
    }
}