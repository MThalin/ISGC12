package com.example.labb4;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private EditText Input;
    private Button BSearch;
    private Button BFav;
    private TextView ContainsText;
    private Context mainC;

    private static Context myStaticMainC;
    private static ListView ListContains;
    private static ArrayAdapter aa;
    private static ArrayList<String> finalContains;

    DataModel DM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DM = new DataModel(getApplicationContext());

        mainC = this;
        myStaticMainC = mainC;

        BSearch = findViewById(R.id.SearchButton);
        BFav = findViewById(R.id.FavoriteButton);
        BSearch.setOnClickListener(this);
        BFav.setOnClickListener(this);
        Input = findViewById(R.id.MyInput);
        ListContains = findViewById(R.id.ListCont);
        ListContains.setOnItemClickListener(this);
        ContainsText = findViewById(R.id.TextCont);
        finalContains = new ArrayList<>();
    }

    protected void onDestroy() {
        super.onDestroy();
        DM.requestQueue.stop();
    }

    public static void refreshMain(ArrayList<String> x) {

            finalContains = x;
            aa = new ArrayAdapter<String> (myStaticMainC, android.R.layout.simple_list_item_1, finalContains);
            ListContains.setAdapter(aa);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.FavoriteButton) {

            Intent favoriteIntent = new Intent (this, FavoriteActivity.class);
            this.startActivity(favoriteIntent);
        }

        if (view.getId() == R.id.SearchButton) {

            try {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {

            }
            Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
            ContainsText.setText("Movie titles containing \""+Input.getText().toString()+"\":");
            DM.getContaining(Input.getText().toString());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent popupIntent = new Intent (this, PopupActivity.class);
        popupIntent.putExtra("title", (CharSequence) aa.getItem(position));
        this.startActivity(popupIntent);
    }
}