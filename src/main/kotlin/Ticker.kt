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

        private val scheduler = Executors.newScheduledThreadPool(1)
        private val future = AtomicReference<ScheduledFuture<*>>()

        override fun start(tickListener: TickListener) {
            future.set(scheduler.scheduleAtFixedRate({ tickListener.onTick() }, 0, 1, TimeUnit.SECONDS))
            println("Ticker: resumeStopwatch() started scheduler prbbly")
        }

        override fun stop() {
            // TODO should it be true or false?
            future.get().cancel(true)
            println("Ticker: canceled the future")
        }
    }
}