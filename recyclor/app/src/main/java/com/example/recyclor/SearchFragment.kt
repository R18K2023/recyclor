package com.example.recyclor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class SearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var wasteList: ArrayList<Waste>
    private lateinit var searchView: SearchView
    private lateinit var searchList: ArrayList<Waste>
    private lateinit var adapterClass: SearchResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupRecyclerView()
        getData()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                performSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                performSearch(newText)
                return false
            }
        })

        adapterClass.onItemClick = { data ->
            navigateToSearchResultFragment(data)
        }
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.rv_jätteet)
        searchView = view.findViewById(R.id.sv_hae)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        wasteList = arrayListOf<Waste>()
        searchList = arrayListOf<Waste>()
        adapterClass = SearchResultAdapter(searchList)
        recyclerView.adapter = adapterClass
    }

    private fun performSearch(searchText: String? = searchView.query.toString()) {
        searchList.clear()
        val normalizedSearchText = searchText?.lowercase(Locale.getDefault()) ?: ""
        if (normalizedSearchText.isNotEmpty()) {
            wasteList.forEach {
                if (it.wasteName.lowercase(Locale.getDefault()).contains(normalizedSearchText)) {
                    searchList.add(it)
                }
            }
        } else {
            searchList.addAll(wasteList)
        }
        recyclerView.adapter!!.notifyDataSetChanged()
    }

    private fun navigateToSearchResultFragment(data: Waste) {
        val bundle = Bundle().apply {
            putString("title", data.wasteName)
            putString("detail", data.wasteDetail)
            putString("where", data.wasteWhereTo)
        }

        val searchResultFragment = SearchResultFragment()
        searchResultFragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, searchResultFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun getData() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("waste")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                wasteList.clear()
                for (wasteSnapshot in dataSnapshot.children) {
                    val waste = wasteSnapshot.getValue(Waste::class.java)
                    if (waste != null) {
                        wasteList.add(waste)
                    }
                }
                searchList.addAll(wasteList)
                adapterClass.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "Error fetching data: ${databaseError.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}
