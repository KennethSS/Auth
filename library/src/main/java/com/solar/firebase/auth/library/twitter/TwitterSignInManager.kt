package com.solar.firebase.auth.library.twitter

import com.google.firebase.auth.OAuthProvider

class TwitterSignInManager {
  val provider: OAuthProvider.Builder = OAuthProvider.newBuilder("twitter.com")
}