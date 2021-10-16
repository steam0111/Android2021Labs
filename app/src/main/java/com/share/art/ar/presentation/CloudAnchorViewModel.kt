package com.share.art.ar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ar.core.codelab.cloudanchor.helpers.FirebaseManager
import com.share.art.core.DI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CloudAnchorViewModel : ViewModel() {

    private val cloudAnchorRepository by lazy {
        DI.getCloudAnchorRepository()
    }

    fun storeCloudAnchor(cloudAnchorId: String, listener: FirebaseManager.ShortCodeListener) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val shortCode = cloudAnchorRepository.storeCloudAnchor(cloudAnchorId)

                withContext(Dispatchers.Main) {
                    listener.onShortCodeAvailable(shortCode)
                }
            } catch (e: Throwable) {
                withContext(Dispatchers.Main) {
                    listener.onShortCodeAvailable(null)
                }
            }
        }
    }

    fun getCloudAnchorId(shortCode: Long, listener: FirebaseManager.CloudAnchorIdListener) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cloudAnchorId = cloudAnchorRepository.getCloudAnchorId(shortCode)
                withContext(Dispatchers.Main) {
                    listener.onCloudAnchorIdAvailable(cloudAnchorId)
                }
            } catch (e: Throwable) {
                withContext(Dispatchers.Main) {
                    listener.onCloudAnchorIdAvailable(null)
                }
            }
        }
    }
}