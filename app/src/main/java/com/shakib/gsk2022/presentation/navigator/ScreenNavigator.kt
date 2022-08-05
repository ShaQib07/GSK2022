package com.shakib.gsk2022.presentation.navigator

import androidx.navigation.NavController
import com.shakib.gsk2022.presentation.screens.camera.CameraFragmentDirections
import com.shakib.gsk2022.presentation.screens.picker.MediaPickerFragmentDirections
import javax.inject.Inject

class ScreenNavigator @Inject constructor() {

    fun toMediaPickerFragment(navController: NavController, maxSelection: String) =
        navController.navigate(MediaPickerFragmentDirections.homeToMediaPicker(maxSelection))

    fun toCameraFragment(navController: NavController, maxSelection: String) =
        navController.navigate(CameraFragmentDirections.homeToCamera(maxSelection))
}
