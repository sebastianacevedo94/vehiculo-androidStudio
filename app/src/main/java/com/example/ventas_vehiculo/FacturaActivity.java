package com.example.ventas_vehiculo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class FacturaActivity extends AppCompatActivity {

    EditText jetffactura,jetffecha,jetfplaca,jetfmarca,jetfmodelo,jetfvalor;
    CheckBox jcbfactivo;

    ClsOpenHelper admin=new ClsOpenHelper(this,"Concesionario.db",null,1);

    long resp;
    String placa,marca,modelo,valor;
    int sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura);

        getSupportActionBar().hide();
        jetffactura.findViewById(R.id.etffactura);
        jetffecha.findViewById(R.id.etffecha);
        jetfplaca.findViewById(R.id.etfplaca);
        jetfmarca.findViewById(R.id.etfmarca);
        jetfmodelo.findViewById(R.id.etfmodelo);
        jetfvalor.findViewById(R.id.etfvalor);
        jcbfactivo.findViewById(R.id.cbfactivo);
        sw=0;
    }

    public void Guardar(View view) {
        placa = jetfplaca.getText().toString();
        marca = jetfmarca.getText().toString();
        modelo = jetfmodelo.getText().toString();
        valor = jetfvalor.getText().toString();
        if (placa.isEmpty() || marca.isEmpty() || modelo.isEmpty() || valor.isEmpty()) {
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetfplaca.requestFocus();
        } else {
            SQLiteDatabase db = admin.getWritableDatabase();
            ContentValues registro = new ContentValues();
            registro.put("placa", placa);
            registro.put("marca", marca);
            registro.put("modelo", modelo);
            registro.put("valor", Integer.parseInt(valor));
            if (sw == 0)
                resp = db.insert("TblFactura", null, registro);
            else {
                resp = db.update("TblFactura", registro, "placa'" + placa + "'", null);
                sw = 0;
            }
            if (resp > 0) {
                Toast.makeText(this, "Registro guardado", Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            } else
                Toast.makeText(this, "Error guardando registro", Toast.LENGTH_SHORT).show();
            db.close();

        }
    }

    public void Consultar(View view){
        placa=jetfplaca.getText().toString();
        if (placa.isEmpty()){
            Toast.makeText(this, "La placa es requerida", Toast.LENGTH_SHORT).show();
            jetfplaca.requestFocus();
    }
        else{
            SQLiteDatabase db=admin.getReadableDatabase();
            Cursor fila=db.rawQuery("select * from TblFactura where placa='" + placa + "'",null);
            if (fila.moveToNext()){
                sw=1;
                jetfmarca.setText(fila.getString(1));
                jetfmodelo.setText(fila.getString(2));
                jetfvalor.setText(fila.getString(3));
                if(fila.getString(4).equals("si"))
                    jcbfactivo.setChecked(true);
                else
                    jcbfactivo.setChecked(false);

    }
            else
                Toast.makeText(this, "factura no registrada no registrado", Toast.LENGTH_SHORT).show();
            db.close();
        }
    }

    public void Anular (View view){
        if(sw==0){
            Toast.makeText(this,"primero debe consultar",Toast.LENGTH_SHORT).show();
            jetfmarca.requestFocus();
        }
        else{
            SQLiteDatabase db=admin.getReadableDatabase();
            ContentValues registro=new ContentValues();
            registro.put("activo", "no");
            resp=db.update("TblVehiculo",registro,"placa='" + placa+"'",null);
            if(resp>0){
                Toast.makeText(this,"registro anulado",Toast.LENGTH_SHORT).show();
                Limpiar_campos();
            }
            else
                Toast.makeText(this,"Error anulado registrado",Toast.LENGTH_SHORT).show();
            db.close();
        }
    }

    public void cancelar(View view){
        Limpiar_campos();
    }
    public void regresar (View view){
        Intent intmain=new Intent(this,MainActivity.class);
        startActivity(intmain);
    }

    private void Limpiar_campos(){
        jetfplaca.setText("");
        jetfvalor.setText("");
        jetfmodelo.setText("");
        jetfmarca.setText("");
        jcbfactivo.setChecked(false);
        jetfplaca.requestFocus();
    }
}

