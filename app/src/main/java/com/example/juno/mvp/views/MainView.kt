package com.example.juno.mvp.views

import android.util.Log
import com.example.juno.mvp.models.GithubApiService
import com.example.juno.mvp.models.Repository

/**
 * Created by Andrei on 03.05.2018.
 */

interface MainView {
    companion object {
        lateinit var apiService: GithubApiService
    }

    fun showResult(result: List<Repository>?) {
        Log.d("debug", result.toString())
    }
    fun showError(error: Throwable) {
        Log.d("debug", error.message)
    }
}
