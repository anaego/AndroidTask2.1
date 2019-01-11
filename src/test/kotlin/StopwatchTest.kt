import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito
import org.mockito.Mockito.never

class StopwatchTest {

    @Test
    fun itWorks() {
        val ticker = TestTicker()
        val stopwatch = Stopwatch.Impl(ticker)

        val stopwatchListener = Mockito.mock(Stopwatch.StopwatchListener::class.java)

        stopwatch.resumeStopwatch(stopwatchListener)
        ticker.tickListener?.onTick()

        Mockito.verify(stopwatchListener).onValueChange(1)

        ticker.tickListener?.onTick()

        Mockito.verify(stopwatchListener).onValueChange(2)

        stopwatch.pauseStopwatch(stopwatchListener)
        ticker.tickListener?.onTick()

        Mockito.verify(stopwatchListener, never()).onValueChange(3)
    }


    @Test
    fun stopwatchValueTest() {
        val ticker = TestTicker()
        val stopwatch = Stopwatch.Impl(ticker)

        val stopwatchListener = Mockito.mock(Stopwatch.StopwatchListener::class.java)

        stopwatch.resumeStopwatch(stopwatchListener)
        ticker.tickListener?.onTick()

        assertEquals(1, stopwatch.value.get())

        ticker.tickListener?.onTick()

        assertEquals(2, stopwatch.value.get())

        stopwatch.pauseStopwatch(stopwatchListener)
        ticker.tickListener?.onTick()

        assertNotEquals(3, stopwatch.value.get())
    }

}