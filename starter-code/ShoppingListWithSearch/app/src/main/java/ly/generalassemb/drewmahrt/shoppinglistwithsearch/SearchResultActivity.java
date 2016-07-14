package ly.generalassemb.drewmahrt.shoppinglistwithsearch;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;

public class SearchResultActivity extends AppCompatActivity {
    ListView mListView;
    CursorAdapter mCursorAdapter;
    ShoppingSQLiteOpenHelper mShoppingSQLiteOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        mListView = (ListView) findViewById(R.id.listview);

        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())){
            String query = getIntent().getStringExtra(SearchManager.QUERY);
            Cursor cursor = mShoppingSQLiteOpenHelper.getInstance(this).searchItems(query);

            CursorAdapter cursorAdapter = new CursorAdapter(this, cursor,
                    android.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {
                @Override
                public View newView(Context context, Cursor cursor, ViewGroup parent) {
                    return LayoutInflater.from(SearchResultActivity.this)
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
}
