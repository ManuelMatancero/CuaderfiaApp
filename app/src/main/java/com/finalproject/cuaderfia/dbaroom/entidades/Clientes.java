package com.finalproject.cuaderfia.dbaroom.entidades;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Clientes")
public class Clientes {

    @PrimaryKey(autoGenerate = true)
    int id_Cliente;

    @ColumnInfo(name= "nombre")
    public String nombre;

    @ColumnInfo(name= "direccion")
    public String direccion;

    @ColumnInfo(name= "celular")
    public String celular;

    @ColumnInfo(name= "deuda")
    public double deuda;

    @ColumnInfo(name= "credito")
    public double credito;

    @ColumnInfo(name = "resta")
    public double resta;

    public double getResta() {
        return resta;
    }

    public void setResta(double resta) {
        this.resta = resta;
    }

    public double getCredito() {
        return credito;
    }

    public void setCredito(double credito) {
        this.credito = credito;
    }

    public int getId_Cliente() {
        return id_Cliente;
    }

    public void setId_Cliente(int id_Cliente) {
        this.id_Cliente = id_Cliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public double getDeuda() {
        return deuda;
    }

    public void setDeuda(double deuda) {
        this.deuda = deuda;
    }
}
