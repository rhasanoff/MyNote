package com.example.mynote.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mynote.data.DatabaseHelper;
import com.example.mynote.fragments.NoteListFragment;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialize.util.KeyboardUtil;

import android.os.Bundle;

import com.example.mynote.R;
import com.example.mynote.fragments.NoteLinedEditorFragment;
import com.example.mynote.fragments.NotePlainEditorFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private com.mikepenz.materialdrawer.Drawer result = null;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        databaseHelper = new DatabaseHelper(this);
        mDatabase = databaseHelper.getWritableDatabase();

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withActionBarDrawerToggle(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.title_home).withIcon(FontAwesome.Icon.faw_home).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.title_editor).withIcon(FontAwesome.Icon.faw_edit).withIdentifier(2),
                        new PrimaryDrawerItem().withName(R.string.title_about).withIcon(FontAwesome.Icon.faw_list).withIdentifier(3)

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int i, IDrawerItem drawerItem) {
                        if (drawerItem != null && drawerItem instanceof Nameable){
                            String name = ((Nameable)drawerItem).getName().getText(MainActivity.this);
                            mToolbar.setTitle(name);
                        }

                        if (drawerItem != null){
                            int selectedScren = (int)drawerItem.getIdentifier();
                            switch (selectedScren){
                                case 1:
                                    //goto list of notes
                                    openFragment(new NoteListFragment(), "Notes");
                                    break;
                                case 2:
                                    //goto editor screen
                                    startActivity(new Intent(MainActivity.this, NoteEditorActivity.class));
                                    break;
                                case 3:
                                    //about
                                    Toast.makeText(MainActivity.this, "MyNote created by Rashad Hasanov", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                        return false;
                    }


                })
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View view) {
                        KeyboardUtil.hideKeyboard(MainActivity.this);

                    }

                    @Override
                    public void onDrawerClosed(View view) {

                    }

                    @Override
                    public void onDrawerSlide(View view, float v) {

                    }
                })
                .withFireOnInitialOnClick(true)
                .withSavedInstance(savedInstanceState)
                .build();

        if (savedInstanceState == null){
            result.setSelection(1);
        }
    }

    private void openFragment(final Fragment fragment, String title){
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
        getSupportActionBar().setTitle(title);
    }

}
