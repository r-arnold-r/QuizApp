package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.quizapp.databinding.ActivityMainBinding
import com.example.quizapp.repository.Repository
import com.example.quizapp.utils.PickContact
import com.example.quizapp.utils.readMyQuestions
import com.example.quizapp.utils.readQuestions


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var view : View
    private lateinit var viewModel : MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        view = binding.root
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        setContentView(view)

        imageMenuHandler()
        navigationUISetup()

        readQuestions(this, this, this, viewModel)
        readMyQuestions(this, viewModel)
    }

    private fun navigationUISetup()
    {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.navView.itemIconTintList = null

        NavigationUI.setupWithNavController(binding.navView, navController)
    }

    private fun imageMenuHandler()
    {
        binding.imageMenu.setOnClickListener{
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


}