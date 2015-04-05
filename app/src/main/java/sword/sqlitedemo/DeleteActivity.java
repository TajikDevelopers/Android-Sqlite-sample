package sword.sqlitedemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sword.data.Db;
import sword.data.models.Contact;


public class DeleteActivity extends Activity {

    Spinner spinnerContacts;
    Button btnClose, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        spinnerContacts = (Spinner) findViewById(R.id.spinnerContacts);
        PopulateContactsSpinner();

        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setEnabled(false);
        btnClose = (Button) findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteActivity.this.finish();
            }
        });

        spinnerContacts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Contact contact = (Contact) spinnerContacts.getSelectedItem();
                btnDelete.setEnabled(false);
                if (contact.name != (null)) {
                    btnDelete.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Contact contact = (Contact) spinnerContacts.getSelectedItem();
                if (contact.name != (null)) {
                    try {

                        new AlertDialog.Builder(DeleteActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Warning")
                                .setMessage("Are you sure you want to delete this guy ?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Db db = new Db(getApplicationContext());
                                        db.Delete(contact.id);
                                        Toast.makeText(getApplicationContext(), "Deleted successfully !", Toast.LENGTH_LONG).show();
                                        PopulateContactsSpinner();
                                    }

                                })
                                .setNegativeButton("No", null)
                                .show();

                    } catch (Exception ex) {
                        Log.e("Error", ex.toString());
                    }
                }
            }
        });
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
}
