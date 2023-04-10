package com.finalproject.cuaderfia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.cuaderfia.dbaroom.dao.ClientesDAO;
import com.finalproject.cuaderfia.dbaroom.dao.FiadosDAO;
import com.finalproject.cuaderfia.dbaroom.dao.PagosDAO;
import com.finalproject.cuaderfia.dbaroom.database.AppDatabase;
import com.finalproject.cuaderfia.dbaroom.entidades.Clientes;
import com.finalproject.cuaderfia.dbaroom.entidades.Fiados;
import com.finalproject.cuaderfia.dbaroom.entidades.Pagos;

import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Detalles extends AppCompatActivity {


    private String telefono;
    private ListView lvl;
    private ListView lvl2;


    private TextView campoNombre, campoDireccion, campoTelefono, campoFiado, campoId, campoDescripcion, campoAbonado, campoResta;

    public ImageButton btnLLamar, btnFiar, btnPagar, btnEditar, btnBorrar, btnSaldar;
    Button whatsApp;
    private static final int REQUEST_CALL = 1;

    private LinearLayout btnWrite;

    ///Variables de instancia de la base de datos/////////////////////////
    private AppDatabase db;
    private FiadosDAO fiadoDAO;
    private ClientesDAO clientesDAO;
    private PagosDAO pagosDAO;
    double deuda;
    double credito;
    double resta;

    private UsersAdapter1 adapter;
    private UsersAdapter2 adapter2;
    NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();

    private Clientes cliente = MainActivity.clientesData;
    int idClientes = cliente.getId_Cliente();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        //Activar el boton de ir atras en el actionBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ////////////////instancia de la base de datos////////////////////////////////

        db = AppDatabase.getInstance(getApplicationContext());
        fiadoDAO = db.fiadosDAO();
        clientesDAO = db.clientesDAO();
        pagosDAO = db.pagosDAO();

//////////////////////////////////////////////////////////////////////////////
        TextView emptyText1 = (TextView)findViewById(R.id.empty1);
        TextView emptyText2 = (TextView)findViewById(R.id.empty2);
        campoResta = findViewById(R.id.cResta);
        campoAbonado = findViewById(R.id.cAbonados);
        campoNombre = findViewById(R.id.cNombre);
        campoDireccion = findViewById(R.id.cDireccion);
        campoTelefono = findViewById(R.id.cTelefono);
        campoFiado = findViewById(R.id.cFiados);
        lvl = findViewById(R.id.lvlist1);
        lvl2 = findViewById(R.id.lvlist2);
        whatsApp = findViewById(R.id.Whats);
        btnLLamar = findViewById(R.id.btnCopiar);
        btnFiar = findViewById(R.id.btnFiar);
        btnPagar = findViewById(R.id.btnPagar);
        btnEditar = findViewById(R.id.btnEditar);
        btnBorrar = findViewById(R.id.btnBorrar);
        btnSaldar = findViewById(R.id.saldar);
        btnWrite = findViewById(R.id.btnPdf);

        lvl.setEmptyView(emptyText1);
        lvl2.setEmptyView(emptyText2);

        //Trae los datos de la base de datos a los campos nombre, direccion y telefono
        llenarCampos();

        //Aqui se obtiene el id de la variable estatica del home fragment referente al item(objeto seleccionado de la lista) se aplica el metodo getDeudaCliente que suma todos los valores de la tabla concerniente a ese cliente
        // y se guarda en una variable de tipo double
        getClienteDeudaCredito();

        //Actualiza la deuda de la tabla clientes
        updateDeudasCreditoCliente();

        //Muestra la lista de deudas(fiados) en el listView
        mostrarListaFiados();

        ////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////
        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAndSaveFile();
            }
        });

        ////////// Pasa al siguiente activity con los datos del textView de historial

        btnFiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Detalles.this, Add_Fiado.class);
                startActivity(i);

            }
        });
        ////////////////////////////////////////////////////////////////////////////////////


        ///////////////envia historial por whatsapp///////////////////////////////
        whatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telefono = campoTelefono.getText().toString();


                if (appInstalledOrNot()) {


                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + telefono + "&text= " + listToString()));
                    startActivity(intent);

                } else {
                    Toast.makeText(Detalles.this, "WhatsApp no esta instalado en tu Smartphone", Toast.LENGTH_LONG).show();

                }

            }
        });


        /////////////////////////////////////////////////////////////////////

        /////////////////// Pasa al siguiente activity con los datos del textView de historial///

        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(Detalles.this, Pagar.class);
                startActivity(a);
            }
        });

        /////////////////////////////////////////////////////////////////////


        //////////////Accion para saldar cuenta////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        btnSaldar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(Detalles.this).create();
                alertDialog.setTitle("CUIDADO");
                alertDialog.setMessage("Se saldara la cuenta del cliente: " + MainActivity.clientesData.getNombre() + " ");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Aceptar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                pagosDAO.deletePagos(idClientes);
                                fiadoDAO.deleteFiados(idClientes);
                                clientesDAO.setDeuda(idClientes, 0);
                                clientesDAO.setCredito(idClientes, 0);
                                clientesDAO.setResta(idClientes, 0);
                                Toast.makeText(Detalles.this, "CUENTA SALDADA, REGISTROS ELIMINADOS", Toast.LENGTH_LONG).show();
                                finish();

                            }
                        });
                alertDialog.show();

            }
        });


        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /////Accion para editar Contacto de cliente//////////////////////////////////////////////////////////////////////


        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Detalles.this, EditarContacto.class);
                startActivity(i);

            }
        });


        /////////////////////////////////////////////////////////////////////////


        ////// Accion para eliminar cliente////////////////////////////////////////////

        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Detalles.this);

                builder.setMessage("ESTA ACCION NO SE PUEDE DESHACER ¿Esta seguro que quiere ELIMINAR esta cuenta?")
                        .setCancelable(false)
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                /////////////////////agreaga funcion///////////////////
                                clientesDAO.delete(cliente);
                                pagosDAO.deletePagos(idClientes);
                                fiadoDAO.deleteFiados(idClientes);
                                Toast.makeText(Detalles.this, "CLIENTE ELIMINADO", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        })

                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //////////////agrega funcion///////////////////////
                                dialog.cancel();

                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });


        /////////////////////////////////////////////////////////////////////////////////

        ////////////////////////Llamar numero de telefono//////////////////////////////////

        btnLLamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makePhoneCall();

            }
        });
        //////////
    }

    /////////////////////////metodo para hacer llamada//////////////////////////////////////////////// y pedir permisos////////////////////////////////
    private void makePhoneCall() {


        if (campoTelefono.getText().length() > 0) {

            if (ContextCompat.checkSelfPermission(Detalles.this,
                    android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Detalles.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);

            } else {
                String s = "tel:" + campoTelefono.getText();
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(s)));

            }

        } else {

            Toast.makeText(Detalles.this, "El cliente no posee numero de telefono", Toast.LENGTH_LONG).show();

        }

    }
    //Permisos para poder realizar llamadas/////////////////////////////////////////////////
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                makePhoneCall();
            } else {
                Toast.makeText(Detalles.this, "Permiso Denegado", Toast.LENGTH_LONG).show();
            }

        }
    }

    //Boton para ir hacia atras
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                Log.i("ActionBar", "Atrás!");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    ///////////////////////////metodo para ver si whatsapp esta instalado///////////////////////////////////////////////////////////

    private boolean appInstalledOrNot() {
        PackageManager pm = getPackageManager();
        boolean whatsappInstalled;
        try {
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            whatsappInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            whatsappInstalled = false;
        }

        return whatsappInstalled;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////

    ////UserAdapter del area de Fiados////////////////////////////////////////
    public class UsersAdapter1 extends ArrayAdapter<Fiados> {


        public UsersAdapter1(Context context, ArrayList<Fiados> fiados) {
            super(context, 0 , fiados);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //Obtiene los datos desde su posicion
            Fiados fiados = getItem(position);

            //Verifica si una vista existente esta siendo reutilizada, de lo contrario muestrame la vista
            if (convertView==null){

                convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_ticket2,parent,false);
            }

            //Busca una vista para popular los datos
            TextView txtDescripcion = (TextView) convertView.findViewById(R.id.descripcionLV);
            TextView txtDeuda = (TextView) convertView.findViewById(R.id.deudaLVDescripcion);
            TextView  txtfecha= convertView.findViewById(R.id.txtFechaDetalles)
                    ;            //Popula los datos dentro de la plantilla
            txtDescripcion.setText(String.valueOf(fiados.getDescripcion()));
            txtDeuda.setText(String.valueOf(defaultFormat.format(fiados.getMonto())));
            txtfecha.setText(fiados.getFecha());

            //devuelve la vista completa para mostrarla en pantalla
            return convertView;
        }

    }

    //User Adapter del area de Pagos//////////////////////////////////////////////
    public class UsersAdapter2 extends ArrayAdapter<Pagos> {


        public UsersAdapter2(Context context, ArrayList<Pagos> pagos) {
            super(context, 0 , pagos);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //Obtiene los datos desde su posicion
            Pagos pagos = getItem(position);

            //Verifica si una vista existente esta siendo reutilizada, de lo contrario muestrame la vista
            if (convertView==null){

                convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_ticket3,parent,false);
            }

            //Busca una vista para popular los datos
            TextView txtDescripcion = (TextView) convertView.findViewById(R.id.descripcionLV);
            TextView txtDeuda = (TextView) convertView.findViewById(R.id.deudaLVDescripcion);
            TextView  txtfecha= convertView.findViewById(R.id.txtFechaDetalles)
                    ;            //Popula los datos dentro de la plantilla
            txtDescripcion.setText(String.valueOf(pagos.getDescripcion()));
            txtDeuda.setText(String.valueOf(defaultFormat.format(pagos.getMonto())));
            txtfecha.setText(pagos.getFecha());

            //devuelve la vista completa para mostrarla en pantalla
            return convertView;
        }

    }

    //Sobreescritura del metodo onResume
    @Override
    public void onResume() {
        super.onResume();
        llenarCampos();
        getClienteDeudaCredito();
        updateDeudasCreditoCliente();
        mostrarListaFiados();


    }

    //obtiene la deuda del cliente y la agrega al textView
    public void getClienteDeudaCredito(){

        //Aqui se obtiene el id de la variable estatica del home fragment referente al item(objeto seleccionado de la lista) se aplica el metodo getDeudaCliente que suma todos los valores de la tabla concerniente a ese cliente
        // y se guarda en una variable de tipo double
        int idCliente = MainActivity.clientesData.getId_Cliente();

        deuda =fiadoDAO.getDeudaCliente(idCliente);

        credito = pagosDAO.getPagosCliente(idCliente);

        resta = deuda-credito;
        //el campo fiado va a mostrar la suma de todos los fiados
        campoFiado.setText(String.valueOf(defaultFormat.format(deuda)));
        campoAbonado.setText(String.valueOf(defaultFormat.format(credito)));
        campoResta.setText(String.valueOf(defaultFormat.format(resta)));
    }

    public void updateDeudasCreditoCliente(){
        //Actualizo la tabla clientes para que me muestre la deuda actual

        int idCliente = MainActivity.clientesData.getId_Cliente();
        clientesDAO.setDeuda(idCliente, deuda);
        clientesDAO.setCredito(idCliente, credito);
        clientesDAO.setResta(idCliente, resta);

    }

    //Muestra la lista de fiados
    public void mostrarListaFiados(){

        //Fiados////////////////////////////////////////
        ////////////////////////////////////////////////
        //fiadosDAO = db.fiadosDAO();
        List<Fiados> clientesList = fiadoDAO.getFiados(MainActivity.clientesData.getId_Cliente());
        ArrayList<Fiados> miListaFiados = new ArrayList<Fiados>();
        miListaFiados.addAll(clientesList);
        //Construye el recurso de datos
        ArrayList<Fiados> arrayofClientes = new ArrayList<Fiados>();
        //Crea un adaptador para convertir el array a vista
        adapter = new UsersAdapter1(getApplicationContext(), arrayofClientes);
        // agrega el adaptador al listView
        lvl.setAdapter(adapter);
        adapter.addAll(miListaFiados);
        ///////////////////////////////////////////////////
        ///////////////////////////////////////////////////

        //Pagos////////////////////////////////////////////
        ///////////////////////////////////////////////////
        //pagosDAO = db.pagosDAO();
        List<Pagos> pagosList = pagosDAO.getPagos(MainActivity.clientesData.getId_Cliente());
        ArrayList<Pagos> miListaPagos = new ArrayList<Pagos>();
        miListaPagos.addAll(pagosList);
        //Construye el recurso de datos
        ArrayList<Pagos> arrayofPagos = new ArrayList<Pagos>();
        //Crea un adaptador para convertir el array a vista
        adapter2 = new UsersAdapter2(getApplicationContext(), arrayofPagos);
        // agrega el adaptador al listView
        lvl2.setAdapter(adapter2);
        adapter2.addAll(miListaPagos);

    }

    public void llenarCampos(){
        campoNombre.setText(clientesDAO.getNombre(cliente.getId_Cliente()));
        campoDireccion.setText(clientesDAO.getDireccion(cliente.getId_Cliente()));
        campoTelefono.setText(clientesDAO.getCelular(cliente.getId_Cliente()));
    }

    //Convierto los List a string para poder enviar el historial a traves de whatsapp
    public String listToString(){
        List<Fiados> clientesList = fiadoDAO.getFiados(MainActivity.clientesData.getId_Cliente());
        Fiados[] arrayFiados = new Fiados[clientesList.size()];
        clientesList.toArray(arrayFiados);
        List<Pagos> pagosList = pagosDAO.getPagos(MainActivity.clientesData.getId_Cliente());
        Pagos [] arrayPagos = new Pagos[pagosList.size()];
        pagosList.toArray(arrayPagos);

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(String.format("*%s*", "Historial de Deudas")+ "\n");
        for (int i = 0 ; i<arrayFiados.length; i++){

            stringBuilder.append("  ***" + arrayFiados[i].getFecha() + "***" + "\n" +
                    "  " + arrayFiados[i].getDescripcion() + "\n" + "  Total: " + defaultFormat.format(arrayFiados[i].getMonto()) + "\n" + "  *******************" + "\n");
        }
        stringBuilder.append(String.format("*%s*", "Historial de Pagos") + "\n");
        for (int i = 0 ; i<arrayPagos.length; i++){

            stringBuilder.append("  ***" + arrayPagos[i].getFecha() + "***" + "\n" +
                    "  " + arrayPagos[i].getDescripcion()+ "\n" + "  Total: " + defaultFormat.format(arrayPagos[i].getMonto()) + "\n" + "  *******************" +  "\n");
        }

        return stringBuilder.toString();

    }
    //////////////////////////////////////////////////////////////////////////////////
    //Crear y guardar archivo txt
    private void createAndSaveFile(){
        Intent  intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "Historial_"+MainActivity.clientesData.getNombre()+".txt");
        startActivityForResult(intent,1);

    }

    //Metodo del activity result para guardar archivo .txt
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if (resultCode==RESULT_OK){
                Uri uri = data.getData();
                try {
                    OutputStream outputStream = getContentResolver().openOutputStream(uri);
                    outputStream.write(listToString().getBytes());
                    outputStream.close();
                    Toast.makeText(this,"Archivo Guardado Correctamente", Toast.LENGTH_SHORT).show();
                }catch (IOException e){
                    Toast.makeText(this,"Error al guardar el archivo", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this,"Archivo no guardado", Toast.LENGTH_SHORT).show();
            }


        }
    }

}