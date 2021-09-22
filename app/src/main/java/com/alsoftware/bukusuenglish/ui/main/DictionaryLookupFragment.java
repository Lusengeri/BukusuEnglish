package com.alsoftware.bukusuenglish.ui.main;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
    public void onAttach(@NonNull Context context) {
        mListener = (DictionaryFragmentsListener) context;
        mainViewModel = ((MainActivity) context).getMainViewModel();
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word_list, container, false);
        AlphabetScrollRecyclerViewAdapter adapter = new AlphabetScrollRecyclerViewAdapter(mListener, getContext());

        recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.addItemDecoration(new AlphabetScrollRecyclerViewItemDecoration(getContext()));

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

        currWord = mListener.getCurrentWord();
        currWordIndex = ((AlphabetScrollRecyclerViewAdapter) recyclerView.getAdapter()).getIndexOf(currWord);
        recyclerView.getAdapter().notifyDataSetChanged();
        return view;
    }

    public void scrollToCurrentWord() {
        recyclerView.scrollToPosition(currWordIndex);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        scrollToCurrentWord();
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

    @Override
    public String getCurrentSearchTerm() {
        return null;
    }

    @Override
    public void storeCurrentSearchTerm() {

    }
}