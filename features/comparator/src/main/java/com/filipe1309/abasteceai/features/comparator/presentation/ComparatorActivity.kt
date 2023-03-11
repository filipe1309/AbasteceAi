package com.filipe1309.abasteceai.features.comparator.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.filipe1309.abasteceai.features.comparator.R

class ComparatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ComparatorActivity", "onCreate")
        setContentView(R.layout.activity_comparator)
    }
}
