package net.gerardomedina.meetandeat.view.table;

import android.app.Activity;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;

public class FoodTable {

    private final TableColumnWeightModel columnModel;
    private final TableView foodTable;
    private final Activity activity;

    public FoodTable(Activity activity, TableView foodTable) {
        this.foodTable = foodTable;
        this.activity = activity;

        columnModel = new TableColumnWeightModel(4);
        columnModel.setColumnWeight(1, 2);
        columnModel.setColumnWeight(2, 2);
        this.foodTable.setColumnModel(columnModel);


        final String[][] dataToShow = { { "This", "is", "a", "test" },
                { "and", "a", "second", "test" } };
        foodTable.setDataAdapter(new SimpleTableDataAdapter(activity, dataToShow));


    }
}
