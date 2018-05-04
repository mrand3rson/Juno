package com.example.juno.mvp.presenters

import com.example.juno.mvp.models.GithubApiService
import com.example.juno.mvp.models.Repository
import com.example.juno.mvp.views.MainView
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.internal.verification.VerificationModeFactory
import java.util.*

/**
 * Created by Andrei on 03.05.2018.
 */
class MainPresenterTest {

    @Mock
    lateinit var apiService: GithubApiService

    @Mock
    lateinit var view : MainView

    private lateinit var presenter : MainPresenter
    private lateinit var testScheduler: TestScheduler
    private val list = ArrayList<Repository>()


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        testScheduler = TestScheduler()
        presenter = MainPresenter(view, apiService, testScheduler, testScheduler)
    }

    @Test
    fun getRepositories_whenDataIsAvailable_shouldShowResult() {
        whenever(apiService.getJunoRepositories()).thenReturn(Observable.just(Collections.emptyList()))
        presenter.getRepositories()
        testScheduler.triggerActions()
        verify(view, VerificationModeFactory.times(0)).showError(any())
        verify(view, VerificationModeFactory.only()).showResult(any())
    }

    @Test
    fun getRepositories_whenDataIsAvailable_resultNonNull() {
        whenever(apiService.getJunoRepositories()).thenReturn(Observable.just(Collections.emptyList()))
        presenter.getRepositories()
        testScheduler.triggerActions()
        verify(view, VerificationModeFactory.times(0)).showResult(null)
    }

    @Test
    fun filterRecyclerItems() {
        list.add(Repository(0, "abcdefghijklmnopqrstuvwxyz"))
        list.add(Repository(1, "abcdefghij"))
        list.add(Repository(2, "abcd"))

        presenter.data = list
        assertTrue(presenter.filterRecyclerItems("").size == 3)
        assertTrue(presenter.filterRecyclerItems("abcd").size == 3)
        assertTrue(presenter.filterRecyclerItems("abcdef").size == 2)
        assertTrue(presenter.filterRecyclerItems("op").size == 1)
        assertTrue(presenter.filterRecyclerItems("po").isEmpty())
    }
}