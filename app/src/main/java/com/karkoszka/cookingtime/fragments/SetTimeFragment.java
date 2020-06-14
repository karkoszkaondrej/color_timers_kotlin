package com.karkoszka.cookingtime.fragments;

import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karkoszka.cookingtime.R;

/**
 * A simple {@link Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link SetTimeFragment.OnSetTimeFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link SetTimeFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class SetTimeFragment extends Fragment {
	private static final String PLATE = "plate";
	private static final String COLOR = "color";



	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment SetTimeFragment.
	 */
	public static SetTimeFragment newInstance(String param1, String param2) {
		SetTimeFragment fragment = new SetTimeFragment();
		Bundle args = new Bundle();
		args.putString(PLATE, param1);
		args.putString(COLOR, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public SetTimeFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_set_time, container, false);
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
	public interface OnSetTimeFragmentInteractionListener {
		public void onFragmentInteraction(Uri uri);
	}

}
