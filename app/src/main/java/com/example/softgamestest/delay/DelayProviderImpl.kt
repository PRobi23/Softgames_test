package com.example.softgamestest.delay

/***
 * @inheritDoc}
 */
class DelayProviderImpl : DelayProvider {
    /***
     * @inheritDoc}
     */
    override fun delay(millisecond: Long) = Thread.sleep(millisecond)
}