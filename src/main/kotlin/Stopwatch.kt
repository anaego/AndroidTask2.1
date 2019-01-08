import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.atomic.AtomicLong

interface Stopwatch {

    interface Listener {
        fun onChange(value: Long)
    }

    fun start(listener: Listener)
    fun stop(listener: Listener)

    class Impl(private val ticker: Ticker) : Stopwatch, Ticker.TickListener {

        private val listeners = CopyOnWriteArraySet<Listener>()
        private val value = AtomicLong(0)

        override fun onTick() {
            listeners.forEach { it.onChange(value.incrementAndGet()) }
        }

        override fun start(listener: Listener) {
            listeners.add(listener)

            if (listeners.size == 1) {
                ticker.start(object : Ticker.TickListener {
                    override fun onTick() {

                    }
                })
            }
        }

        override fun stop(listener: Listener) {
            listeners.remove(listener)

            if (listeners.size == 0) {
                ticker.stop()
            }
        }
    }
}