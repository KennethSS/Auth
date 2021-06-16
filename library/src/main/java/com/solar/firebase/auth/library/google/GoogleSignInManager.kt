package com.solar.firebase.auth.library.google

import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.solar.firebase.auth.library.FirebaseAuthModel
import com.solar.firebase.auth.library.SignInState
import com.solar.firebase.auth.library.SignInState.Failure
import com.solar.firebase.auth.library.SignInState.Success

class GoogleSignInManager(
  private val onResult: (state: SignInState) -> Unit
) {
  fun googleSignIn(auth: FirebaseAuth, data: Intent?) {
    onActivityResult(auth, data)
  }

  private fun onActivityResult(auth: FirebaseAuth, data: Intent?) {

    val task = GoogleSignIn.getSignedInAccountFromIntent(data)

    try {
      // Google Sign In was successful, authenticate with Firebase
      val account = task.getResult(ApiException::class.java)!!
      Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
      firebaseAuthWithGoogle(auth, account.idToken!!)
    } catch (e: ApiException) {
      // Google Sign In failed, update UI appropriately
      Log.w(TAG, "Google sign in failed", e)
      onResult(Failure(e))
    }
  }

  private fun firebaseAuthWithGoogle(auth: FirebaseAuth, idToken: String) {
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    auth.signInWithCredential(credential)
      .addOnCompleteListener { task ->
        if (task.isSuccessful) {
          // Sign in success, update UI with the signed-in user's information
          Log.d(TAG, "signInWithCredential:success")
          val currentUser = auth.currentUser
          if (currentUser != null) {
            Log.d(TAG, "current user null")
            onResult(Success(mapCurrentUserToFirebaseAuthModel(currentUser, null)))
          } else {
            onResult(Failure(Exception("User is Null")))
          }
        } else {
          // If sign in fails, display a message to the user.
          Log.w(TAG, "signInWithCredential:failure", task.exception)
          onResult(Failure(task.exception ?: Exception("Unknown Error")))
        }
      }
  }

  private fun mapCurrentUserToFirebaseAuthModel(
    user: FirebaseUser,
    token: String?
  ): FirebaseAuthModel =
    FirebaseAuthModel(user.uid, token, user.email, user.displayName, user.photoUrl.toString())

  companion object {
    private const val TAG = "GoogleSignInManager"
  }
}