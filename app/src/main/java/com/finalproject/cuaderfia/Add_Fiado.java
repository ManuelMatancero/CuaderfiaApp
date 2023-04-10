package com.finalproject.cuaderfia;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.cuaderfia.dbaroom.dao.FiadosDAO;
import com.finalproject.cuaderfia.dbaroom.database.AppDatabase;
import com.finalproject.cuaderfia.dbaroom.entidades.Fiados;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Add_Fiado extends AppCompatActivity {

    EditText desc, total;
    Button add;
    public Date today;
    public SimpleDateFormat format1;
    String dateToStr1;
    private AppDatabase db;
    private FiadosDAO fiadosDAO;
    private Fiados fiado;
    private TextView txtFormat;

    NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fiado);

        txtFormat = findViewById(R.id.txtValorFormat);
        desc = findViewById(R.id.Descripciones);

        total = findViewById(R.id.TotalCompra);

        add = findViewById(R.id.agregar);

        ////Instanciamos la base de datos/////////////////////////////////////////////////////////////////////////
        db= AppDatabase.getInstance(getApplicationContext());
        fiadosDAO = db.fiadosDAO();


        /////////codigo de obtener fecha actual////////////////////////////////////////////////////////////////////////
        today = new Date();
        format1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dateToStr1 = format1.format(today);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        ////Codigo para pasar texto de historial de fiados al area de actualizacion/////////////

///////////////////////////////////////////////////////////////////////////////////////////////////////


        // priceClick(total);

        ///codigo de agregar fiado y su descripcion////////////////////////////////////////////////////////////////////////////////
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (total.getText().toString().trim().isEmpty()) {
                    total.setError("Debe agregar un MONTO");
                    Toast.makeText(getApplicationContext(), "Debe agregar un MONTO", Toast.LENGTH_SHORT).show();

                } else {

                    AlertDialog alertDialog = new AlertDialog.Builder(Add_Fiado.this).create();
                    alertDialog.setTitle("Agregar deuda");
                    alertDialog.setMessage("Agregar la deuda de " + total.getText().toString() + " al cliente: " + MainActivity.clientesData.getNombre() + " ");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Aceptar",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Almaceno los datos del objeto estatico del homeF en un objeto de tipo Fiados para luego insertarlos en la tabla
                                    fiado = new Fiados();
                                    fiado.setId_Cliente(MainActivity.clientesData.getId_Cliente());
                                    fiado.setDescripcion(desc.getText().toString());
                                    fiado.setMonto(Double.parseDouble(total.getText().toString()));
                                    fiado.setFecha(dateToStr1);
                                    fiadosDAO.insert(fiado);

                                    Add_Fiado.this.finish();
                                }
                            });
                    alertDialog.show();

                    //
                }
            }
        });

        total.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.toString().trim().length()==0){return;}
                double totales = Double.parseDouble(total.getText().toString());
                txtFormat.setText(defaultFormat.format(totales));

            }
        });


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