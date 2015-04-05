package sword.sqlitedemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import sword.data.Db;
import sword.data.models.Contact;


public class CreateActivity extends Activity {

    EditText txtName, txtPhone, txtAddress;
    Button btnSave, btnClose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        txtName = (EditText) findViewById(R.id.txtName);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtAddress = (EditText) findViewById(R.id.txtAddress);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnClose = (Button) findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateActivity.this.finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Contact contact = new Contact();
                    if(txtName.getText().toString().trim().isEmpty())
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CreateActivity.this);
                        builder.setMessage("Please input Name !")
                                .setCancelable(true)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        return;
                    }
                    contact.name = txtName.getText().toString();
                    contact.phone = txtPhone.getText().toString();
                    contact.address = txtAddress.getText().toString();
                    new Db(getApplicationContext()).Insert(contact);

                    ClearFields();
                    Toast.makeText(getApplicationContext(), "Saved successfully", Toast.LENGTH_LONG).show();
                }
                catch (Exception ex)
                {
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void ClearFields() {
        txtName.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
    }

}
