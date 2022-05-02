package com.soninhodobeb.helpers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.soninhodobeb.R


class ErrorHelper {

    companion object Helper {
        fun showToast(
            ctx: Context,
            errorString: String = ctx.getString(R.string.error_message),
            length: Int = Toast.LENGTH_SHORT
        ) {
            Toast.makeText(ctx, errorString, length)
                .show()

        }

        fun showSnackbar(
            snackView: View,
            errorString: String = snackView.context.getString(R.string.error_message),
            length: Int = Snackbar.LENGTH_SHORT
        ) {
            Snackbar.make(snackView, errorString, length)
                .setAction(snackView.context.getString(R.string.contact_button)) {
                    sendEmail(snackView.context)
                }
                .show()
        }

        fun sendEmail(ctx: Context) {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")

            intent.putExtra(Intent.EXTRA_EMAIL, ctx.getString(R.string.support_mail_address))
            intent.putExtra(Intent.EXTRA_SUBJECT, ctx.getString(R.string.support_mail_title))
            ctx.startActivity(intent)
        }
    }
}