package com.shakib.gsk2022.data.repository

import android.content.Context
import com.shakib.gsk2022.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class DirectoryRepoImpl @Inject constructor(@ApplicationContext private val context: Context) :
    DirectoryRepo {
    override fun getOutputDirectory(): File {
        val mediaDir = context.externalCacheDirs.firstOrNull()?.let {
            File(it, context.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else context.filesDir
    }
}
