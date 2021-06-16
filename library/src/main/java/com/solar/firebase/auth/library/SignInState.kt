package com.solar.firebase.auth.library

sealed class SignInState {
  data class Success(
    val user: FirebaseAuthModel
  ) : SignInState()

  data class Failure(
    val throwable: Throwable
  ) : SignInState()
}
