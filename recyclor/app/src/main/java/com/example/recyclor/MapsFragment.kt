package com.example.recyclor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recyclor.model.RecyclingPointsResponse
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL


class MapsFragment : Fragment() {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private suspend fun getRecyclingPoints(wasteCode: String?): RecyclingPointsResponse {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                gson()
            }
        }
        val apiBaseUrl =
            "http://api.kierratys.info/collectionspots/?api_key=83926c47ccaf5260ee9d2e597923190377aa7200&limit=265&municipality=Oulu"
        val urlBuilder = StringBuilder(apiBaseUrl)
        if (wasteCode != null) {
            urlBuilder.append("&material=$wasteCode")
        }
        val url = URL(urlBuilder.toString())
        return client.get(url).body()
    }


    private val callback = OnMapReadyCallback { googleMap ->
        coroutineScope.launch(Dispatchers.Main) {
            val recyclingPoints = getRecyclingPoints(arguments?.getString("code"))
            for (item in recyclingPoints.results.orEmpty()) {
                if (item != null) {
                    val pointLat = item.geometry?.coordinates?.get(1)
                    val pointLng = item.geometry?.coordinates?.get(0)
                    val pointName = item.name
                    val pointAddress = item.address
                    val pointPostalCode = item.postalCode
                    val pointPostOffice = item.postOffice
                    val pointTittle = ("$pointName, $pointAddress, $pointPostalCode $pointPostOffice")

                    if (pointLat != null) {
                        val pointCoordinates = LatLng(pointLat as Double, pointLng as Double)
                        googleMap.addMarker(
                            MarkerOptions().position(pointCoordinates).title(pointTittle)
                        )
                    }
                }
            }
            val oulu = LatLng(65.01, 25.46)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(oulu, 12f))
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