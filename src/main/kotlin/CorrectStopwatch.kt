import io.reactivex.Observable
import io.reactivex.Scheduler
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

interface CorrectStopwatch {

    val value: Observable<Long>
    fun pause()
    fun resume()

    class Impl(private val scheduler: Scheduler) : CorrectStopwatch {

        private val counter = AtomicLong()

        private val observable = Observable.interval(1, TimeUnit.SECONDS, scheduler)
            .map{tick -> counter.getAndIncrement()}
            .share()

        override val value: Observable<Long>
            get() = observable

        override fun pause() {

        }

        override fun resume() {

        }
    }
}
