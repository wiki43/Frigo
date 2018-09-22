package hotbitinfotech.com.frigo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        holder.Namebarcode.setText(model.getpName());
        holder.Numberbarcode.setText(model.getpBarcodeNumber());
        holder.Datebarcode.setText(model.getpBarcodeExp());
        holder.Quantitybarcode.setText(model.getpBarcodeQuant());

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

        //TODO:- click on barcode cardview
        holder.barcodeCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("barNumberKEY", model.getpBarcodeNumber());
                intent.putExtra("barNameKEY", model.getpName());
                intent.putExtra("barDateKEY", model.getpBarcodeExp());
                intent.putExtra("barQuantityKEY", model.getpBarcodeQuant());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return barcodeArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView Numberbarcode, Namebarcode, Datebarcode, Quantitybarcode;
        public ImageView deleteBarcode;
        public CardView barcodeCardview;

        public MyViewHolder(View view) {
            super(view);
            Numberbarcode = view.findViewById(R.id.productManualBarcodext);
            Namebarcode = view.findViewById(R.id.productNameTxt);
            Datebarcode = view.findViewById(R.id.productDate);
            Quantitybarcode = view.findViewById(R.id.productquantityTxt);
            deleteBarcode = view.findViewById(R.id.deleteImgBarcode);
            barcodeCardview = view.findViewById(R.id.cardView);
        }
    }


}
