package com.udacity.project4.locationreminders.savereminder

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.R
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O])
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {


    //TODO: provide testing to the SaveReminderView and its live data objects

    private lateinit var viewModel: SaveReminderViewModel
    private lateinit var dataSource: FakeDataSource

    private val reminder = ReminderDataItem("Reminder", "Description", "Location_busan",35.13,129.05)
    private val reminder_noTitle = ReminderDataItem("","Description", "Location_seoul", 37.33,126.59)
    private val reminder_noLocation = ReminderDataItem("Second_Reminder","Description", "", 37.33,126.59)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUpViewModel() {
        stopKoin()
        dataSource = FakeDataSource()
        viewModel = SaveReminderViewModel(ApplicationProvider.getApplicationContext(), dataSource)
    }

    @Test
    fun saveReminder() = mainCoroutineRule.runBlockingTest{
        viewModel.saveReminder(reminder)
        assertThat(viewModel.showToast.getOrAwaitValue(), `is`("Reminder Saved !"))
    }

    @Test
    fun saveReminder_noTitle() = mainCoroutineRule.runBlockingTest{
        viewModel.validateAndSaveReminder(reminder_noTitle)
        assertThat(viewModel.showSnackBarInt.getOrAwaitValue(), notNullValue())
    }

    @Test
    fun saveReminder_noLocation() = mainCoroutineRule.runBlockingTest{
        viewModel.validateAndSaveReminder(reminder_noLocation)
        assertThat(viewModel.showSnackBarInt.getOrAwaitValue(), notNullValue())
    }
}