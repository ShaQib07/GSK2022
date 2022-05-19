package com.shakib.gsk2022.domain

import com.shakib.gsk2022.data.repository.DirectoryRepo
import javax.inject.Inject

class DirectoryUseCase @Inject constructor(private val directoryRepo: DirectoryRepo) {

    fun getOutputDirectory() = directoryRepo.getOutputDirectory()
}