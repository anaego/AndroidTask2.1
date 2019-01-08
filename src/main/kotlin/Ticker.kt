import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

interface Ticker {

    interface TickListener {
        fun onTick()
    }

    fun start(listener: TickListener)
    fun stop()

    class Impl : Ticker {

        private val scheduler = Executors.newScheduledThreadPool(1)
        private val future = AtomicReference<ScheduledFuture<*>>()

        override fun start(listener: TickListener) {
            future.set(scheduler.scheduleAtFixedRate({ listener.onTick() }, 0, 1, TimeUnit.SECONDS))
        }

        override fun stop() {
            // TODO should it be true or false?
            future.get().cancel(true)
        }
    }
}