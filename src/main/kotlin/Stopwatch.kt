import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.atomic.AtomicLong

interface Stopwatch {

    interface StopwatchListener {
        fun onValueChange(value: Long)
    }

    fun resumeStopwatch(stopwatchListener: StopwatchListener)
    fun pauseStopwatch(stopwatchListener: StopwatchListener)

    class Impl(private val ticker: Ticker) : Stopwatch, Ticker.TickListener {

        private val listeners = CopyOnWriteArraySet<StopwatchListener>()
        private val value = AtomicLong(0)

        override fun onTick() {
            // TODO had to change it cause the value was different for every listener
            val newValue = value.incrementAndGet()
            listeners.forEach { it.onValueChange(newValue) }
            println("Stopwatch : onTick() in class, value " + value.incrementAndGet())
        }

        override fun resumeStopwatch(stopwatchListener: StopwatchListener) {
            listeners.add(stopwatchListener)

            if (listeners.size == 1) {
                ticker.start(object : Ticker.TickListener {
                    override fun onTick() {
                        // TODO should there be something else???
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