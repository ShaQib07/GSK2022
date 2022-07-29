package com.shakib.gsk2022.presentation.screens.home

import androidx.work.*
import com.shakib.gsk2022.common.base.BaseViewModel
import com.shakib.gsk2022.data.model.Image
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val workManager: WorkManager,
    private val constraints: Constraints
) : BaseViewModel() {

    fun createOneTimeWorkRequest(imageList: ArrayList<Image>) {
        val imageArray = Array<String?>(imageList.size) { null }
        for (index in imageList.indices) {
            imageArray[index] = imageList[index].path
        }
        val data = Data.Builder()
        data.putStringArray("key", imageArray)

        val work = OneTimeWorkRequestBuilder<FileUploadWorker>()
            .setConstraints(constraints)
            .setInputData(data.build())
            .addTag("fileUpload")
            .build()

        workManager.enqueueUniqueWork(
            "oneTimeFileUpload",
            ExistingWorkPolicy.KEEP,
            work
        )
    }

    override fun onClear() {}
}