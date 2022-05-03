package com.karkoszka.cookingtime.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.fragment.app.ListFragment
import androidx.lifecycle.LifecycleObserver
import com.karkoszka.cookingtime.R
import com.karkoszka.cookingtime.common.CTColor
import com.karkoszka.cookingtime.common.ChooseColorAdapter

class ChooseColorListFragment : ListFragment(), LifecycleObserver {
    private var mListener: OnChooseColorFragmentInteractionListener? = null

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)
        val adapter = ChooseColorAdapter(requireActivity(), valuesFromXml)
        listAdapter = adapter
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        mListener!!.onColorChosen(valuesFromXml[position]!!.color)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = try {
            activity as OnChooseColorFragmentInteractionListener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString()
                    + " must implement OnChooseColorFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    private val valuesFromXml: Array<CTColor?>
        get() {
            return CTColor.createArray(requireActivity().resources.getIntArray(R.array.colors))
        }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated to
     * the activity and potentially other fragments contained in that activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnChooseColorFragmentInteractionListener {
        fun onColorChosen(color: Int)
    }
}