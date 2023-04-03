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

        wasteDetail = arrayOf(
            getString(R.string.aerosolipullo),
            getString(R.string.aerosolipullo_tyhja),
            getString(R.string.akku_laitteen_pieni),
            getString(R.string.akku_lyijyakku),
            getString(R.string.akku_sahkopyoran),
            getString(R.string.alumiinipinnoitettu_paperi),
            getString(R.string.astiat_pieni),
            getString(R.string.astiat_suuri),
            getString(R.string.autonlasi),
            getString(R.string.betoni),
            getString(R.string.biojate),
            getString(R.string.cdlevy),
            getString(R.string.energiansaastolamppu),
            getString(R.string.folio),
            getString(R.string.grilli_kaasu_hiili),
            getString(R.string.grilli_sahko),
            getString(R.string.haitalliset_vieraskasvit),
            getString(R.string.halogeenilamppu),
            getString(R.string.hehkulamppu),
            getString(R.string.hiuslakkapullo),
            getString(R.string.hiuslakkapullo_tyhja),
            getString(R.string.huonekalu_pieni),
            getString(R.string.huonekalu_iso)
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
            val dataClass = Waste(wasteName[i], wasteDetail[i])
            wasteList.add(dataClass)
        }
        searchList.addAll(wasteList)
        recyclerView.adapter = SearchResultAdapter(searchList)
    }

}