package com.finalproject.cuaderfia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.finalproject.cuaderfia.dbaroom.dao.ClientesDAO;
import com.finalproject.cuaderfia.dbaroom.database.AppDatabase;
import com.finalproject.cuaderfia.dbaroom.entidades.Clientes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> listainformacion;
    AppDatabase db;
    ClientesDAO clientesDAO;
    //Variable estatica que me sirve para guardar el objeto seleccionado del listView///////////////////
    public static Clientes clientesData;
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    TextView txtDeuda;
    TextView txtCredito;

    private View view;
    private SearchView search;

    FloatingActionButton floatingActionButton;
    UsersAdapter adapter;
    ListView lv;
    NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instancia de la base de datos////////////////////////////////////////////////////////////////////////
        db= AppDatabase.getInstance(MainActivity.this);

        lv = findViewById(android.R.id.list);
        TextView emptyText = (TextView)findViewById(android.R.id.empty);
        lv.setEmptyView(emptyText);

        //////////Evento para buscar clientes//////////////////////////
        search =findViewById(R.id.searchView);

//Al hacer clic en el searchView este sera clicable en toda el area////////////////
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setIconified(false);
            }
        });
//Al cambiar el texto en el searchView se ejecutara el metodo mostrarBusquedaClientes con el string que se introduzca en el searchView
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mostrarBusquedaClientes(s);
                return true;
            }
        });

        //Metodo que muestra los clientes en el listView/////////////////////////
        mostrarClientes();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent i = new Intent(MainActivity.this, Detalles.class);
                startActivity(i);

                clientesData=adapter.getItem(position);


                //////////////
            }
        });

        // Accion para navegar desde es fragment home hasta el activiti agregar cliente
        floatingActionButton = findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, NuevoCliente.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        mostrarClientes();

    }

    public class UsersAdapter extends ArrayAdapter<Clientes> {


        public UsersAdapter(Context context, ArrayList<Clientes> clientes) {
            super(context, 0, clientes);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //Obtiene los datos desde su posicion
            Clientes cliente = getItem(position);

            //Verifica si una vista existente esta siendo reutilizada, de lo contrario muestrame la vista
            if (convertView == null) {

                convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_ticket, parent, false);
            }

            //Busca una vista para popular los datos
            TextView txtid = (TextView) convertView.findViewById(R.id.idCliente);
            TextView txtnombre = (TextView) convertView.findViewById(R.id.nombreCliente);
            txtDeuda = (TextView) convertView.findViewById(R.id.deudaCliente);
            txtCredito = (TextView) convertView.findViewById(R.id.creditoCliente);
            //Popula los datos dentro de la plantilla
            txtid.setText(String.valueOf(cliente.getId_Cliente()));
            txtnombre.setText(cliente.getNombre());
            txtDeuda.setText(String.valueOf(defaultFormat.format(cliente.getResta())));
            txtCredito.setText(defaultFormat.format(cliente.getCredito()));

            //devuelve la vista completa para mostrarla en pantalla
            return convertView;
        }

    }
    public void mostrarClientes(){
        clientesDAO = db.clientesDAO();
        List<Clientes> clientesList = clientesDAO.getAll();;
        ArrayList<Clientes> miListaClientes = new ArrayList<Clientes>();

        miListaClientes.addAll(clientesList);

        //Construye el recurso de datos
        ArrayList<Clientes> arrayofClientes = new ArrayList<Clientes>();
        //Crea un adaptador para convertir el array a vista
        adapter = new UsersAdapter(MainActivity.this, arrayofClientes);
        // agrega el adaptador al listView
        lv.setAdapter(adapter);
        adapter.addAll(miListaClientes);
    }

    public void mostrarBusquedaClientes(String buscar){

        List<Clientes> clientesBusqueda = clientesDAO.getSearch(buscar);
        ArrayList<Clientes> listaBusClientes = new ArrayList<Clientes>();

        listaBusClientes.addAll(clientesBusqueda);

        ArrayList<Clientes> array = new ArrayList<Clientes>();
        adapter = new UsersAdapter(MainActivity.this, array);

        lv.setAdapter(adapter);
        adapter.addAll(listaBusClientes);
    }


}