fun main(arguments: Array<String>) {
    // TODO Stopwatch.Impl(Ticker.Impl()) instead of Stopwatch(Ticker.Impl())?
    val stopwatch = Stopwatch.Impl(Ticker.Impl())

    val listener1 = object : Stopwatch.StopwatchListener {
        override fun onValueChange(value: Long) {
            println("Main: Listener1: $value")

            if (value > 9) {
                stopwatch.pauseStopwatch(this) // TODO this instead of listener1?
                println("Main: Listener1 reached 10 & stopped the stopwatch")
            }
        }
    }

    val listener2 = object : Stopwatch.StopwatchListener {
        override fun onValueChange(value: Long) {
            println("Main: Listener2: $value")

            if (value > 4) {
                stopwatch.pauseStopwatch(this) // TODO this instead of listener2?
                println("Main: Listener2 reached 5 & stopped the stopwatch")
            }
        }
    }

    stopwatch.resumeStopwatch(listener1)
    stopwatch.resumeStopwatch(listener2)
}