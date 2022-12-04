package com.example.softgamestest.datetimeprovider

import java.time.LocalDate

/***
 * @inheritDoc}
 */
class DateProviderImpl : DateProvider {

    /***
     * @inheritDoc}
     */
    override fun getCurrentLocalDate(): LocalDate = LocalDate.now()
}