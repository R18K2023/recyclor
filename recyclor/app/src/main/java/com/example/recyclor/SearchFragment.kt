package com.example.recyclor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList


class SearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var wasteList: ArrayList<Waste>
    lateinit var wasteName: Array<String>
    private lateinit var searchView: SearchView
    private lateinit var searchBtn: Button
    private lateinit var searchList: ArrayList<Waste>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
            "aerosolipullo (hölskyy, pihisee)",
            "aerosolipullo (tyhjä; ei hölsky eikä pihise)",
            "akku (laitteen pieni akku)",
            "akku (ajoneuvon lyijyakku)",
            "akku (sähköpyörän tai -laudan)",
            "alumiinipinnoitettu paperi",
            "astiat (pieni määrä, alle 5 kg)",
            "astiat (suuri määrä, yli 5 kg)",
            "autonlasi",
            "betoni",
            "biojäte",
            "CD-levy",
            "energiansäästölamppu",
            "folio (paperi, vuoka)",
            "grilli (kaasu, hiili)",
            "grilli (sähkö)",
            "haitalliset vieraskasvilajit (esim. jättipalsami, jättiputki, komealupiini, kurtturuusu)",
            "halogeenilamppu",
            "hehkulamppu",
            "hiuslakkapullo (hölskyy, pihisee)",
            "hiuslakkapullo (ei hölsky eikä pihise)",
            "huonekalu tai sen osa (pieni, pisin mitta 40 cm)",
            "huonekalu (iso)"
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
    }

    private fun getData() {
        for (i in wasteName.indices) {
            val dataClass = Waste(wasteName[i])
            wasteList.add(dataClass)
        }
        searchList.addAll(wasteList)
        recyclerView.adapter = SearchResultAdapter(searchList)
    }

}