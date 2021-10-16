package com.share.art

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.codelab.cloudanchor.helpers.FullScreenHelper
import com.share.art.ar.presentation.CloudAnchorFragment

class MainActivity : AppCompatActivity(R.layout.main_activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addArFragment()
    }

    private fun addArFragment() {
        var arFragment = supportFragmentManager.findFragmentById(R.id.arContainer)
        if (arFragment == null) {
            arFragment = CloudAnchorFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.arContainer, arFragment).commit()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        FullScreenHelper.setFullScreenOnWindowFocusChanged(this, hasFocus)
    }
}