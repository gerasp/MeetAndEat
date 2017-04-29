package net.gerardomedina.meetandeat.view.table;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.common.Food;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.SortStateViewProviders;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;


public class SortableFoodTableView extends SortableTableView<Food> {

    public SortableFoodTableView(final Context context) {
        this(context, null);
    }

    public SortableFoodTableView(final Context context, final AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
    }

    public SortableFoodTableView(final Context context, final AttributeSet attributes, final int styleAttributes) {
        super(context, attributes, styleAttributes);

        final SimpleTableHeaderAdapter simpleTableHeaderAdapter =
                new SimpleTableHeaderAdapter(context, R.string.dummy, R.string.what, R.string.how_many, R.string.who);
        simpleTableHeaderAdapter.setTextColor(ContextCompat.getColor(context, R.color.primary_text));
        setHeaderAdapter(simpleTableHeaderAdapter);

        final int rowColorEven = ContextCompat.getColor(context, R.color.background);
        final int rowColorOdd = ContextCompat.getColor(context, R.color.light_grey);
        setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(rowColorEven, rowColorOdd));
        setHeaderSortStateViewProvider(SortStateViewProviders.brightArrows());

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(4);
        tableColumnWeightModel.setColumnWeight(0, 2);
        tableColumnWeightModel.setColumnWeight(1, 3);
        tableColumnWeightModel.setColumnWeight(2, 3);
        tableColumnWeightModel.setColumnWeight(3, 2);
        setColumnModel(tableColumnWeightModel);

        setColumnComparator(1, FoodComparators.getFoodDescriptionComparator());
        setColumnComparator(2, FoodComparators.getFoodAmountComparator());
        setColumnComparator(3, FoodComparators.getFoodUsernameComparator());
    }

}
