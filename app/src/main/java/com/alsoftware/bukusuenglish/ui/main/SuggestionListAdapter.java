package com.alsoftware.bukusuenglish.ui.main;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.alsoftware.bukusuenglish.MainActivity;
import com.alsoftware.bukusuenglish.R;

public class SuggestionListAdapter extends RecyclerView.Adapter<SuggestionListAdapter.SuggestionViewHolder> {
    private Cursor cursor;
    private final MainActivity activity;

    public SuggestionListAdapter(MainActivity parentActivity, @Nullable Cursor suggestionCursor) {
        cursor = suggestionCursor;
        activity = parentActivity;
    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_list_item, parent, false);
        return new SuggestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionViewHolder holder, int position) {
        cursor.moveToPosition(position);
        String word = cursor.getString(1);
        holder.suggestView.setText(word);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.returnFromSearch(word);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (cursor == null)
            return 0;
        else {
            return cursor.getCount();
        }
    }

    public static class SuggestionViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView suggestView;
        public SuggestionViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            suggestView = itemView.findViewById(R.id.dictionaryWord);
        }
    }
}