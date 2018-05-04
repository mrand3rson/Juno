package com.example.juno.mvp.presenters

import com.example.juno.mvp.models.GithubApiService
import com.example.juno.mvp.models.Repository
import com.example.juno.mvp.views.MainView
import io.reactivex.Scheduler

/**
 * Created by Andrei on 03.05.2018.
 */

class MainPresenter(private val view: MainView,
                    private val apiService: GithubApiService,
                    private val processScheduler : Scheduler,
                    private val androidScheduler : Scheduler) {

    lateinit var data: List<Repository>


    fun filterRecyclerItems(text: String): List<Repository> {
        val filteredData = data.filter { repository -> repository.name.contains(text) }
        return filteredData
    }

    fun getRepositories() {
        apiService.getJunoRepositories()
                .observeOn(androidScheduler)
                .subscribeOn(processScheduler)
                .subscribe(
                        {result -> showResult(result)},
                        {error  -> showError(error)}
                )
    }

    private fun showResult(result: List<Repository>) {
        view.showResult(result)
    }

    private fun showError(error: Throwable) {
        view.showError(error)
    }
}
