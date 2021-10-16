package com.share.art.ar.data

import com.google.firebase.database.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CloudAnchorRepository(
    private val rootReference: DatabaseReference,
    private val authRepository: AuthRepository
) {
    companion object {
        private const val SHORT_CODES = "short_codes"
        private const val ANCHOR_ID = "anchor:"
    }

    suspend fun storeCloudAnchor(cloudAnchorId: String): Long {
        val nextShorCode = getShortCode()
        rootReference.child(ANCHOR_ID + nextShorCode).setValue(cloudAnchorId)
        return nextShorCode
    }

    suspend fun getCloudAnchorId(shortCode: Long): String = suspendCoroutine {
        rootReference
            .child(ANCHOR_ID + shortCode)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        it.resume(snapshot.value.toString())
                    }

                    override fun onCancelled(error: DatabaseError) {
                        it.resumeWithException(Throwable())
                    }
                }
            )
    }

    private suspend fun getShortCode(): Long = suspendCoroutine {
        rootReference
            .child(authRepository.getUuid())
            .child(SHORT_CODES)
            .runTransaction(object : Transaction.Handler {
                override fun doTransaction(currentData: MutableData): Transaction.Result {
                    var currentCode = currentData.getValue(Long::class.java)
                    if (currentCode == null) {
                        currentCode = 0
                    }

                    currentData.value = currentCode + 1

                    return Transaction.success(currentData)
                }

                override fun onComplete(
                    error: DatabaseError?,
                    committed: Boolean,
                    currentData: DataSnapshot?
                ) {
                    if (!committed || currentData == null) {
                        it.resumeWithException(Throwable())
                    } else {
                        val code = currentData.getValue(Long::class.java) ?: 0

                        it.resume(code)
                    }
                }
            })
    }
}