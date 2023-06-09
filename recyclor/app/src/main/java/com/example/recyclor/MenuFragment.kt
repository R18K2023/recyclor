package com.example.recyclor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.commit
import com.example.recyclor.databinding.FragmentMenuBinding
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //firebaseAuth
    private lateinit var auth: FirebaseAuth

    // viewbinding
    private lateinit var binding: FragmentMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.buLajittelu.setOnClickListener { replaceFragment(SearchFragment()) }
        binding.buNouto.setOnClickListener { replaceFragment(Orders()) }
        binding.buAsetukset.setOnClickListener { replaceFragment(SettingsFragment()) }
        binding.buKartta.setOnClickListener { replaceFragment(MapsFragment()) }

        // alustetaan firebaseAuth
        auth = FirebaseAuth.getInstance()
        checkUser()

        return view
    }

    private fun checkUser() {
        // jos ollaan jo kirjauduttu sisälle niin ohjataan muualle.
        // haetaan käyttäjätiedot
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
            // ollaan jo kirjauduttu sisään -> näytetään sähköpostiosoite ruudulla textviewissä
            val email = firebaseUser.email
            binding.tvEmail.text = email

        }

    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = parentFragmentManager
        fragmentManager.commit {
            replace(R.id.frame_layout, fragment)
            addToBackStack(null)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MenuFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MenuFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}