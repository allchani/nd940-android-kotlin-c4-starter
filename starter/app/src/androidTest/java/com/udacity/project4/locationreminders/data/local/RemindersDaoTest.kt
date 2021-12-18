package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.udacity.project4.locationreminders.data.dto.ReminderDTO

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Test

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

//    TODO: Add testing implementation to the RemindersDao.kt
    private lateinit var database: RemindersDatabase

    private val reminder = ReminderDTO("title","description", "location", 35.13,129.05)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun saveReminderAndGetReminderById() = runBlockingTest {
        // GIVEN
        database.reminderDao().saveReminder(reminder)

        // WHEN
        val savedReminder = database.reminderDao().getReminderById(reminder.id)

        // THEN
        assertThat(savedReminder as ReminderDTO, notNullValue())
        assertThat(savedReminder.id, `is`(reminder.id))
        assertThat(savedReminder.title, `is`(reminder.title))
        assertThat(savedReminder.description, `is`(reminder.description))
        assertThat(savedReminder.location, `is`(reminder.location))
        assertThat(savedReminder.latitude, `is`(reminder.latitude))
        assertThat(savedReminder.longitude, `is`(reminder.longitude))
    }

}