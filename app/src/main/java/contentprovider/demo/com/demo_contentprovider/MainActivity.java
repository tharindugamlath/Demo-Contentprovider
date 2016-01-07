package contentprovider.demo.com.demo_contentprovider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    static final Uri CONTENT_URL = Uri.parse("content://contentprovider.demo.com.demo_contentprovider.StudentsProvider/students");

    EditText studentId;
    EditText name;
    EditText grade;
    TextView contacts;


    CursorLoader cursorLoader;
    ContentResolver contentResolver;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        studentId = (EditText) findViewById(R.id.studentId);
        name = (EditText) findViewById(R.id.name);
        grade = (EditText) findViewById(R.id.grade);
        contacts= (TextView) findViewById(R.id.showContactDetails);

        contentResolver = getContentResolver();

        getAllContacts();
    }

    public void addStudent(View view) {

        String _name=name.getText().toString();
        String _grade = grade.getText().toString();

        ContentValues values = new ContentValues();
        values.put(StudentsProvider.NAME,_name);
        values.put(StudentsProvider.GRADE, _grade);

        Uri uri = getContentResolver().insert(StudentsProvider.CONTENT_URI,values);

        Toast.makeText(getApplicationContext(), "New Student has Added !!!", Toast.LENGTH_SHORT).show();

       getAllContacts();
        name.setText("");
        grade.setText("");

    }

    public void getAllContacts(){

            String[] projection = new String[]{"_id", "name","grade"};
        Cursor cursor =contentResolver.query(CONTENT_URL,projection,null,null,null);
        String contactList="";

        if (cursor.moveToFirst()){

            do {
                String id = cursor.getString(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String grade = cursor.getString(cursor.getColumnIndex("grade"));

                contactList = contactList + id + " ." + name + "-" + grade + "\n";
            }while (cursor.moveToNext());
        }

        contacts.setText(contactList);
    }


    public void deleteStudent(View view) {

        String selectedId = studentId.getText().toString();
        long idDelete = contentResolver.delete(CONTENT_URL,"_id = ?",new String[]{selectedId});
        Toast.makeText(getApplicationContext(),"Student has deleted !!!",Toast.LENGTH_SHORT).show();
        getAllContacts();
        studentId.setText("");
    }
}
