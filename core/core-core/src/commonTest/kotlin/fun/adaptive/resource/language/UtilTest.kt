package `fun`.adaptive.resource.language

import kotlinx.datetime.LocalTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UtilTest {
    
    @Test
    fun testParseLocalizedHourAndMinuteOrNull() {
        // Test valid formats
        assertEquals(LocalTime(12, 34), "12:34".parseLocalizedHourAndMinuteOrNull())
        assertEquals(LocalTime(0, 0), "00:00".parseLocalizedHourAndMinuteOrNull())
        assertEquals(LocalTime(23, 59), "23:59".parseLocalizedHourAndMinuteOrNull())
        
        // Test with spaces
        assertEquals(LocalTime(12, 34), "  12:34  ".parseLocalizedHourAndMinuteOrNull())
        
        // Test with AM/PM
        assertEquals(LocalTime(0, 34), "12:34AM".parseLocalizedHourAndMinuteOrNull())
        assertEquals(LocalTime(0, 34), "12:34 am".parseLocalizedHourAndMinuteOrNull())
        assertEquals(LocalTime(12, 34), "12:34PM".parseLocalizedHourAndMinuteOrNull())
        assertEquals(LocalTime(12, 34), "12:34 pm".parseLocalizedHourAndMinuteOrNull())
        assertEquals(LocalTime(5, 30), "5:30AM".parseLocalizedHourAndMinuteOrNull())
        assertEquals(LocalTime(17, 30), "5:30PM".parseLocalizedHourAndMinuteOrNull())
        
        // Test invalid formats
        assertNull("".parseLocalizedHourAndMinuteOrNull())
        assertNull("12".parseLocalizedHourAndMinuteOrNull())
        assertNull("12:".parseLocalizedHourAndMinuteOrNull())
        assertNull(":34".parseLocalizedHourAndMinuteOrNull())
        assertNull("12:34:56".parseLocalizedHourAndMinuteOrNull())
        assertNull("24:00".parseLocalizedHourAndMinuteOrNull())
        assertNull("12:60".parseLocalizedHourAndMinuteOrNull())
        assertNull("13:00PM".parseLocalizedHourAndMinuteOrNull()) // Invalid PM hour
        assertNull("abc".parseLocalizedHourAndMinuteOrNull())
        assertNull("12:ab".parseLocalizedHourAndMinuteOrNull())
    }
}