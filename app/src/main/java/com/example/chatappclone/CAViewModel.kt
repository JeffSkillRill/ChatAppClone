package com.example.chatappclone

import android.os.Message
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.chatappclone.data.COLLECTION_USER
import com.example.chatappclone.data.Event
import com.example.chatappclone.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CAViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore,
    val storage: FirebaseStorage
) : ViewModel() {


    val inProgress = mutableStateOf(false)
    val popupNotification = mutableStateOf<Event<String>?>(null)
    val signedIn = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)

    init {
        val currentUser = auth.currentUser
        signedIn.value = currentUser != null
    }

    fun onSignUp(name: String, number: String, email: String, password: String) {
        if (name.isEmpty() or number.isEmpty() or email.isEmpty() or password.isEmpty()) {
            handleException(customMessage = "Please fill all gaps")
            return
        }
        inProgress.value = true
        db.collection(COLLECTION_USER).whereEqualTo("number", number)
            .get()
            .addOnSuccessListener {
                if (it.isEmpty)
                 auth.createUserWithEmailAndPassword(email, password)
                     .addOnCompleteListener{ task ->
                         if (task.isSuccessful){
                             signedIn.value = true
                             createOrUpdateProfile(name = name,number = number)
                         } else
                             handleException(task.exception, "Signup failed")
                     }
               else
                   handleException(customMessage = "number already exists")
                inProgress.value = false
            }
            .addOnFailureListener {
                handleException(it)
            }
    }
    private fun createOrUpdateProfile(

        name: String? = null,
        number: String? = null,
        imageUrl: String? = null
    ){
        val uid = auth.currentUser?.uid
        val userData = UserData(
            userId = uid,
            name = name,
            number = number,
            imageUrl = imageUrl
        )

        uid?.let { uid->
            inProgress.value = true
            db.collection(COLLECTION_USER).document(uid)
                .get()
                .addOnSuccessListener {
                    if (it.exists())
                    {
                        it.reference.update(userData.toMap())
                            .addOnSuccessListener{
                                inProgress.value = false
                            }
                            .addOnFailureListener{
                                handleException(it, "Cannot update user")
                            }
                    }else{
                        db.collection(COLLECTION_USER).document(uid).set(userData)
                        inProgress.value = false
                    }

                }
                .addOnFailureListener{
                    handleException(it, "Cannot retrieve user")
                }
        }
    }
    private fun getUserData(uid: String)
    {
        inProgress.value = true
        db.collection(COLLECTION_USER).document(uid)
            .addSnapshotListener { value, error ->
                if (error != null)
                    handleException(error, "Cannot retrieve user data")
                if (value != null){
                    val user = value.toObject<UserData>()
                    userData.value = user
                    inProgress.value = false
                }
            }
    }


    private fun handleException(exception: Exception? = null, customMessage: String = "") {
        Log.e("ChatAppClone", "Chat app exception", exception)
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage ?: ""
        val message = if (customMessage.isEmpty()) errorMsg else "$customMessage: $errorMsg"
        popupNotification.value = Event(message)
        inProgress.value = false
    }
}


