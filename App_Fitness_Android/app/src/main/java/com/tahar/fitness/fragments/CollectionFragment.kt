package com.tahar.fitness.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tahar.fitness.MainActivity
import com.tahar.fitness.R
import com.tahar.fitness.SportRepository.singleton.sportList
import com.tahar.fitness.adapter.SportAdapter
import com.tahar.fitness.adapter.SportItemDecoration

class CollectionFragment (
    private val context : MainActivity
        ) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.fragment_collection, container, false)

        //récupérer ma recyclerView
        val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.collection_recycler_list)
        collectionRecyclerView.adapter = SportAdapter(context, sportList.filter { it.liked },R.layout.item_vertical_sport)
        collectionRecyclerView.layoutManager = LinearLayoutManager(context)
        collectionRecyclerView.addItemDecoration(SportItemDecoration())


        return view
    }
}