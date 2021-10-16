package com.share.art.core

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.share.art.ar.data.AuthRepository
import com.share.art.ar.data.CloudAnchorRepository

@SuppressLint("StaticFieldLeak")
object DI {
    private lateinit var context: Context

    fun init(context: Context) {
        if (context !is Application) throw IllegalStateException("Here can be only Application")
        DI.context = context
    }

    private fun getSharedPreferences(): SharedPreferences =
        context.getSharedPreferences("global_share_preferences", Context.MODE_PRIVATE)

    private fun getAuthRepository(): AuthRepository {
        return AuthRepository(getSharedPreferences())
    }

    fun getCloudAnchorRepository(): CloudAnchorRepository {
        val keyRoot = "shared_anchor_root"

        val firebaseApp =
            FirebaseApp.initializeApp(context)
                ?: throw IllegalStateException("firebaseApp can't be null")

        val rootReference =
            FirebaseDatabase.getInstance(firebaseApp).reference.child(keyRoot)

        DatabaseReference.goOnline()

        return CloudAnchorRepository(rootReference, getAuthRepository())
    }
}