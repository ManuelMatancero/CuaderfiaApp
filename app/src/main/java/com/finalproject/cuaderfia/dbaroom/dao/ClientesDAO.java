package com.finalproject.cuaderfia.dbaroom.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.finalproject.cuaderfia.dbaroom.entidades.Clientes;

import java.util.List;



@Dao
public interface ClientesDAO {

    @Insert
    void insert(Clientes cliente);

    @Delete
    void delete(Clientes cliente);

    @Query("SELECT nombre FROM Clientes WHERE id_Cliente = :id")
    String getNombre(int id);

    @Query("SELECT direccion FROM Clientes WHERE id_Cliente = :id")
    String getDireccion(int id);

    @Query("SELECT celular FROM Clientes WHERE id_Cliente = :id")
    String getCelular(int id);

    @Query("SELECT deuda FROM Clientes WHERE id_Cliente = :id")
    double getDeuda(int id);

    @Query("SELECT credito FROM Clientes WHERE id_Cliente = :id")
    double getCredito(int id);

    @Query("SELECT * FROM Clientes WHERE id_Cliente = :id")
    Clientes getCliente(int id);

    @Query("SELECT * FROM Clientes")
    List<Clientes> getAll();

    @Query("SELECT * FROM Clientes WHERE nombre LIKE '%' ||  :buscar || '%'")
    List<Clientes> getSearch(String buscar);

    @Query("UPDATE Clientes SET deuda = :suma WHERE id_Cliente = :id")
    void setDeuda(int id, double suma);

    @Query("UPDATE Clientes SET credito = :suma WHERE id_Cliente = :id")
    void setCredito(int id, double suma);

    @Query("UPDATE Clientes SET resta = :resta WHERE id_Cliente = :id")
    void setResta(int id, double resta);

    @Query("SELECT resta FROM Clientes WHERE id_Cliente = :id")
    double getRestaCliente(int id);

    @Query("UPDATE Clientes SET nombre = :nombre, direccion = :direccion, celular = :celular WHERE id_Cliente = :id")
    void updateCliente(String nombre, String direccion, String celular, int id);

    @Query("DELETE FROM Clientes")
    void deleteAllData();


}
