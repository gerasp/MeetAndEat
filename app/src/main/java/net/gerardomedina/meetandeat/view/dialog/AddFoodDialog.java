package net.gerardomedina.meetandeat.view.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.task.AddFoodTask;
import net.gerardomedina.meetandeat.view.fragment.BaseFragment;

public class AddFoodDialog extends AlertDialog {
    private final BaseFragment fragment;
    private final Activity activity;
    private EditText descriptionInput, amountInput;
    private ImageView selectedIcon;
    private String selectedIconParameter;


    public AddFoodDialog(BaseFragment fragment) {
        super(fragment.getActivity());
        this.fragment = fragment;
        this.activity = fragment.getActivity();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedIcon = (ImageView) findViewById(R.id.selectedIcon);
        selectedIcon.setImageResource(R.drawable.ic_1);
        selectedIconParameter = "ic_1";

        TableLayout iconsTable = (TableLayout) findViewById(R.id.iconsTable);
        generateIconsTable(iconsTable);

        descriptionInput = (EditText) findViewById(R.id.descriptionInput);
        amountInput = (EditText) findViewById(R.id.amountInput);
        descriptionInput.requestFocus();
    }

    public void attemptAddFood () {

        descriptionInput.setError(null);
        amountInput.setError(null);

        String description = descriptionInput.getText().toString();
        String amount = amountInput.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(description)) {
            descriptionInput.setError(activity.getString(R.string.error_field_required));
            focusView = descriptionInput;
            cancel = true;
        }
        if (description.length() > 15) {
            descriptionInput.setError(activity.getString(R.string.error_field_too_long));
            focusView = descriptionInput;
            cancel = true;
        }
        if (TextUtils.isEmpty(amount)) {
            amountInput.setError(activity.getString(R.string.error_field_required));
            focusView = amountInput;
            cancel = true;
        }

        if (cancel) focusView.requestFocus();
        else {
            new AddFoodTask(fragment,selectedIconParameter,
                    descriptionInput.getText().toString(),
                    amountInput.getText().toString()).execute();
            dismiss();
        }
    }


    private void generateIconsTable(TableLayout table) {
        try {
            int counter = 0;
            for (int i = 1; i <= 40; i++) {
                TableRow row = new TableRow(activity);
                row.setGravity(Gravity.CENTER_HORIZONTAL);
                for (int j = 1; j <= 5; j++) {
                    counter = counter+1;
                    int id = R.drawable.class.getField("ic_"+counter).getInt(0);
                    ImageView icon = new ImageView(activity);
                    icon.setImageResource(id);
                    final int finalCounter = counter;
                    icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                selectedIcon.setImageResource(R.drawable.class.getField("ic_"+ finalCounter).getInt(0));
                                selectedIconParameter = "ic_"+ finalCounter;
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (NoSuchFieldException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    row.addView(icon);
                }
                table.addView(row);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
