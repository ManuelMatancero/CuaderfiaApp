package com.finalproject.cuaderfia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class EditarContacto extends AppCompatActivity {
    EditText EDTNombre, EDTDireccion, EDTTelefono;
    Button Actualizar, Cancelar;

    public String dameNombre, dameDireccion, dameTelefono;

    private AppDatabase db;
    private ClientesDAO clientesDAO;
    private Clientes cliente;
    private int idCliente = MainActivity.clientesData.getId_Cliente();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_contacto);

        ////////////Instancia de la base de datos/////////////////////
        db = AppDatabase.getInstance(EditarContacto.this);
        clientesDAO= db.clientesDAO();
        ///////////////////////////////////////////////////////////

        EDTNombre = findViewById(R.id.EDTnombre);
        EDTDireccion = findViewById(R.id.EDTdireccion);
        EDTTelefono = findViewById(R.id.EDTtelefono);
        Actualizar = findViewById(R.id.ActualizarCliente);
        Cancelar = findViewById(R.id.cancelarEdiatr);
        EDTTelefono.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        cliente = new Clientes();

        cliente = clientesDAO.getCliente(idCliente);
        EDTNombre.setText(cliente.getNombre());
        EDTDireccion.setText(cliente.getDireccion());
        EDTTelefono.setText(cliente.getCelular());


////////////////////////////Actualizar datos de cliente////////////////////////////////////////////////////////////
        Actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (EDTNombre.getText().toString().trim().isEmpty()) {
                    EDTNombre.setError("El Cliente debe tener NOMBRE");
                    Toast.makeText(getApplicationContext(), "El Cliente debe tener NOMBRE", Toast.LENGTH_LONG).show();

                } else {

                    dameNombre = EDTNombre.getText().toString();
                    dameDireccion =EDTDireccion.getText().toString();
                    dameTelefono= EDTTelefono.getText().toString();
                    clientesDAO.updateCliente(dameNombre, dameDireccion, dameTelefono, idCliente);
                    Toast.makeText(getApplicationContext(), "Cliente Actualizado", Toast.LENGTH_LONG).show();
                    onBackPressed();

                }


            }
        });
        /////////////////////////////////////////////////////////////////////////////////////////

        //////////////////cancelar//////////////
        Cancelar.setOnClickListener(new View.OnClickListener() {
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