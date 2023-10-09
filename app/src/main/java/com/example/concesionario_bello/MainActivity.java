package com.example.concesionario_bello;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Ocultar la barra de titulo por defecto
        getSupportActionBar().hide();
    }//fin metodo onCreate

    public void Clientes(View view){
        Intent intclientes=new Intent(this,ClientesActivity.class);
        startActivity(intclientes);
    } //fin metodo Cliente

    public void Vehiculos(View view){
        Intent intvehiculos=new Intent(this,VehiculosActivity.class);
        startActivity(intvehiculos);
    } //fin metodo Vehiculos

    public void Facturas(View view){
        Intent intfacturas=new Intent(this,FacturasActivity.class);
        startActivity(intfacturas);
    } //fin metodo Facturas
}