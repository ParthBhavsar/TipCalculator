package io.zeroxp.parth_bhavsar_assignment_1;

/*
    Developer Name: Parth Bhavsar
    Last Updated: September 22nd, 2016
 */
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //initialize app widgets
    EditText etxt_HSTAmount, etxt_TipOther;
    CheckBox cb_AddHST;
    Spinner sp_Tip, sp_NumberOfPeople;
    Button btn_Calculate, btn_Clear;
    TextView txt_tip, txt_total, txt_PerPerson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //declare app widgets
        etxt_HSTAmount = (EditText)findViewById(R.id.etxtAmount);
        etxt_TipOther = (EditText)findViewById(R.id.etxt_TipOther);
        cb_AddHST = (CheckBox)findViewById(R.id.cb_AddHST);
        sp_Tip = (Spinner)findViewById(R.id.sp_tipValue);
        sp_NumberOfPeople = (Spinner)findViewById(R.id.sp_numOfPeople);
        btn_Calculate = (Button)findViewById(R.id.btn_Calculate);
        btn_Clear = (Button)findViewById(R.id.btn_Clear);
        txt_tip = (TextView)findViewById(R.id.txt_Tip);
        txt_total = (TextView)findViewById(R.id.txt_Total);
        txt_PerPerson = (TextView)findViewById(R.id.txt_PerPerson);


        //Tried to put onclick listener for the spinner and found out that it doesn't support onClick events
        //instead you have to create onItemSelectedListener
        //http://stackoverflow.com/questions/12108893/set-onclicklistener-for-spinner-item
        sp_Tip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                //toggle visibility of the etxt_TipOther
                if(selectedItem.equals("Other"))
                {
                    etxt_TipOther.setVisibility(View.VISIBLE);
                }

                else
                {
                    etxt_TipOther.setVisibility(View.INVISIBLE);
                    etxt_TipOther.setText("");
                }
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        cb_AddHST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideResult();
            }
        });

        //listener to see if the text is changed or not
        etxt_TipOther.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            //when the text is being changed
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                hideResult();
            }

            //after the has changed
            @Override
            public void afterTextChanged(Editable editable) {

                hideResult();
            }
        });

        etxt_HSTAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                hideResult();
            }

            @Override
            public void afterTextChanged(Editable editable) {

                hideResult();
            }
        });




    }


    public void Calculate(View view)
    {
        /*
        This event is strictly responsible for calculating TIP, HST & the TOTAL
        If an error exists, the user will be prompted an error message

        Return Type: void
         */
        //check to see if etxt_HSTAmount is empty or not
        if (etxt_HSTAmount.getText().toString().matches(""))
        {
            promptErrorDialog("Error", "Invalid Bill Amount");
            Toast.makeText(this, "Invalid Bill Amount", Toast.LENGTH_LONG).show();
        }

        else
        {
            //get all the values
            double amount = Double.parseDouble(etxt_HSTAmount.getText().toString());
            double tip = getTipValue();

            if (tip == -1)
            {
                //clear();
                return;
            }

            //check to see if hst is included or not
            boolean addHST = cb_AddHST.isChecked();
            double hst = 0;
            if (addHST == true)
            {
                hst = 0.13;
            }

            double splitPerPerson = Double.parseDouble(sp_NumberOfPeople.getSelectedItem().toString());
            //calculate hst
            double resultHST = ((amount) * hst);
            //calculate tip
            double resultTip = (amount * (tip/100)) + ((amount * (tip/100)) * hst);
            //calculate the total
            double resultTotal = amount + resultHST + resultTip;
            //calculate the split amongst people
            double resultSplit = resultTotal/splitPerPerson;


            displayResult(resultTip, resultTotal, resultHST, splitPerPerson, resultSplit);


        }

    }

    private void displayResult(double resultTip, double resultTotal, double resultHST, double splitPerPerson, double resultSplit)
    {
        /*
        This method is responsible for displaying the result to the user
        Return type: void
         */
        txt_tip.setVisibility(View.VISIBLE);
        txt_total.setVisibility(View.VISIBLE);
        txt_tip.setText("Tip is : $"+ String.format("%.2f", resultTip));
        txt_total.setText("Total is : $" + String.format("%.2f", resultTotal) + " ($" + String.format("%.2f", resultHST) + " HST)");

        if (splitPerPerson == 1)
        {
            txt_PerPerson.setVisibility(View.INVISIBLE);
        }

        else
        {
            txt_PerPerson.setVisibility(View.VISIBLE);
            txt_PerPerson.setText("Per Person : $" + String.format("%.2f", resultSplit));
        }
    }


    public void onClear(View view)
    {
        clear();
    }

    private void clear()
    {
        /*
        This method resets the UI to its initial state
        Return type: void
         */
        //Reset the UI
        etxt_HSTAmount.setText("");
        etxt_TipOther.setVisibility(View.INVISIBLE);
        sp_Tip.setSelection(0);
        sp_NumberOfPeople.setSelection(0);
        hideResult();
        cb_AddHST.setChecked(false);

    }

    private void hideResult()
    {
        /*
        hide all the results output
        Return Type: Void
         */
        txt_tip.setVisibility(View.INVISIBLE);
        txt_total.setVisibility(View.INVISIBLE);
        txt_PerPerson.setVisibility(View.INVISIBLE);
    }

    public double getTipValue()
    {
        /*
        This method gets the tip value from the UI and if there is an error, the user will get an error message
        Return type: double
         */
        double tipValue = 0;
        if (sp_Tip.getSelectedItem().toString().matches("Other"))
        {
            //when the etxt_TipOther is empty generate a dialog showing an error
            if(etxt_TipOther.getText().toString().matches(""))
            {
                promptErrorDialog("Error", "Invalid Tip Percentage Given");
                Toast.makeText(this, "Invalid Tip Percentage Given", Toast.LENGTH_LONG).show();

                tipValue = -1;
            }
            else
            {
                tipValue = Double.parseDouble(etxt_TipOther.getText().toString());
            }
        }

        else {
            //split string to get rid of the % sign in the Spinner
            String CurrentString = sp_Tip.getSelectedItem().toString();
            String[] separated = CurrentString.split("%");
            tipValue = Double.parseDouble(separated[0]);
        }

        return tipValue;
    }

    public void promptErrorDialog(String title, String message)
    {
        /*
        This method creates and displays the error message
        Return Type: void

        http://stackoverflow.com/questions/26097513/android-simple-alert-dialog

        I added a alert dialog instead of a toast because the alert dialog leaves only if the user touches the button OK. Where as
        toast only stays for short amount of time. I have already talked to you about it and you agreed on the change.




         */
        AlertDialog errorDialog = new AlertDialog.Builder(MainActivity.this).create();
        errorDialog.setTitle(title);
        errorDialog.setMessage(message);
        errorDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        errorDialog.show();
    }
}
