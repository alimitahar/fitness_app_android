package com.tahar.fitness

import android.net.Uri
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.tahar.fitness.SportRepository.singleton.databaseRef
import com.tahar.fitness.SportRepository.singleton.downloadUri
import com.tahar.fitness.SportRepository.singleton.sportList
import com.tahar.fitness.SportRepository.singleton.storageReference
import java.util.*

class SportRepository {

    object singleton {
        //donner le lien pour accéder au bucket
         private val BUCKET_URL: String = "gs://nature-ab277.appspot.com"


        //se connecter à notre espace de stockage
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)

        //Se connecter à la référence "sports"
        val databaseRef = FirebaseDatabase.getInstance().getReference("sports")


        //Créer une liste de training exercises
        val sportList = arrayListOf<SportModel>()

        //contenir le lien du traning exercise courant
        var downloadUri : Uri? =null
    }

    fun updateData(callback : () -> Unit) {
        // absorber les données depuis la databaseRef  --> liste de de traning exercises
        databaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                //retirer les anciens traning exercises
                sportList.clear()

                // récolter la liste
                for(ds in p0.children){
                    // construire un objet sport
                    val sport = ds.getValue(SportModel::class.java)
                    //Vérifier que le sport n'est pas null
                    if(sport != null) {
                        // ajouter le sport à notre liste
                        sportList.add(sport)
                    }
                }
                //actionner le callback
                callback()
            }

            override fun onCancelled(p0: DatabaseError) {}

        })

    }

    //Créer une fonction pour envoyer des fichiers sur le storage
    fun uploadImage(file:Uri, callback: () -> Unit){
        // vérifier que "file" n'est pas null
        if(file != null){
            val fileName = UUID.randomUUID().toString() + ".jpg"
            //UUID.randomUUID génère Auto un ID de type String(toString)
            val ref = storageReference.child(fileName)
            val uploadTask =  ref.putFile(file)

            //démarrer la tâche d'envoi
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>>{ task ->
                //snapshot => nouvelle donnée qui vient de s'enregistrer

                //vérifier s'il y a eu un pblm lors de l'envoi du fichier
                if(!task.isSuccessful){
                    task.exception?.let { throw it }
                    //mettre ? => Si l'exception est incorrecte, il ne va pas déclencher le it
                }
                return@Continuation ref.downloadUrl

            //pour retourner le lien du traning exercise en question

            }).addOnCompleteListener { task ->
                //Ajouter un évènement si la tâche est accomplie
                //vérifier si tout a bien fonctionné
                if(task.isSuccessful){
                    //récupérer l'img
                        //------------val downloadURI = task.result
                    downloadUri = task.result
                    callback()
                }
            }

        }
    }

    //mettre à jour l'objet "sport" en BD
    fun updateSport(sport: SportModel) = databaseRef.child(sport.id).setValue(sport)

    //insérer une nouveau training exercise dans la BD
    fun insertSport(sport : SportModel) = databaseRef.child(sport.id).setValue(sport)

    //suppression d'un traning exercise de la BD
    fun deletSport(sport : SportModel) = databaseRef.child(sport.id).removeValue()

}