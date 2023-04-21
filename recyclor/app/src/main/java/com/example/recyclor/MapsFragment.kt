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
import io.ktor.http.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MapsFragment : Fragment() {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private suspend fun getRecyclingPoints(wasteCode: String?): RecyclingPointsResponse {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                gson()
            }
        }
        // laita oma api key tohon alempaan kohtaan vietiin se poijes
        val recyclingPoints = client.get("http://api.kierratys.info/") {
            url {
                appendPathSegments("collectionspots")
                parameters.append("api_key", "LAITA TÄHÄN OMA API KEY")
                parameters.append("limit", "265")
                parameters.append("municipality", "Oulu")
                if(wasteCode != null){
                    parameters.append("material", wasteCode)
                }
            }
        }

        return recyclingPoints.body()
    }


    private val callback = OnMapReadyCallback { googleMap ->
        coroutineScope.launch(Dispatchers.Main) {
            val oulu = LatLng(65.01, 25.46)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(oulu, 9f))
            val recyclingPoints = getRecyclingPoints(arguments?.getString("code"))
            for (item in recyclingPoints.results.orEmpty()) {
                if (item != null) {
                    val pointLat = item.geometry?.coordinates?.get(1)
                    val pointLng = item.geometry?.coordinates?.get(0)
                    val pointName = item.name
                    val pointAddress = item.address
                    val pointPostalCode = item.postalCode
                    val pointPostOffice = item.postOffice
                    val pointTittle = ("$pointAddress, $pointPostalCode $pointPostOffice")

                    if (pointLat != null) {
                        val pointCoordinates = LatLng(pointLat as Double, pointLng as Double)
                        googleMap.addMarker(
                            MarkerOptions().position(pointCoordinates).title(pointName).snippet(pointTittle)
                        )
                    }
                }
            }
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
