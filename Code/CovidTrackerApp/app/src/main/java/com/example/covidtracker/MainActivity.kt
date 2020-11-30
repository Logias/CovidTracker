package com.example.covidtracker

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.covidtracker.db.ArticleDatabase
import com.example.covidtracker.repository.NewsRepository
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {
    lateinit var viewModel: NewsViewModel

    private var mAppBarConfiguration: AppBarConfiguration? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = NewsRepository(ArticleDatabase(this))  //instantiate repository and pass the database
        val viewModelProviderFactory = NewsViewModelProviderFactory(repository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        val myToolBar: Toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(myToolBar)
        myToolBar.setTitleTextColor(Color.parseColor("#FFFFFF"))
        myToolBar.title = "Covid-19 Tracker"

        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)

        mAppBarConfiguration = AppBarConfiguration.Builder(R.id.nav_home).setDrawerLayout(drawer).build()

        val navController =
            Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration!!)
        NavigationUI.setupWithNavController(navigationView, navController)

        val covidDiag = navigationView.menu.findItem(R.id.covid_diag_frag)
        val charity = navigationView.menu.findItem(R.id.charity_frag)
        val support = navigationView.menu.findItem(R.id.fragment_support)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) : Boolean {
        val id = item!!.itemId
        return if (id == R.id.action_settings) {
            // goes to settings fragment
            Toast.makeText(applicationContext, "Settings clicked successfully", Toast.LENGTH_LONG).show()
            // Need to rewrite Settings fragment because it keeps crashing
            true
        } else {
            false
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController =
            Navigation.findNavController(this, R.id.nav_host_fragment)
        return (NavigationUI.navigateUp(navController, mAppBarConfiguration!!)
                || super.onSupportNavigateUp())
    }
}