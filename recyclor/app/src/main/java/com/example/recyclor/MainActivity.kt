package com.example.recyclor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var dataStoreManager: DataStoreManager
    private val viewModel: MainViewModel by viewModels()

    //firebaseAuth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dataStoreManager = DataStoreManager(this)
        if(savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.frame_layout, MenuFragment())
            }
        }

        checkThemeMode()
        auth = FirebaseAuth.getInstance()

      drawerLayout= findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout,R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            it.isChecked = true

            when(it.itemId){
                R.id.nav_map -> replaceFragment(MapsFragment())
                R.id.nav_order -> replaceFragment(Orders())
                R.id.nav_search -> replaceFragment(SearchFragment())
                R.id.nav_menu -> replaceFragment(MenuFragment())
                R.id.nav_settings -> replaceFragment(SettingsFragment())
                R.id.nav_log -> log()

            }
            true
        }
    }

    private fun log() {
        val firebaseUser = auth.currentUser
        if (firebaseUser != null){
            // ollaan jo kirjauduttu sisään -> kirjaudutaan ulos
            auth.signOut()
            replaceFragment(MenuFragment())

        }
        else{
            // jos ei olla kirjauduttu sisälle, navigoidaan loginiin
            replaceFragment(Login())
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
    }

    private fun checkThemeMode() {
        lifecycleScope.launch {
            when (viewModel.getTheme.first()) {
                true -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }

                false -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }
}