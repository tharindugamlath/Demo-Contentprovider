package contentprovider.demo.com.demo_contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by gama on 1/4/16.
 */
public class StudentsProvider extends ContentProvider {

    static final String PROVIDER_NAME= "contentprovider.demo.com.demo_contentprovider.StudentsProvider";
    static final String URL = "content://"+PROVIDER_NAME+"/students";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final String _ID = "_id";
    static final String NAME = "name";
    static final String GRADE = "grade";

    private DataBaseHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private static HashMap<String, String> values;

    static final int uriCode=1;
    static final UriMatcher uriMacher;

    static {

        uriMacher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMacher.addURI(PROVIDER_NAME,"students",uriCode);

    }

    @Override
    public boolean onCreate() {
        dbHelper = new DataBaseHelper(getContext());
        sqLiteDatabase = dbHelper.getWritableDatabase();

        if (sqLiteDatabase != null){
            return true;
        }


        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder quarybuilder = new SQLiteQueryBuilder();
        quarybuilder.setTables(DataBaseHelper.STUDENTS_TABLE_NAME);

        switch (uriMacher.match(uri)){

            case uriCode:
                quarybuilder.setProjectionMap(values);
                break;
            default:
                throw new IllegalArgumentException("unknown"+uri);
        }



        Cursor cursor = quarybuilder.query(sqLiteDatabase,projection,selection,
                selectionArgs,null,null,sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMacher.match(uri)) {
            case uriCode:
                return "vnd.android.cursor.dir/students";

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
       long rowId= sqLiteDatabase.insert(DataBaseHelper.STUDENTS_TABLE_NAME,null,values);
        if (rowId>0){

            Uri _uri = ContentUris.withAppendedId(CONTENT_URI,rowId);
            getContext().getContentResolver().notifyChange(_uri,null);
            return _uri;
        }
        else {

            Toast.makeText(getContext(),"Insertion Failed",Toast.LENGTH_LONG).show();
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
       int rowsDelete=0;
        switch (uriMacher.match(uri)) {
            case uriCode:
                rowsDelete = sqLiteDatabase.delete(DataBaseHelper.STUDENTS_TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI for deletion: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return rowsDelete;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowsUpdate=0;

        switch (uriMacher.match(uri)) {
            case uriCode:
                rowsUpdate = sqLiteDatabase.update(DataBaseHelper.STUDENTS_TABLE_NAME,values,selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI for deletion: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return rowsUpdate;

    }
}
