package com.example.ex026;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The type Credits.
 *
 * @author Tahel Hazan <th8887@bs.amalnet.k12.il>
 * @version 1.1.6
 * @since 11.12.2020  The credit page shows the author and ways to contact him in case something doesn't work.
 */
public class Credits extends AppCompatActivity {
    TextView credits;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        credits=(TextView) findViewById(R.id.credits);

        SharedPreferences settings= getSharedPreferences("stm", MODE_PRIVATE);
        SharedPreferences.Editor editor=settings.edit();
        String s = "Author: Tahel Hazan            Contact= th8887@bs.amalnet.k12.il        " +
                "    version 1.1.4        since 25.11.2020   contact if something doesn't work!";
        editor.putString("contact",s);
        editor.commit();
        credits.setText(settings.getString("contact",null));
    }

    /**
     * Fin.
     *
     * @param item The user maves back to Main Activity(filling personal information) activity.
     */
    public void fin(MenuItem item) {
        Intent c= new Intent(this,MainActivity.class);
        startActivity(c);
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
     * @param item the item Sends a Toast to let the user know he is in the current page he chose from the OptionMenu.
     */
    public void cred(MenuItem item) {
        Toast.makeText(this, "You are already here :)", Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
