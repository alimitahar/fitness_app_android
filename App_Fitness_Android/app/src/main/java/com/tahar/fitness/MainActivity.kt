package com.tahar.fitness

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tahar.finess.fragments.HomeFragment
import com.tahar.fitness.fragments.AddSportFragment
import com.tahar.fitness.fragments.CollectionFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadFragment(HomeFragment(this),R.string.home_page_title)

        //importer la BottomNavigationView
        val navigationView = findViewById<BottomNavigationView>(R.id.navigation_view)
        navigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home_page -> {
                    loadFragment(HomeFragment(this),R.string.home_page_title)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.collection_page -> {
                    loadFragment(CollectionFragment(this),R.string.collection_page_title)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.add_sport_page -> {
                    loadFragment(AddSportFragment(this),R.string.add_sport_page_title)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }




    }

    private fun loadFragment(fragment:Fragment, nomChaine : Int) {

        //charger notre plantRepository
        val repo = SportRepository()

        //actualiser le titre de la page
        findViewById<TextView>(R.id.page_title).text = resources.getString(nomChaine)

        //mettre à jour la liste de plantes
        repo.updateData {

            // Injecter le frangment dans notre noite (fragment_container)
            val transaction=supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }


    }
}