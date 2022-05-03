package com.karkoszka.cookingtime.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.karkoszka.cookingtime.R

class MainScreen6Fragment : Fragment() {
    private var mListener: OnMainScreenFragmentInteractionListener? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_screen6, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = try {
            activity as OnMainScreenFragmentInteractionListener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated to
     * the activity and potentially other fragments contained in that activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnMainScreenFragmentInteractionListener {
        fun onStartPressed(plate: Int)
        fun onSetPressed(plate: Int)
    }
}