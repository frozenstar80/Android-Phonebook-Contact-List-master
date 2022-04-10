package com.example.user.android.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.user.android.R
import com.example.user.android.databinding.ActivityMainBinding
import com.example.user.android.view.ContactListFragment.Companion.newInstance

class MainActivity : AppCompatActivity() {
    private val REQUESTCODE = 1
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(
            layoutInflater
        )
        val view: View = binding!!.root
        setContentView(view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPhoneContactsPermission(Manifest.permission.READ_CONTACTS)) requestPermissions(
                arrayOf(
                    Manifest.permission.READ_CONTACTS
                ), REQUESTCODE
            )
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, newInstance())
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                .commitNow()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUESTCODE) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, newInstance())
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                .commitNow()
        }
    }

    private fun hasPhoneContactsPermission(permission: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val hasPermission = ContextCompat.checkSelfPermission(applicationContext, permission)
            hasPermission == PackageManager.PERMISSION_GRANTED
        } else true
    }
}