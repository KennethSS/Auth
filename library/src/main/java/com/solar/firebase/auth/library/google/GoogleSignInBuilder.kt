package com.solar.firebase.auth.library.google

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.solar.firebase.auth.library.R

class GoogleSignInBuilder(context: Context) {
  val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
    .requestIdToken(context.getString(R.string.default_web_client_id))
    .requestEmail()
    .build()

  val googleSignInClient = GoogleSignIn.getClient(context, gso)

  inline fun signIn(start: (intent: Intent) -> Unit) {
    val signInIntent = googleSignInClient.signInIntent
    start(signInIntent)
  }
}