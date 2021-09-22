package com.alsoftware.bukusuenglish;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.alsoftware.bukusuenglish.database.DictEntry;
import com.alsoftware.bukusuenglish.ui.main.DictionaryFragmentsListener;
import com.alsoftware.bukusuenglish.ui.main.DictionaryInterfaceFragment;
import com.alsoftware.bukusuenglish.ui.main.MainViewModel;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends BaseActivity  implements DictionaryFragmentsListener {
    private SearchView wordSearchView;
    public MainViewModel mainViewModel;
    private String currentWord;
    private NavController controller;
    private TabLayout tabLayout;

    public MainViewModel getMainViewModel() {
        return mainViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.mainTabLayout);
        currentWord = getPreferences(MainActivity.MODE_PRIVATE).getString("word", null);

        configureDataSources();
    }

    @Override
    protected void onStart() {
        super.onStart();
        controller = Navigation.findNavController(findViewById(R.id.fragmentContainerView));
    }

    private void configureDataSources() {
        SavedStateViewModelFactory factory = new SavedStateViewModelFactory(getApplication(),this);
        mainViewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);

        Observer<DictEntry> currentWordObserver = dictEntry -> {
            if (!dictEntry.getUnaccented().equals("")) {
                currentWord = dictEntry.getUnaccented();
            }
        };
        mainViewModel.getCurrentWord().observe(this, currentWordObserver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);

        wordSearchView = (SearchView) searchViewItem.getActionView();
        wordSearchView.setIconifiedByDefault(true);
        wordSearchView.setQueryHint("Search for a word");

        wordSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordSearchView.setQuery(getPreferences(MainActivity.MODE_PRIVATE).getString("query", null), false);
                removeTabLayout();
                controller.navigate(R.id.action_to_search);
                removeTabLayout();
            }
        });

        wordSearchView.setOnCloseListener(() -> {
            controller.navigate(R.id.action_to_dictionary);
            reinstateTabLayout();
            return false;
        });

        wordSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mainViewModel.searchForWord(query);
                controller.navigate(R.id.action_to_dictionary);
                reinstateTabLayout();
                storeCurrentSearchTerm();
                wordSearchView.onActionViewCollapsed();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mainViewModel.searchForSuggestions(newText+"%");
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case (R.id.action_settings_and_help):
                startActivity(new Intent(MainActivity.this, SettingsAndHelpActivity.class));
                return true;
            case (R.id.action_rate_app):
                startActivity(SettingsAndHelpActivity.getAppStoreRatingIntent());
                return true;
            case (R.id.action_invite):
                startActivity(SettingsAndHelpActivity.getAppShareIntent());
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onListFragmentInteraction(String word) {
        mainViewModel.searchForWord(word);
        for (Fragment frag:  ((NavHostFragment)getSupportFragmentManager()
                .getPrimaryNavigationFragment())
                .getChildFragmentManager()
                .getFragments()) {
            if (frag instanceof DictionaryInterfaceFragment) {
                ((DictionaryInterfaceFragment) frag).resetPager();
            }
        }
    }

    @Override
    public Cursor getWordList() {
        return mainViewModel.getWordList();
    }

    @Override
    public String getCurrentWord() {
        return currentWord;
    }

    public void returnFromSearch(String word) {
        mainViewModel.searchForWord(word);
        controller.navigate(R.id.action_to_dictionary);
        reinstateTabLayout();
        storeCurrentSearchTerm();
        wordSearchView.onActionViewCollapsed();
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }

    private void removeTabLayout() {
        tabLayout.setVisibility(View.GONE);
    }

    private void reinstateTabLayout() {
        tabLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        String current_destination = controller.getCurrentDestination().getLabel().toString();

        if (current_destination.equals("fragment_dictionary_interface")) {
            this.finish();
        } else if (current_destination.equals("fragment_search_dialog")) {
            controller.navigate(R.id.action_to_dictionary);
            reinstateTabLayout();
            storeCurrentSearchTerm();
            wordSearchView.onActionViewCollapsed();
        }
    }

    @Override
    public void storeCurrentSearchTerm() {
        String queryStore = wordSearchView.getQuery().toString();
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString("query", queryStore);
        editor.commit();
        Toast.makeText(this, "storing term: " + queryStore, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getCurrentSearchTerm() {
        return getPreferences(MainActivity.MODE_PRIVATE).getString("query", "");
    }
}