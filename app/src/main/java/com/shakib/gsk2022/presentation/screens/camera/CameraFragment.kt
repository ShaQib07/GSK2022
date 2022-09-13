package com.shakib.gsk2022.presentation.screens.camera

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.shakib.gsk2022.MainActivity
import com.shakib.gsk2022.R
import com.shakib.gsk2022.common.base.BaseFragment
import com.shakib.gsk2022.common.extensions.showLongToast
import com.shakib.gsk2022.data.model.Image
import com.shakib.gsk2022.databinding.FragmentCameraBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@SuppressLint("RestrictedApi", "MissingPermission")
@AndroidEntryPoint
class CameraFragment : BaseFragment<FragmentCameraBinding>() {

    companion object {
        const val IMAGE_EXTENSION = ".jpg"
    }

    private val viewModel: CameraViewModel by viewModels()
    private var maxSelection = 1
    private val clickedImages: ArrayList<Image> = ArrayList()

    private var imageCapture: ImageCapture? = null
    private var videoCapture: VideoCapture? = null
    private var preferredCamera = CameraSelector.DEFAULT_BACK_CAMERA
    private var preferredFlashMode = ImageCapture.FLASH_MODE_OFF
    private var isVideo = false

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var preview: Preview
    private lateinit var cameraControl: CameraControl

    override fun getBaseViewModel() = viewModel

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCameraBinding.inflate(layoutInflater, container, false)

    override fun configureViews(savedInstanceState: Bundle?) {
        super.configureViews(savedInstanceState)
        data?.let { maxSelection = it.toInt() }
        binding.fabCapture.apply {
            setOnClickListener {
                if (clickedImages.size >= maxSelection)
                    context.showLongToast(getString(R.string.max_selection))
                else
                    takePhoto()
            }
            setOnLongClickListener {
                (activity as MainActivity).selectedImages = clickedImages
                findNavController().navigateUp()
                true
            }
        }
        binding.switchCameraIv.setOnClickListener {
            preferredCamera = if (preferredCamera == CameraSelector.DEFAULT_BACK_CAMERA)
                CameraSelector.DEFAULT_FRONT_CAMERA
            else
                CameraSelector.DEFAULT_BACK_CAMERA

            updatePreview(preferredCamera, preferredFlashMode, isVideo)
        }
        binding.flashIv.setOnClickListener {
            preferredFlashMode = if (preferredFlashMode == ImageCapture.FLASH_MODE_OFF) {
                binding.flashIv.setImageResource(R.drawable.ic_flash_blue)
                ImageCapture.FLASH_MODE_ON
            } else {
                binding.flashIv.setImageResource(R.drawable.ic_flash_off_blue)
                ImageCapture.FLASH_MODE_OFF
            }

            updatePreview(preferredCamera, preferredFlashMode, isVideo)
        }
        binding.seekBarZoom.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                cameraControl.setLinearZoom(progress.toFloat() / 100)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        startCamera()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun startCamera() {
        context?.apply {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
            cameraProviderFuture.addListener({
                // Used to bind the lifecycle of cameras to the lifecycle owner
                cameraProvider = cameraProviderFuture.get()

                // Preview
                preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                    }

                updatePreview(preferredCamera, preferredFlashMode, isVideo)

            }, ContextCompat.getMainExecutor(this))
        }
    }

    private fun updatePreview(
        preferredCamera: CameraSelector,
        preferredFlashMode: Int,
        isVideo: Boolean
    ) {
        try {
            // Unbind use cases before rebinding
            cameraProvider.unbindAll()

            // Bind use cases to camera
            if (isVideo) {
                // The Configuration of how we want to capture the video
                videoCapture = VideoCapture.Builder().apply {
                    setTargetAspectRatio(AspectRatio.RATIO_16_9)
                    setCameraSelector(preferredCamera)
                    setVideoFrameRate(30)
                    setTargetRotation(binding.viewFinder.display.rotation)
                }.build()

                cameraProvider.bindToLifecycle(this, preferredCamera, videoCapture, preview)
                    .also { cameraControl = it.cameraControl }
            } else {
                imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                    .setTargetRotation(binding.root.display.rotation)
                    .setFlashMode(preferredFlashMode)
                    .build()

                cameraProvider.bindToLifecycle(this, preferredCamera, imageCapture, preview)
                    .also { cameraControl = it.cameraControl }
            }
        } catch (exc: Exception) {
            Timber.d("Use case binding failed ${exc.message}")
        }
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val photoFile = File(viewModel.outputDirectory, "${System.currentTimeMillis()}$IMAGE_EXTENSION")

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has been taken
        context?.apply {
            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(this),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        Timber.d("Photo capture failed: ${exc.message}")
                    }

                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        showLongToast(getString(R.string.capture_hint))
                        clickedImages.add(
                            Image(
                                photoFile.toUri(),
                                photoFile.name,
                                photoFile.absolutePath
                            )
                        )
                    }
                })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
