class TestTicker : Ticker {

    var listener: Ticker.TickListener? = null

    override fun start(listener: Ticker.TickListener) {
        this.listener = listener
    }

    override fun stop() {
        this.listener = null
    }
}