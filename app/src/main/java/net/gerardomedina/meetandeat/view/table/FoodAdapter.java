package net.gerardomedina.meetandeat.view.table;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.model.Food;

import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;


public class FoodAdapter extends LongPressAwareTableDataAdapter<Food> {

    private static final int TEXT_SIZE = 12;
    private final Context context;


    public FoodAdapter(final Context context, final List<Food> data, final TableView<Food> tableView) {
        super(context, data, tableView);
        this.context = context;
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
        View renderedView = null;

        return renderedView;
    }

    private View renderIcon(final Food food, final ViewGroup parentView) {
        ImageView imageView = new ImageView(context);
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
