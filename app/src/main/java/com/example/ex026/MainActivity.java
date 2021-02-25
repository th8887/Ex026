package com.example.ex026;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

/**
 * The type Main activity.
 *
 * @author Tahel Hazan <th8887@bs.amalnet.k12.il>
 * @version 1.1.6
 * @since 11.12.2020  This activity is part 1 of entering information- the user inserts personal information about students.
 */
public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;
    HelperDB hlp;
    ContentValues cv= new ContentValues();
    EditText name, phoneP, add, phoneH, nameM, phoneM, nameF, phoneF;
    String nP, a, nM, nF, pP, pM, pF, pH;
    /**
     * @param d - define for int.
     */
    AdapterView d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name=(EditText)findViewById(R.id.name);
        phoneP=(EditText)findViewById(R.id.phoneP);
        add=(EditText)findViewById(R.id.add);
        phoneH=(EditText)findViewById(R.id.phoneH);
        nameM=(EditText)findViewById(R.id.nameM);
        phoneM=(EditText)findViewById(R.id.phoneM);
        nameF=(EditText)findViewById(R.id.nameF);
        phoneF=(EditText)findViewById(R.id.phoneF);

        hlp=new HelperDB(this);
        db=hlp.getWritableDatabase();
        db.close();
    }

    /**
     * Submit.
     *
     * @param view
     * collects all the information from TextViews and checks for errors at the inputs, then takes
     * the information and adds it to the SQlite Database for student's personal information.
     */
    public void submit(View view) {
        pP = phoneP.getText().toString();
        pH = phoneH.getText().toString();
        nP = name.getText().toString();
        a = add.getText().toString();
        nM= nameM.getText().toString();
        pM=phoneM.getText().toString();
        nF = nameF.getText().toString();
        pF = phoneF.getText().toString();
        if ((a.equals("")) || (nP.equals("")) || (pH.equals("")) || (pP.equals(""))||(nM.equals(""))||(pM.equals(""))||
                (nF.equals(""))||(pF.equals(""))) {
            Toast.makeText(this, "enter information according to hint and submit, if there is no info enter '---' ", Toast.LENGTH_LONG).show();
        }
        else {
            cv.put(Students.NAME, nP);
            cv.put(Students.PHONES,pP);
            cv.put(Students.PHONEH,pH);
            cv.put(Students.ADDRESS, a);
            cv.put(Students.NAMEM, nM);
            cv.put(Students.PHONEM, pM);
            cv.put(Students.NAMEF, nF);
            cv.put(Students.PHONEF, pF);
            cv.put(Students.ACTIVE,1);

            db = hlp.getWritableDatabase();
            db.insert(Students.TABLE_STUDENTS, null, cv);
            db.close();
            name.setText("");
            phoneP.setText("");
            add.setText("");
            phoneH.setText("");
            nameM.setText("");
            phoneM.setText("");
            nameF.setText("");
            phoneF.setText("");
        }
    }

    /**
     * Fin.
     *
     * @param item The item Sends a Toast to let the user know he is in the current page he chose from the OptionMenu.
     */
    public void fin(MenuItem item) {
        Toast.makeText(this, "You are already here :)", Toast.LENGTH_SHORT).show();
    }

    /**
     * Fing.
     *
     * @param item Moves user to filling grades activity.
     */
    public void fing(MenuItem item) {
        Intent c= new Intent(this,Filling_Grades.class);
        startActivity(c);
    }

    /**
     * Pn.
     *
     * @param item Moves the user to Personal_info activity.
     */
    public void org(MenuItem item) {
        Intent c= new Intent(this,Organize.class);
        startActivity(c);
    }

    /**
     * Qg.
     *
     * @param item Moves user to Grades_info activity.
     */
    public void sad(MenuItem item) {
        Intent c= new Intent(this,ShowAndDelete.class);
        startActivity(c);
    }

    /**
     * Cred.
     *
     * @param item Moves the user to credits activity.
     */
    public void cred(MenuItem item) {
        Intent c= new Intent(this,Credits.class);
        startActivity(c);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}