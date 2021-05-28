package com.tahar.fitness.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tahar.fitness.*

class SportAdapter(
    val context: MainActivity,
    //private val currentSport:
    private val sportList: List<SportModel>,
    private val layoutId : Int
) : RecyclerView.Adapter<SportAdapter.ViewHolder>() {
    // Boîte pour ranger tous les composants à contrôler
    class ViewHolder(view:View) : RecyclerView.ViewHolder(view) {
        // Comment récupérer l'image d'un traning exercise
        val sportImage=view.findViewById<ImageView>(R.id.image_item)
        val sportName : TextView? = view.findViewById(R.id.name_item)
        val sportDescription : TextView? = view.findViewById(R.id.description_item)
        val starIcon = view.findViewById<ImageView>(R.id.star_icon)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(layoutId,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Récupérer les infos d'un traning exercise ayant une position spécifique
        val currentSport = sportList[position]

        //récupérer le repository
        val repo = SportRepository()

        // Utiliser glide pour récupérer l'exercice à partir de son lien et l'ajouter à notre composant
        Glide.with(context).load(Uri.parse(currentSport.imageUrl)).into(holder.sportImage)
        // context est une BD interne qui va contenir ttes les infos contextuelles de l'app

        // Mettre à jour le nom d'un traning exercise
        holder.sportName?.text = currentSport.name

        // Mettre à jour la description
        holder.sportDescription?.text = currentSport.description

        //Vérifier si l'exercice a été liké !!!
        if (currentSport.liked) {
            holder.starIcon.setImageResource(R.drawable.ic_star_foreground)
        }
        else {
            holder.starIcon.setImageResource(R.drawable.ic_unstar_foreground)
        }

        //rajouter une intéraction sur cette étoile
        holder.starIcon.setOnClickListener{
            //inversé si le bouton est liked ou non
            currentSport.liked = !currentSport.liked

            //mettre à jour l'objet sport
            repo.updateSport(currentSport)
        }

        //interaction lors du clic sur un exercice d'entrainement
        holder.itemView.setOnClickListener{
            //afficher la popup
            SportPopup(this, currentSport).show()
        }

    }

    override fun getItemCount(): Int = sportList.size
    // ea à : override fun getItemCount(): Int {   return 5}
}