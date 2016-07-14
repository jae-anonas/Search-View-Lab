package ly.generalassemb.drewmahrt.shoppinglistwithsearch;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Locale;

import ly.generalassemb.drewmahrt.shoppinglistwithsearch.setup.DBAssetHelper;

public class MainActivity extends AppCompatActivity {
    ListView mListView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        ComponentName componentName = new ComponentName(this, this.getClass());

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        mListView = (ListView) findViewById(R.id.listview);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            Cursor cursor = ShoppingSQLiteOpenHelper.getInstance(this).searchItems(query);

            android.support.v4.widget.CursorAdapter cursorAdapter = new android.support.v4.widget.CursorAdapter(this, cursor,
                    android.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {
                @Override
                public View newView(Context context, Cursor cursor, ViewGroup parent) {
                    return LayoutInflater.from(MainActivity.this)
                            .inflate(R.layout.shopping_item_layout, parent, false);
                }

                @Override
                public void bindView(View view, Context context, Cursor cursor) {
                    TextView itemName = (TextView) view.findViewById(R.id.text_itemName);
                    TextView itemType = (TextView) view.findViewById(R.id.text_itemType);
                    TextView itemPrice = (TextView) view.findViewById(R.id.text_price);

                    itemName.setText(cursor.getString(cursor
                            .getColumnIndex(ShoppingSQLiteOpenHelper.COL_ITEM_NAME)));
                    itemType.setText(cursor.getString(cursor
                            .getColumnIndex(ShoppingSQLiteOpenHelper.COL_ITEM_DESCRIPTION)));
                    itemPrice.setText(String.format(Locale.ENGLISH,"$ %s", cursor.getString(cursor
                            .getColumnIndex(ShoppingSQLiteOpenHelper.COL_ITEM_PRICE))));

                }
            };
            mListView.setAdapter(cursorAdapter);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Ignore the two lines below, they are for setup
        DBAssetHelper dbSetup = new DBAssetHelper(MainActivity.this);
        dbSetup.getReadableDatabase();

        Intent intent = new Intent(Intent.ACTION_SEARCH);
        intent.putExtra(SearchManager.QUERY, "");
        onNewIntent(intent);

    }
}
