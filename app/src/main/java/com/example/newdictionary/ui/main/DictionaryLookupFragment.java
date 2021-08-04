package com.example.newdictionary.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.newdictionary.MainActivity;
import com.example.newdictionary.R;
import com.example.newdictionary.alphabetscroll.AlphabetScrollRecyclerView;
import com.example.newdictionary.alphabetscroll.AlphabetScrollRecyclerViewItemDecoration;
import com.example.newdictionary.alphabetscroll.AlphabetScrollRecyclerViewAdapter;
import com.example.newdictionary.database.DictEntry;

public class DictionaryLookupFragment extends Fragment {
    private DictionaryFragmentsListener mListener;
    private AlphabetScrollRecyclerView recyclerView;
    private AlphabetScrollRecyclerViewAdapter alphabetScrollRecyclerViewAdapter;
    private MainViewModel mainViewModel;
    private String currWord;
    private int currWordIndex;

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
        alphabetScrollRecyclerViewAdapter = new AlphabetScrollRecyclerViewAdapter(mListener, getContext());
        recyclerView = (AlphabetScrollRecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(alphabetScrollRecyclerViewAdapter);
        recyclerView.addItemDecoration(new AlphabetScrollRecyclerViewItemDecoration(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        MainActivity parentActivity = (MainActivity) getActivity();
        mainViewModel = parentActivity.getMainViewModel();

        Observer<DictEntry> currentWordObserver = new Observer<DictEntry>() {
            @Override
            public void onChanged(DictEntry dictEntry) {
                currWord = dictEntry.getUnaccented();
                AlphabetScrollRecyclerViewAdapter adapter = (AlphabetScrollRecyclerViewAdapter) recyclerView.getAdapter();
                currWordIndex = adapter.getIndexOf(currWord);
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(currWordIndex);
            }
        };
        mainViewModel.currentWord.observe(getViewLifecycleOwner(), currentWordObserver);

        return view;
    }

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //MainActivity parentActivity = (MainActivity) getActivity();
        SharedPreferences preferences = getActivity().getPreferences(MainActivity.MODE_PRIVATE);
        currWord = preferences.getString("word", "");
        AlphabetScrollRecyclerViewAdapter adapter = (AlphabetScrollRecyclerViewAdapter) recyclerView.getAdapter();
        currWordIndex = adapter.getIndexOf(currWord);
        recyclerView.scrollToPosition(currWordIndex);
        adapter.notifyDataSetChanged();
    }

    public void scrollToCurrentWord() {
        recyclerView.scrollToPosition(currWordIndex);
    }

    @Override
    public void onStart() {
        super.onStart();
        scrollToCurrentWord();
    }

    @Override
    public void onResume() {
        super.onResume();
        scrollToCurrentWord();
    }

    @Override
    public void onPause() {
        super.onPause();
        scrollToCurrentWord();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}