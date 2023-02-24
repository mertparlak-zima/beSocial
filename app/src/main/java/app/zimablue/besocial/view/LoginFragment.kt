package app.zimablue.besocial.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import app.zimablue.besocial.R
import app.zimablue.besocial.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.signinButton.setOnClickListener {
            signInClicked()
        }

        binding.signUpbutton.setOnClickListener {
            signUpClicked()
        }

        val currentUser = auth.currentUser

        if (currentUser != null){
            val action = LoginFragmentDirections.actionLoginFragmentToFlowFragment()
            Navigation.findNavController(requireActivity(),R.id.MainActivityFragmentContainerView).navigate(action)
        }


        super.onViewCreated(view, savedInstanceState)
    }


    private fun signInClicked(){

        val email = binding.mailText.text.toString()

        val password = binding.passwordText.text.toString()

        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(requireContext(),"Enter email and Password!", Toast.LENGTH_LONG).show()
        }else{
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                val action = LoginFragmentDirections.actionLoginFragmentToFlowFragment()
                Navigation.findNavController(requireActivity(),R.id.MainActivityFragmentContainerView).navigate(action)
            }.addOnFailureListener {
                Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun signUpClicked(){

        val email = binding.mailText.text.toString()

        val password = binding.passwordText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()){

            if (email.isEmpty() || email.isEmpty()){
                Toast.makeText(activity,"Enter email and password!",Toast.LENGTH_LONG).show()
            }else{
                auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                    val action = LoginFragmentDirections.actionLoginFragmentToFlowFragment()
                    Navigation.findNavController(requireActivity(),R.id.MainActivityFragmentContainerView).navigate(action)
                }.addOnFailureListener {
                    Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
                }
            }
        }

    }

}