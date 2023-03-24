package com.filipe1309.abasteceai.ui.comparator

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class ComparatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ComparatorActivity", "onCreate")
        setContentView(R.layout.activity_comparator)
    }
}
