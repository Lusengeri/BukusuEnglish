package com.example.newdictionary;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends BaseActivity  implements  WordListFragment.OnListFragmentInteractionListener {

    private SimpleCursorAdapter cursorAdapter;
    private SearchView searchView;
    private DatabaseOpenHelper helper;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        configureTabLayout();

        String [] columnNames = { "unaccented" };
        int [] viewNames = { R.id.suggestView };
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.suggestion_list_item, null, columnNames, viewNames, 0);

        helper = new DatabaseOpenHelper(getApplicationContext());
    }

    void configureTabLayout() {
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

        searchView = (SearchView) searchViewItem.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search for a word");
        searchView.setSuggestionsAdapter(cursorAdapter);

        String query = getPreferences(MODE_PRIVATE).getString("query", null);

        if (query != null) {
            searchView.setQuery(query, false);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewPager.setCurrentItem(0);

                SQLiteDatabase db = helper.getReadableDatabase();
                String database_query = "SELECT * FROM definitions WHERE unaccented = '" + query + "';";
                Cursor def = db.rawQuery(database_query, null);

                if (def.getCount() == 0) {
                    for (Fragment fragment :getSupportFragmentManager().getFragments()) {
                        if (fragment instanceof SearchResultFragment) {
                            ((SearchResultFragment) fragment).showUnsuccessful();
                        }
                    }
                    cursorAdapter.changeCursor(null);
                } else {
                    def.moveToFirst();
                    String word = def.getString(1);
                    String pos = def.getString(2);
                    String definition = def.getString(3);
                    String spelled = def.getString(4);

                    for (Fragment fragment :getSupportFragmentManager().getFragments()) {
                        if (fragment instanceof SearchResultFragment) {
                            ((SearchResultFragment) fragment).showSelectedDefinition(word, pos, definition, spelled);
                        }
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                SQLiteDatabase db = helper.getReadableDatabase();
                String database_query = "SELECT * FROM definitions WHERE unaccented LIKE '" + newText + "%';";
                Cursor def = db.rawQuery(database_query, null);

                if (def.getCount() == 0) {
                    cursorAdapter.changeCursor(null);
                } else {
                    cursorAdapter.changeCursor(def);
                }
                return false;
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                viewPager.setCurrentItem(0);

                Cursor cursor = searchView.getSuggestionsAdapter().getCursor();
                cursor.moveToPosition(position);
                String word = cursor.getString(1);
                String pos = cursor.getString(2);
                String definition = cursor.getString(3);
                String spelled = cursor.getString(4);

                for (Fragment fragment :getSupportFragmentManager().getFragments()) {
                    if (fragment instanceof SearchResultFragment) {
                        ((SearchResultFragment) fragment).showSelectedDefinition(word, pos, definition, spelled);
                    }
                }
                return false;
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
                editor.putString("query", searchView.getQuery().toString());
                editor.commit();
                startActivity(intent2);
                return true;
            case (R.id.action_help):
                Intent help_feedback_intent = new Intent(this, HelpAndFeedbackActivity.class);
                editor.putString("query", searchView.getQuery().toString());
                editor.commit();
                startActivity(help_feedback_intent);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onListFragmentInteraction(String word) {
        searchForWord(word);
    }

    @Override
    public Cursor getCursor() {
        SQLiteDatabase db = helper.getReadableDatabase();
        String database_query = "SELECT * FROM definitions";
        Cursor def = db.rawQuery(database_query, null);
        return def;
    }

    public void searchForWord(String query) {
        viewPager.setCurrentItem(0);
        SQLiteDatabase db = helper.getReadableDatabase();
        String database_query = "SELECT * FROM definitions WHERE unaccented = '" + query + "';";
        Cursor def = db.rawQuery(database_query, null);

        if (def.getCount() == 0) {
            for (Fragment fragment :getSupportFragmentManager().getFragments()) {
                if (fragment instanceof SearchResultFragment) {
                    ((SearchResultFragment) fragment).showUnsuccessful();
                }
            }
            cursorAdapter.changeCursor(null);
        } else {
            def.moveToFirst();
            String word = def.getString(1);
            String pos = def.getString(2);
            String definition = def.getString(3);
            String spelled = def.getString(4);

            for (Fragment fragment :getSupportFragmentManager().getFragments()) {
                if (fragment instanceof SearchResultFragment) {
                    ((SearchResultFragment) fragment).showSelectedDefinition(word, pos, definition, spelled);
                }
            }
        }
    }
}