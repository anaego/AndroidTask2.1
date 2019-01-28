import hu.akarnokd.rxjava2.operators.ObservableTransformers
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong


interface CorrectStopwatch {

    val value: Observable<Long>
    fun pause()
    fun resume()

    class Impl(private val scheduler: Scheduler) : CorrectStopwatch {

        private val manager = PublishSubject.create<Long>()
        private val valueEmitterThing = BehaviorSubject.interval(1, TimeUnit.SECONDS, scheduler)
            .compose(ObservableTransformers.valve(manager.scan{ x, y->x+y}.map{ x -> x > 0}, false))
            .map{tick -> counter.getAndIncrement()}
            .share()
        private val counter = AtomicLong()

        override val value: Observable<Long>
            get() = valueEmitterThing

        override fun pause() {
            manager.onNext(-1)
        }

        override fun resume() {
            manager.onNext(1)
        }
    }
}