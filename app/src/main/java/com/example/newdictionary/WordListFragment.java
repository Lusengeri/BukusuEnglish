package com.example.newdictionary;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newdictionary.database.DictEntry;
import com.example.newdictionary.fastscroll.FastScrollRecyclerViewItemDecoration;
import com.example.newdictionary.fastscroll.MyWordRecyclerViewAdapter;
import com.example.newdictionary.ui.main.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class WordListFragment extends Fragment {
    private DictionaryFragmentsListener mListener;
    private RecyclerView recyclerView;
    private MyWordRecyclerViewAdapter myWordRecyclerViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WordListFragment() {
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
        myWordRecyclerViewAdapter = new MyWordRecyclerViewAdapter(mListener);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(myWordRecyclerViewAdapter);
        recyclerView.addItemDecoration(new FastScrollRecyclerViewItemDecoration(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}