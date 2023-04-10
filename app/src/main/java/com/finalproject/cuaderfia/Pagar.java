package com.finalproject.cuaderfia;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
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

import com.finalproject.cuaderfia.dbaroom.dao.ClientesDAO;
import com.finalproject.cuaderfia.dbaroom.dao.FiadosDAO;
import com.finalproject.cuaderfia.dbaroom.dao.PagosDAO;
import com.finalproject.cuaderfia.dbaroom.database.AppDatabase;
import com.finalproject.cuaderfia.dbaroom.entidades.Clientes;
import com.finalproject.cuaderfia.dbaroom.entidades.Fiados;
import com.finalproject.cuaderfia.dbaroom.entidades.Pagos;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Pagar extends AppCompatActivity {

    private EditText descripcionPagado, TotalPagado;
    private Button Aceptar, Cancelar;
    private TextView txtFormat;
    public Date today1;
    public SimpleDateFormat format2;
    private String dateToStr2;
    private AppDatabase db;
    private ClientesDAO clientesDAO;
    private Clientes cliente;
    private FiadosDAO fiadosDAO;
    private Fiados fiados;
    private PagosDAO pagosDAO;
    private Pagos pagos = new Pagos();
    private int idcliente;
    NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
    double resta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagar);

        //Activar el boton de ir atras en el actionBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        txtFormat = findViewById(R.id.txtValorFormat);
        descripcionPagado = findViewById(R.id.descripcionPagar);
        TotalPagado = findViewById(R.id.descripcionTotalPagado);
        Aceptar = findViewById(R.id.Acept);
        //Instancia de la base de datos//////////////////////////////////
        db = AppDatabase.getInstance(getApplicationContext());
        //La interfaz va a ser igual al metodo abstracto de la calse AppDatabase/////////////////////////////////////
        pagosDAO = db.pagosDAO();
        fiadosDAO = db.fiadosDAO();
        clientesDAO = db.clientesDAO();
        /////////////////////////////////////////////////////////////

        /////////codigo de obtener fecha actual////////////////////////////////////////////////////////////////////////
        today1 = new Date();
        format2 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dateToStr2 = format2.format(today1);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        idcliente = MainActivity.clientesData.getId_Cliente();

        ///Obtener resta del cliente////////////////////////////////////////////
        resta = clientesDAO.getRestaCliente(idcliente);
        ////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////////////////////////////
        TotalPagado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.toString().trim().length()==0){return;}
                double totales = Double.parseDouble(TotalPagado.getText().toString());
                txtFormat.setText(defaultFormat.format(totales));

            }
        });

        ///codigo de saldar o Pagar y su descripcion////////////////////////////////////////////////////////////////////////////////
        Aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TotalPagado.getText().toString().trim().isEmpty()) {
                    TotalPagado.setError("Debe agregar un MONTO");
                    Toast.makeText(getApplicationContext(), "Debe agregar un MONTO", Toast.LENGTH_SHORT).show();

                }else if(Double.parseDouble(TotalPagado.getText().toString())>=resta) {
                    if (Double.parseDouble(TotalPagado.getText().toString())==resta){
                        AlertDialog alertDialog = new AlertDialog.Builder(Pagar.this).create();
                        alertDialog.setTitle("CUIDADO");
                        alertDialog.setMessage("Se saldara la cuenta del cliente: " + MainActivity.clientesData.getNombre() + " ");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Aceptar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        pagosDAO.deletePagos(idcliente);
                                        fiadosDAO.deleteFiados(idcliente);
                                        clientesDAO.setDeuda(idcliente, 0);
                                        clientesDAO.setCredito(idcliente, 0);
                                        clientesDAO.setResta(idcliente, 0);

                                        Pagar.this.finish();

                                    }
                                });
                        alertDialog.show();
                    }else{
                        double restante = Double.parseDouble(TotalPagado.getText().toString())-resta;
                        AlertDialog alertDialog = new AlertDialog.Builder(Pagar.this).create();
                        alertDialog.setTitle("CUIDADO");
                        alertDialog.setMessage("El valor digitado supera el monto total de la deuda por $"+restante+ ". No se tomara en cuenta esta diferencia para este abono."+ "\n"+"Se saldara la cuenta del cliente: " + MainActivity.clientesData.getNombre() + " ");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Aceptar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        pagosDAO.deletePagos(idcliente);
                                        fiadosDAO.deleteFiados(idcliente);
                                        clientesDAO.setDeuda(idcliente, 0);
                                        clientesDAO.setCredito(idcliente, 0);
                                        clientesDAO.setResta(idcliente, 0);

                                        Pagar.this.finish();

                                    }
                                });
                        alertDialog.show();
                    }
                }
                else {

                    AlertDialog alertDialog = new AlertDialog.Builder(Pagar.this).create();
                    alertDialog.setTitle("Agregar pago");
                    alertDialog.setMessage("Agregar el pago de "+ TotalPagado.getText().toString()+ " al cliente: " + MainActivity.clientesData.getNombre() + " ");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Aceptar",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    pagos.setId_Cliente(idcliente);
                                    pagos.setMonto(Double.parseDouble(TotalPagado.getText().toString()));
                                    pagos.setDescripcion(descripcionPagado.getText().toString());
                                    pagos.setFecha(dateToStr2);
                                    pagosDAO.insert(pagos);

                                    Pagar.this.finish();

                                }
                            });
                    alertDialog.show();

                    //
                }


            }
        });
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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