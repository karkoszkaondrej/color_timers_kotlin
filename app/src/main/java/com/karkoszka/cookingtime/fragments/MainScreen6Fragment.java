package com.karkoszka.cookingtime.fragments;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karkoszka.cookingtime.R;

public class MainScreen6Fragment extends Fragment {

	private OnMainScreenFragmentInteractionListener mListener;


	public static MainScreen6Fragment newInstance() {
		MainScreen6Fragment fragment = new MainScreen6Fragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public MainScreen6Fragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_main_screen6, container, false);
	}

	public void onButtonPressed(int plate) {
		if (mListener != null) {
			mListener.onStartPressed(plate);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnMainScreenFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
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
	public interface OnMainScreenFragmentInteractionListener {
		public void onStartPressed(int plate);
		public void onSetPressed(int plate);
	}
	

}
