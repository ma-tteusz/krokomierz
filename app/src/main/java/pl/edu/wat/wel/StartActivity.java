package pl.edu.wat.wel.krokomierz;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class StartActivity extends ActionBarActivity {

    private long ms = 0;
    private long czasPowitania = 1000;
    private boolean aktywowac = true;
    private boolean pauza = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        Thread powitanie = new Thread() {
            public void run() {
                try {
                    while (aktywowac && ms < czasPowitania) {
                        if (!pauza)
                            ms = ms + 100;
                        sleep(100);
                    }
                } catch (Exception e) {
                } finally {
                    Intent intent = new Intent(StartActivity.this, DataActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

        };
        powitanie.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}