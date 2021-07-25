package com.example.newdictionary.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newdictionary.R;
import com.example.newdictionary.alphabetscroll.AlphabetScrollRecyclerViewItemDecoration;
import com.example.newdictionary.alphabetscroll.AlphabetScrollRecyclerViewAdapter;

public class DictionaryLookupFragment extends Fragment {
    private DictionaryFragmentsListener mListener;
    private RecyclerView recyclerView;
    private AlphabetScrollRecyclerViewAdapter alphabetScrollRecyclerViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DictionaryLookupFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof DictionaryFragmentsListener) {
            mListener = (DictionaryFragmentsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DictionaryFragmentsListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word_list, container, false);
        alphabetScrollRecyclerViewAdapter = new AlphabetScrollRecyclerViewAdapter(mListener);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(alphabetScrollRecyclerViewAdapter);
        recyclerView.addItemDecoration(new AlphabetScrollRecyclerViewItemDecoration(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}