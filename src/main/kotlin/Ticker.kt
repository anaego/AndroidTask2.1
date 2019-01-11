import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

interface Ticker {

    interface TickListener {
        fun onTick()
    }

    fun start(tickListener: TickListener)
    fun stop()

    class Impl : Ticker {

        private val scheduler = Executors.newScheduledThreadPool(0)
        private val future = AtomicReference<ScheduledFuture<*>>()

        override fun start(tickListener: TickListener) {
            future.set(scheduler.scheduleAtFixedRate({ tickListener.onTick() }, 0, 1, TimeUnit.SECONDS))
        }

        override fun stop() {
            future.get().cancel(true)
        }
    }
}