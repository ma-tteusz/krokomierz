package pl.edu.wat.wel.krokomierz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class DataActivity extends AppCompatActivity {

    SeekBar seekBar;
    SeekBar seekBar2;
    int numberone;
    int numbertwo;
    TextView Waga;
    TextView Wzrost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        Waga = (TextView) findViewById(R.id.textWaga);
        seekBar = (SeekBar) findViewById(R.id.seekbarwaga);
        seekBar.setOnSeekBarChangeListener(seekBarListener);
        numberone = 0;
        Waga.setText(String.valueOf(numberone));

        Wzrost = (TextView) findViewById(R.id.textWzrost);
        seekBar2 = (SeekBar) findViewById(R.id.seekbarwzrost);
        seekBar2.setOnSeekBarChangeListener(seekBarListener2);
        numbertwo = 0;
        Wzrost.setText(String.valueOf(numbertwo));

    }

    private SeekBar.OnSeekBarChangeListener seekBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            numberone = seekBar.getProgress();
            Waga.setText(String.valueOf(numberone));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private SeekBar.OnSeekBarChangeListener seekBarListener2 = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            numbertwo = seekBar.getProgress();
            Wzrost.setText(String.valueOf(numbertwo));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


    public void mainactivity(View view) {
        Intent getDane = new Intent(this, MainActivity.class);
        getDane.putExtra("danewzrostu", numbertwo);
        getDane.putExtra("danewagi", numberone);
        startActivity(getDane);
    }
}
