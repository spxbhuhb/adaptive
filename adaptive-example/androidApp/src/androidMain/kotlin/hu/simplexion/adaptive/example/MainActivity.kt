package hu.simplexion.adaptive.example

import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import hu.simplexion.adaptive.base.Adaptive
import hu.simplexion.adaptive.base.producer.poll
import hu.simplexion.adaptive.example.api.CounterApi
import hu.simplexion.adaptive.ktor.BasicWebSocketServiceCallTransport
import hu.simplexion.adaptive.service.defaultServiceCallTransport
import hu.simplexion.adaptive.service.getService
import hu.simplexion.adaptive.ui.android.adapter.AdaptiveViewAdapter
import hu.simplexion.adaptive.ui.android.adapter.android
import hu.simplexion.adaptive.ui.basic.text
import hu.simplexion.adaptive.wireformat.withJson
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds

class MainActivity : AppCompatActivity() {

    lateinit var adapter: AdaptiveViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.setBackgroundColor(0xffffff)
        linearLayout.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        withJson()

        defaultServiceCallTransport = BasicWebSocketServiceCallTransport(
            "ws://10.0.2.2:8080/adaptive/service",
            trace = false
        ).also { it.start() }

        adapter = android(this, linearLayout) {
            counter()
        }

        // Set the TextView as the content view of the activity
        setContentView(linearLayout)
    }
}

val counterService = getService<CounterApi>()

fun now() = Clock.System.now()

@Adaptive
fun counter() {

    val counter = poll(1.seconds, 0) { counterService.incrementAndGet() }
    val time = poll(10.milliseconds, default = now()) { now() }
    val time2 = time.minus((time.nanosecondsOfSecond % 1_000_000).nanoseconds).toString().removeSuffix("Z")

    text("$time2 $counter")
    text("$time2 $counter")
    text("$time2 $counter")
    text("$time2 $counter")
    text("$time2 $counter")
    text("$time2 $counter")
    text("$time2 $counter")
    text("$time2 $counter")
    text("$time2 $counter")
    text("$time2 $counter")

    text("$time2 $counter")
    text("$time2 $counter")
    text("$time2 $counter")
    text("$time2 $counter")
    text("$time2 $counter")
    text("$time2 $counter")
    text("$time2 $counter")
    text("$time2 $counter")
    text("$time2 $counter")
    text("$time2 $counter")

    text("$time2 $counter")
    text("$time2 $counter")
    text("$time2 $counter")
    text("$time2 $counter")
    text("$time2 $counter")
    text("$time2 $counter")
    text("$time2 $counter")
    text("$time2 $counter")
    text("$time2 $counter")
    text("$time2 $counter")
}