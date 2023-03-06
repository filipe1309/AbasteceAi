package com.filipe1309.abasteceai

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate")
        setContentView(R.layout.activity_main)
        startActivity(
            Intent("com.filipe1309.abasteceai.features.comparator.open")
                .setPackage(this.packageName)
        )
    }
}
