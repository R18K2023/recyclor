package com.example.recyclor

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.commit
import com.example.recyclor.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Login : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // viewbinding
    private lateinit var binding:FragmentLoginBinding

    // tietokantarefrenssi on tässä
    val database = Firebase.database
    val myRef = database.reference

    //firebaseAuth
    private lateinit var auth: FirebaseAuth
    private var email = ""
    private var salasana = ""

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
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        // alustetaan firebaseAuth
        auth = FirebaseAuth.getInstance()
        checkUser()



        // klikkikäsittely
        binding.buKirjaudu.setOnClickListener {
            // datan varmennus ennen kirjautumista
            validateData()
        }

        binding.registerNowBtn.setOnClickListener {
            replaceFragment(SignUp())
        }

        return view
    }

    private fun validateData() {
        // vastaanotetaan syötetyt kirjautumistiedot
        email = binding.etEmail.text.toString().trim()
        salasana = binding.etSalasana.text.toString()

        // varmennetaan ne
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //vääränmuotoinen säpo
            binding.etEmail.error = "Tarkista Sähköposti"
        }
        else if (TextUtils.isEmpty(salasana)){
            // salasanaa ei syötetty
            binding.etSalasana.error = "Syötä Salasana"
        }
        else{
            // syöttö varmennettu
            firebaseLogin()
        }

    }

    private fun firebaseLogin() {
        auth.signInWithEmailAndPassword(email,salasana)
            .addOnSuccessListener {
                // kirjautuminen onnistui
                val firebaseUser = auth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this@Login.context, "Kirjauduttu tunnuksella $email", Toast.LENGTH_SHORT).show()

                // avataan noutotilaussivu
                replaceFragment(MenuFragment())
            }
            .addOnFailureListener {e->
                // kirjautuminen epäonnistui
                Toast.makeText(this@Login.context, "kirjautuminen epäonnistui ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser() {
        // jos ollaan jo kirjauduttu sisälle niin ohjataan muualle.
        // haetaan käyttäjätiedot
        val firebaseUser = auth.currentUser
        if (firebaseUser != null){
            // ollaan jo kirjauduttu sisään -> navigoidaan muualle
            replaceFragment(MenuFragment())
        }


    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = parentFragmentManager
        fragmentManager.commit {
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
         * @return A new instance of fragment MenuFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Login().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}