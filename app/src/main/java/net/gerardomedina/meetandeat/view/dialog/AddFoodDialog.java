package net.gerardomedina.meetandeat.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.gerardomedina.meetandeat.R;

import org.json.JSONException;

import java.util.Locale;

class AddFoodDialog extends Dialog implements
        android.view.View.OnClickListener {
    private Button ok, cancel;
    private EditText descriptionInput, amountInput;

    AddFoodDialog(Activity a) {
        super(a);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_addfood);

        setTitle(getOwnerActivity().getString(R.string.add_food_label));
        descriptionInput = (EditText) findViewById(R.id.email_edit_input);
        amountInput = (EditText) findViewById(R.id.country_edit_input);
        changePassword = (EditText) findViewById(R.id.password_edit_input);

        descriptionInput.setText(appCommon.getUser().getEmail());
        amountInput.setText((new Locale("", appCommon.getUser().getCountry())).getDisplayCountry());
        amountInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCountryList();
            }
        });
        amountInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) showCountryList();
            }
        });

        ok = (Button) findViewById(R.id.btn_edit_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (changePassword.getText().toString().length() < 6 && changePassword.getText().toString().length() > 0) {
                    showToast("Password should be longer");
                } else {
                    newCountry = selectedCountry;
                    newEmail = descriptionInput.getText().toString();
                    try {
                        editAccount(descriptionInput.getText().toString(),
                                selectedCountry,
                                changePassword.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                dismiss();
            }
        });
        cancel = (Button) findViewById(R.id.btn_edit_cancel);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    private void showCountryList() {
        CountryPicker picker = CountryPicker.getInstance(getString(R.string.select_country),new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code) {
                amountInput.setText(name);
                selectedCountry = code;
                ((DialogFragment) getActivity().getSupportFragmentManager().findFragmentByTag("CountryPicker")).dismiss();
            }
        });
        picker.show(getActivity().getSupportFragmentManager(), "CountryPicker");
    }
}
