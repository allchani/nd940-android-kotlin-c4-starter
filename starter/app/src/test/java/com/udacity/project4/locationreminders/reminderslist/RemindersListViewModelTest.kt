package com.udacity.project4.locationreminders.reminderslist

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O])
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {
    //TODO: provide testing to the RemindersListViewModel and its live data objects

    private lateinit var viewModel: RemindersListViewModel
    private lateinit var dataSource: FakeDataSource

    private val reminder1 = ReminderDTO("First_Reminder", "Description", "Location_busan",35.13,129.05)
    private val reminder2 = ReminderDTO("Second_Reminder","Description", "Location_seoul", 37.33,126.59)
    private val remindersList = mutableListOf<ReminderDTO>()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUpViewModel() {
        stopKoin()
//        remindersList.add(reminder1)
//        remindersList.add(reminder2)
        dataSource = FakeDataSource()
        viewModel = RemindersListViewModel(ApplicationProvider.getApplicationContext(), dataSource)
    }

    @Test
    fun loadReminders_showLoadingTest() = runBlockingTest{
        //pauseDispatcher to wait
        mainCoroutineRule.pauseDispatcher()

        viewModel.loadReminders()

        assertThat(viewModel.showLoading.getOrAwaitValue(), `is`(true))

        mainCoroutineRule.resumeDispatcher()

        assertThat(viewModel.showLoading.getOrAwaitValue(),`is`(false))
    }

    @Test
    fun loadReminders_showNoDataTest() = runBlockingTest{
        viewModel.loadReminders()
        assertThat(viewModel.showNoData.getOrAwaitValue(),`is`(true))
    }

    @Test
    fun loadRemindersRemindersNotFound_callErrorMessage() = runBlockingTest{
        dataSource.setShouldReturnError(true)
        viewModel.loadReminders()


        assertThat(viewModel.showSnackBar.getOrAwaitValue(), `is`("Reminders not found"))
    }

    @Test
    fun loadReminders_deleteAndNoData_showsError() = runBlockingTest{
        dataSource.deleteAllReminders()
        viewModel.loadReminders()
        assertThat(viewModel.showNoData.getOrAwaitValue(), `is`(true))
    }


}