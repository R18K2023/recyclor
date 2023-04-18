package com.example.recyclor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL
import com.example.recyclor.model.RecyclingPointsResponse


class MapsFragment : Fragment() {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private suspend fun getRecyclingPoints(): RecyclingPointsResponse {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                gson()
            }
        }
        val url = URL("http://api.kierratys.info/collectionspots/?api_key=83926c47ccaf5260ee9d2e597923190377aa7200&limit=265&municipality=Oulu")

        return client.get(url).body()
    }


    private val callback = OnMapReadyCallback { googleMap ->
       coroutineScope.launch(Dispatchers.Main) {
            val recyclingPoints =  getRecyclingPoints()
           for (item in recyclingPoints.results.orEmpty()) {
               if (item != null) {
                   val pointLat = item.geometry?.coordinates?.get(1)
                   val pointLng = item.geometry?.coordinates?.get(0)
                   val pointName = item.name
                   if (pointLat != null) {
                       val pointCoordinates = LatLng(pointLat as Double, pointLng as Double)
                       googleMap.addMarker(
                           MarkerOptions().position(pointCoordinates).title(pointName)
                       )
                   }
               }
           }
           val oulu = LatLng(65.01, 25.46)
           googleMap.moveCamera(CameraUpdateFactory.newLatLng(oulu))
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}