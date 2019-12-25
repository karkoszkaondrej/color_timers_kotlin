package com.karkoszka.cookingtime.fragments;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.karkoszka.cookingtime.R;
import com.karkoszka.cookingtime.common.CTColor;
import com.karkoszka.cookingtime.common.ChooseColorAdapter;

public class ChooseColorListFragment extends ListFragment {

	private OnChooseColorFragmentInteractionListener mListener;
    private CTColor[] values;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_choose_color, container,
				false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ChooseColorAdapter adapter = new ChooseColorAdapter(getActivity(), getValuesFromXml());
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		mListener.onColorChosen(values[position].getColor());
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnChooseColorFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnChooseColorFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	private CTColor[] getValuesFromXml() {
		String[] colorNames = getActivity().getResources().getStringArray(R.array.colorNames);
		values = new CTColor[colorNames.length];
		int[] ta = getActivity().getResources().getIntArray(R.array.colors);
		for(int i=0; i<colorNames.length; i++)
		{
			values[i] = new CTColor(ta[i], colorNames[i], getActivity().getResources().getColor(R.color.Black));
		}

		return values;
	}
	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnChooseColorFragmentInteractionListener {
		public void onColorChosen(int color);
	}
}
