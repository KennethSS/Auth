package com.solar.firebase.auth.library

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.facebook.CallbackManager
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.solar.firebase.auth.library.FirebaseAuthType.FACEBOOK
import com.solar.firebase.auth.library.FirebaseAuthType.GOOGLE
import com.solar.firebase.auth.library.SignInState.Failure
import com.solar.firebase.auth.library.SignInState.Success
import com.solar.firebase.auth.library.facebook.FacebookSignInManager
import com.solar.firebase.auth.library.google.GoogleSignInBuilder
import com.solar.firebase.auth.library.google.GoogleSignInManager

/**
 *  app/google-service.json
 *
 *  1. Google
 *  - implementation 'com.google.android.gms:play-services-auth:18.0.0'
 *  - implementation 'com.firebaseui:firebase-ui-auth:4.3.1'
 *  - apply plugin: 'com.google.gms.google-services'
 *
 */
abstract class AbstractFirebaseAuthActivity : ProgressActivity() {

  lateinit var auth: FirebaseAuth
  lateinit var defaultWebClientId: String

  private val callbackManager: CallbackManager by lazy { CallbackManager.Factory.create() }
  private val googleSignInBuilder: GoogleSignInBuilder by lazy { GoogleSignInBuilder(this, defaultWebClientId) }

  private val facebookSignInManager: FacebookSignInManager by lazy {
    FacebookSignInManager(auth, ::onResultState)
  }

  private val googleSignInManager: GoogleSignInManager by lazy {
    GoogleSignInManager(::onResultState)
  }

  protected abstract fun onSuccess(data: FirebaseAuthModel)
  protected abstract fun onFailed(code: Int, message: String)

  private fun onResultState(state: SignInState) {
    when (state) {
      is Success -> {
        onSuccess(state.user)
      }
      is Failure -> {
        onFailed(0, state.throwable.localizedMessage ?: "")
      }
    }
  }

  fun login(type: FirebaseAuthType, loginBtn: LoginButton? = null) {
    when (type) {
      GOOGLE -> googleLogin()
      FACEBOOK -> facebookLogin(loginBtn!!)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    auth = Firebase.auth
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    dismissProgress()
    callbackManager.onActivityResult(requestCode, resultCode, data)
    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);

    Log.d(TAG, "requestCode $requestCode")
    Log.d(TAG, "resultCode $resultCode")

    when (requestCode) {
      REQUEST_CODE_GOOGLE_SIGN_IN -> googleSignInManager.googleSignIn(auth, data)
    }
  }

  private fun facebookLogin(btn: LoginButton) {
    btn.run {
      setReadPermissions("email", "public_profile")
      registerCallback(callbackManager, facebookSignInManager)
      performClick()
    }
  }

  private fun googleLogin() {
    showProgress()
    googleSignInBuilder.signIn {
      startActivityForResult(it, REQUEST_CODE_GOOGLE_SIGN_IN)
    }

    //val providers = arrayListOf(
    //AuthUI.IdpConfig.GoogleBuilder().build())

    // Create and launch sign-in intent
    /*startActivityForResult(
            AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
            RC_SIGN_IN
    )*/
  }

  companion object {
    private const val REQUEST_CODE_GOOGLE_SIGN_IN = 0
    private const val TAG = "FirebaseLoginActivity"
  }
}