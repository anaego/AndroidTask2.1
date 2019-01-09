class TestTicker : Ticker {

    var tickListener: Ticker.TickListener? = null

    override fun start(tickListener: Ticker.TickListener) {
        this.tickListener = tickListener
    }

    override fun stop() {
        this.tickListener = null
    }
}