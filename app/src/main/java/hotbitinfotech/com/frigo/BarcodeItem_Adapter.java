package hotbitinfotech.com.frigo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class BarcodeItem_Adapter extends RecyclerView.Adapter<BarcodeItem_Adapter.MyViewHolder> {

    private ArrayList<Model> barcodeArrayList;
    private Context context;
    private DatabaseHelper db;

    public BarcodeItem_Adapter(BarcodeListItems barcodeListItems, ArrayList<Model> arrayListBarcode) {
        this.context = barcodeListItems;
        this.barcodeArrayList = arrayListBarcode;
        this.db = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trash_barcode_product_structure, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Model model = barcodeArrayList.get(position);

        holder.Numberbarcode.setText(model.getpBarcodeNumber());
        holder.Datebarcode.setText(model.getpBarcodeExp());

        holder.Namebarcode.setText(model.getpName());
        holder.Quantitybarcode.setText(model.getpBarcodeQuant());

        //TODO:- click event
            //editext name.
        holder.Namebarcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e(TAG, "beforeTextChanged: "+charSequence.toString() );
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e(TAG, "onTextChanged: "+charSequence.toString() );
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.e(TAG, "afterTextChanged: "+editable.toString() );
                int updateName = db.updateBarcodeName(model.getpBarcodeNumber(), editable.toString());
                if(updateName>0){
                    Toast.makeText(context, "Barcode Name Updated.", Toast.LENGTH_SHORT).show();
                }else {
                    Log.e(TAG, "update info :- barcode name not updated. " );
                }
            }
        });

            //quantity.
        holder.Quantitybarcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e(TAG, "beforeTextChanged: "+charSequence.toString() );
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e(TAG, "onTextChanged: "+charSequence.toString() );
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.e(TAG, "afterTextChanged: "+editable.toString() );
                int updateName = db.updateBarcodeQuantity(model.getpBarcodeNumber(), editable.toString());
                if(updateName>0){
                    Toast.makeText(context, "Barcode Quantity Updated.", Toast.LENGTH_SHORT).show();
                }else {
                    Log.e(TAG, "update info :- barcode Quantity not updated. " );
                }
            }
        });

        //TODO:- click event on delete barcode icon.
        holder.deleteBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int countDel = db.delete_SingleData(model.getpBarcodeNumber());
                if (countDel > 0) {
                    Toast.makeText(context, "Deleted.", Toast.LENGTH_SHORT).show();

                    notifyDataSetChanged();

                    Intent intent = new Intent(context, BarcodeListItems.class);
                    context.startActivity(intent);
                } else {
                    Log.e(TAG, "delete image btn :- something error.");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return barcodeArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView Numberbarcode, Datebarcode;
        public ImageView deleteBarcode;
        public CardView barcodeCardview;
        public EditText Namebarcode, Quantitybarcode;

        public MyViewHolder(View view) {
            super(view);
            Numberbarcode = view.findViewById(R.id.productManualBarcodext);
            Datebarcode = view.findViewById(R.id.productDate);
            deleteBarcode = view.findViewById(R.id.deleteImgBarcode);
            barcodeCardview = view.findViewById(R.id.cardView);
            //edit text
            Namebarcode     = view.findViewById(R.id.productNameTxt);
            Quantitybarcode = view.findViewById(R.id.productquantityTxt);
        }
    }


}
