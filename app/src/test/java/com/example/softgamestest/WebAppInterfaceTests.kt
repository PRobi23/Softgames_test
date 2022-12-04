package com.example.softgamestest

import android.app.Activity
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.softgamestest.datetimeprovider.DateProvider
import com.example.softgamestest.datetimeprovider.DateProviderImpl
import com.example.softgamestest.delay.DelayProvider
import com.example.softgamestest.javasriptinterfaces.WebAppInterface
import io.mockk.*
import org.junit.Test

import org.junit.Assert.*
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.time.LocalDate
import java.time.Month

class WebAppInterfaceTests {

    private val dateProvider: DateProvider = mockk()
    private val delayProvider: DelayProvider = mockk()
    private val notificationBuilder: NotificationCompat.Builder = mockk()
    private val notificationManager: NotificationManager = mockk()
    private val notificationManagerCompat: NotificationManagerCompat = mockk()
    private val activity: Activity = mockk()

    @Test
    fun `when sync is called the two string is concatenated`() {
        // given
        val firstName = "firstname"
        val lastName = "lastName"
        val expected = "firstnamelastName"

        // when
        val webAppInterface = createWebAppInterface()
        val concatenatedString = webAppInterface.concatenateTwoString(firstName, lastName)

        // then
        assertEquals(
            expected, concatenatedString
        )
    }

    @Test
    fun `when sync is called the two string is concatenated if firstName is empty`() {
        // given
        val firstName = ""
        val lastName = "lastName"
        val expected = "lastName"

        // when
        val webAppInterface = createWebAppInterface()
        val concatenatedString = webAppInterface.concatenateTwoString(firstName, lastName)

        // then
        assertEquals(
            expected, concatenatedString
        )
    }

    @Test
    fun `when sync is called the two string is concatenated if lastname is empty`() {
        // given
        val firstName = "firstname"
        val lastName = ""
        val expected = "firstname"

        // when
        val webAppInterface = createWebAppInterface()
        val concatenatedString = webAppInterface.concatenateTwoString(firstName, lastName)

        // then
        assertEquals(
            expected, concatenatedString
        )
    }

    @Test
    fun `when actual date is 2022 and user birth date is 2018 4 is returned for age`() {
        // given
        every {
            dateProvider.getCurrentLocalDate()
        } returns LocalDate.of(2022, Month.APRIL, 12)
        every {
            delayProvider.delay(5000)
        } returns Unit

        val expectedAge = 4
        val userBirth = "2018-03-12"
        // when
        val webAppInterface = createWebAppInterface()
        val calculatedAge = webAppInterface.calculateAge(userBirth)

        // then
        assertEquals(
            expectedAge.toString(), calculatedAge
        )
    }

    @Test
    fun `when actual date is 2022 and user birth date is 1998 24 is returned for age`() {
        // given
        every {
            dateProvider.getCurrentLocalDate()
        } returns LocalDate.of(2022, Month.APRIL, 12)
        every {
            delayProvider.delay(5000)
        } returns Unit

        val expectedAge = 24
        val userBirth = "1998-04-12"
        // when
        val webAppInterface = createWebAppInterface()
        val calculatedAge = webAppInterface.calculateAge(userBirth)

        // then
        assertEquals(
            expectedAge.toString(), calculatedAge
        )
    }

    @Test
    fun `when actual date is 2012 and user birth date is not in propfer format unknown is returned`() {
        // given
        every {
            dateProvider.getCurrentLocalDate()
        } returns LocalDate.of(2012, Month.APRIL, 12)
        every {
            delayProvider.delay(5000)
        } returns Unit

        val userBirth = "12-14-124"
        // when
        val webAppInterface = createWebAppInterface()
        val calculatedAge = webAppInterface.calculateAge(userBirth)

        // then
        assertEquals(
            "Unknown", calculatedAge
        )
    }

    @Test
    fun `test show notification is set the proper values`() {
        // given
        val notification: Notification = mockk()
        every {
            delayProvider.delay(7000)
        } returns Unit
        setStaticFieldViaReflection(Build.VERSION::class.java.getField("SDK_INT"), 23)
        mockkStatic(NotificationManagerCompat::class)
        every {
            activity.finish()
        } returns Unit
        every {
            NotificationManagerCompat.from(activity)
        } returns notificationManagerCompat
        every {
            notificationBuilder.build()
        } returns notification
        every {
            notificationManagerCompat.notify(1, notification)
        } returns Unit

        // when
        createWebAppInterface().showNotification()

        // then
        verify {
            notificationManagerCompat.notify(1, notification)
        }
    }

    private fun setStaticFieldViaReflection(field: Field, value: Any) {
        field.isAccessible = true
        Field::class.java.getDeclaredField("modifiers").apply {
            isAccessible = true
            setInt(field, field.modifiers and Modifier.FINAL.inv())
        }
        field.set(null, value)
    }

    private fun createWebAppInterface() = WebAppInterface(
        delayProvider = delayProvider,
        dateProvider = dateProvider,
        context = activity,
        notificationBuilder = notificationBuilder,
        notificationManager = notificationManager
    )
}