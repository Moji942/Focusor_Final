package com.focusor.presentation.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.focusor.R
import com.focusor.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupNavigation()
        observeViewModel()
    }
    
    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_main) as NavHostFragment
        val navController = navHostFragment.navController
        
        binding.bottomNavigation.setupWithNavController(navController)
    }
    
    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                // Handle UI state changes
                updateUI(state)
            }
        }
    }
    
    private fun updateUI(state: MainUiState) {
        // Update UI based on state
    }
}