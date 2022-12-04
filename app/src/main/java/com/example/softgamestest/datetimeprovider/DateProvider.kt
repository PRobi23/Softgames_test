package com.example.softgamestest.datetimeprovider

import java.time.LocalDate

/***
 * Returns everything date related content.
 */
interface DateProvider {

    /***
     * Returns current time
     */
    fun getCurrentLocalDate(): LocalDate
}