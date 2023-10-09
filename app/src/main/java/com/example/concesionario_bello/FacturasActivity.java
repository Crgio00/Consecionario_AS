package com.example.concesionario_bello;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FacturasActivity extends AppCompatActivity {

    EditText etcodigo,etfecha,etidentificacion,etplaca;
    TextView tvnombre,tvtelefono,tvmarca,tvvalor;
    CheckBox cbactivo;
    Button btadicionar,btanular;
    String codigo,identificacion,nombre, telefono,placa,marca ,valor, fecha;

    long respuesta;
    boolean sw;

    ClsOpenHelper admin=new ClsOpenHelper(this,"Concesionario1.db",null,1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturas);
        //ocultar la barra de titulo por defecto
        getSupportActionBar().hide();
        //Asociar los objetos Java con los objetos Xml
        etcodigo=findViewById(R.id.etcodigo);
        etfecha=findViewById(R.id.etfecha);
        etidentificacion=findViewById(R.id.etidentificacion);
        etplaca=findViewById(R.id.etplaca);
        tvnombre=findViewById(R.id.tvnombre);
        tvtelefono=findViewById(R.id.tvtelefono);
        tvmarca=findViewById(R.id.tvmarca);
        tvvalor=findViewById(R.id.tvvalor);
        cbactivo=findViewById(R.id.cbactivo);
        btadicionar=findViewById(R.id.btadicionar);
        btanular=findViewById(R.id.btanular);
        etcodigo.requestFocus();
        sw=false;
    }//fin metodo oncreate

    public void Guardar (View view) {
//Llevar el contenido de los objetos a variables
        codigo = etcodigo.getText().toString();
        placa = etplaca.getText().toString();
        fecha = etfecha.getText().toString();
        identificacion = etidentificacion.getText().toString();
        telefono = tvtelefono.getText().toString();
        marca = tvmarca.getText().toString();
        valor = tvvalor.getText().toString();
        //Validar que digito la informacion
        if (codigo.isEmpty() || placa.isEmpty() || fecha.isEmpty() || identificacion.isEmpty() ) {
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            etcodigo.requestFocus();
        } else {
            //abrir la conexion
            SQLiteDatabase db = admin.getWritableDatabase();
            ContentValues fila = new ContentValues();
            ContentValues filadetalle = new ContentValues();
            //Llenar el contenedor
            fila.put("Cod_factura", codigo);
            fila.put("Ident_cliente", identificacion);
            fila.put("Fecha", fecha);

            filadetalle.put("Cod_factura", codigo);
            filadetalle.put("Placa", placa);
            filadetalle.put("Valor_venta", valor);

            //Guardar registro en la base de datos
            //A traves del switche adiciono o modifico
            if (sw == false){
                respuesta=db.insert("TblFactura",null,fila);
                respuesta=db.insert("TblDetalle_Factura",null,filadetalle);}
            else{
                sw=false;
                respuesta=db.update("TblFactura",fila,"Cod_factura='"+codigo+"'",null);
                respuesta=db.update("TblDetalle_Factura",filadetalle,"Cod_factura='"+codigo+"'",null);
            }

            //Verificar si se guardo o no
            if (respuesta > 0) {
                Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
                Limpiar();
            } else {
                Toast.makeText(this, "Error guardando registro", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }
    }
    public void Consultar(View view) {
        codigo = etcodigo.getText().toString();
        if (!codigo.isEmpty()) {
            //Abrir la conexion en modo lectura
            SQLiteDatabase db=admin.getReadableDatabase();
            //Definir el Cursor y realizar la consulta
            Cursor registro=db.rawQuery("select fecha,TblFactura.Ident_cliente,Nom_Cliente,Tel_Cliente," +
                    "TblDetalle_Factura.Placa,Marca,Valor_venta," +
                    "TblFactura.Activo from TblCliente inner join " +
                    "TblFactura on TblCliente.Ident_cliente=TblFactura.Ident_cliente inner join " +
                    "TblDetalle_Factura on TblFactura.Cod_Factura=TblDetalle_Factura.Cod_Factura inner join " +
                    "TblVehiculo on TblDetalle_Factura.Placa=TblVehiculo.Placa where " +
                    "Tblfactura.Cod_factura='"+codigo+"'",null);
            if (registro.moveToNext()){
                sw=true;
                etfecha.setText(registro.getString(0));
                etidentificacion.setText(registro.getString(1));
                tvnombre.setText(registro.getString(2));
                tvtelefono.setText(registro.getString(3));
                etplaca.setText(registro.getString(4));
                tvmarca.setText(registro.getString(5));
                tvvalor.setText(registro.getString(6));

                if(registro.getString(7).equals("si"))
                    cbactivo.setChecked(true);
                else
                    cbactivo.setChecked(false);

            }else {
                Toast.makeText(this, "Codigo de factura no existe  ", Toast.LENGTH_SHORT).show();
                etcodigo.requestFocus();}
            db.close();
        }else{
            Toast.makeText(this, "codigo es requerido", Toast.LENGTH_SHORT).show();
            etcodigo.requestFocus();
        }
    }

    public void Cliente(View view){
        //Validar que el campo identificacion no esta vacio
        identificacion=etidentificacion.getText().toString();
        if (!identificacion.isEmpty()){
            //Abrir la conexion en modo lectura
            SQLiteDatabase db=admin.getReadableDatabase();
            //Definir el Cursor y realizar la consulta
            Cursor registro=db.rawQuery("select * from TblCliente where Ident_cliente='"+identificacion+"'",null);
            //Verificar si encontro registros o no
            if (registro.moveToNext()){
                etidentificacion.setText(registro.getString(0));
                tvnombre.setText(registro.getString(1));
                tvtelefono.setText(registro.getString(3));

            }else{
                Toast.makeText(this, "Cliente no registrado", Toast.LENGTH_SHORT).show();
            }

            db.close();
        }else{
            Toast.makeText(this, "Identificacion es requerida", Toast.LENGTH_SHORT).show();
            etidentificacion.requestFocus();
        }
    }

    public void Vehiculo(View view){
        //Validar que el campo identificacion no esta vacio
        placa=etplaca.getText().toString();
        if (!placa.isEmpty()){
            //Abrir la conexion en modo lectura
            SQLiteDatabase db=admin.getReadableDatabase();
            //Definir el Cursor y realizar la consulta
            Cursor registro=db.rawQuery("select * from TblVehiculo where Placa='"+placa+"'",null);
            //Verificar si encontro registros o no
            if (registro.moveToNext()){
                etplaca.setText(registro.getString(0));
                tvmarca.setText(registro.getString(2));
                tvvalor.setText(registro.getString(3));
            }else{
                Toast.makeText(this, "Vehicuo no registrado", Toast.LENGTH_SHORT).show();
            }

            db.close();
        }else{
            Toast.makeText(this, "Placa es requerida", Toast.LENGTH_SHORT).show();
        }
    }

    public void Regresar(View view){
        Intent intmain=new Intent(this,MainActivity.class);
        startActivity(intmain);
    }

    public void Cambiar_estado(View view){
        //Abrir conexion
        SQLiteDatabase db=admin.getWritableDatabase();
        ContentValues fila=new ContentValues();
        if (cbactivo.isChecked())
            fila.put("Activo","No");
        else
            fila.put("Activo","Si");
        //Modificar el estado
        respuesta=db.update("TblFactura",fila,"Cod_factura='"+codigo+"'",null);
        if (respuesta > 0){
            Toast.makeText(this, "Estado de registro cambiado", Toast.LENGTH_SHORT).show();
            Limpiar();
        }else{
            Toast.makeText(this, "Error cambiando estado", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    public void Limpiar(){
        etcodigo.setText("");
        etfecha.setText("");
        etidentificacion.setText("");
        etplaca.setText("");
        tvnombre.setText("");
        tvtelefono.setText("");
        tvmarca.setText("");
        tvvalor.setText("");
        sw=false;
    }
}
