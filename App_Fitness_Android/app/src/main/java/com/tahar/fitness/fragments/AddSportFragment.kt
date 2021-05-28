package com.tahar.fitness.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.tahar.fitness.MainActivity
import com.tahar.fitness.R
import com.tahar.fitness.SportModel
import com.tahar.fitness.SportRepository
import com.tahar.fitness.SportRepository.singleton.downloadUri
import java.util.*

class AddSportFragment (
    private val context:MainActivity

        ) : Fragment() {

    private var file:Uri? = null
    private var uploadedImage : ImageView? = null  //Mettre var si l'attribut peut changer de valeur

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_add_sport, container, false)

        //récupérer uploadedImage pour lui associer son composant
        uploadedImage = view.findViewById(R.id.preview_image)

        // récupérer le bouton pour charger un traning exercise
        val pickupImageButton = view.findViewById<Button>(R.id.upload_button)

        // lorsqu'on clique dessus, ça ouvre les images du téléphone
        pickupImageButton.setOnClickListener{pickupImage()}

        //Récupérer le bouton confirmer
        val confirmButton = view.findViewById<Button>(R.id.confirm_button)
        confirmButton.setOnClickListener{sendForm(view)}

        return view
    }

    private fun sendForm(view: View) {
        val repo = SportRepository()
        repo.uploadImage(file!!) // les !! ==> Bypass
        {
            val sportName = view.findViewById<EditText>(R.id.name_input).text.toString()
            val sportDescription = view.findViewById<EditText>(R.id.description_input).text.toString()
            val grow = view.findViewById<Spinner>(R.id.grow_spinner).selectedItem.toString()
            val downloadImageUrl = downloadUri

            //Créer un nouvel objet SportModel
            val sport = SportModel(
                UUID.randomUUID().toString(),
                sportName,
                sportDescription,
                downloadImageUrl.toString(),
                grow
            )
                //envoyer SportModel dans la BD
                repo.insertSport(sport)
        }
    }

    private fun pickupImage() {
        // intent Permet d'ouvrir les contacts, les images, ou changer d'activité ...
        val intent = Intent()
        intent.type = "image/" // Il faut l'écrire au singulier pour pouvoir ouvrir les images
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 47)
        //47 est un code = request code = dire quelle action a été faite

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==47 && resultCode == Activity.RESULT_OK){

            // Vérifier si les données sont nulles
            if(data==null || data.data == null) return //si c'est le cas on retourne

            //récupérer l'exercice sélectionné
            file = data.data

            //Mettre à jour l'aperçu d'un traning exercise

            //*------uploadedImage?.setImageURI(selectedImage)
            uploadedImage?.setImageURI(file)




        }
    }
}