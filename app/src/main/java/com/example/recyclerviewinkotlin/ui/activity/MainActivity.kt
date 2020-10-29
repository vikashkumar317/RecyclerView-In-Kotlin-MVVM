package com.example.recyclerviewinkotlin.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.recyclerviewinkotlin.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Action Bar Title also changes with change in fragment
        NavigationUI.setupActionBarWithNavController(this, findNavController(R.id.fragment))
    }

    // Back Arrow is added in Action Bar
    override fun onSupportNavigateUp() = findNavController(R.id.fragment).navigateUp()
}