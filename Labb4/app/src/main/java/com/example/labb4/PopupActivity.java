package com.example.labb4;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class PopupActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Button BBack;
    private Button BAdd;
    private TextView MovieText;
    private Context popupC;

    private static Context myStaticPopupC;
    private static ArrayAdapter aa;
    private static ArrayList<String> finalSimilar;
    private static ListView ListSimilar;

    DataModel DM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        DisplayMetrics d = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(d);
        int width = d.widthPixels;
        int height = d.heightPixels;
        getWindow().setLayout((int)(width*0.9),(int)(height*0.9));

        DM = new DataModel(getApplicationContext());
        DM.readFromFile();

        popupC = this;
        myStaticPopupC = popupC;

        BBack = findViewById(R.id.CloseButton);
        BAdd = findViewById(R.id.AddButton);
        BBack.setOnClickListener(this);
        BAdd.setOnClickListener(this);
        ListSimilar = findViewById(R.id.ListSim);
        ListSimilar.setOnItemClickListener(this);
        MovieText = findViewById(R.id.TitleMovie);
        MovieText.setText(getIntent().getStringExtra("title"));

        similar();
    }

    public static void refreshPopup(ArrayList<String> x) {

        finalSimilar = x;
        aa = new ArrayAdapter<String>(myStaticPopupC, android.R.layout.simple_list_item_1, finalSimilar);
        ListSimilar.setAdapter(aa);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.CloseButton) {

            finish();
        }

        if (view.getId() == R.id.AddButton) {

            DM.listFav.add((String) MovieText.getText());
            DM.writeToFile();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        MovieText.setText((CharSequence) aa.getItem(position));
        similar();
    }

    public void similar() {

        Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
        DM.getSimilar((String) MovieText.getText());
    }
}