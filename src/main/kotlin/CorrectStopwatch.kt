import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
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

        override val value: Observable<Long>
            get() = combinedParts

        // we need this to control combineLatest so that it only emits when valueEmitterThing emits, not when manager emits
        private fun valueChecker(valueEmitterThingValue : Long, managerValue : Long) : Long {
            val newValue : Long
            if (valueEmitterThingCounter.get() - 1 != valueEmitterThingKeeper.get())
                newValue = (valueEmitterThingValue + 1) * managerValue
            else
                newValue = 0
            valueEmitterThingKeeper.set(valueEmitterThingCounter.get() - 1)
            return newValue
        }

        private val actualValueCounter = AtomicLong()
        private val valueEmitterThingCounter = AtomicLong()
        private val valueEmitterThingKeeper = AtomicLong(200)
        private val manager = PublishSubject.create<Long>()
        private val valueEmitterThing = BehaviorSubject.interval(1, TimeUnit.SECONDS, scheduler)
        private val combinedParts = Observable.combineLatest(valueEmitterThing
                    .map{ _ -> valueEmitterThingCounter.getAndIncrement()},
                manager.scan{ x, y -> x + y },
                BiFunction {valueEmitterThingValue : Long, managerValue : Long
                    -> valueChecker(valueEmitterThingValue, managerValue)})
            .filter{ x -> x > 0 }
            .map{ _ -> actualValueCounter.getAndIncrement() }
            .share()

        override fun pause() {
            manager.onNext(-1)
        }

        override fun resume() {
            manager.onNext(1)
        }
    }
}
