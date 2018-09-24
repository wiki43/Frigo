package hotbitinfotech.com.frigo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "frigo_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //create table
        sqLiteDatabase.execSQL(Note.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //TODO:- drop table if already exist
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NAME);

        //TODO:- create table
        onCreate(sqLiteDatabase);
    }

    public long insertBarcodeData(String prodcutName, String BarcodeExpireDate, String ManualBarcode, String productQuantity, String productTrash) {
        //TODO:- create writable database
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Note.COLUMN_NAME, prodcutName);
        values.put(Note.COLUMN_EXPIRE_DATA, BarcodeExpireDate);
        values.put(Note.COLUMN_MANUAL_BARCODE, ManualBarcode);
        values.put(Note.COLUMN_QUANTITY, productQuantity);
        values.put(Note.COLUMN_TRASH, productTrash);

        //TODO:- insert row
        long id = db.insert(Note.TABLE_NAME, null, values);

        //TODO:- close db
        db.close();

        //TODO:- return new id when data inserted
        return id;
    }

    public Note getBarcodeData(long id) {
        //get readable data base.
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(Note.TABLE_NAME,
                new String[]{Note.COLUMN_ID, Note.COLUMN_NAME, Note.COLUMN_EXPIRE_DATA, Note.COLUMN_MANUAL_BARCODE, Note.COLUMN_QUANTITY, Note.COLUMN_TRASH},
                Note.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);

        if (cursor != null)
            cursor.moveToFirst();

        //prepare note class object
        Note note = new Note(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID))
                , cursor.getString(cursor.getColumnIndex(Note.COLUMN_NAME))
                , cursor.getString(cursor.getColumnIndex(Note.COLUMN_EXPIRE_DATA))
                , cursor.getString(cursor.getColumnIndex(Note.COLUMN_MANUAL_BARCODE))
                , cursor.getString(cursor.getColumnIndex(Note.COLUMN_QUANTITY))
                , cursor.getString(cursor.getColumnIndex(Note.COLUMN_TRASH))
        );

        //close database connection
        cursor.close();

        return note;
    }

    public List<Note> getAllBarcodeData() {
        SQLiteDatabase db = getWritableDatabase();

        List<Note> notesArraylist = new ArrayList<Note>();

        //TODO:- :-comment code is old code.
        //select query to get all data

        String selectQuary = "SELECT * FROM " + Note.TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQuary, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)));
                note.setName(cursor.getString(cursor.getColumnIndex(Note.COLUMN_NAME)));
                note.setEpiredata(cursor.getString(cursor.getColumnIndex(Note.COLUMN_EXPIRE_DATA)));
                note.setManualbarcode(cursor.getString(cursor.getColumnIndex(Note.COLUMN_MANUAL_BARCODE)));
                note.setQuantity(cursor.getString(cursor.getColumnIndex(Note.COLUMN_QUANTITY)));

                notesArraylist.add(note);
            } while (cursor.moveToNext());
        }

        //TODO:- close connection
        db.close();

        return notesArraylist;

    }

    public List<Note> getCheckBarcodeAvilablity(String barcodeN) {
        //String countQuery = "SELECT  * FROM " + Note.TABLE_NAME+ "WHERE "+Note.COLUMN_MANUAL_BARCODE = '"sdfsdf"';

        List<Note> notesArraylist = new ArrayList<Note>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Note.TABLE_NAME, new String[]{Note.COLUMN_ID, Note.COLUMN_NAME, Note.COLUMN_EXPIRE_DATA, Note.COLUMN_MANUAL_BARCODE, Note.COLUMN_QUANTITY, Note.COLUMN_TRASH,},
                Note.COLUMN_MANUAL_BARCODE + "=?",
                new String[]{String.valueOf(barcodeN)},
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)));
                note.setName(cursor.getString(cursor.getColumnIndex(Note.COLUMN_NAME)));
                note.setEpiredata(cursor.getString(cursor.getColumnIndex(Note.COLUMN_EXPIRE_DATA)));
                note.setManualbarcode(cursor.getString(cursor.getColumnIndex(Note.COLUMN_MANUAL_BARCODE)));
                note.setQuantity(cursor.getString(cursor.getColumnIndex(Note.COLUMN_QUANTITY)));
                note.setQuantity(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TRASH)));

                Log.e(TAG, "getCheckBarcodeAvilablity: "
                        + cursor.getString(cursor.getColumnIndex(Note.COLUMN_QUANTITY)));
                notesArraylist.add(note);

            } while (cursor.moveToNext());
        }

        int count = cursor.getCount();
        Log.e(TAG, "getCheckBarcodeAvilablity: " + count);
        cursor.close();


        // return count
        return notesArraylist;
    }

    public int updateBarcodeName(String barcodeNumber, String barcodeName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Note.COLUMN_NAME, barcodeName);

        // updating row
        int updateCount = db.update(Note.TABLE_NAME, values, Note.COLUMN_MANUAL_BARCODE + " = ?",
                new String[]{String.valueOf(barcodeNumber)});

        if (updateCount == 0) {
            Log.e(TAG, "deleteNote: Not delete. " + updateCount);
        } else {
            Log.e(TAG, "deleteNote: after delete. " + updateCount);
        }
        db.close();

        return updateCount;
    }
    public int updateBarcodeQuantity(String barcodeNumber, String quantity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Note.COLUMN_QUANTITY, quantity);

        // updating row
        int updateCount = db.update(Note.TABLE_NAME, values, Note.COLUMN_MANUAL_BARCODE + " = ?",
                new String[]{String.valueOf(barcodeNumber)});

        if (updateCount == 0) {
            Log.e(TAG, "deleteNote: Not delete. " + updateCount);
        } else {
            Log.e(TAG, "deleteNote: after delete. " + updateCount);
        }
        db.close();

        return updateCount;
    }

    //TODO:- delete single data
    public int delete_SingleData(String barcodeNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(Note.TABLE_NAME, Note.COLUMN_MANUAL_BARCODE + " = ?",
                new String[]{String.valueOf(barcodeNumber)});
        if (i == 0) {
            Log.e(TAG, "deleteNote: Not delete. " + i);
        } else {
            Log.e(TAG, "deleteNote: after delete. " + i);
        }

        db.close();
        return i;
    }
}
