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
            // changed: deleted function body. should something be in there? it's not used anywhere
        }

        override fun resumeStopwatch(stopwatchListener: StopwatchListener) {
            listeners.add(stopwatchListener)

            if (listeners.size == 1) {
                ticker.start(object : Ticker.TickListener {
                    override fun onTick() {
                        // changed: should there be something else???
                        val newValue = value.incrementAndGet()
                        listeners.forEach { it.onValueChange(newValue) }
                        println("Stopwatch : onTick() in Ticker.TickListener, value " + newValue)
                    }
                })
            }
        }

        override fun pauseStopwatch(stopwatchListener: StopwatchListener) {
            listeners.remove(stopwatchListener)
            println("Stopwatch: pauseStopwatch() - removed stopwatchListener")

            if (listeners.size == 0) {
                ticker.stop()
                println("Stopwatch: pauseStopwatch() - stopped ticker")
            }
        }
    }
}