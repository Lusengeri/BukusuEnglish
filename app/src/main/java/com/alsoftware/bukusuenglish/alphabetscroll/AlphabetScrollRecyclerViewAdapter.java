package com.alsoftware.bukusuenglish.alphabetscroll;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alsoftware.bukusuenglish.ui.main.DictionaryFragmentsListener;
import com.alsoftware.bukusuenglish.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class AlphabetScrollRecyclerViewAdapter extends AlphabetScrollRecyclerView.Adapter<AlphabetScrollRecyclerViewAdapter.ViewHolder>
                                        implements AlphabetScrollRecyclerViewInterface {
    private final DictionaryFragmentsListener mListener;
    private final HashMap<String, Integer> mMapIndex;
    private final Cursor wordList;
    private final Context ctx;
    private final ArrayList<String> wordArrayList = new ArrayList<>();

    public AlphabetScrollRecyclerViewAdapter(DictionaryFragmentsListener listener, Context context) {
        mListener = listener;
        ctx = context;
        wordList = mListener.getWordList();
        wordList.moveToFirst();

        do {
            wordArrayList.add(wordList.getString(0));
        } while ( wordList.moveToNext());

        mMapIndex = calculateIndexesFromWordList(wordArrayList);
    }

    private HashMap<String, Integer> calculateIndexesFromWordList(ArrayList<String> items) {
        HashMap<String, Integer> mapIndex = new LinkedHashMap<>();
        for (int i = 0; i<items.size(); i++) {
            String name = items.get(i);
            String index = name.substring(0,1);

            if (index.equals("-")){
                index = name.substring(1,2);
            }

            index = index.toUpperCase();

            if (!mapIndex.containsKey(index)) {
                mapIndex.put(index, i);
            }
        }
        return mapIndex;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.word_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        wordList.moveToPosition(position);
        String word = wordList.getString(0);
        holder.wordView.setText(word);

        int currentTheme = PreferenceManager.getDefaultSharedPreferences(ctx)
                .getInt("Theme", R.style.CustomAppTheme);


        if (word.equals(mListener.getCurrentWord())) {
            if (currentTheme == R.style.CustomAppTheme) {
                holder.wordView.setBackgroundColor(ctx.getResources().getColor(R.color.list_card_highlight_light));
                holder.wordView.setTextColor(ctx.getResources().getColor(R.color.primaryLight));
            } else {
                holder.wordView.setBackgroundColor(ctx.getResources().getColor(R.color.list_card_highlight_dark));
                holder.wordView.setTextColor(ctx.getResources().getColor(R.color.brightWhite));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.background.setElevation(6.0F);
            }
        } else {
            if (currentTheme == R.style.CustomAppTheme) {
                holder.wordView.setBackgroundColor(ctx.getResources().getColor(R.color.brightWhite));
                holder.wordView.setTextColor(ctx.getResources().getColor(R.color.primaryLight));
            } else {
                holder.wordView.setBackgroundColor(ctx.getResources().getColor(R.color.surfaceDark));
                holder.wordView.setTextColor(ctx.getResources().getColor(R.color.brightWhite));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.background.setElevation(0.0F);
            }
        }

        holder.layoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onListFragmentInteraction(holder.wordView.getText().toString());
                notifyDataSetChanged();
            }
        });
    }

    public int getIndexOf(String word) {
        return wordArrayList.indexOf(word);
    }

    @Override
    public HashMap<String, Integer> getMapIndex() {
        return mMapIndex;
    }

    @Override
    public int getItemCount() {
        return wordList.getCount();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView wordView;
        public final CardView background;
        public final View layoutView;

        public ViewHolder(View view) {
            super(view);
            layoutView = view;
            wordView = view.findViewById(R.id.wordTextView);
            background = view.findViewById(R.id.backgroundCardView);
        }
    }
}