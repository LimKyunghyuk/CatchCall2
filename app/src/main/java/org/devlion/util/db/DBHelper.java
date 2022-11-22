package org.devlion.util.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBHelper extends DBConfig {

    private SQLiteDatabase db;
    private DatabaseHelper helper;
    private Context context;

    public DBHelper(Context context){
        this.context = context;
    }

    public DBHelper open() throws SQLException {
        helper = new DatabaseHelper(context, DATABASE_NAME, null, _DATABASE_VERSION);
        db = helper.getWritableDatabase();
        return this;
    }

    public void close(){
        db.close();
    }

    public void create(){
        helper.onCreate(db);
    }

    public long insert(CallLogVo callLogVo){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        ContentValues values = new ContentValues();
        values.put(LOG_DT, dateFormat.format(date));

        values.put(CELL_PHONE, callLogVo.getCellPhone());
        values.put(COMPANY_PHONE, callLogVo.getCompanyPhone());
        values.put(COMPANY_CODE, callLogVo.getCompanyCode());
        values.put(AD_DISPLAY_NAME, callLogVo.getAddisplayName());
        values.put(CHARGE_JOB, callLogVo.getChargeJob());
        values.put(MAIN_DEPT_CODE, callLogVo.getMainDeptCode());
        values.put(COMPANY_NAME, callLogVo.getCompanyName());
        values.put(DEPT_NAME_NAME, callLogVo.getDeptName());

        return db.insert(TABLE_NAME, null, values);
    }

    public Cursor selectColumns(){
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public void deleteLog(String logId){

        String query = "DELETE FROM " + TABLE_NAME + " WHERE LOG_ID = '" + logId + "';";
        db.execSQL(query);
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(CREATE_QUERY);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
