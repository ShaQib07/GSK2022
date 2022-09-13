package com.shakib.gsk2022.domain

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.shakib.gsk2022.data.repository.DirectoryRepo
import com.shakib.gsk2022.data.repository.DirectoryRepoImpl
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DirectoryUseCaseTest {

    private lateinit var systemUnderTest: DirectoryUseCase
    private lateinit var directoryRepo: DirectoryRepo

    @Before
    fun setup() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        directoryRepo = DirectoryRepoImpl(appContext)
        systemUnderTest = DirectoryUseCase(directoryRepo)
    }

    @Test
    fun getOutputDirectory_success() {
        Assert.assertEquals(true, systemUnderTest.getOutputDirectory().name.equals("GSK2022"))
    }

    @Test
    fun getOutputDirectory_failure() {
        Assert.assertEquals(false, systemUnderTest.getOutputDirectory().name.equals("Error"))
    }
}
