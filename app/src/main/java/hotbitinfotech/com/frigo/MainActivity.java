package hotbitinfotech.com.frigo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.Result;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ZXingScannerView.ResultHandler {

    private DrawerLayout drawerLayout;

    private static final String TAG = "MainActivity";
    private ZXingScannerView zXingScannerView;

    private Button addBarcodeDetails;
    private Button barcodeUpdateBtn;
    private EditText barcodenameEdit, barcodeEpireDateET, barcodeManualEt, barcodeQualityEt, enterBarcoedET;
    private String barcodenameString;
    private String barcodeEpireDateString;
    private String barcodeManualString;
    private String barcodeQualityString;

    private RelativeLayout relativeLayout;

    private DatabaseHelper db;

    private static final Integer CAMERA = 0x5;
    private static final Integer WRITE_EXST = 0x3;
    private static final Integer READ_EXST = 0x4;
    private ArrayList<Note> arrayListCheckBarcode = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        zXingScannerView = new ZXingScannerView(getApplicationContext()) {
            @Override
            protected IViewFinder createViewFinderView(Context context) {
                return new CustomZXingScannerView(context);
            }
        };
        //TODO:- SetContentView
        setContentView(R.layout.activity_main);
        //TODO:- find view
        initview();
        //TODO:- start barcode
        askForPermission(Manifest.permission.CAMERA, CAMERA);
        relativeLayout.addView(zXingScannerView);
        zXingScannerView.setHorizontalFadingEdgeEnabled(false);
        zXingScannerView.setVerticalFadingEdgeEnabled(true);
        zXingScannerView.setResultHandler(MainActivity.this);
        zXingScannerView.startCamera();

        //TODO:- get value from intent
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            Log.e(TAG, "bundle is null ");
        } else {

            barcodeEpireDateET.setEnabled(false);
            barcodeManualEt.setEnabled(false);
            enterBarcoedET.setEnabled(false);

            barcodenameString = bundle.getString("barNameKEY");
            barcodeEpireDateString = bundle.getString("barDateKEY");
            barcodeQualityString = bundle.getString("barQuantityKEY");
            barcodeManualString = bundle.getString("barNumberKEY");

            barcodeUpdateBtn.setVisibility(View.VISIBLE);

            barcodenameEdit.setText(barcodenameString);
            barcodeEpireDateET.setText(barcodeEpireDateString);
            barcodeManualEt.setText(barcodeManualString);
            enterBarcoedET.setText(barcodeManualString);
            barcodeQualityEt.setText(barcodeQualityString);
        }
        //---------------------------------------------------------------------
        db = new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //----------------------------------------------------------------------
        addBarcodeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, READ_EXST);
                //intializing scan object
                final String prodcutName = barcodenameEdit.getText().toString();
                final String prodcutEpire = barcodeEpireDateET.getText().toString();
                String prodcutManual = barcodeManualEt.getText().toString();
                final String prodcutQuality = barcodeQualityEt.getText().toString();
                String prodcutBarcodeNumber = enterBarcoedET.getText().toString();
                String barcodeNumberIS = null;

                //TODO:- net connection check code hear.
                if (!prodcutName.isEmpty() && !prodcutEpire.isEmpty() && (!prodcutManual.isEmpty() || !prodcutBarcodeNumber.isEmpty()) && !prodcutQuality.isEmpty()) {

                    if (prodcutBarcodeNumber.isEmpty() || prodcutBarcodeNumber.equals(" ")) {
                        barcodeNumberIS = prodcutManual;
                    } else {
                        barcodeNumberIS = prodcutBarcodeNumber;
                    }

                    //TODO:- check barcode available or not
                    arrayListCheckBarcode.addAll(db.getCheckBarcodeAvilablity(barcodeNumberIS));

                    if (arrayListCheckBarcode.size() > 0) {
                        final String finalBarcodeRemoveIS1 = barcodeNumberIS;
                        Snackbar snackbar =
                                Snackbar.make(drawerLayout, "Data available", Snackbar.LENGTH_SHORT);
                        snackbar.show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < arrayListCheckBarcode.size(); i++) {

                                    barcodenameEdit.setText(arrayListCheckBarcode.get(i).getName());
                                    barcodeEpireDateET.setText(arrayListCheckBarcode.get(i).getEpiredata());
                                    barcodeManualEt.setText(arrayListCheckBarcode.get(i).getManualbarcode());
                                    enterBarcoedET.setText(arrayListCheckBarcode.get(i).getManualbarcode());
                                    barcodeQualityEt.setText(arrayListCheckBarcode.get(i).getQuantity());
                                }
                            }
                        }, 200);
                    } else {
                        Snackbar snackbar =
                                Snackbar.make(drawerLayout, "No Data In Local DB.", Snackbar.LENGTH_SHORT);
                        snackbar.show();

                        final String finalBarcodeNumberIS = barcodeNumberIS;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (NetConnection.IsNetPresent(MainActivity.this)) {
                                    //TODO:- check barcode to server.
                                    checkBarcodeToserver(finalBarcodeNumberIS, prodcutName, prodcutEpire, prodcutQuality);
                                } else {
                                    Snackbar snackbar =
                                            Snackbar.make(drawerLayout, "Check Network Connection.", Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                }
                            }
                        }, 200);


                    }
                } else {
                    Snackbar snackbar =
                            Snackbar.make(drawerLayout, "All field required.", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }

            }
        });

        //TODO:- click event on update button
        barcodeUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String barcodeNo, barcodeName, barcodeQuantity;

                barcodeEpireDateET.setClickable(false);
                barcodeEpireDateET.setFocusable(false);

                barcodeManualEt.setClickable(false);
                barcodeManualEt.setFocusable(false);

                enterBarcoedET.setClickable(false);
                enterBarcoedET.setFocusable(false);

                barcodeNo = barcodeManualEt.getText().toString();
                barcodeName = barcodenameEdit.getText().toString();
                barcodeQuantity = barcodeQualityEt.getText().toString();

                int updateCount = db.updateNote(barcodeNo, barcodeName, barcodeQuantity);
                if (updateCount > 0) {

                    barcodenameEdit.setText("");
                    barcodeEpireDateET.setText("");
                    barcodeManualEt.setText("");
                    enterBarcoedET.setText("");
                    barcodeQualityEt.setText("");

                    Snackbar snackbar =
                            Snackbar.make(drawerLayout, "Data Updated.", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else {
                    Log.e(TAG, "Data not updated.");
                }
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void checkBarcodeToserver(final String barcodeNumberIS, final String prodcutName, final String prodcutEpire, final String prodcutQuality) {

        String URL = Common.CheckBarcodeInfo;
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "onResponse: check barcode " + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        final JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String status = jsonObject.getString("status");

                        if (status.equalsIgnoreCase("true")) {
                            Snackbar snackbar =
                                    Snackbar.make(drawerLayout, "Data Available.", Snackbar.LENGTH_SHORT);
                            snackbar.show();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        barcodenameEdit.setText(jsonObject.getString("product_name"));
                                        barcodeEpireDateET.setText(jsonObject.getString("product_expire_date"));
                                        barcodeManualEt.setText(jsonObject.getString("product_manual_bar"));
                                        enterBarcoedET.setText(jsonObject.getString("product_manual_bar"));
                                        barcodeQualityEt.setText(jsonObject.getString("product_quantity"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, 300);

                        } else {
                            Snackbar snackbar =
                                    Snackbar.make(drawerLayout, "Data Not Available On Server.", Snackbar.LENGTH_SHORT);
                            snackbar.show();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //TODO:- insert new data to local data
                                    createBarcodeData(prodcutName, prodcutEpire, barcodeNumberIS, prodcutQuality);
                                }
                            }, 500);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: "+error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> param = new HashMap<>();
                param.put("bar_code", barcodeNumberIS);
                return param;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public void onResume() {
        super.onResume();
        zXingScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        zXingScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();           // Stop camera on pause
    }

    private void createBarcodeData(String prodcutName, String prodcutEpire, String prodcutManual, String prodcutQuality) {
        long id = db.insertBarcodeData(prodcutName, prodcutEpire, prodcutManual, prodcutQuality, "0");
        barcodenameEdit.setText("");
        barcodeEpireDateET.setText("");
        barcodeManualEt.setText("");
        enterBarcoedET.setText("");
        barcodeQualityEt.setText("");

        Snackbar snackbar =
                Snackbar.make(drawerLayout, "Data inserted.", Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        } else {
        }
    }

    private void initview() {
        //relative
        relativeLayout = findViewById(R.id.relativelayout);
        //drawer layout
        drawerLayout = findViewById(R.id.drawer_layout);
        //edit text
        barcodenameEdit = findViewById(R.id.productnameTxt);
        barcodeEpireDateET = findViewById(R.id.productExprieTxt);
        barcodeManualEt = findViewById(R.id.productManualBarcodeTxt);
        barcodeQualityEt = findViewById(R.id.productquantityTxt);
        enterBarcoedET = findViewById(R.id.productEnterBarcodeTxt);
        //buttons
        addBarcodeDetails = findViewById(R.id.addOrremovBtn);
        barcodeUpdateBtn = findViewById(R.id.updateBarcodeBtn);

        //TODO:- set To button click
        barcodenameEdit.setText("");

        barcodeEpireDateET.setText("");
        barcodeEpireDateET.setEnabled(true);

        barcodeManualEt.setText("");
        barcodeManualEt.setEnabled(true);

        barcodeQualityEt.setText("");

        enterBarcoedET.setText("");
        enterBarcoedET.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_barcodeData) {
            askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXST);
            startActivity(new Intent(MainActivity.this, BarcodeListItems.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void handleResult(Result result) {

        enterBarcoedET.setText(result.getText());
        if (!enterBarcoedET.getText().toString().isEmpty()) {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                assert v != null;
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                assert v != null;
                v.vibrate(500);
            }
        }

        zXingScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {

            Snackbar snackbar =
                    Snackbar.make(drawerLayout, "Permission granted.", Snackbar.LENGTH_SHORT);
            snackbar.show();
        } else {
            Snackbar snackbar =
                    Snackbar.make(drawerLayout, "Permission denied.", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }


}
