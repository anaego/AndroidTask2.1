import org.junit.jupiter.api.Test
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
    fun twoListenersTest() {
        val ticker = TestTicker()
        val stopwatch = Stopwatch.Impl(ticker)

        val stopwatchListener1 = Mockito.mock(Stopwatch.StopwatchListener::class.java)
        val stopwatchListener2 = Mockito.mock(Stopwatch.StopwatchListener::class.java)

        stopwatch.resumeStopwatch(stopwatchListener1)

        ticker.tickListener?.onTick()
        Mockito.verify(stopwatchListener1).onValueChange(1)
        Mockito.verify(stopwatchListener2, never()).onValueChange(1)

        stopwatch.resumeStopwatch(stopwatchListener2)

        ticker.tickListener?.onTick()
        Mockito.verify(stopwatchListener1).onValueChange(2)
        Mockito.verify(stopwatchListener2).onValueChange(2)

        stopwatch.pauseStopwatch(stopwatchListener1)

        ticker.tickListener?.onTick()
        Mockito.verify(stopwatchListener1, never()).onValueChange(3)
        Mockito.verify(stopwatchListener2).onValueChange(3)

        stopwatch.pauseStopwatch(stopwatchListener2)

        ticker.tickListener?.onTick()
        Mockito.verify(stopwatchListener2, never()).onValueChange(4)
    }

    @Test
    fun fiveListenersTest() {
        val ticker = TestTicker()
        val stopwatch = Stopwatch.Impl(ticker)

        val stopwatchListener1 = Mockito.mock(Stopwatch.StopwatchListener::class.java)
        val stopwatchListener2 = Mockito.mock(Stopwatch.StopwatchListener::class.java)
        val stopwatchListener3 = Mockito.mock(Stopwatch.StopwatchListener::class.java)
        val stopwatchListener4 = Mockito.mock(Stopwatch.StopwatchListener::class.java)
        val stopwatchListener5 = Mockito.mock(Stopwatch.StopwatchListener::class.java)

        stopwatch.resumeStopwatch(stopwatchListener1)

        ticker.tickListener?.onTick()
        Mockito.verify(stopwatchListener1).onValueChange(1)
        Mockito.verify(stopwatchListener2, never()).onValueChange(1)
        Mockito.verify(stopwatchListener3, never()).onValueChange(1)
        Mockito.verify(stopwatchListener4, never()).onValueChange(1)
        Mockito.verify(stopwatchListener5, never()).onValueChange(1)

        stopwatch.resumeStopwatch(stopwatchListener2)

        ticker.tickListener?.onTick()
        Mockito.verify(stopwatchListener1).onValueChange(2)
        Mockito.verify(stopwatchListener2).onValueChange(2)
        Mockito.verify(stopwatchListener3, never()).onValueChange(2)
        Mockito.verify(stopwatchListener4, never()).onValueChange(2)
        Mockito.verify(stopwatchListener5, never()).onValueChange(2)

        stopwatch.resumeStopwatch(stopwatchListener3)

        ticker.tickListener?.onTick()
        Mockito.verify(stopwatchListener1).onValueChange(3)
        Mockito.verify(stopwatchListener2).onValueChange(3)
        Mockito.verify(stopwatchListener3).onValueChange(3)
        Mockito.verify(stopwatchListener4, never()).onValueChange(3)
        Mockito.verify(stopwatchListener5, never()).onValueChange(3)

        stopwatch.pauseStopwatch(stopwatchListener2)

        ticker.tickListener?.onTick()
        Mockito.verify(stopwatchListener1).onValueChange(4)
        Mockito.verify(stopwatchListener2, never()).onValueChange(4)
        Mockito.verify(stopwatchListener3).onValueChange(4)
        Mockito.verify(stopwatchListener4, never()).onValueChange(4)
        Mockito.verify(stopwatchListener5, never()).onValueChange(4)

        stopwatch.pauseStopwatch(stopwatchListener1)

        ticker.tickListener?.onTick()
        Mockito.verify(stopwatchListener1, never()).onValueChange(5)
        Mockito.verify(stopwatchListener2, never()).onValueChange(5)
        Mockito.verify(stopwatchListener3).onValueChange(5)
        Mockito.verify(stopwatchListener4, never()).onValueChange(5)
        Mockito.verify(stopwatchListener5, never()).onValueChange(5)

        stopwatch.resumeStopwatch(stopwatchListener4)

        ticker.tickListener?.onTick()
        Mockito.verify(stopwatchListener1, never()).onValueChange(6)
        Mockito.verify(stopwatchListener2, never()).onValueChange(6)
        Mockito.verify(stopwatchListener3).onValueChange(6)
        Mockito.verify(stopwatchListener4).onValueChange(6)
        Mockito.verify(stopwatchListener5, never()).onValueChange(6)

        stopwatch.pauseStopwatch(stopwatchListener4)

        ticker.tickListener?.onTick()
        Mockito.verify(stopwatchListener1, never()).onValueChange(7)
        Mockito.verify(stopwatchListener2, never()).onValueChange(7)
        Mockito.verify(stopwatchListener3).onValueChange(7)
        Mockito.verify(stopwatchListener4, never()).onValueChange(7)
        Mockito.verify(stopwatchListener5, never()).onValueChange(7)

        stopwatch.resumeStopwatch(stopwatchListener5)

        ticker.tickListener?.onTick()
        Mockito.verify(stopwatchListener1, never()).onValueChange(8)
        Mockito.verify(stopwatchListener2, never()).onValueChange(8)
        Mockito.verify(stopwatchListener3).onValueChange(8)
        Mockito.verify(stopwatchListener4, never()).onValueChange(8)
        Mockito.verify(stopwatchListener5).onValueChange(8)

        stopwatch.pauseStopwatch(stopwatchListener3)

        ticker.tickListener?.onTick()
        Mockito.verify(stopwatchListener1, never()).onValueChange(9)
        Mockito.verify(stopwatchListener2, never()).onValueChange(9)
        Mockito.verify(stopwatchListener3, never()).onValueChange(9)
        Mockito.verify(stopwatchListener4, never()).onValueChange(9)
        Mockito.verify(stopwatchListener5).onValueChange(9)

        stopwatch.pauseStopwatch(stopwatchListener5)

        ticker.tickListener?.onTick()
        Mockito.verify(stopwatchListener1, never()).onValueChange(10)
        Mockito.verify(stopwatchListener2, never()).onValueChange(10)
        Mockito.verify(stopwatchListener3, never()).onValueChange(10)
        Mockito.verify(stopwatchListener4, never()).onValueChange(10)
        Mockito.verify(stopwatchListener5, never()).onValueChange(10)
    }

    @Test
    fun resumeAfterPauseAll() {
        val ticker = TestTicker()
        val stopwatch = Stopwatch.Impl(ticker)

        val stopwatchListener1 = Mockito.mock(Stopwatch.StopwatchListener::class.java)
        val stopwatchListener2 = Mockito.mock(Stopwatch.StopwatchListener::class.java)

        stopwatch.resumeStopwatch(stopwatchListener1)

        ticker.tickListener?.onTick()
        Mockito.verify(stopwatchListener1).onValueChange(1)
        Mockito.verify(stopwatchListener2, never()).onValueChange(1)

        stopwatch.pauseStopwatch(stopwatchListener1)

        ticker.tickListener?.onTick()
        Mockito.verify(stopwatchListener1, never()).onValueChange(2)
        Mockito.verify(stopwatchListener2, never()).onValueChange(2)

        stopwatch.resumeStopwatch(stopwatchListener2)

        ticker.tickListener?.onTick()
        Mockito.verify(stopwatchListener1, never()).onValueChange(2)
        Mockito.verify(stopwatchListener2).onValueChange(2)

        ticker.tickListener?.onTick()
        Mockito.verify(stopwatchListener1, never()).onValueChange(3)
        Mockito.verify(stopwatchListener2).onValueChange(3)

        stopwatch.pauseStopwatch(stopwatchListener2)

        ticker.tickListener?.onTick()
        Mockito.verify(stopwatchListener1, never()).onValueChange(4)
        Mockito.verify(stopwatchListener2, never()).onValueChange(4)
    }

    @Test
    fun wrongResumePauseOrder() {
        val ticker = TestTicker()
        val stopwatch = Stopwatch.Impl(ticker)

        val stopwatchListener1 = Mockito.mock(Stopwatch.StopwatchListener::class.java)

        stopwatch.pauseStopwatch(stopwatchListener1)

        ticker.tickListener?.onTick()
        Mockito.verify(stopwatchListener1, never()).onValueChange(1)

        stopwatch.resumeStopwatch(stopwatchListener1)

        ticker.tickListener?.onTick()
        Mockito.verify(stopwatchListener1).onValueChange(1)

        stopwatch.pauseStopwatch(stopwatchListener1)

        ticker.tickListener?.onTick()
        Mockito.verify(stopwatchListener1, never()).onValueChange(2)

        stopwatch.resumeStopwatch(stopwatchListener1)

        ticker.tickListener?.onTick()
        Mockito.verify(stopwatchListener1).onValueChange(2)

        stopwatch.resumeStopwatch(stopwatchListener1)

        ticker.tickListener?.onTick()
        Mockito.verify(stopwatchListener1).onValueChange(3)

        stopwatch.pauseStopwatch(stopwatchListener1)

        ticker.tickListener?.onTick()
        Mockito.verify(stopwatchListener1, never()).onValueChange(4)

        stopwatch.pauseStopwatch(stopwatchListener1)

        ticker.tickListener?.onTick()
        Mockito.verify(stopwatchListener1, never()).onValueChange(5)
    }

}