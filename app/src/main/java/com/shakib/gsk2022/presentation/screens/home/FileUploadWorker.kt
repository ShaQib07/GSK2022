package com.shakib.gsk2022.presentation.screens.home

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.google.firebase.storage.FirebaseStorage
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltWorker
class FileUploadWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        // 1
        setForeground(createForegroundInfo())
        // 2
        var data = "0 successful upload"
        inputData.getStringArray("key")?.let { imageArray ->
            imageArray.forEach {
                data = uploadFiles(it)
            }
        }
        // 3
        return Result.success(workDataOf("DATA" to data))
    }

    private suspend fun uploadFiles(imageList: String) = suspendCoroutine<String> { continuation ->
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val file = Uri.fromFile(File(imageList))
        val riversRef = storageRef.child("images/${file.lastPathSegment}")
        val uploadTask = riversRef.putFile(file)
        var message: String
        uploadTask.addOnFailureListener {
            Timber.d("Upload | Failed - $it")
            message = "Upload failed with ${it.message}"
            continuation.resume(message)
        }.addOnSuccessListener {
            Timber.d("Upload | Success")
            message = "Upload successful"
            continuation.resume(message)
        }
    }

    private fun createForegroundInfo(): ForegroundInfo {
        // 1
        val intent = WorkManager.getInstance(applicationContext).createCancelPendingIntent(id)
        // 2
        val notification = NotificationCompat.Builder(
            applicationContext, "imageUpload"
        )
            .setContentTitle("Uploading Your Image")
            .setTicker("Uploading Your Image")
            .setSmallIcon(androidx.core.R.drawable.notification_action_background)
            .setOngoing(true)
            .addAction(android.R.drawable.ic_delete, "Cancel Upload", intent)
        // 3
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(notification)
        }
        return ForegroundInfo(1, notification.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(
        notificationBuilder: NotificationCompat.Builder,
        id: String = "imageUpload"
    ) {
        val notificationManager =
            appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE)
        val channel = NotificationChannel(id, "GSK2022", NotificationManager.IMPORTANCE_HIGH)
        channel.description = "GSK22 Notifications"
        notificationManager.createNotificationChannel(channel)
    }
}