package com.solar.firebase.auth.library

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

abstract class ProgressActivity : AppCompatActivity(){
  private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

  private val _progressDialog: ProgressDialog by lazy {
    ProgressDialog(this)
  }

  fun showProgress() {
    _progressDialog.show()
  }

  fun dismissProgress() {
    if (_progressDialog.isShowing)
      _progressDialog.dismiss()
  }
}