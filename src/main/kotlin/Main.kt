fun main(arguments: Array<String>) {
    val stopwatch = Stopwatch.Impl(Ticker.Impl())

    val listener1 = object : Stopwatch.StopwatchListener {
        override fun onValueChange(value: Long) {
            println("listener1 value $value")
            if (value > 9) {
                stopwatch.pauseStopwatch(this) // changed: this instead of listener1
            }
        }
    }

    val listener2 = object : Stopwatch.StopwatchListener {
        override fun onValueChange(value: Long) {
            println("listener2 value $value")
            if (value > 4) {
                stopwatch.pauseStopwatch(this) // changed: this instead of listener2
            }
        }
    }

    stopwatch.resumeStopwatch(listener1)
    stopwatch.resumeStopwatch(listener2)
}