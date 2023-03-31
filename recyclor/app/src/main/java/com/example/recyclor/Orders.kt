package com.example.recyclor

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class Orders : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_orders, container, false)
        val buOrder = view.findViewById<Button>(R.id.buOrder)

        buOrder.setOnClickListener {
            // Luo uusi intent "Tilaus vastaanotettu" -ilmoituksen näyttämiseksi
            val intent = Intent(activity, OrderReceived::class.java)

            // Käynnistä OrderReceived
            startActivity(intent)
        }

        return view
    }
}

