package net.gerardomedina.meeteat.view.table;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.gerardomedina.meeteat.R;
import net.gerardomedina.meeteat.common.AppCommon;
import net.gerardomedina.meeteat.model.Food;
import net.gerardomedina.meeteat.task.DeleteFoodTask;
import net.gerardomedina.meeteat.view.activity.BaseActivity;
import net.gerardomedina.meeteat.view.fragment.BaseFragment;

import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;


public class FoodAdapter extends LongPressAwareTableDataAdapter<Food> {
    AppCommon appCommon = AppCommon.getInstance();
    private static final int TEXT_SIZE = 12;
    private final BaseActivity activity;
    private final BaseFragment fragment;


    public FoodAdapter(final BaseFragment fragment, final BaseActivity activity, final List<Food> data, final TableView<Food> tableView) {
        super(activity, data, tableView);
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final Food food = getRowData(rowIndex);
        View renderedView = null;
        switch (columnIndex) {
            case 0:
                renderedView = renderIcon(food, parentView);
                break;
            case 1:
                renderedView = renderString(food.getDescription());
                break;
            case 2:
                renderedView = renderString(String.valueOf(food.getAmount()));
                break;
            case 3:
                renderedView = renderString(food.getUsername());
                break;
        }
        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final Food food = getRowData(rowIndex);
        if (columnIndex == 0) {
            if (food.getUsername().equals(appCommon.getUser().getUsername()) ||
                    appCommon.getSelectedMeeting().getAdmin().equals(appCommon.getUser().getUsername())) {
                new AlertDialog.Builder(activity, R.style.MyAlertDialogStyle)
                        .setMessage(R.string.delete_food)
                        .setNegativeButton(fragment.getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton(fragment.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new DeleteFoodTask(fragment,food.getId()).execute();
                            }
                        })
                        .show();
            } else {
                activity.showSimpleDialog(R.string.this_food_is_not_yours);
            }
        }
        return getDefaultCellView(rowIndex, columnIndex, parentView);
    }

    private View renderIcon(final Food food, final ViewGroup parentView) {
        ImageView imageView = new ImageView(activity);
        try {
            imageView.setImageResource(R.drawable.class.getField(food.getIcon()).getInt(0));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return imageView;
    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(10, 5, 10, 5);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }
}
