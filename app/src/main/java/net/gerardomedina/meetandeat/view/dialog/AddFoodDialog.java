package net.gerardomedina.meetandeat.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import net.gerardomedina.meetandeat.R;

public class AddFoodDialog extends Dialog {
    private final Activity activity;
    private EditText descriptionInput, amountInput;
    private ImageView selectedIcon;

    public AddFoodDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_addfood);

        selectedIcon = (ImageView) findViewById(R.id.selectedIcon);
        selectedIcon.setImageResource(R.drawable.ic_1);

        TableLayout iconsTable = (TableLayout) findViewById(R.id.iconsTable);
        generateIconsTable(iconsTable);

        descriptionInput = (EditText) findViewById(R.id.descriptionInput);
        amountInput = (EditText) findViewById(R.id.amountInput);

        Button ok = (Button) findViewById(R.id.okAddFood);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button cancel = (Button) findViewById(R.id.cancelAddFood);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void generateIconsTable(TableLayout table) {
        try {
            int counter = 0;
            for (int i = 1; i <= 50; i++) {
                TableRow row = new TableRow(activity);
                row.setGravity(Gravity.CENTER_HORIZONTAL);
                for (int j = 1; j < 5; j++) {
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
