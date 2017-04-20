package org.sysmob.biblivirti.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.sysmob.biblivirti.R;

/**
 * Created by micro99 on 20/04/2017.
 */

public class EditarSenhaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_recuperacao);

        // Habilita o botao de voltar na actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        // Carrega os widgets da tela
//        loadWidgets();
//
//        // Carrega os listeners do widgets
//        loadListeners();
    }
}
