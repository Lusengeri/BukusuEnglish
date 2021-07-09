package com.example.newdictionary;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newdictionary.fastscroll.FastScrollRecyclerViewItemDecoration;
import com.example.newdictionary.fastscroll.FastScrollerRecyclerView;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class WordListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private Cursor wordCursor;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WordListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static WordListFragment newInstance(int columnCount) {
        WordListFragment fragment = new WordListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word_list, container, false);
        // Set the adapter

        FastScrollerRecyclerView recyclerView = (FastScrollerRecyclerView) view.findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new MyWordRecyclerViewAdapter(wordCursor, mListener));
        FastScrollRecyclerViewItemDecoration decoration = new FastScrollRecyclerViewItemDecoration(view.getContext());
        recyclerView.addItemDecoration(decoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        /*
        if (view instanceof FastScrollerRecyclerView) {
            Context context = view.getContext();
            FastScrollerRecyclerView recyclerView = (FastScrollerRecyclerView) view;

            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }


            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(new MyWordRecyclerViewAdapter(wordCursor, mListener));
            FastScrollRecyclerViewItemDecoration decoration = new FastScrollRecyclerViewItemDecoration(context);
            recyclerView.addItemDecoration(decoration);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
        */
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
        wordCursor = mListener.getCursor();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(String word);
        Cursor getCursor();
    }
}
