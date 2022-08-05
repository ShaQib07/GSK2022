package com.shakib.gsk2022.common.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.shakib.gsk2022.common.utils.SingleLiveEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    sealed class Progress {
        object Show : Progress()
        object Hide : Progress()
    }

    private val progressLiveData = SingleLiveEvent<Progress>()

    private var progressCount = 0
    private val errorLiveData = SingleLiveEvent<String>()

    val progress: LiveData<Progress>
        get() = progressLiveData

    val error: LiveData<String>
        get() = errorLiveData

    fun showProgress() {
        if (++progressCount == 1) {
            progressLiveData.value = Progress.Show
        }
    }

    fun hideProgress() {
        if (--progressCount == 0) {
            progressLiveData.value = Progress.Hide
        }

        if (progressCount < 0) {
            progressCount = 0
        }
    }

    fun showError(error: String) {
        errorLiveData.value = error
    }

    override fun onCleared() {
        compositeDisposable.clear()
        onClear()
        super.onCleared()
    }

    abstract fun onClear()
}
