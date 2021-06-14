package com.pascal.weatherapp.utils

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

fun View.showActionSnackBar(
    text: String,
    actionText: String,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length).setAction(actionText, action)
        .show()
}

fun View.showSnackBar(
    text: String,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length)
        .show()
}

fun View.showSnackBar(
    @StringRes stringId: Int,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, this.resources.getText(stringId), length)
        .show()
}