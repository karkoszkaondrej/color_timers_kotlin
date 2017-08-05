package com.karkoszka.cookingtime.fragments;

import com.karkoszka.cookingtime.R;
import com.karkoszka.cookingtime.common.CTColor;
import com.karkoszka.cookingtime.common.ChooseColorAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ChooseColorListFragment extends ListFragment {

	private OnChooseColorFragmentInteractionListener mListener;
	private CTColor[] values;

	public ChooseColorListFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

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
		values = getColors();
		ChooseColorAdapter adapter = new ChooseColorAdapter(getActivity(), values);
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
		CTColor[] colors =  {new CTColor(0,"Default")
		, new CTColor(getActivity().getResources().getColor(R.color.yellow),"Yellow")
		, new CTColor(getActivity().getResources().getColor(R.color.fuchsia),"fuchsia")
		, new CTColor(getActivity().getResources().getColor(R.color.red),"red")
		, new CTColor(getActivity().getResources().getColor(R.color.silver),"silver")
		, new CTColor(getActivity().getResources().getColor(R.color.gray),"gray")
		, new CTColor(getActivity().getResources().getColor(R.color.olive),"olive")
		, new CTColor(getActivity().getResources().getColor(R.color.purple),"purple")
		, new CTColor(getActivity().getResources().getColor(R.color.maroon),"maroon")
		, new CTColor(getActivity().getResources().getColor(R.color.aqua),"aqua")
		, new CTColor(getActivity().getResources().getColor(R.color.lime),"lime")
		, new CTColor(getActivity().getResources().getColor(R.color.teal),"teal")
		, new CTColor(getActivity().getResources().getColor(R.color.green),"green")
		, new CTColor(getActivity().getResources().getColor(R.color.blue),"blue")
		, new CTColor(getActivity().getResources().getColor(R.color.navy),"navy")};
		return colors;
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
