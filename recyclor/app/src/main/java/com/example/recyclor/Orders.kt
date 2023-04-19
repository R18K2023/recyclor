package com.example.recyclor

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class Orders : Fragment() {

    // tietokantarefrenssi on tässä
    private val database = Firebase.database
    private val myRef = database.reference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_orders, container, false)
        val buOrder = view.findViewById<Button>(R.id.buOrder)
        val typeEditText = view.findViewById<EditText>(R.id.Type)
        val sizeEditText = view.findViewById<EditText>(R.id.sizeTxt)
        val descEditText = view.findViewById<EditText>(R.id.Desc)
        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)

        buOrder.setOnClickListener {
            // Tallenna käyttäjän syöttämät tiedot Firebase-tietokantaan
            val type = typeEditText.text.toString()
            val size = sizeEditText.text.toString()
            val desc = descEditText.text.toString()
            val selectedDate = Date(calendarView.date)

            val data = hashMapOf(
                "type" to type,
                "size" to size,
                "description" to desc,
                "date" to selectedDate.toString()
            )

            myRef.child("orders").push().setValue(data)

            // Luo uusi intent "Tilaus vastaanotettu" -ilmoituksen näyttämiseksi
            val intent = Intent(activity, OrderReceived::class.java)

            // Käynnistä OrderReceived
            startActivity(intent)
        }

        return view
    }
}

