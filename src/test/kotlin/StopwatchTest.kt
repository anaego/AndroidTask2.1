import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.never


class StopwatchTest {

    @Test
    fun itWorks() {
        val ticker = TestTicker()
        val stopwatch = Stopwatch.Impl(ticker)

        // TODO Stopwatch.Listener::class.java instead of Stopwatch.Listener::class?
        val stopwatchListener = Mockito.mock(Stopwatch.StopwatchListener::class.java)

        stopwatch.resumeStopwatch(stopwatchListener)
        ticker.tickListener?.onTick()

        Mockito.verify(stopwatchListener).onValueChange(1)

        ticker.tickListener?.onTick()

        Mockito.verify(stopwatchListener).onValueChange(2)

        stopwatch.pauseStopwatch(stopwatchListener)
        ticker.tickListener?.onTick()

        // TODO  instead of Mockito.verifyNever(listener).onChange(3)?
        Mockito.verify(stopwatchListener, never()).onValueChange(3)
    }
}