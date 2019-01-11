import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.atomic.AtomicLong

interface Stopwatch {

    interface StopwatchListener {
        fun onValueChange(value: Long)
    }

    fun resumeStopwatch(stopwatchListener: StopwatchListener)
    fun pauseStopwatch(stopwatchListener: StopwatchListener)

    class Impl(val ticker: Ticker) : Stopwatch, Ticker.TickListener {

        private val listeners = CopyOnWriteArraySet<StopwatchListener>()
        val value = AtomicLong(0)

        override fun onTick() {
            val newValue = value.incrementAndGet()
            listeners.forEach { it.onValueChange(newValue) }
        }

        override fun resumeStopwatch(stopwatchListener: StopwatchListener) {
            listeners.add(stopwatchListener)

            if (listeners.size == 1) {
                ticker.start(this)
            }
        }

        override fun pauseStopwatch(stopwatchListener: StopwatchListener) {
            listeners.remove(stopwatchListener)
            if (listeners.size == 0) {
                ticker.stop()
            }
        }
    }
}