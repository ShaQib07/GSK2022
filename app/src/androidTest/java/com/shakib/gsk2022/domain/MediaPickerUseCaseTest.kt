package com.shakib.gsk2022.domain

import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.shakib.gsk2022.common.utils.Resource
import com.shakib.gsk2022.data.model.Image
import com.shakib.gsk2022.data.repository.MediaPickerRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MediaPickerUseCaseTest {

    private val fakeImage = Image(Uri.parse("fake_uri"), "fake_title", "fake_path")
    private lateinit var systemUnderTest: MediaPickerUseCase
    private val mediaPickerRepoTestDouble = MediaPickerRepoTestDouble()

    @Before
    fun setup() {
        systemUnderTest = MediaPickerUseCase(mediaPickerRepoTestDouble)
    }

    @Test
    fun fetchAllImages_success() {
        mediaPickerRepoTestDouble.isSuccess = true
        runBlocking {
            systemUnderTest.fetchAllImages().collectLatest {
                assertEquals(Resource.Success(listOf(fakeImage)), it)
            }
        }
    }

    @Test
    fun fetchAllImages_failure() {
        mediaPickerRepoTestDouble.isSuccess = false
        runBlocking {
            systemUnderTest.fetchAllImages().collectLatest {
                assertEquals(Resource.Success<List<Image>>(listOf()), it)
            }
        }
    }

    inner class MediaPickerRepoTestDouble : MediaPickerRepo {
        var isSuccess = true
        override suspend fun fetchAllImages(): List<Image> {
            return withContext(Dispatchers.Main) {
                if (isSuccess)
                    listOf(fakeImage)
                else
                    listOf()
            }
        }
    }
}
