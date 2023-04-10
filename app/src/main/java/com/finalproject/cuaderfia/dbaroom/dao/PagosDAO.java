package com.finalproject.cuaderfia.dbaroom.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.finalproject.cuaderfia.dbaroom.entidades.Pagos;

import java.util.List;



@Dao
public interface PagosDAO {

    @Insert
    void insert (Pagos pago);

    @Delete
    void delete (Pagos pago);

    @Query("SELECT * FROM Pagos WHERE id_Cliente = :idCliente")
    List<Pagos> getPagos(int idCliente);

    @Query("SELECT SUM(monto) as total FROM Pagos")
    double getPagosTotal();

    @Query("SELECT SUM(monto) as total FROM Pagos WHERE id_Cliente = :idCliente")
    double getPagosCliente(int idCliente);

    @Query("DELETE FROM Pagos WHERE id_Cliente = :id")
    void deletePagos(int id);

    @Query("DELETE FROM Pagos")
    void deleteAllData();



}
