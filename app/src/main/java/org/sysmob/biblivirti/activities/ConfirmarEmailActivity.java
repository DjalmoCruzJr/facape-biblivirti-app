package org.sysmob.biblivirti.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.sysmob.biblivirti.R;

public class ConfirmarEmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_email);
    }

    public void clickReenviarToken(View view) {
        Toast.makeText(this, "Esta funcionalidade ainda não foi implementada!", Toast.LENGTH_SHORT).show();
    }

    public void clickValidarToken(View view) {
        Toast.makeText(this, "Esta funcionalidade ainda não foi implementada!", Toast.LENGTH_SHORT).show();
    }
}
