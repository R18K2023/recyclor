package com.example.recyclor

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.example.recyclor.databinding.FragmentMenuBinding
import com.example.recyclor.databinding.FragmentSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val viewModel: MainViewModel by activityViewModels()

    //firebaseAuth
    private lateinit var auth: FirebaseAuth

    // tietokantarefrenssi on tässä
    val database = Firebase.database
    val myRef = database.reference

    // viewbinding
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root
        // alustetaan firebaseAuth
        auth = FirebaseAuth.getInstance()
        checkUser()
        //napit ja värkit
        binding.buLogin.setOnClickListener { replaceFragment(Login()) }
        binding.buRegister.setOnClickListener { replaceFragment(SignUp()) }
        binding.switchNightLight.setOnCheckedChangeListener { _, b ->
            when (b) {
                true ->  {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    viewModel.setTheme(true)
                }

                false -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    viewModel.setTheme(false)
                }
            }
        }
        val isNightModeOn: Boolean = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        binding.switchNightLight.isChecked = isNightModeOn

        binding.buDelete.setOnClickListener { fireBaseDelete() }

        return view
    }

    private fun fireBaseDelete() {
        val user = auth.currentUser!!
        myRef.child("users").orderByChild("email").equalTo(user.email).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children){
                        userSnapshot.ref.removeValue()
                    }
                    user.delete()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this@SettingsFragment.context, "Käyttäjätunnus poistettu.", Toast.LENGTH_SHORT).show()
                                replaceFragment(MenuFragment())
                            }
                        }


                } else {
                    Toast.makeText(
                        activity,
                        "Käyttäjän tietoja ei löytynyt",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Käsittely virhetilanteessa
                Toast.makeText(this@SettingsFragment.context, "Tietokantavirhe", Toast.LENGTH_SHORT).show()
            }
        })
        checkUser()
    }

    private fun checkUser() {
        // jos ollaan jo kirjauduttu sisälle niin ohjataan muualle.
        // haetaan käyttäjätiedot
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
            // ollaan jo kirjauduttu sisään -> näytetään sähköpostiosoite ruudulla textviewissä
            val email = firebaseUser.email
            binding.tvEmail.text = email
            binding.buLogin.visibility = View.INVISIBLE
            binding.buRegister.visibility = View.INVISIBLE
        }
        else{
            // käyttäjä ei oo kirjautunu sisälle lisää tähän jotaki...
            binding.buDelete.visibility = View.INVISIBLE

        }
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.commit {
            replace(R.id.frame_layout, fragment)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}