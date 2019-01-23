import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.never
import java.util.concurrent.TimeUnit

class CorrectStopwatchTest {

    @Test
    fun itWorks() {
        val testScheduler = TestScheduler()
        val stopwatch = CorrectStopwatch.Impl(testScheduler)

        val observer1 = TestObserver<Long>()

        stopwatch.value.subscribe(observer1)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertValueAt(observer1.valueCount()-1, 0)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertValueAt(observer1.valueCount()-1, 1)

        observer1.dispose()

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertValueCount(2)
        observer1.assertValueAt(observer1.valueCount()-1, 1)
    }

    @Test
    fun twoObserversTest() {
        val testScheduler = TestScheduler()
        val stopwatch = CorrectStopwatch.Impl(testScheduler)

        val observer1 = TestObserver<Long>()
        val observer2 = TestObserver<Long>()

        stopwatch.value.subscribe(observer1)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertValueAt(observer1.valueCount()-1, 0)

        stopwatch.value.subscribe(observer2)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertValueAt(observer1.valueCount()-1, 1)
        observer2.assertValueAt(observer2.valueCount()-1, 1)

        observer1.dispose()

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertValueAt(observer1.valueCount()-1, 1)
        observer2.assertValueAt(observer2.valueCount()-1, 2)

        observer2.dispose()

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertValueAt(observer1.valueCount()-1, 1)
        observer2.assertValueAt(observer2.valueCount()-1, 2)
    }

    @Test
    fun fiveObserversTest() {
        val testScheduler = TestScheduler()
        val stopwatch = CorrectStopwatch.Impl(testScheduler)

        val observer1 = TestObserver<Long>()
        val observer2 = TestObserver<Long>()
        val observer3 = TestObserver<Long>()
        val observer4 = TestObserver<Long>()
        val observer5 = TestObserver<Long>()

        stopwatch.value.subscribe(observer1)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertValueAt(observer1.valueCount()-1, 0)
        observer2.assertNever(0)
        observer3.assertNever(0)
        observer4.assertNever(0)
        observer5.assertNever(0)

        stopwatch.value.subscribe(observer2)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertValueAt(observer1.valueCount()-1, 1)
        observer2.assertValueAt(observer2.valueCount()-1, 1)
        observer3.assertNever(1)
        observer4.assertNever(1)
        observer5.assertNever(1)

        stopwatch.value.subscribe(observer3)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertValueAt(observer1.valueCount()-1, 2)
        observer2.assertValueAt(observer2.valueCount()-1, 2)
        observer3.assertValueAt(observer3.valueCount()-1, 2)
        observer4.assertNever(2)
        observer5.assertNever(2)

        observer2.dispose()

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertValueAt(observer1.valueCount()-1, 3)
        observer2.assertNever(3)
        observer3.assertValueAt(observer3.valueCount()-1, 3)
        observer4.assertNever(3)
        observer5.assertNever(3)

        observer1.dispose()

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertNever(4)
        observer2.assertNever(4)
        observer3.assertValueAt(observer3.valueCount()-1, 4)
        observer4.assertNever(4)
        observer5.assertNever(4)

        stopwatch.value.subscribe(observer4)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertNever(5)
        observer2.assertNever(5)
        observer3.assertValueAt(observer3.valueCount()-1, 5)
        observer4.assertValueAt(observer4.valueCount()-1, 5)
        observer5.assertNever(5)

        observer4.dispose()

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertNever(6)
        observer2.assertNever(6)
        observer3.assertValueAt(observer3.valueCount()-1, 6)
        observer4.assertNever(6)
        observer5.assertNever(6)

        stopwatch.value.subscribe(observer5)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertNever(7)
        observer2.assertNever(7)
        observer3.assertValueAt(observer3.valueCount()-1, 7)
        observer4.assertNever(7)
        observer5.assertValueAt(observer5.valueCount()-1, 7)

        observer3.dispose()

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertNever(8)
        observer2.assertNever(8)
        observer3.assertNever(8)
        observer4.assertNever(8)
        observer5.assertValueAt(observer5.valueCount()-1, 8)

        observer5.dispose()

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertNever(9)
        observer2.assertNever(9)
        observer3.assertNever(9)
        observer4.assertNever(9)
        observer5.assertNever(9)
    }

    @Test
    fun resumeAfterPauseAll() {
        val testScheduler = TestScheduler()
        val stopwatch = CorrectStopwatch.Impl(testScheduler)

        val observer1 = TestObserver<Long>()
        val observer2 = TestObserver<Long>()

        stopwatch.value.subscribe(observer1)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertValueAt(observer1.valueCount()-1, 0)
        observer2.assertNever(0)

        observer1.dispose()

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertNever(1)
        observer2.assertNever(1)

        stopwatch.value.subscribe(observer2)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertNever(1)
        observer2.assertValueAt(observer2.valueCount()-1, 1)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertNever(2)
        observer2.assertValueAt(observer2.valueCount()-1, 2)

        observer2.dispose()

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertNever(3)
        observer2.assertNever(3)
    }

    fun wrongResumePauseOrder() {
        val testScheduler = TestScheduler()
        val stopwatch = CorrectStopwatch.Impl(testScheduler)

        val observer1 = TestObserver<Long>()

        observer1.cancel()

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertNever(0)

        stopwatch.value.subscribe(observer1)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertValueAt(observer1.valueCount()-1, 0)

        observer1.cancel()

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertNever(1)

        stopwatch.value.subscribe(observer1)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertValueAt(observer1.valueCount()-1, 1)

        stopwatch.value.subscribe(observer1)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertValueAt(observer1.valueCount()-1, 2)

        observer1.cancel()

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertNever(3)

        observer1.cancel()

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertNever(4)
    }

    fun resubscribeTest() {
        val testScheduler = TestScheduler()
        val stopwatch = CorrectStopwatch.Impl(testScheduler)

        val observer1 = TestObserver<Long>()

        stopwatch.value.subscribe(observer1)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertValueAt(observer1.valueCount()-1, 0)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertValueAt(observer1.valueCount()-1, 1)

        observer1.dispose()

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertValueCount(2)
        observer1.assertValueAt(observer1.valueCount()-1, 1)

        stopwatch.value.subscribe(observer1)
        observer1.assertSubscribed()

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        observer1.assertValueAt(observer1.valueCount()-1, 2)
    }

}