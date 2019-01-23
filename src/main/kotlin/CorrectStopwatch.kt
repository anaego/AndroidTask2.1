import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicBoolean

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


fun main(arguments: Array<String>) {

    val testScheduler = TestScheduler()

    val stopwatch = CorrectStopwatch.Impl(testScheduler)
    val observer1 = TestObserver<Long>()
    val observer2 = TestObserver<Long>()

    stopwatch.value.subscribe(observer1)
    testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
    observer1.assertValue(0)
    observer1.sub
    //observer1.dispose()
    observer1.cancel()

    stopwatch.value.subscribe(observer2)
    stopwatch.value.subscribe(observer1)
    testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
    observer2.assertValue(1)
    testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
}