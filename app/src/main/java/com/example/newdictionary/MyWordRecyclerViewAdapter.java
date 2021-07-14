package com.example.newdictionary;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.newdictionary.WordListFragment.OnListFragmentInteractionListener;
import com.example.newdictionary.fastscroll.FastScrollerRecyclerViewInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class MyWordRecyclerViewAdapter extends RecyclerView.Adapter<MyWordRecyclerViewAdapter.ViewHolder>
                                        implements FastScrollerRecyclerViewInterface {
    private Cursor wCursor;
    private OnListFragmentInteractionListener mListener;
    private HashMap<String, Integer> mMapIndex;

    public MyWordRecyclerViewAdapter(Cursor cursor, OnListFragmentInteractionListener listener) {
        wCursor = cursor;
        mListener = listener;

        ArrayList<String> items = new ArrayList<>();
        while (wCursor.moveToNext()) {
            items.add(wCursor.getString(4));
        }

        /* mMapIndex holds the indices within the created dataset at which each letter begins */
        mMapIndex = calculateIndexesForName(items);
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
        wCursor.moveToPosition(position);
        holder.wordView.setText(wCursor.getString(4));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.wordView.getText().toString());
                }
            }
        });
    }

    @Override
    public HashMap<String, Integer> getMapIndex() {
        return this.mMapIndex;
    }

    @Override
    public int getItemCount() {
        return wCursor.getCount();
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
