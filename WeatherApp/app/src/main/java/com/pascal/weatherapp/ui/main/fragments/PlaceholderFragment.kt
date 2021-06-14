package com.pascal.weatherapp.ui.main.fragments
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//
//class PlaceholderFragment : Fragment() {
//
//    private lateinit var pageViewModel: PageViewModel
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
//            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
//        }
//    }
//
//    companion object {
//
//        private const val ARG_SECTION_NUMBER = "section_number"
//
//        @JvmStatic
//        fun newInstance(sectionNumber: Int): PlaceholderFragment {
//            return PlaceholderFragment().apply {
//                arguments = Bundle().apply {
//                    putInt(ARG_SECTION_NUMBER, sectionNumber)
//                }
//            }
//        }
//    }
//}