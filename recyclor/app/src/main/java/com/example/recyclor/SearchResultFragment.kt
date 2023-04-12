package com.example.recyclor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class SearchResultFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_result, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val wasteNameTV=view.findViewById<TextView>(R.id.tv_jätteenNimi)
        val wasteDetailTV=view.findViewById<TextView>(R.id.tv_lajitteluohje)
        val wasteWhereToTV=view.findViewById<TextView>(R.id.tv_minneOhjeet)

        val wasteName =arguments?.getString("title")
        val wasteDetail = arguments?.getString("detail")
        val wasteWhereTo=arguments?.getString("where")

        wasteNameTV.text=wasteName
        wasteDetailTV.text=wasteDetail
        wasteWhereToTV.text=wasteWhereTo
    }
}