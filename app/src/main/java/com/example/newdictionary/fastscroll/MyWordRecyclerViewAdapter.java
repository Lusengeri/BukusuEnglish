package com.example.newdictionary.fastscroll;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.newdictionary.DictionaryFragmentsListener;
import com.example.newdictionary.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MyWordRecyclerViewAdapter extends RecyclerView.Adapter<MyWordRecyclerViewAdapter.ViewHolder>
                                        implements FastScrollerRecyclerViewInterface {
    private final DictionaryFragmentsListener mListener;
    private HashMap<String, Integer> mMapIndex = new HashMap<>();
    private final Cursor wordList;

    public MyWordRecyclerViewAdapter(DictionaryFragmentsListener listener) {
        mListener = listener;
        wordList = mListener.getWordList();

        ArrayList<String> wordArrayList = new ArrayList<>();
        while ( wordList.moveToNext()) {
            wordArrayList.add(wordList.getString(0));
        }
        mMapIndex = calculateIndexesForName(wordArrayList);
    }


    private HashMap<String, Integer> calculateIndexesForName(ArrayList<String> items) {
        HashMap<String, Integer> mapIndex = new LinkedHashMap<>();
        for (int i = 0; i<items.size(); i++){
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
                .inflate(R.layout.fragment_word, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //String word = wordList.get(position);
        wordList.moveToPosition(position);
        String word = wordList.getString(0);
        holder.wordView.setText(word);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.wordView.getText().toString());
                }
            }
        });
    }

    @Override
    public HashMap<String, Integer> getMapIndex() {
        return mMapIndex;
    }



    @Override
    public int getItemCount() {
        return wordList.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView wordView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            wordView = view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + wordView.getText() + "'";
        }
    }
}