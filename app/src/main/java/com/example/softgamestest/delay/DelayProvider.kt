package com.example.softgamestest.delay

/***
 * Provides delay
 */
interface DelayProvider {
    /**
     * adds delay on the call
     * @param millisecond Delay in milliseconds
     */
    fun delay(millisecond: Long)
}