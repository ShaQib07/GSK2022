package com.shakib.gsk2022.common.di

import com.shakib.gsk2022.data.repository.DirectoryRepo
import com.shakib.gsk2022.data.repository.DirectoryRepoImpl
import com.shakib.gsk2022.data.repository.MediaPickerRepo
import com.shakib.gsk2022.data.repository.MediaPickerRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindMediaPickerRepo(mediaPickerRepoImpl: MediaPickerRepoImpl): MediaPickerRepo

    @Binds
    abstract fun bindDirectoryRepo(directoryRepoImpl: DirectoryRepoImpl): DirectoryRepo
}
