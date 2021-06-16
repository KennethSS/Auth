package com.solar.firebase.auth.library

/**
 *  Created by Kenneth on 12/18/20
 */
data class FirebaseAuthModel(
        val uid: String,
        val token: String? = null,
        val email: String? = null,
        val nickname: String? = null,
        val photoUrl: String? = null
)