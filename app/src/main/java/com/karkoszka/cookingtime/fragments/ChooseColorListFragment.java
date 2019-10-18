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
import com.karkoszka.cookingtime.common.XmlColorAdapter;

public class ChooseColorListFragment extends ListFragment {

	private OnChooseColorFragmentInteractionListener mListener;
    private CTColor[] values;
    private CTColor[] valuesOriginal;

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
		values = getValuesFromXml();
		//values = getColors();
		ChooseColorAdapter adapter = new ChooseColorAdapter(getActivity(), values);
		//XmlColorAdapter adapter = new XmlColorAdapter(getActivity());
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

	private CTColor[] getColors() {
		return new CTColor[] {new CTColor(0,"Default", getActivity().getResources().getColor(R.color.back_black))
				, new CTColor(getActivity().getResources().getColor(R.color.yellow),"Yellow", getActivity().getResources().getColor(R.color.back_black))
				, new CTColor(getActivity().getResources().getColor(R.color.fuchsia),"fuchsia", getActivity().getResources().getColor(R.color.back_black))
				, new CTColor(getActivity().getResources().getColor(R.color.red),"red", getActivity().getResources().getColor(R.color.back_black))
				, new CTColor(getActivity().getResources().getColor(R.color.silver),"silver", getActivity().getResources().getColor(R.color.back_black))
				, new CTColor(getActivity().getResources().getColor(R.color.gray),"gray", getActivity().getResources().getColor(R.color.back_white))
				, new CTColor(getActivity().getResources().getColor(R.color.olive),"olive", getActivity().getResources().getColor(R.color.back_white))
				, new CTColor(getActivity().getResources().getColor(R.color.purple),"purple", getActivity().getResources().getColor(R.color.back_white))
				, new CTColor(getActivity().getResources().getColor(R.color.maroon),"maroon", getActivity().getResources().getColor(R.color.back_white))
				, new CTColor(getActivity().getResources().getColor(R.color.aqua),"aqua", getActivity().getResources().getColor(R.color.back_black))
				, new CTColor(getActivity().getResources().getColor(R.color.lime),"lime", getActivity().getResources().getColor(R.color.back_black))
				, new CTColor(getActivity().getResources().getColor(R.color.teal),"teal", getActivity().getResources().getColor(R.color.back_white))
				, new CTColor(getActivity().getResources().getColor(R.color.green),"green", getActivity().getResources().getColor(R.color.back_white))
				, new CTColor(getActivity().getResources().getColor(R.color.blue),"blue", getActivity().getResources().getColor(R.color.back_black))
				, new CTColor(getActivity().getResources().getColor(R.color.navy),"navy", getActivity().getResources().getColor(R.color.back_white))};
	}

	private CTColor[] getValuesFromXml() {
		String[] colorNames = getActivity().getResources().getStringArray(R.array.colorNames);
		CTColor[] values = new CTColor[colorNames.length];
		for(int i=0; i<colorNames.length; i++)
		{
			//Getting the color resource id
			TypedArray ta = getActivity().getResources().obtainTypedArray(R.array.colors);
			int colorToUse = ta.getResourceId(i, 0);

			values[i] = new CTColor(colorToUse,colorNames[i], getActivity().getResources().getColor(R.color.back_black));
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
