package com.example.recyclor

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.commit
import com.example.recyclor.databinding.FragmentLoginBinding
import com.example.recyclor.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignUp.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUp : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // viewbinding
    private lateinit var binding: FragmentSignUpBinding

    // tietokantarefrenssi on tässä
    val database = Firebase.database
    val myRef = database.reference
    private var enimi = ""
    private var snimi = ""
    private var puh = ""
    private var email = ""
    private var salasana = ""

    //firebaseauth
    private lateinit var auth: FirebaseAuth

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
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        val view = binding.root

        // alustetaan firebaseAuth
        auth = FirebaseAuth.getInstance()
        checkUser()

        // klikinkäsittely
        binding.buRekisteri.setOnClickListener {
            // varmistetaan syöttötiedot
            validateData()
        }
        binding.loginNowBtn.setOnClickListener {
            replaceFragment(Login())
        }

        return view
    }

    private fun validateData() {
        // vastaanotetaan syötetyt kirjautumistiedot
        enimi = binding.etENimi.text.toString()
        snimi = binding.etSNimi.text.toString()
        puh = binding.etPNro.text.toString()
        email = binding.etEmail.text.toString().trim()
        salasana = binding.etSalasana.text.toString()

        // varmennetaan ne
        if (TextUtils.isEmpty(enimi)){
            // etunimi puuttuu
            binding.etENimi.error = "Etunimi Puuttuu"
        }
        else if (TextUtils.isEmpty(snimi)){
            // sukunimi puuttuu
            binding.etSNimi.error = "Sukunimi Puuttuu"
        }
        else if (TextUtils.isEmpty(puh)){
            // puhelinnumero uupuu
            binding.etPNro.error = "Puhelinnumero Puuttuu"
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //vääränmuotoinen säpo
            binding.etEmail.error = "Tarkista Sähköpostiosoite"
        }
        else if (TextUtils.isEmpty(salasana)){
            // salasanaa ei syötetty
            binding.etSalasana.error = "Salasana puuttuu"
        }
        else if (salasana.length < 6){
            // salasanan minimipituus
            binding.etSalasana.error = "Salasanan on oltava vähintään 6"
        }
        else{
            // syöttö varmennettu
            myRef.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@SignUp.context, "Tietokantavirhe", Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild(puh)){
                        Toast.makeText(this@SignUp.context, "Puhelinnumero on jo rekisteröity", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        // sending data to firebase realtime database
                        // we are using phone number as unique identity of every user
                        // so all the other details of user comes under phone number
                        myRef.child("users").child(puh).child("enimi").setValue(enimi)
                        myRef.child("users").child(puh).child("snimi").setValue(snimi)
                        myRef.child("users").child(puh).child("email").setValue(email)
                        myRef.child("users").child(puh).child("salasana").setValue(salasana)

                        // show a success message at the end
                        Toast.makeText(this@SignUp.context, "Tallennettu järjestelmään", Toast.LENGTH_SHORT).show()

                        firebaseSignUp()
                    }
                }

            })
        }
    }


    private fun firebaseSignUp() {
        // luodaan tili
        auth.createUserWithEmailAndPassword(email, salasana)
            .addOnSuccessListener {
                // rekisteröinti onnistui
                val firebaseUser = auth.currentUser
                val email = firebaseUser!!.email

                Toast.makeText(this@SignUp.context, "tili luotu tunnuksella $email", Toast.LENGTH_SHORT).show()
                replaceFragment(MenuFragment())
            }
            .addOnFailureListener { e->
                // rekiteröinti epäonnistui
                Toast.makeText(this@SignUp.context, "rekisteröinti epäonnistui ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser() {
        // jos ollaan jo kirjauduttu sisälle niin ohjataan muualle.
        // haetaan käyttäjätiedot
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
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
         * @return A new instance of fragment SignUp.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignUp().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}