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
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import com.parse.*
import java.io.File

class MainActivity : AppCompatActivity() {

    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    val photoFileName = "photo.jpg"
    var photoFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<ImageView>(R.id.iv_profile).setOnClickListener {
            ParseUser.logOut()
            val currentUser = ParseUser.getCurrentUser()
            if (ParseUser.getCurrentUser() == null){
                Log.e(TAG, "Logged out")
                val toast = Toast.makeText(MainActivity@ this, "Successfully logged out", Toast.LENGTH_LONG).show()
                goToLoginActivity()
                finish()
            } else {
                Log.e(TAG, "Failed logging out")
                val toast = Toast.makeText(MainActivity@ this, "Error while logging out", Toast.LENGTH_LONG).show()
            }
        }

        findViewById<Button>(R.id.btnSubmit).setOnClickListener{
            val description = findViewById<EditText>(R.id.description).text.toString()
            val user = ParseUser.getCurrentUser()
            if(photoFile != null) {
                submitPost(description, user, photoFile!!)
            } else {
                Log.e(TAG, "No picture")
                val toast = Toast.makeText(MainActivity@ this, "Cannot share without picture", Toast.LENGTH_LONG).show()
            }
        }


        findViewById<Button>(R.id.btnSubmit).setOnClickListener{
            val description = findViewById<EditText>(R.id.description).text.toString()
            val user = ParseUser.getCurrentUser()
            if(photoFile != null) {
                submitPost(description, user, photoFile!!)
            } else {
                Log.e(TAG, "No picture")
                val toast = Toast.makeText(MainActivity@ this, "Cannot share without picture", Toast.LENGTH_LONG).show()
            }
        }

        findViewById<Button>(R.id.btnTakePicture).setOnClickListener{
            onLaunchCamera()
        }

        queryPosts()
    }

    fun submitPost(description: String, user: ParseUser, file: File){
        val pb = findViewById<View>(R.id.progressload)
        pb.visibility = ProgressBar.VISIBLE
        val post = Post()
        post.setDescription(description)
        post.setUser(user)
        post.setImage(ParseFile(file))
        post.saveInBackground { exception ->
            if (exception != null){
                Log.e(TAG, "Error while saving post")
                exception.printStackTrace()
                pb.visibility = ProgressBar.INVISIBLE
            } else {
                Log.i(TAG, "Successfully posted")
                pb.visibility = ProgressBar.INVISIBLE
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG , "onActivityResult")
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
//                val intent = Intent(this, CreatePost::class.java)
//                intent.putExtra("TWEET_EXTRA", photoFile!!.absolutePath)
//                startActivityForResult(intent, SHARE_POST_SUCCESS_REQUEST_CODE)
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)

                val ivPreview: ImageView = findViewById(R.id.postImage)
                ivPreview.setImageBitmap(takenImage)
            } else {
                Log.d(TAG, "Failed to take picture")
                val toast = Toast.makeText(MainActivity@ this, "Failed to take picture", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    private fun queryPosts() {

        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_USER)
        query.findInBackground(object: FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if(e != null){
                    Log.e(TAG, "Error fetching posts")
                }
                else{
                    if(posts != null){
                        for (post in posts)
                            Log.i(TAG, "Post: " + post.getDescription() + " , username: " + post.getUser()?.username)
                    }
                }
            }
        })
    }

    companion object {
        const val TAG = "MainActivity"
    }

    private fun goToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

}