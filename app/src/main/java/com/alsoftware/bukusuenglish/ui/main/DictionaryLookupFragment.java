package com.alsoftware.bukusuenglish.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alsoftware.bukusuenglish.MainActivity;
import com.alsoftware.bukusuenglish.R;
import com.alsoftware.bukusuenglish.alphabetscroll.AlphabetScrollRecyclerView;
import com.alsoftware.bukusuenglish.alphabetscroll.AlphabetScrollRecyclerViewItemDecoration;
import com.alsoftware.bukusuenglish.alphabetscroll.AlphabetScrollRecyclerViewAdapter;
import com.alsoftware.bukusuenglish.database.DictEntry;

public class DictionaryLookupFragment extends Fragment implements DictionaryFragmentsListener{
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
        mListener = (DictionaryFragmentsListener) context;
        mainViewModel = ((MainActivity) getActivity()).getMainViewModel();
        alphabetScrollRecyclerViewAdapter = new AlphabetScrollRecyclerViewAdapter(mListener, getContext());
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word_list, container, false);
        recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(alphabetScrollRecyclerViewAdapter);
        recyclerView.addItemDecoration(new AlphabetScrollRecyclerViewItemDecoration(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

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
        mainViewModel.getCurrentWord().observe(getViewLifecycleOwner(), currentWordObserver);

        return view;
    }

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

    public void onListFragmentInteraction(String word) {
        mListener.onListFragmentInteraction(word);
    }

    @Override
    public Cursor getWordList() {
        return mainViewModel.getWordList();
    }

    @Override
    public String getCurrentWord() {
        return mainViewModel.getCurrentWord().getValue().getWord();
    }
}