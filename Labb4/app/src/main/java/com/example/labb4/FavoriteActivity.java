package com.example.labb4;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class FavoriteActivity extends AppCompatActivity implements View.OnClickListener {

    private Button BBack;
    private Button BDel;
    private ListView ListFav;
    private TextView TitleText;
    private ArrayAdapter aa;

    DataModel DM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        DM = new DataModel(getApplicationContext());

        BBack = findViewById(R.id.BackButton);
        BDel = findViewById(R.id.DelButton);
        BBack.setOnClickListener(this);
        BDel.setOnClickListener(this);
        ListFav = findViewById(R.id.ListFavorite);
        TitleText = findViewById(R.id.TitleMovie);

        DM.readFromFile();
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DM.getFavList());
        ListFav.setAdapter(aa);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.BackButton) {

            finish();
        }

        if (view.getId() == R.id.DelButton) {

            DM.emptyFavList();
            DM.writeToFile();
            aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DM.getFavList());
            ListFav.setAdapter(aa);
        }
    }
}