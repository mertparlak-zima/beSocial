package app.zimablue.besocial.view

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import app.zimablue.besocial.R
import app.zimablue.besocial.databinding.FragmentPostShareBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.*
import java.util.jar.Manifest


class PostShareFragment : Fragment() {

    private lateinit var binding: FragmentPostShareBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage



    var selectedPictureUri : Uri? = null






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerLauncher()

        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPostShareBinding.inflate(inflater, container, false)
        return binding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        binding.imageView.setOnClickListener {
            selectImage()
        }

        binding.shareButton.setOnClickListener {
            shareButton()
        }

        super.onViewCreated(view, savedInstanceState)
    }


    private fun shareButton(){

        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"

        val reference = storage.reference
        val imageReference = reference.child("images").child(imageName)

        if (selectedPictureUri != null){

            imageReference.putFile(selectedPictureUri!!).addOnSuccessListener {

                val uploadPictureReference = storage.reference.child("images").child(imageName)

                uploadPictureReference.downloadUrl.addOnSuccessListener {uri ->


                    if (auth.currentUser != null){
                        val downloadUri = uri.toString()

                        val postMap = hashMapOf<String, Any>()
                        postMap["downloadUri"] = downloadUri
                        postMap["userEmail"] = auth.currentUser!!.email!!
                        postMap["comment"] = binding.commentText.text.toString()
                        postMap["date"] = Timestamp.now()

                        firestore.collection("Post").add(postMap).addOnSuccessListener {

                            Toast.makeText(requireContext(),"Post Sharing",Toast.LENGTH_SHORT).show()

//                            val action = PostShareFragmentDirections.actionPostShareFragmentToFlowFragment()
//                            Navigation.findNavController(requireActivity(),R.id.MainActivityFragmentContainerView).navigate(action)



                        }.addOnFailureListener {
                            Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
                        }

                    }

                }


                val action = PostShareFragmentDirections.actionPostShareFragmentToFlowFragment()
                Navigation.findNavController(requireActivity(),R.id.MainActivityFragmentContainerView).navigate(action)

            }.addOnFailureListener {
                Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun selectImage(){

        if(ContextCompat.checkSelfPermission(requireContext().applicationContext, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(requireView(),"Permission Needed for to gallery", Snackbar.LENGTH_INDEFINITE).setAction("Giv permission",View.OnClickListener {
                    permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }).show()
            }else{
                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)


            }
        }else{
            val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
        }

    }

    private fun registerLauncher(){

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->

            if (result.resultCode == AppCompatActivity.RESULT_OK){
                val intentFromResult = result.data
                if (intentFromResult != null){
                    selectedPictureUri = intentFromResult.data
                    selectedPictureUri.let {
                        binding.imageView.setImageURI(it)
                    }
                }
            }
        }


        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){result ->
            if (result){
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)

            }else{
                Toast.makeText(requireContext(),"Permission Needed!",Toast.LENGTH_LONG).show()
            }
        }

    }



}