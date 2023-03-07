package com.filipe1309.abasteceai

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.filipe1309.abasteceai.libraries.actions.Actions

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate")
        setContentView(R.layout.activity_main)
        startActivity(Actions.openComparatorIntent(this))
    }
}
