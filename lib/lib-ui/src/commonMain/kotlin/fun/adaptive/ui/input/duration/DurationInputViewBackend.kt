package `fun`.adaptive.ui.input.duration

import `fun`.adaptive.ui.fragment.structural.PopupSourceViewBackend
import `fun`.adaptive.ui.input.InputViewBackend
import kotlinx.datetime.DateTimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class DurationInputViewBackend(
    value: Duration? = null,
    label: String? = null,
    isSecret: Boolean = false
) : InputViewBackend<Duration, DurationInputViewBackend>(
    value, label, isSecret
), PopupSourceViewBackend {

    /**
     * Converts a Duration to a Pair<String?, DateTimeUnit> by finding the most appropriate unit
     * and calculating the amount.
     *
     * @return Pair where the first element is a String? (amount) and the second is a DateTimeUnit (unit)
     */
    fun fromDuration(): Pair<Int?, DateTimeUnit> {
        val duration = inputValue ?: return Pair(null, DateTimeUnit.SECOND)

        // Get the total milliseconds in the duration
        val totalMillis = duration.inWholeMilliseconds

        // Check if the duration can be represented as whole weeks without remainder
        if (totalMillis % (7 * 24 * 60 * 60 * 1000) == 0L) {
            return Pair((totalMillis / (7 * 24 * 60 * 60 * 1000)).toInt(), DateTimeUnit.WEEK)
        }
        
        // Check if the duration can be represented as whole days without remainder
        if (totalMillis % (24 * 60 * 60 * 1000) == 0L) {
            return Pair((totalMillis / (24 * 60 * 60 * 1000)).toInt(), DateTimeUnit.DAY)
        }
        
        // Check if the duration can be represented as whole hours without remainder
        if (totalMillis % (60 * 60 * 1000) == 0L) {
            return Pair((totalMillis / (60 * 60 * 1000)).toInt(), DateTimeUnit.HOUR)
        }
        
        // Check if the duration can be represented as whole minutes without remainder
        if (totalMillis % (60 * 1000) == 0L) {
            return Pair((totalMillis / (60 * 1000)).toInt(), DateTimeUnit.MINUTE)
        }
        
        // Check if the duration can be represented as whole seconds without remainder
        if (totalMillis % 1000 == 0L) {
            return Pair((totalMillis / 1000).toInt(), DateTimeUnit.SECOND)
        }
        
        // If none of the above, represent as milliseconds
        return Pair(totalMillis.toInt(), DateTimeUnit.MILLISECOND)
    }

    /**
     * Converts an amount and a DateTimeUnit to a Duration.
     *
     * @param amount The amount as an Integer
     * @param unit The DateTimeUnit
     * @return Duration
     */
    fun toDuration(amount: Int, unit: DateTimeUnit): Duration {
        when (unit) {
            DateTimeUnit.WEEK -> amount.days * 7
            DateTimeUnit.DAY -> amount.days
            DateTimeUnit.HOUR -> amount.hours
            DateTimeUnit.MINUTE -> amount.minutes
            DateTimeUnit.SECOND -> amount.seconds
            DateTimeUnit.MILLISECOND -> amount.milliseconds
            else -> error("Unsupported unit: $unit")
        }.also {
            return it
        }
    }

    fun setInputValue(amount: Int, unit: DateTimeUnit) {
        inputValue = toDuration(amount, unit)
    }
    
}