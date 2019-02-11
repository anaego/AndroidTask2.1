import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong


interface Stopwatch {

    val value: Subject<Long>
    fun pause()
    fun resume()

    class Impl(private val scheduler: Scheduler) : Stopwatch {

        override val value =  BehaviorSubject.create<Long>().toSerialized()
        private val consumersCountChange = PublishSubject.create<Long>().toSerialized()
        private val actualValueCounter = AtomicLong()

        init {
            consumersCountChange
                .scan{ x, y -> x + y }
                .filter{ x -> x in 0..1}
                .switchMap { o -> if (o < 1) Observable.never<Long>()
                                  else Observable
                                                    .interval(1, TimeUnit.SECONDS, scheduler)
                                                    .map{ _ -> actualValueCounter.getAndIncrement() } }
                .subscribe(value)
            value.share()
        }

        override fun pause() {
            consumersCountChange.onNext(-1)
        }

        override fun resume() {
            consumersCountChange.onNext(1)
        }
    }
}