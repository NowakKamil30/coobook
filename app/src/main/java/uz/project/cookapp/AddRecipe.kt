package uz.project.cookapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


class AddRecipe : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var imageView: ImageView
    private lateinit var photoButton: Button
    private val collectionRef = Firebase.firestore.collection("recipe")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        imageView = findViewById(R.id.my_avatar_imageview)
        photoButton = findViewById(R.id.btn_add_photo)
        auth = Firebase.auth

        photoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 3)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == 3) {
                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val imageFileName = "${auth.currentUser?.email}_JPEG_$timestamp.${data?.data?.let {
                    getFileExt(
                        it
                    )
                }}"
                imageView.setImageURI(data?.data)

                data?.data?.let { uploadImageToFirebase(imageFileName, it) }
            }
        }
    }

    private fun uploadImageToFirebase(imageFileName: String, data: Uri) {
        collectionRef.add(hashMapOf("test" to "work"))
    }

    private fun getFileExt(uri: Uri): String? {
        val c = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(c.getType(uri))
    }
}