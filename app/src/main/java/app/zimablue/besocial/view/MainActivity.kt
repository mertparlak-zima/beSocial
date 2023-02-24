package app.zimablue.besocial.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import app.zimablue.besocial.R
import app.zimablue.besocial.databinding.ActivityMainBinding
import app.zimablue.besocial.databinding.FragmentFlowBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        auth = Firebase.auth


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.MainActivityFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this,navController)


    }


    private fun signInClicked(view: View){

    }

    private fun signUpClicked(view: View){


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_list,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.postShare){
            val action = FlowFragmentDirections.actionFlowFragmentToPostShareFragment()
            Navigation.findNavController(this,R.id.MainActivityFragmentContainerView).navigate(action)
        }

        if (item.itemId == R.id.logout){
            auth.signOut()
            val action = FlowFragmentDirections.actionFlowFragmentToLoginFragment()
            Navigation.findNavController(this,R.id.MainActivityFragmentContainerView).navigate(action)
        }



        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {

        val navController = this.findNavController(R.id.MainActivityFragmentContainerView)

        return navController.navigateUp()
    }
}