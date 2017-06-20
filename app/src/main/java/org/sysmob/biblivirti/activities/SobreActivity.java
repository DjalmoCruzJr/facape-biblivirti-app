package org.sysmob.biblivirti.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.sysmob.biblivirti.R;
import org.sysmob.biblivirti.business.AccountBO;
import org.sysmob.biblivirti.exceptions.ValidationException;
import org.sysmob.biblivirti.model.Usuario;
import org.sysmob.biblivirti.utils.BiblivirtiUtils;

public class SobreActivity extends AppCompatActivity {

    private ImageButton buttonVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);

        // Carrega os widgets da tela
        loadWidgets();

        // Carrega os listeners do widgets
        loadListeners();
    }

    /********************************************************
     * PRIVATE METHODS
     *******************************************************/
    private void enableWidgets(boolean status) {
    }

    private void loadWidgets() {
        this.buttonVoltar = (ImageButton) findViewById(R.id.buttonVoltar);
    }

    private void loadListeners() {
        this.buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
