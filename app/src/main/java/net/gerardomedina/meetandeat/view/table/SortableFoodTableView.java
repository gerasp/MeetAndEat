package net.gerardomedina.meetandeat.view.table;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import net.gerardomedina.meetandeat.R;
import net.gerardomedina.meetandeat.common.AppCommon;
import net.gerardomedina.meetandeat.common.Food;
import net.gerardomedina.meetandeat.common.Meeting;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.SortStateViewProviders;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;


public class SortableFoodTableView extends SortableTableView<Food> {

    AppCommon appCommon = AppCommon.getInstance();

    public SortableFoodTableView(final Context context) {
        this(context, null);
    }

    public SortableFoodTableView(final Context context, final AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
    }

    public SortableFoodTableView(final Context context, final AttributeSet attributes, final int styleAttributes) {
        super(context, attributes, styleAttributes);

        Meeting meeting = appCommon.getSelectedMeeting();

        final SimpleTableHeaderAdapter simpleTableHeaderAdapter =
                new SimpleTableHeaderAdapter(context, R.string.dummy, R.string.what, R.string.how_many, R.string.who);
        if (appCommon.isColorDark(meeting.getColor())) {
            simpleTableHeaderAdapter.setTextColor(ContextCompat.getColor(context, R.color.inverted_text));
            setHeaderSortStateViewProvider(SortStateViewProviders.brightArrows());
        }
        else {
            simpleTableHeaderAdapter.setTextColor(ContextCompat.getColor(context, R.color.primary_text));
            setHeaderSortStateViewProvider(SortStateViewProviders.darkArrows());
        }

        simpleTableHeaderAdapter.setTextSize(14);
        setHeaderBackgroundColor(Color.parseColor(meeting.getColor()));
        setHeaderAdapter(simpleTableHeaderAdapter);

        final int rowColorEven = ContextCompat.getColor(context, R.color.even_grey);
        final int rowColorOdd = ContextCompat.getColor(context, R.color.odd_grey);
        setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(rowColorEven, rowColorOdd));

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(4);
        tableColumnWeightModel.setColumnWeight(0, 1);
        tableColumnWeightModel.setColumnWeight(1, 4);
        tableColumnWeightModel.setColumnWeight(2, 2);
        tableColumnWeightModel.setColumnWeight(3, 3);
        setColumnModel(tableColumnWeightModel);

        setColumnComparator(1, FoodComparators.getFoodDescriptionComparator());
        setColumnComparator(2, FoodComparators.getFoodAmountComparator());
        setColumnComparator(3, FoodComparators.getFoodUsernameComparator());
    }

}
