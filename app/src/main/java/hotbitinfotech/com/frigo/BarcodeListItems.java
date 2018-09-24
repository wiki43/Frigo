package hotbitinfotech.com.frigo;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.View;

import java.util.ArrayList;


public class BarcodeListItems extends AppCompatActivity {

    private Toolbar barcodeToolbar;
    private RecyclerView barcodeRecyclerview;

    private ArrayList<Model> arrayListBarcode = new ArrayList<>();
    private ArrayList<Note> arrayListDB = new ArrayList<>();
    private BarcodeItem_Adapter adapterBarcode;

    private DatabaseHelper db;

    private ConstraintLayout barcodeConstraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_list_items);

        db = new DatabaseHelper(this);

        //TODO:- widget
        barcodeToolbar = findViewById(R.id.toolbar);
        barcodeRecyclerview = findViewById(R.id.recyclerViewGloble);
        barcodeConstraintLayout = findViewById(R.id.constraintLayoutListBarcode);

        //TODO:- click event on toolbar.
        barcodeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BarcodeListItems.this, MainActivity.class));
                finish();
            }
        });
        //TODO:- get barcode data.
        Snackbar snackbar =
                Snackbar.make(barcodeConstraintLayout, "Data From Local DB.", Snackbar.LENGTH_SHORT);
        snackbar.show();

        arrayListDB.addAll(db.getAllBarcodeData());
        String pName, pBarcodeDate, pBarcodeManual, pBarcodeQuantit;

        if (arrayListDB.size() != 0) {
            for (int i = 0; i < arrayListDB.size(); i++) {
                pName = arrayListDB.get(i).getName();
                pBarcodeDate = arrayListDB.get(i).getEpiredata();
                pBarcodeManual = arrayListDB.get(i).getManualbarcode();
                pBarcodeQuantit = arrayListDB.get(i).getQuantity();

                arrayListBarcode.add(new Model(pName, pBarcodeDate, pBarcodeManual, pBarcodeQuantit));
            }

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            barcodeRecyclerview.setLayoutManager(layoutManager);
            adapterBarcode = new BarcodeItem_Adapter(this, arrayListBarcode);
            barcodeRecyclerview.setAdapter(adapterBarcode);
        } else {
            Snackbar snackbar1 =
                    Snackbar.make(barcodeConstraintLayout, "No Record Available.", Snackbar.LENGTH_SHORT);
            snackbar1.show();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(BarcodeListItems.this, MainActivity.class));
        finish();
    }
}
