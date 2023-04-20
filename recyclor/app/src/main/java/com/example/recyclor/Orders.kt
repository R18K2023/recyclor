package com.example.recyclor

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.commit
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_orders, container, false)
        val buOrder = view.findViewById<Button>(R.id.buOrder)
        val typeEditText = view.findViewById<EditText>(R.id.Type)
        val sizeEditText = view.findViewById<EditText>(R.id.sizeTxt)
        val descEditText = view.findViewById<EditText>(R.id.Desc)
        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)

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
                        buOrder.setOnClickListener {
                            val type = typeEditText.text.toString()
                            val size = sizeEditText.text.toString()
                            val desc = descEditText.text.toString()
                            val selectedDate = Date(calendarView.date)

                            val data = hashMapOf(
                                "type" to type,
                                "size" to size,
                                "description" to desc,
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
                    // Esim. virheilmoituksen näyttäminen käyttäjälle
                }
            })

            return view
        }

    private fun checkUser() {
        // jos ei olla kirjauduttu sisälle niin ohjataan muualle.
        // haetaan käyttäjätiedot
        val firebaseUser = auth.currentUser
        if (firebaseUser != null){
            // ollaan kirjauduttu sisään -> pysytään sivulla
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

