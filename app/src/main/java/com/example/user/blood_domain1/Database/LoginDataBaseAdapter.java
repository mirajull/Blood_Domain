package com.example.user.blood_domain1.Database;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.user.blood_domain1.Models.User;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class LoginDataBaseAdapter
{
    static final String DATABASE_NAME = "login.db";
    static final int DATABASE_VERSION = 1;
    public static final int NAME_COLUMN = 1;

    // SQL Statement to create a new database.
    static final String DATABASE_CREATE = "create table "+"LOGIN"+
            "( " +"ID"+" integer primary key autoincrement,"+ "USERNAME  text,PASSWORD text," +
            " ADDRESS text, LAT real, LANG real, BLOODGROUP text ); ";
    // Variable to hold the database instance
    public static  SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DataBaseHelper dbHelper;

    public static String username=null;


    public  LoginDataBaseAdapter(Context _context)
    {
        context = _context;
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public  LoginDataBaseAdapter open() throws SQLException
    {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public  SQLiteDatabase getDatabaseInstance()
    {
        return db;
    }

    public static String insertEntry(User user)
    {
        Cursor cursor=db.query("LOGIN", null, " USERNAME=?", new String[]{user.getUsername()}, null, null, null);
        if(cursor.getCount()>0) // UserName Not Exist
        {
            cursor.close();
            return "Username has been taken. Try again.";
        }
        cursor.close();

        //db.beginTransaction();
        ContentValues newValues = new ContentValues(6);
        // Assign values for each row.
        newValues.put("USERNAME", user.getUsername());
        newValues.put("PASSWORD",user.getPassword());
        newValues.put("ADDRESS",user.getAddress());
        newValues.put("LAT",user.getLat());
        newValues.put("LANG",user.getLang());
        newValues.put("BLOODGROUP",user.getBloodGroup());
        // Insert the row into your table
        db.insert("LOGIN", null, newValues);
        ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
        //db.endTransaction();
        return "Account Successfully Created";
    }
    public int deleteEntry(String UserName)
    {
        //String id=String.valueOf(ID);
        String where="USERNAME=?";
        int numberOFEntriesDeleted= db.delete("LOGIN", where, new String[]{UserName}) ;
        // Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
    }

    public static String getSinlgeEntry(String userName)
    {
        username=userName;
        //db.beginTransaction();
        Cursor cursor=db.query("LOGIN", null, " USERNAME=?", new String[]{userName}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex("PASSWORD"));
        cursor.close();
        //db.endTransaction();

        return password;
    }


    public static ArrayList<User> getUsersLocation(double lat, double lang,String bloodgroup)
    {
        ArrayList<User> latLngs= new ArrayList<User>();
        String[] params= new String[]{username ,bloodgroup,String.valueOf(lat-0.02),String.valueOf(lat+0.02),
                String.valueOf(lang-0.02),String.valueOf(lang+0.02)};
        Log.d("Blood_Group", bloodgroup);

        Cursor cursor=db.query("LOGIN", null, "USERNAME!=? and BLOODGROUP=? and LAT>? and LAT<? and LANG>? and LANG<? ",params , null, null, null);
        if(cursor.getCount()<1)
        {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            String name= cursor.getString(cursor.getColumnIndex("USERNAME"));
            double t1 = cursor.getDouble(cursor.getColumnIndex("LAT"));
            double t2 = cursor.getDouble(cursor.getColumnIndex("LANG"));

            Log.d("lat-long", "" + t1 + "......." + t2);

            User latLng= new User(name,"","",t1,t2,"");
            latLngs.add(latLng);
        }
        cursor.close();
        return latLngs;
    }


    public void  updateEntry(String userName,String password, String address)
    {
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues(3);
        // Assign values for each row.
        updatedValues.put("USERNAME", userName);
        updatedValues.put("PASSWORD",password);
        updatedValues.put("ADDRESS",address);

        String where="USERNAME = ?";
        db.update("LOGIN",updatedValues, where, new String[]{userName});
    }
}