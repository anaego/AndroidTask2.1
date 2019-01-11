import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito
import org.mockito.Mockito.never


class StopwatchTest {

    @Test
    fun itWorks() {
        val ticker = TestTicker()
        val stopwatch = Stopwatch.Impl(ticker)

        // changed: Stopwatch.Listener::class.java instead of Stopwatch.Listener::class
        val stopwatchListener = Mockito.mock(Stopwatch.StopwatchListener::class.java)

        stopwatch.resumeStopwatch(stopwatchListener)
        ticker.tickListener?.onTick()

        Mockito.verify(stopwatchListener).onValueChange(1)

        ticker.tickListener?.onTick()

        Mockito.verify(stopwatchListener).onValueChange(2)

        stopwatch.pauseStopwatch(stopwatchListener)
        ticker.tickListener?.onTick()

        // changed: instead of Mockito.verifyNever(listener).onChange(3)?
        Mockito.verify(stopwatchListener, never()).onValueChange(3)
        assertEquals(2, 3)
    }


    @Test
    fun stopwatchValueAfterStop() {
        val stopwatch = Stopwatch.Impl(Ticker.Impl())
        var stopwatchRightAfterStop : Long? = null

        val listener1 = object : Stopwatch.StopwatchListener {
            override fun onValueChange(value: Long) {
                println("Main: Listener1: $value")

                if (value > 9) {
                    stopwatch.pauseStopwatch(this) // changed: this instead of listener1
                    assertEquals(stopwatch.value.get(), value)
                    assertTrue(stopwatch.value.get() > 9)
                    println("Main: Listener1 reached 10 & stopped the stopwatch")
                    stopwatchRightAfterStop = stopwatch.value.get()
                }
            }
        }

        val listener2 = object : Stopwatch.StopwatchListener {
            override fun onValueChange(value: Long) {
                println("Main: Listener2: $value")

                if (value > 4) {
                    stopwatch.pauseStopwatch(this) // changed: this instead of listener2
                    assertEquals(stopwatch.value.get(), value)
                    assertTrue(stopwatch.value.get() > 4)
                    println("Main: Listener2 reached 5 & stopped the stopwatch")
                }
            }
        }

        stopwatch.resumeStopwatch(listener1)
        stopwatch.resumeStopwatch(listener2)

        Thread.sleep(20000)
        assertEquals(stopwatchRightAfterStop, stopwatch.value.get())
    }


    // TODO: can't find how to make this work!
    @Test
    fun wasFutureCanceled() {
        val stopwatch = Stopwatch.Impl(Ticker.Impl())

        val listener1 = object : Stopwatch.StopwatchListener {
            override fun onValueChange(value: Long) {
                println("Main: Listener1: $value")

                if (value > 9) {
                    stopwatch.pauseStopwatch(this) // changed: this instead of listener1
                    println("Main: Listener1 reached 10 & stopped the stopwatch")
                    //assertTrue(stopwatch.ticker.future.isCancelled())
                }
            }
        }

        val listener2 = object : Stopwatch.StopwatchListener {
            override fun onValueChange(value: Long) {
                println("Main: Listener2: $value")

                if (value > 4) {
                    stopwatch.pauseStopwatch(this) // changed: this instead of listener2
                    assertEquals(stopwatch.value.get(), value)
                    assertTrue(stopwatch.value.get() > 4)
                    println("Main: Listener2 reached 5 & stopped the stopwatch")
                }
            }
        }

        stopwatch.resumeStopwatch(listener1)
        stopwatch.resumeStopwatch(listener2)
    }
}