package com.tahar.finess.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tahar.fitness.MainActivity
import com.tahar.fitness.R
import com.tahar.fitness.SportRepository.singleton.sportList
import com.tahar.fitness.adapter.SportAdapter
import com.tahar.fitness.adapter.SportItemDecoration

class HomeFragment (private val context:MainActivity) : Fragment() {
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState:Bundle?): View? {
        val view =  inflater?.inflate(R.layout.fragment_home, container,false)


        // Récupérer le 1er recycler view
        val horizontalRecyclerView = view.findViewById<RecyclerView>(R.id.horizontal_recycler_view)
        horizontalRecyclerView.adapter = SportAdapter(context, sportList.filter { !it.liked }, R.layout.item_horizontal_sport)

        // Récupérer le second RecyclerView
        val verticalRecyclerview = view.findViewById<RecyclerView>(R.id.vertical_recycler_view)
        verticalRecyclerview.adapter = SportAdapter(context, sportList, R.layout.item_vertical_sport)
        verticalRecyclerview.addItemDecoration(SportItemDecoration())

        return view
    }
}