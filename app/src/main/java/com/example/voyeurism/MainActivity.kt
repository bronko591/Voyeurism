package com.example.voyeurism

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat.getActionView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.commit
import com.example.voyeurism.fragments.ChaturbateFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var toolBar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    //
    private lateinit var searchView: SearchView
    private lateinit var searchItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
            .apply {
                toolBar = findViewById(R.id.toolBar)
                drawerLayout = findViewById(R.id.drawerLayout)
                navigationView = findViewById(R.id.navigationView)
            }
        setSupportActionBar(toolBar).apply {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
        drawerLayout.apply { addDrawerListener(
            ActionBarDrawerToggle(this@MainActivity, drawerLayout, toolBar,0,0).apply
            {
                syncState()
                drawerArrowDrawable.color = ContextCompat.getColor(this@MainActivity, R.color.black) })
            isClickable = true
        }
        navigationView.apply { itemIconTintList = null }.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                supportFragmentManager.commit{
                    //  replace(R.id.fragmentContainerView, FavoriteFragment.newInstance())
                }
                setActionBarTitle("Favorite Cams", "")
                writeToast("Open Favorites")
            }
            R.id.chaturbate_featured -> {replaceFragment("")
                setActionBarTitle("Featured Cams", "on Chaturbate")
                writeToast("Open Featured Cams")
            }
            R.id.chaturbate_female -> {replaceFragment("f")
                setActionBarTitle("Female Cams", "on Chaturbate")
                writeToast("Open Female Cams")
            }
            R.id.chaturbate_male -> {replaceFragment("m")
                setActionBarTitle("Male Cams", "on Chaturbate")
                writeToast("Open Male Cams")
            }
            R.id.chaturbate_couple -> {replaceFragment("c")
                setActionBarTitle("Couple Cams", "on Chaturbate")
                writeToast("Open Couple Cams")
            }
            R.id.chaturbate_trans -> {replaceFragment("s")
                setActionBarTitle("Trans Cams", "on Chaturbate")
                writeToast("Open Trans Cams")
            }
            R.id.settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                setActionBarTitle("Settings", "")
                writeToast("Open Settings")
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }



    private fun writeToast(message:String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun setActionBarTitle(title:String, subtitle:String){
        supportActionBar?.apply {
            setTitle(title)
            setSubtitle(subtitle)
        }
    }

    fun replaceFragment(url:String){
        supportFragmentManager.commit{
            replace(R.id.fragmentContainerView, ChaturbateFragment.newInstance(url))
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
          //  setActionBarTitle(titleActionBar,"on Chaturbate")
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            menuInflater.inflate(R.menu.main_menu, menu)
            searchItem = menu.findItem(R.id.app_bar_search)
            searchView = getActionView(searchItem) as SearchView
            searchView.maxWidth = Int.MAX_VALUE
            searchView.setOnCloseListener { true }

            val searchPlate = searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
            searchPlate.hint = "Suche..."
            val searchPlateView: View = searchView.findViewById(androidx.appcompat.R.id.search_plate)
            searchPlateView.setBackgroundColor(ContextCompat.getColor(this,android.R.color.transparent))

            searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    replaceFragment("https://chaturbate.com/?keywords=$query&")
                    setActionBarTitle(query,"")
                    writeToast("Open $query")
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })

            val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }

}