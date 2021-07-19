package com.example.newdictionary;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.newdictionary.ui.main.MainViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity  implements  DictionaryFragmentsListener {
    private SearchView wordSearchView;
    private ViewPager viewPager;
    public MainViewModel mainViewModel;

    public MainViewModel getMainViewModel() {
        return mainViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        configureDataSources();
        configureTabLayout();
    }

    private void configureDataSources() {
        SavedStateViewModelFactory factory = new SavedStateViewModelFactory(getApplication(),this);
        mainViewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);

        Observer<Cursor> suggestionObserver = new Observer<Cursor>() {
            @Override
            public void onChanged(Cursor suggestions) {
                wordSearchView.getSuggestionsAdapter().swapCursor(suggestions);
            }
        };
        mainViewModel.getSuggestionsList().observe(this, suggestionObserver);
    }

    private void configureTabLayout() {
        TabLayout mainTab = findViewById(R.id.mainTab);
        PagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager(), mainTab.getTabCount());

        viewPager = findViewById(R.id.mainViewPager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mainTab));

        mainTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);

        wordSearchView = (SearchView) searchViewItem.getActionView();
        wordSearchView.setIconifiedByDefault(false);
        wordSearchView.setSubmitButtonEnabled(true);
        wordSearchView.setQueryHint("Search for a word");
        wordSearchView.setSuggestionsAdapter(new SuggestionCursorAdapter(getApplication(),
                null, true));

        String query = getPreferences(MODE_PRIVATE).getString("query", null);

        if (query != null) {
            wordSearchView.setQuery(query, false);
        }

        wordSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mainViewModel.searchForWord(query);
                viewPager.setCurrentItem(0);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mainViewModel.searchForSuggestions(newText);
                return false;
            }
        });

        wordSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cursor = wordSearchView.getSuggestionsAdapter().getCursor();
                cursor.moveToPosition(position);
                mainViewModel.searchForWord(cursor.getString(1));
                viewPager.setCurrentItem(0);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        switch (menuItem.getItemId()) {
            case (R.id.action_settings):
                Intent intent2 = new Intent(this, DisplaySettingsActivity.class);
                editor = getPreferences(MODE_PRIVATE).edit();
                editor.putString("query", wordSearchView.getQuery().toString());
                editor.commit();
                startActivity(intent2);
                return true;
            case (R.id.action_help):
                Intent help_feedback_intent = new Intent(this, HelpAndFeedbackActivity.class);
                editor.putString("query", wordSearchView.getQuery().toString());
                editor.commit();
                startActivity(help_feedback_intent);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onListFragmentInteraction(String word) {
        mainViewModel.searchForWord(word);
        viewPager.setCurrentItem(0);
    }

    @Override
    public Cursor getWordList() {
        return mainViewModel.getWordList();
    }
}