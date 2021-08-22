package com.alsoftware.bukusuenglish.ui.main;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alsoftware.bukusuenglish.MainActivity;
import com.alsoftware.bukusuenglish.R;

public class SearchDialogFragment extends Fragment {
    private MainViewModel mainViewModel;
    private MainActivity mainActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
        mainViewModel = mainActivity.getMainViewModel();
        mainViewModel.searchForSuggestions("%");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_dialog, container, false);

        Cursor startingCursor = mainViewModel.getSuggestionsList().getValue();
        Context ctx = getContext();
        RecyclerView suggestionList = view.findViewById(R.id.suggestionList);

        suggestionList.setLayoutManager(new LinearLayoutManager(ctx));
        SuggestionListAdapter adapter = new SuggestionListAdapter(mainActivity, startingCursor);
        suggestionList.setAdapter(adapter);
        suggestionList.addItemDecoration(new DividerItemDecoration(ctx, DividerItemDecoration.VERTICAL));
        suggestionList.setItemAnimator(new DefaultItemAnimator());

        Observer<Cursor> suggestionObserver = newCursor -> {
            SuggestionListAdapter listAdapter = ((SuggestionListAdapter)suggestionList.getAdapter());
            if (listAdapter != null) {
                listAdapter.swapCursor(newCursor);
            }
        };
        mainViewModel.getSuggestionsList().observe(getViewLifecycleOwner(), suggestionObserver);
        return view;
    }
}
