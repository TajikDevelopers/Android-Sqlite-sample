package sword.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sword.data.models.Contact;

/**
 * Created by Sword on 13.12.2014.
 */
public class Db {

    private String tableName = "AddressBook";
    Context context;
    String DB_PATH;

    public Db(Context ctx) {
        context = ctx;
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        File file = new File(DB_PATH);

            if(!file.exists())
                file.mkdir();

        context = ctx;
    }


    private SQLiteDatabase GetDb() {
        SQLiteDatabase myDB = null;
        try {
            myDB = SQLiteDatabase.openOrCreateDatabase(DB_PATH + "/AddressBook.sqlite", null, null);
            myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                    + tableName
                    + " (Id INTEGER PRIMARY KEY  AUTOINCREMENT, Name VARCHAR, Phone VARCHAR, Address VARCHAR);");
        } catch (Exception ex) {
            Log.e("Error", "Error", ex);
        }
        return myDB;
    }

    public void Insert(Contact contact) {
        SQLiteDatabase myDb = GetDb();

        String sql = "INSERT INTO " + tableName +
                " (Name,Phone,Address) VALUES (" +
                "'" + contact.name + "'," +
                "'" + contact.phone + "'," +
                "'" + contact.address + "'" +
                ");";

        myDb.execSQL(sql);

        if (myDb != null)
            myDb.close();
    }

    public List<Contact> GetAll() {
        List<Contact> contactList = new ArrayList<Contact>();
        String sql = "SELECT  * FROM " + tableName + " ORDER BY Name";
        SQLiteDatabase myDb = GetDb();
        Cursor cursor = myDb.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.id = (Integer.parseInt(cursor.getString(cursor.getColumnIndex("Id"))));
                contact.name = (cursor.getString(cursor.getColumnIndex("Name")));
                contact.phone = (cursor.getString(cursor.getColumnIndex("Phone")));
                contact.address = (cursor.getString(cursor.getColumnIndex("Address")));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        return contactList;
    }

    public int GetCount()
    {
        SQLiteDatabase myDb = GetDb();
        String sql = "SELECT Id FROM " + tableName;
        Cursor cursor = myDb.rawQuery(sql,null);
        return cursor.getCount();
    }
    public void Update(Contact contact) {
        SQLiteDatabase myDb = GetDb();
        String sql = "UPDATE " + tableName + " SET Name='" + contact.name + "'," +
                "Phone='" + contact.phone + "'," +
                "Address = '" + contact.address + "'" +
                " WHERE Id=" + contact.id;
        myDb.execSQL(sql);
    }

    public void Delete(int id) {
        SQLiteDatabase myDb = GetDb();
        String sql = "DELETE FROM " + tableName + " WHERE Id=" + id;
        myDb.execSQL(sql);
    }

}
