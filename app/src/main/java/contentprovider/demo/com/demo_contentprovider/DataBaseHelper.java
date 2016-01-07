package contentprovider.demo.com.demo_contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gama on 1/4/16.
 */
public class DataBaseHelper extends SQLiteOpenHelper{


    private SQLiteDatabase sqlDb;
    static final String DATABASE_NAME = "mydb";
    static final String STUDENTS_TABLE_NAME = "students";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DATABSE = " CREATE TABLE " + STUDENTS_TABLE_NAME +
            " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " name TEXT NOT NULL, " +
            " grade TEXT NOT NULL);";

    public DataBaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_DATABSE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " +  STUDENTS_TABLE_NAME);
        onCreate(db);

    }
}
