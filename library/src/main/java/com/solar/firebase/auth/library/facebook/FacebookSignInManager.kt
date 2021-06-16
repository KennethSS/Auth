package com.solar.firebase.auth.library.facebook

import android.util.Log
import com.facebook.AccessToken
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.solar.firebase.auth.library.FirebaseAuthModel
import com.solar.firebase.auth.library.SignInState
import com.solar.firebase.auth.library.SignInState.Failure
import com.solar.firebase.auth.library.SignInState.Success

class FacebookSignInManager(
  private val auth: FirebaseAuth,
  private val onResult: (state: SignInState) -> Unit
) : FacebookCallback<LoginResult> {

  override fun onSuccess(result: LoginResult?) {
    Log.d(TAG, "facebook:onSuccess:$result")
    if (result != null) {
      handleFacebookAccessToken(result.accessToken)
    }
  }

  override fun onCancel() {
    Log.d(TAG, "facebook:onCancel")
    onResult(Failure(Exception("Facebook login canceled")))
  }

  override fun onError(error: FacebookException) {
    Log.d(TAG, "facebook:onError", error)
    onResult(Failure(error))
  }

  private fun handleFacebookAccessToken(token: AccessToken) {
    Log.d(TAG, "handleFacebookAccessToken:$token")

    val credential = FacebookAuthProvider.getCredential(token.token)
    auth.signInWithCredential(credential)
      .addOnCompleteListener() { task ->
        if (task.isSuccessful) {
          // Sign in success, update UI with the signed-in user's information
          Log.d(TAG, "signInWithCredential:success")
          val user = auth.currentUser
          if (user != null) {
            onResult(Success(mapCurrentUserToFirebaseAuthModel(user, token.token)))
          }
        } else {
          // If sign in fails, display a message to the user.
          Log.w(TAG, "signInWithCredential:failure", task.exception)
          /*Toast.makeText(baseContext, "Authentication failed.",
            Toast.LENGTH_SHORT).show()*/
          onResult(Failure(task.exception ?: Exception("UnKnown")))
        }
      }
  }
  private fun mapCurrentUserToFirebaseAuthModel(
    user: FirebaseUser,
    token: String?
  ): FirebaseAuthModel =
    FirebaseAuthModel(user.uid, token, user.email, user.displayName, user.photoUrl.toString())


  companion object {
    private const val TAG = "FacebookSignInManager"
  }
}