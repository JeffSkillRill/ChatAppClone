package com.example.chatappclone

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn

import dagger.hilt.android.components.ViewModelComponent



@Module
@InstallIn(ViewModelComponent::class)
class HiltModule {
    @Provides
    fun provideAuthentication() : FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideFireStore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    fun provideStorage(): FirebaseStorage = FirebaseStorage.getInstance()
}