package com.solar.firebase.auth.library

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 *  Created by Kenneth on 12/18/20
 */
object FirebaseAuthHelper {

    fun isSignIn(): Boolean = Firebase.auth.currentUser != null

    fun logout() {
        Firebase.auth.signOut()
    }

    fun getCurrentUser(): FirebaseUser? {
        return Firebase.auth.currentUser
    }

    private lateinit var auth: FirebaseAuth
}