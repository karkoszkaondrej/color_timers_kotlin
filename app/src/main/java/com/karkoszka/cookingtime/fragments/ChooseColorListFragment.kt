package com.karkoszka.cookingtime.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.core.content.ContextCompat
import androidx.fragment.app.ListFragment
import com.karkoszka.cookingtime.R
import com.karkoszka.cookingtime.common.CTColor
import com.karkoszka.cookingtime.common.ChooseColorAdapter

class ChooseColorListFragment : ListFragment() {
    private var mListener: OnChooseColorFragmentInteractionListener? = null
    private lateinit var valuesDTO: Array<CTColor>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_color, container,
                false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val adapter = ChooseColorAdapter(activity!!, valuesFromXml)
        listAdapter = adapter
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        mListener!!.onColorChosen(valuesDTO[position].color)
    }

    @SuppressWarnings("deprecation")
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
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

    private val valuesFromXml: Array<CTColor>
        get() {
            val colorNames = activity!!.resources.getStringArray(R.array.colorNames)
            val ta = activity!!.resources.getIntArray(R.array.colors)
            valuesDTO = Array(colorNames.size) { CTColor()}
            for (i in colorNames.indices) {
                valuesDTO[i] = CTColor(ta[i], colorNames[i])
            }
            return valuesDTO
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