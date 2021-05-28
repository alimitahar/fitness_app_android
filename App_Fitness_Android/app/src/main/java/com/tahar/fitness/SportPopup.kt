package com.tahar.fitness

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.tahar.fitness.adapter.SportAdapter

class SportPopup(
    private val adapter: SportAdapter,
    private val currentSport: SportModel
    ) : Dialog(adapter.context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_sports_details)
        setupComponents()
        setupCloseButton()
        setupDeletButton()
        setupStarButton()
    }

    private fun updateStar(button: ImageView){
        if(currentSport.liked){
            button.setImageResource(R.drawable.ic_star_foreground)
        }
        else{
            button.setImageResource(R.drawable.ic_unstar_foreground)
        }

    }

    private fun setupStarButton() {
        //récupérer
        var starButton = findViewById<ImageView>(R.id.star_button)
        updateStar(starButton)


        //interaction (MAJ dans la BD)
        starButton.setOnClickListener{
            currentSport.liked = !currentSport.liked
            val repo = SportRepository()
            repo.updateSport(currentSport)

            updateStar(starButton)
        }
    }

    private fun setupDeletButton() {
        findViewById<ImageView>(R.id.delete_button).setOnClickListener{
            //supprimer un traning exercise de la BD
            val repo = SportRepository()
            repo.deletSport(currentSport)
            dismiss() //pour fermer la fenêtre après suppression

        }
    }

    private fun setupCloseButton() {
        findViewById<ImageView>(R.id.close_button).setOnClickListener{
            //fermer la fenêtre popup
            dismiss()
        }
    }

    private fun setupComponents() {
        // Actualiser l'image d'un traning exercise
        val sportImage = findViewById<ImageView>(R.id.image_item)
        Glide.with(adapter.context).load(Uri.parse(currentSport.imageUrl)).into(sportImage)

        //actualiser le nom d'un traning exercise
        findViewById<TextView>(R.id.popup_sport_name).text = currentSport.name

        //actualiser la description d'un traning exercise
        findViewById<TextView>(R.id.popup_sport_description_subtitle).text = currentSport.description

        //actualiser la difficulté d'un traning exercise
        findViewById<TextView>(R.id.popup_sport_difficult_title).text = currentSport.difficult



    }
}