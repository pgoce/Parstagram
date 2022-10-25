package com.example.parstagram

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import com.example.parstagram.fragments.ComposeFragment
import com.example.parstagram.fragments.FeedFragment
import com.example.parstagram.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.*
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager: FragmentManager = supportFragmentManager

//        findViewById<ImageView>(R.id.iv_profile).setOnClickListener {
//            ParseUser.logOut()
//            val currentUser = ParseUser.getCurrentUser()
//            if (ParseUser.getCurrentUser() == null){
//                Log.e(TAG, "Logged out")
//                val toast = Toast.makeText(MainActivity@ this, "Successfully logged out", Toast.LENGTH_LONG).show()
//                goToLoginActivity()
//                finish()
//            } else {
//                Log.e(TAG, "Failed logging out")
//                val toast = Toast.makeText(MainActivity@ this, "Error while logging out", Toast.LENGTH_LONG).show()
//            }
//        }


        var fragmentToShow: Fragment? = null
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener {
            item ->

            when (item.itemId){
                R.id.action_home -> {
                    fragmentToShow = FeedFragment()
//                    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                }
                R.id.action_compose -> {
                    fragmentToShow = ComposeFragment()
//                    Toast.makeText(this, "Compose", Toast.LENGTH_SHORT).show()
                }
                R.id.action_profile -> {
                    fragmentToShow = ProfileFragment()
//                    Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
                }
            }
            if (fragmentToShow != null){
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragmentToShow!!).commit()
            }

            true
        }

        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.action_home

//        queryPosts()
    }






    companion object {
        const val TAG = "MainActivity"
    }

    private fun goToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

}