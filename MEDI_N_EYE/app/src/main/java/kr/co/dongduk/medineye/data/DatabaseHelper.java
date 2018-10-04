package kr.co.dongduk.medineye.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Owner on 2016-08-25.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private  static final String LOG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mediDatabase";

    private static final String TABLE_NFC = "nfc";

    private static final String KEY_ID = "_id";

    //nfc table column
    private static final String NFC_ID = "name";
    private static final String NFC_TEXT = "text";

    private static final String CREATE_TABLE_NFC = "CREATE TABLE " + TABLE_NFC + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NFC_ID + " TEXT, " + NFC_TEXT + " TEXT);";


    public DatabaseHelper(Context context){super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_NFC);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + TABLE_NFC);
        onCreate(sqLiteDatabase);
    }

    public void saveNFCData(NFCData data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NFC_ID, data.getNFCId());
        values.put(NFC_TEXT, data.getNFCtext());

        db.insert(TABLE_NFC, null, values);
        Log.d("SAVE_NFC", data.getNFCId() + " : " + data.getNFCtext());
    }

    public void deleteNFCData (String nfc_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NFC + " WHERE " + NFC_ID + "='" + nfc_id+"';");
        Log.d("DELETE_NFC", nfc_id);
    }

    public void deleteAllData ()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NFC );
        Log.d("DELETE_NFC",  "");
    }

    public void updataNFCData(String nfc_id, String text){
        SQLiteDatabase db = this.getWritableDatabase();
         db.execSQL("UPDATE " + TABLE_NFC + " SET " + NFC_TEXT + "='" + text + "' WHERE " + NFC_ID + "='" + nfc_id+"';");
        Log.d("UPDATE_NFC", nfc_id);
     }

    public boolean isExistData (String nfc_id){
        boolean isExist = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NFC + " WHERE " + NFC_ID + "='" +  nfc_id + "';" , null);

        if (c.moveToNext()){
            do {
                String value = c.getString(c.getColumnIndex(NFC_ID));
                if ( value != null && value.equals(nfc_id)){
                    isExist = true;
                }
            } while (c.moveToNext());
        }
        Log.d("ISEXISTDATA", ""+isExist);
        return isExist;
    }
    public ArrayList<NFCData> getNFCList ()
    {
        ArrayList<NFCData> data = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NFC + ";" , null);

        while (c.moveToNext()){
            String id = c.getString(c.getColumnIndex(NFC_ID));
            String text = c.getString(c.getColumnIndex(NFC_TEXT));

            data.add(new NFCData(id, text));
        }
        Log.d("getNFCList", ""+data.size());
        return data;
    }
}
