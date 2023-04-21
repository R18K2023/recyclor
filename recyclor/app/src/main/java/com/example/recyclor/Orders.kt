package com.example.recyclor

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.commit
import com.example.recyclor.databinding.ActivityOrdersBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.auth.FirebaseUser
import java.util.*

class Orders : Fragment() {

    // firebase auth ja tietokanta
    private lateinit var auth: FirebaseAuth
    private lateinit var userReference: DatabaseReference
    val database = Firebase.database
    val myRef = database.reference

    // viewbinding
    private lateinit var binding: ActivityOrdersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityOrdersBinding.inflate(inflater, container, false)
        val view = binding.root


        // alustetaan firebaseAuth
        auth = FirebaseAuth.getInstance()
        checkUser()

        // Määritä viite tietokantaan
        val database = Firebase.database

        val currentUser: FirebaseUser? = auth.currentUser
        val user = currentUser?.email


        val userRef = FirebaseDatabase.getInstance().getReference("users")
        val userQuery = userRef.orderByChild("email").equalTo(user)

        userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val enimi = userSnapshot.child("enimi").value.toString()
                        val snimi = userSnapshot.child("snimi").value.toString()
                        val email = userSnapshot.child("email").value.toString()


                        // Tallenna käyttäjän syöttämät tiedot Firebase-tietokantaan yhdessä käyttäjän tietojen kanssa
                        binding.buOrder.setOnClickListener {
                            val selectedDate = Date(binding.calendarView.date)

                            val data = hashMapOf(
                                "type" to binding.Type.text.toString(),
                                "size" to binding.sizeTxt.text.toString(),
                                "description" to binding.Desc.text.toString(),
                                "date" to selectedDate.toString(),
                                "enimi" to enimi,
                                "snimi" to snimi,
                                "email" to email
                            )

                            val database = Firebase.database
                            val myRef = database.reference

                            myRef.child("orders").push().setValue(data)

                            // Luo uusi intent "Tilaus vastaanotettu" -ilmoituksen näyttämiseksi
                            val intent = Intent(activity, OrderReceived::class.java)

                            // Käynnistä OrderReceived
                            startActivity(intent)
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
                    Toast.makeText(this@Orders.context, "Tietokantavirhe", Toast.LENGTH_SHORT).show()
                }
            })

            return view
        }

    private fun checkUser() {
        // jos ei olla kirjauduttu sisälle niin ohjataan muualle.
        // haetaan käyttäjätiedot
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
            // ollaan jo kirjauduttu sisään -> näytetään sähköpostiosoite ruudulla textviewissä
            val email = firebaseUser.email
            binding.tvEmail.text = email
        }
        else{
            // jos ei olla kirjauduttu sisälle, navigoidaan loginiin
            replaceFragment(Login())
        }


    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = parentFragmentManager
        fragmentManager.commit {
            replace(R.id.frame_layout, fragment)
        }
    }
}

