fun main(arguments: Array<String>) {

    val stopwatch = Stopwatch.Impl(Ticker.Impl())

    val listener1 = object : Stopwatch.StopwatchListener {
        override fun onValueChange(value: Long) {
            if (value > 9) {
                stopwatch.pauseStopwatch(this)
            }
        }
    }

    val listener2 = object : Stopwatch.StopwatchListener {
        override fun onValueChange(value: Long) {
            if (value > 4) {
                stopwatch.pauseStopwatch(this)
            }
        }
    }

    stopwatch.resumeStopwatch(listener1)
    stopwatch.resumeStopwatch(listener2)
}