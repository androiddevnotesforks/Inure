package app.simple.inure.viewmodels.activity

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.simple.inure.extensions.viewmodels.WrappedViewModel
import app.simple.inure.util.FileSizeHelper.getDirectorySize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class ManageSpaceViewModel(application: Application) : WrappedViewModel(application) {

    private val trackersCachePath = "${applicationContext().dataDir}/trackers_cache/"
    private val imagesCachePath = "${applicationContext().cacheDir}/image_manager_disk_cache/"

    val trackersCacheSize: MutableLiveData<String> by lazy {
        MutableLiveData<String>().also {
            loadTrackersCacheSize()
        }
    }

    val imagesCacheSize: MutableLiveData<String> by lazy {
        MutableLiveData<String>().also {
            loadImagesCacheSize()
        }
    }

    fun clearTrackersData() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                delay(1000L)
                val file = File(trackersCachePath)
                if (file.deleteRecursively()) {
                    loadTrackersCacheSize()
                }
            }.onFailure {
                it.printStackTrace()
                error.postValue(it.stackTraceToString())
            }
        }
    }

    fun clearImagesData() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                delay(1000L)
                val file = File(imagesCachePath)
                if (file.deleteRecursively()) {
                    loadImagesCacheSize()
                }
            }.onFailure {
                it.printStackTrace()
                error.postValue(it.stackTraceToString())
            }
        }
    }

    private fun loadTrackersCacheSize() {
        viewModelScope.launch(Dispatchers.IO) {
            trackersCacheSize.postValue(trackersCachePath.getDirectorySize())
        }
    }

    private fun loadImagesCacheSize() {
        viewModelScope.launch(Dispatchers.IO) {
            imagesCacheSize.postValue(imagesCachePath.getDirectorySize())
        }
    }
}