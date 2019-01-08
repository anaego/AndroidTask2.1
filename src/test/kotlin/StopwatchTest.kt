import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.never


class StopwatchTest {

    @Test
    fun itWorks() {
        val ticker = TestTicker()
        val stopwatch = Stopwatch.Impl(ticker)

        // TODO Stopwatch.Listener::class.java instead of Stopwatch.Listener::class?
        val listener = Mockito.mock(Stopwatch.Listener::class.java)

        stopwatch.start(listener)
        ticker.listener?.onTick()

        Mockito.verify(listener).onChange(1)

        ticker.listener?.onTick()

        Mockito.verify(listener).onChange(2)

        stopwatch.stop(listener)
        ticker.listener?.onTick()

        // TODO  instead of Mockito.verifyNever(listener).onChange(3)?
        Mockito.verify(listener, never()).onChange(3)
    }
}