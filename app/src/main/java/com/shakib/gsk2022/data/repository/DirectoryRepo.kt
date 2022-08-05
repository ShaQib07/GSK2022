package com.shakib.gsk2022.data.repository

import java.io.File

interface DirectoryRepo {
    fun getOutputDirectory(): File
}
