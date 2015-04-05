package sword.sqlitedemo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sword.data.Db;
import sword.data.models.Contact;


public class UpdateActivity extends Activity {

    Spinner spinnerContacts;
    Button btnClose, btnSave;
    EditText txtName, txtPhone, txtAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        txtName = (EditText) findViewById(R.id.txtName);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtAddress = (EditText) findViewById(R.id.txtAddress);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnClose = (Button) findViewById(R.id.btnClose);

        SetControlsState(false);

        spinnerContacts = (Spinner) findViewById(R.id.spinnerContacts);
        PopulateContactsSpinner();

        spinnerContacts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Contact contact = (Contact) spinnerContacts.getSelectedItem();
                if (contact.name != (null)) {
                    SetControlsState(true);
                    txtName.setText(contact.name);
                    txtPhone.setText(contact.phone);
                    txtAddress.setText(contact.address);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateActivity.this.finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contact contact = (Contact) spinnerContacts.getSelectedItem();
                if (contact.name != (null)) {
                    //updating fields here
                    contact.name = txtName.getText().toString();
                    contact.address = txtAddress.getText().toString();
                    contact.phone = txtPhone.getText().toString();

                    Db db = new Db(getApplicationContext());
                    db.Update(contact);

                    Toast.makeText(getApplicationContext(), "Saved successfully", Toast.LENGTH_LONG).show();
                    ClearFields();
                    SetControlsState(false);
                    PopulateContactsSpinner();
                }

            }
        });
    }

    private void SetControlsState(boolean state) {
        txtName.setEnabled(state);
        txtPhone.setEnabled(state);
        txtAddress.setEnabled(state);
        btnSave.setEnabled(state);
    }

    private void PopulateContactsSpinner() {
        ArrayList<Contact> spinnerArray = new ArrayList<Contact>();
        List<Contact> contacts = new Db(getApplicationContext()).GetAll();

        spinnerArray.add(new Contact()); //empty row

        for (Contact contact : contacts)
            if (contact != null)
                spinnerArray.add(contact);

        ArrayAdapter<Contact> adapter;

        adapter = new ArrayAdapter<Contact>(getApplicationContext(), android.R.layout.simple_spinner_item, spinnerArray) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final TextView v = (TextView) super.getView(position, convertView, parent);
                v.setTextColor(Color.BLACK);
                //v.setTextSize(MainActivity.font(14f));
                //v.setTypeface(null, Typeface.BOLD);
                return v;
            }

            // Affects opened state of the spinner
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                final TextView v = (TextView) super.getView(position, convertView, parent);
                v.setTextColor(Color.BLACK);
                //v.setTextSize(MainActivity.font(14f));
                //v.setTypeface(null, Typeface.BOLD);
                return v;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerContacts.setAdapter(adapter);
    }

    private void ClearFields() {
        txtName.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
    }

}
