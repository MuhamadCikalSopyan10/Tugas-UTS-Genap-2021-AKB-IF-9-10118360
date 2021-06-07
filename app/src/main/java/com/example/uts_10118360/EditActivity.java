package com.example.uts_10118360;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

//05-06-2021 - 10118360 - Muhamad Cikal Sopyan - IF-9

public class EditActivity extends AppCompatActivity {

    com.example.uts_10118360.DBHelper helper;
    EditText TxNama, TxTanggal, TxKegiatan;
    Spinner SpJK;
    long id;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        helper = new com.example.uts_10118360.DBHelper(this);

        id = getIntent().getLongExtra(com.example.uts_10118360.DBHelper.row_id, 0);

        TxTanggal = (EditText)findViewById(R.id.txTgl_Kegiatan_Edit);
        TxNama = (EditText)findViewById(R.id.txNama_Edit);
        SpJK = (Spinner)findViewById(R.id.spJK_Edit);
        TxKegiatan = (EditText)findViewById(R.id.txIsi_Kegiatan_Edit);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        TxTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
        getData();
    }

    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                TxTanggal.setText(dateFormatter.format(newDate.getTime()));
            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void getData(){
        Cursor cursor = helper.oneData(id);
        if(cursor.moveToFirst()){
            String tanggal = cursor.getString(cursor.getColumnIndex(com.example.uts_10118360.DBHelper.row_tglkegiatan));
            String nama = cursor.getString(cursor.getColumnIndex(com.example.uts_10118360.DBHelper.row_nama));
            String jk = cursor.getString(cursor.getColumnIndex(com.example.uts_10118360.DBHelper.row_jk));
            String kegiatan = cursor.getString(cursor.getColumnIndex(com.example.uts_10118360.DBHelper.row_isikegiatan));

            TxTanggal.setText(tanggal);
            TxNama.setText(nama);

            if (jk.equals("Laki-laki")){
                SpJK.setSelection(0);
            }else if(jk.equals("Perempuan")){
                SpJK.setSelection(1);
            }

            TxKegiatan.setText(kegiatan);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_edit:

                String tanggal = TxTanggal.getText().toString().trim();
                String nama = TxNama.getText().toString().trim();
                String jk = SpJK.getSelectedItem().toString().trim();
                String kegiatan = TxKegiatan.getText().toString().trim();

                ContentValues values = new ContentValues();

                values.put(com.example.uts_10118360.DBHelper.row_tglkegiatan, tanggal);
                values.put(com.example.uts_10118360.DBHelper.row_nama, nama);
                values.put(com.example.uts_10118360.DBHelper.row_jk, jk);
                values.put(com.example.uts_10118360.DBHelper.row_isikegiatan, kegiatan);

                if ( tanggal.equals("") || nama.equals("") || kegiatan.equals("")){
                    Toast.makeText(EditActivity.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT);
                }else{
                    helper.updateData(values, id);
                    Toast.makeText(EditActivity.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
        switch (item.getItemId()){
            case R.id.delete_edit:
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                builder.setMessage("Data ini akan dihapus.");
                builder.setCancelable(true);
                builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        helper.deleteData(id);
                        Toast.makeText(EditActivity.this, "Data Terhapus", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}