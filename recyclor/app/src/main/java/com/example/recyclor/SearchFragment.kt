package com.example.recyclor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class SearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var wasteList: ArrayList<Waste>
    lateinit var wasteName: Array<String>
    lateinit var wasteDetail: Array<String>
    lateinit var wasteWhereTo: Array<String>
    private lateinit var searchView: SearchView
    private lateinit var searchBtn: Button
    private lateinit var searchList: ArrayList<Waste>
    private lateinit var adapterClass: SearchResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rv_jätteet)
        searchView = view.findViewById(R.id.sv_hae)
        searchBtn = view.findViewById(R.id.btn_hae)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        wasteList = arrayListOf<Waste>()
        searchList = arrayListOf<Waste>()

        wasteName = arrayOf(
            "Aerosolipullo (hölskyy, pihisee)",
            "Aerosolipullo (tyhjä; ei hölsky eikä pihise)",
            "Akku (laitteen pieni akku)",
            "Akku (ajoneuvon lyijyakku)",
            "Akku (sähköpyörän tai -laudan)",
            "Alumiinipinnoitettu paperi",
            "Astiat (pieni määrä, alle 5 kg)",
            "Astiat (suuri määrä, yli 5 kg)",
            "Autonlasi",
            "Betoni",
            "Biojäte",
            "CD-levy",
            "Energiansäästölamppu",
            "Folio (paperi, vuoka)",
            "Grilli (kaasu, hiili)",
            "Grilli (sähkö)",
            "Haitalliset vieraskasvilajit (esim. jättipalsami, jättiputki, komealupiini, kurtturuusu)",
            "Halogeenilamppu",
            "Hehkulamppu",
            "Hiuslakkapullo (hölskyy, pihisee)",
            "Hiuslakkapullo (ei hölsky eikä pihise)",
            "Huonekalu tai sen osa (pieni, pisin mitta 40 cm)",
            "Huonekalu (iso)"
        )

        wasteDetail = arrayOf(
            "Vaarallinen jäte",
            "Metalli",
            "Vaarallinen jäte",
            "Vaarallinen jäte",
            "Vaarallinen jäte",
            "Sekajäte",
            "Sekajäte",
            "Sekajäte",
            "Lasijäte (tai sekajäte)",
            "Betoni-, tiili- ja laattajäte",
            "Biojäte",
            "Sekajäte",
            "Vaarallinen jäte",
            "Metalli",
            "Metalli",
            "Sähkölaitekeräys",
            "Sekajäte",
            "Sekajäte",
            "Sekajäte",
            "Vaarallinen jäte",
            "Metalli",
            "Sekajäte",
            "Sekajäte"
        )

        wasteWhereTo = arrayOf(
            "Oivapiste, jäteasema tai vaarallisen jätteen vastaanottopiste",
            "Kiinteistön metallinkeräysastia, Rinkiekopiste, Oivapiste tai jäteasema",
            "Kaupan paristokeräys, Oivapiste, jäteasema",
            "Myyjäliike, Oivapiste, jäteasema",
            "Myyjäliike, Oivapiste, jäteasema",
            "null",
            "null",
            "Ruskon jätekeskus tai jäteasema",
            "Ruskon jätekeskus tai jäteasema",
            "Kierrätyspiha Kirsille tai rakennusjätteen joukossa Lajitteluareena Larelle Ruskon jätekeskukseen",
            "Pakkaa tavalliseen muovipussiin, paperipussiin tai sanomalehteen, tai jos pihan biojäteastiassa on suojasäkki, laita biojäteastiaan ilman käärettä tai pussia",
            "null",
            "Kaupat, Oivapiste, jäteasema tai vaarallisen jätteen vastaanottopiste",
            "Kiinteistön metallinkeräysastia, Rinki-ekopiste, Oivapiste tai jäteasema",
            "Oivapiste tai jäteasema",
            "Jäteasema tai Oivapiste",
            "null",
            "null",
            "null",
            "Oivapiste, jäteasema tai vaarallisen jätteen vastaanottopiste",
            "Kiinteistön metallinkeräysastia, jäteaseman metallinkeräys, Rinkiekopisteen metallinkeräys",
            "null",
            "Ruskon jätekeskus tai jäteasema"
        )

        getData()

        searchBtn.setOnClickListener {
            val search = searchView.query.toString()
            searchList.clear()
            val searchText = search.lowercase(Locale.getDefault())
            if (searchText.isNotEmpty()) {
                wasteList.forEach {
                    if (it.wasteName.lowercase(Locale.getDefault()).contains(searchText)) {
                        searchList.add(it)
                    }
                }
                recyclerView.adapter!!.notifyDataSetChanged()
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                val searchText = query?.lowercase(Locale.getDefault()) ?: ""
                if (searchText.isNotEmpty()) {
                    wasteList.forEach {
                        if (it.wasteName.lowercase(Locale.getDefault()).contains(searchText)) {
                            searchList.add(it)
                        }
                    }
                } else {
                    searchList.addAll(wasteList)
                }
                recyclerView.adapter!!.notifyDataSetChanged()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isEmpty()) {
                    searchList.clear()
                    searchList.addAll(wasteList)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
                return false
            }

        })

        adapterClass = SearchResultAdapter(searchList)
        recyclerView.adapter = adapterClass
        adapterClass.onItemClick = { data ->
            val bundle = Bundle().apply {
                putString("title", data.wasteName)
                putString("detail", data.wasteDetail)
                putString("where", data.wasteWhereTo)
            }

            val searchResultFragment= SearchResultFragment()
            searchResultFragment.arguments = bundle

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, searchResultFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun getData() {
        for (i in wasteName.indices) {
            val dataClass = Waste(wasteName[i], wasteDetail[i], wasteWhereTo[i])
            wasteList.add(dataClass)
        }
        searchList.addAll(wasteList)
        recyclerView.adapter = SearchResultAdapter(searchList)
    }

}