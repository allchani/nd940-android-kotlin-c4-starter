package com.udacity.project4.locationreminders.data.local

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O])
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

//    TODO: Add testing implementation to the RemindersLocalRepository.kt
    private lateinit var database: RemindersDatabase
    private lateinit var localRepository: RemindersLocalRepository

    private val reminder = ReminderDTO("title","description", "location", 35.13,129.05)


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUpDbAndRepo() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()

        localRepository = RemindersLocalRepository(database.reminderDao(), Dispatchers.Main)
    }

    @After
    fun cleanUpDb() {
        database.close()
    }

    @Test
    fun saveReminderAndGetReminder() = runBlocking{
        // GIVEN - Save Reminder
        localRepository.saveReminder(reminder)

        // WHEN - Get Reminder
        val result = localRepository.getReminder(reminder.id)

        // THEN
        assertThat(result is Result.Success, `is`(true))
        result as Result.Success

        assertThat(result.data.title, `is`(reminder.title))
        assertThat(result.data.description, `is`(reminder.description))
        assertThat(result.data.latitude, `is`(reminder.latitude))
        assertThat(result.data.longitude, `is`(reminder.longitude))
        assertThat(result.data.location, `is`(reminder.location))
    }

    @Test
    fun deleteAllRemindersAndGetReminder() = runBlocking {
        // GIVEN
        localRepository.saveReminder(reminder)
        localRepository.deleteAllReminders()

        // WHEN
        val result = localRepository.getReminder(reminder.id)

        // THEN
        assertThat(result is Result.Error, `is`(true))
        result as Result.Error

        assertThat(result.message, `is`("Reminder not found!"))

    }


}