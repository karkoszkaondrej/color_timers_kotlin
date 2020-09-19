package com.karkoszka.cookingtime.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.karkoszka.cookingtime.R

/**
 * A simple [Fragment] subclass. Activities that
 * contain this fragment must implement the
 * [SetTimeFragment.OnSetTimeFragmentInteractionListener] interface to handle
 * interaction events. Use the [SetTimeFragment.newInstance] factory
 * method to create an instance of this fragment.
 *
 */
class SetTimeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_time, container, false)
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated to
     * the activity and potentially other fragments contained in that activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnSetTimeFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri?)
    }

    companion object {
        private const val PLATE = "plate"
        private const val COLOR = "color"

        /**
         * Use this factory method to create a new instance of this fragment using
         * the provided parameters.
         *
         * @param param1
         * Parameter 1.
         * @param param2
         * Parameter 2.
         * @return A new instance of fragment SetTimeFragment.
         */
        fun newInstance(param1: String?, param2: String?): SetTimeFragment {
            val fragment = SetTimeFragment()
            val args = Bundle()
            args.putString(PLATE, param1)
            args.putString(COLOR, param2)
            fragment.arguments = args
            return fragment
        }
    }
}