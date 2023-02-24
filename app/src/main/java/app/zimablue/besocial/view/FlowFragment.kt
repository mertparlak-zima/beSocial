package app.zimablue.besocial.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import app.zimablue.besocial.R
import app.zimablue.besocial.adapter.FlowFragmentRecyclerAdapter
import app.zimablue.besocial.databinding.FragmentFlowBinding
import app.zimablue.besocial.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class FlowFragment : Fragment() {

    private lateinit var binding: FragmentFlowBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore

    private lateinit var postArrayList: ArrayList<Post>

    private lateinit var flowFragmentRecyclerAdapter: FlowFragmentRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        db = Firebase.firestore

        postArrayList = ArrayList<Post>()




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFlowBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.flowFragmentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        flowFragmentRecyclerAdapter = FlowFragmentRecyclerAdapter(postArrayList)
        binding.flowFragmentRecyclerView.adapter = flowFragmentRecyclerAdapter

        getData()



    }

    private fun getData(){

        db.collection("Post").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener { value, error ->

            if (error != null){
                Toast.makeText(requireContext(),error.localizedMessage,Toast.LENGTH_SHORT).show()
            }else{
                value?.let {

                    if (!value.isEmpty){
                        val documents = value.documents

                        postArrayList.clear()

                        for (document in documents){

                            //casting
                            val comment = document.get("comment") as String
                            val userEmail = document.get("userEmail") as String
                            val downloadUrl = document.get("downloadUri") as String

                            val post = Post(userEmail,comment,downloadUrl)

                            postArrayList.add(post)


                        }

                        flowFragmentRecyclerAdapter.notifyDataSetChanged()
                    }

                }
            }
        }
    }

}