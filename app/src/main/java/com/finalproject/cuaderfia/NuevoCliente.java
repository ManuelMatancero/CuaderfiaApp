package com.finalproject.cuaderfia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.finalproject.cuaderfia.dbaroom.dao.ClientesDAO;
import com.finalproject.cuaderfia.dbaroom.database.AppDatabase;
import com.finalproject.cuaderfia.dbaroom.entidades.Clientes;
import com.hbb20.CountryCodePicker;

import java.util.Objects;

public class NuevoCliente extends AppCompatActivity {


    private EditText nombre, direccion, telefono;
    private Button agregar, cancelar;
    private CountryCodePicker codePicker;

    private Handler handler;
    AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_cliente);

        db = AppDatabase.getInstance(this.getApplicationContext());
        //Activar el boton de ir atras en el actionBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        codePicker = findViewById(R.id.countryCode_picker);
        nombre = findViewById(R.id.nombre);
        direccion = findViewById(R.id.direccion);
        telefono = findViewById(R.id.telefono);
        telefono.addTextChangedListener(new PhoneNumberFormattingTextWatcher());


        agregar = findViewById(R.id.guardar);
        cancelar = findViewById(R.id.cancelar);


//////////////////////agregar cliente a la base de datos///////////////////////////////////////
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nombre.getText().toString().trim().isEmpty()) {
                    nombre.setError("¡El cliente debe tener un nombre!");
                    Toast.makeText(getApplicationContext(), "Al menos debe tener un NOMBRE", Toast.LENGTH_SHORT).show();


                } else {

                    String code = codePicker.getSelectedCountryCode().toString();

                    ClientesDAO clientesDAO = db.clientesDAO();
                    Clientes cliente = new Clientes();
                    cliente.setNombre(nombre.getText().toString());
                    cliente.setDireccion(direccion.getText().toString());
                    cliente.setCelular(code +" "+ telefono.getText().toString());

                    clientesDAO.insert(cliente);
                    Toast.makeText(getApplicationContext(), "Cliente Agregado Correctamente", Toast.LENGTH_SHORT).show();
                    finish();


                }

            }
        });

        //////////////////////////////////////////////////////////////////////////////////////


        //////////////////cancelar//////////////
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        //////////////////////
    }
    //Metodo para ir hacia atras desde el actionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { //hago un case por si en un futuro agrego mas opciones
            Log.i("ActionBar", "Atrás!");
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
