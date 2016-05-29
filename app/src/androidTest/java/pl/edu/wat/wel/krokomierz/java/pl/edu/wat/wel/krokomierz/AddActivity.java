package pl.edu.wat.wel.krokomierz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    private TextView kroki;
    private EditText nazwakrokow;

    Calendar c = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
    String formattedDate = df.format(c.getTime());

    int danewzrostu;
    int danewagi;
    int numsteps;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        kroki = (TextView) findViewById(R.id.numberofsteps);
        nazwakrokow = (EditText) findViewById(R.id.nazwakrokow);

        Intent getStats = getIntent();
        numsteps =  getStats.getIntExtra("liczbakrokow", 0);
        danewzrostu = getStats.getIntExtra("danewzrostu", 0);
        danewagi = getStats.getIntExtra("danewagi", 0);
        TextView numberofsteps = (TextView) findViewById(R.id.numberofsteps);
        String liczbakrokow = String.valueOf(numsteps);
        numberofsteps.setText(liczbakrokow);


    }

    public void onClick(View view) {
        String textToSave = kroki.getText().toString();
        String textToSave2 = nazwakrokow.getText().toString();

        if (!textToSave.isEmpty()&&!textToSave2.isEmpty()) {
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(getFilesDir().getPath() + "/recycleview.txt", true);
                fileWriter.write(textToSave);
                fileWriter.write("\n");
                fileWriter.write(formattedDate);
                fileWriter.write("\n");
                fileWriter.write(textToSave2);
                fileWriter.write("\n");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (!textToSave.isEmpty()) {
            FileWriter kroki = null;
            try {
                kroki = new FileWriter(getFilesDir().getPath() + "/kroki.txt", true);
                kroki.write(textToSave);
                kroki.write("\n");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (kroki != null) {
                    try {
                        kroki.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Toast.makeText(this, "Dodano do listy", Toast.LENGTH_SHORT).show();
        Intent getDane = new Intent(this, MainActivity.class);
        getDane.putExtra("danewzrostu", danewzrostu);
        getDane.putExtra("danewagi", danewagi);
        getDane.putExtra("Z", 1);
        getDane.putExtra("numsteps", numsteps);
        startActivity(getDane);
        finish();
    }
}
