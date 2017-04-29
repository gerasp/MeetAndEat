package net.gerardomedina.meetandeat.view.table;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import net.gerardomedina.meetandeat.R;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.SortStateViewProviders;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;
import de.codecrafters.tableviewexample.data.Food;


public class SortableFoodTableView extends SortableTableView<Food> {

    public SortableFoodTableView(final Context context) {
        this(context, null);
    }

    public SortableFoodTableView(final Context context, final AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
    }

    public SortableFoodTableView(final Context context, final AttributeSet attributes, final int styleAttributes) {
        super(context, attributes, styleAttributes);

        final SimpleTableHeaderAdapter simpleTableHeaderAdapter = new SimpleTableHeaderAdapter(context, R.string.brand, R.string.model, R.string.power, R.string.price);
        simpleTableHeaderAdapter.setTextColor(ContextCompat.getColor(context, R.color.table_header_text));
        setHeaderAdapter(simpleTableHeaderAdapter);

        final int rowColorEven = ContextCompat.getColor(context, R.color.table_data_row_even);
        final int rowColorOdd = ContextCompat.getColor(context, R.color.table_data_row_odd);
        setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(rowColorEven, rowColorOdd));
        setHeaderSortStateViewProvider(SortStateViewProviders.brightArrows());

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(4);
        tableColumnWeightModel.setColumnWeight(0, 2);
        tableColumnWeightModel.setColumnWeight(1, 3);
        tableColumnWeightModel.setColumnWeight(2, 3);
        tableColumnWeightModel.setColumnWeight(3, 2);
        setColumnModel(tableColumnWeightModel);

        setColumnComparator(0, FoodComparators.getFoodProducerComparator());
        setColumnComparator(1, FoodComparators.getFoodNameComparator());
        setColumnComparator(2, FoodComparators.getFoodPowerComparator());
        setColumnComparator(3, FoodComparators.getFoodPriceComparator());
    }

}
