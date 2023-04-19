package com.example.recyclor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.*

class OrderReceived : AppCompatActivity() {

    private lateinit var orderReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_received)

        // Alusta Firebase-database
        orderReference = FirebaseDatabase.getInstance().getReference("orders")

        val buttonBack = findViewById<Button>(R.id.buReturn)
        buttonBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Hae viimeisin tilaus tietokannasta
        orderReference.orderByKey().limitToLast(1).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children) {
                    // Näytä viimeisin tilaus tiedoilla OrderReceived-aktiviteetissa
                    val type = orderSnapshot.child("type").value.toString()
                    val size = orderSnapshot.child("size").value.toString()
                    val desc = orderSnapshot.child("description").value.toString()
                    val date = orderSnapshot.child("date").value.toString()

                    val textViewOrder = findViewById<TextView>(R.id.textViewOrder)
                    textViewOrder.text = "Tilauksen tiedot:\nTyyppi: $type\nKoko: $size\nKuvaus: $desc\nPäivämäärä: $date"
                }
            }


            override fun onCancelled(error: DatabaseError) {
                // Käsittely virhetilanteessa
                // Esim. virheilmoituksen näyttäminen käyttäjälle
            }
        })
    }
}
