fun main(arguments: Array<String>) {
    // TODO Stopwatch.Impl(Ticker.Impl()) instead of Stopwatch(Ticker.Impl())?
    val stopwatch = Stopwatch.Impl(Ticker.Impl())

    val listener1 = object : Stopwatch.Listener {
        override fun onChange(value: Long) {
            println("Listener 1: $value")

            if (value > 10) {
                stopwatch.stop(this) // TODO this instead of listener1?
            }
        }
    }

    val listener2 = object : Stopwatch.Listener {
        override fun onChange(value: Long) {
            println("Listener 2: $value")

            if (value > 5) {
                stopwatch.stop(this) // TODO this instead of listener2?
            }
        }
    }

    stopwatch.start(listener1)
    stopwatch.start(listener2)
}