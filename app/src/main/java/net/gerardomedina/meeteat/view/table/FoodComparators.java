package net.gerardomedina.meeteat.view.table;

import net.gerardomedina.meeteat.model.Food;

import java.util.Comparator;

public final class FoodComparators {

    private FoodComparators() {
    }

    public static Comparator<Food> getFoodDescriptionComparator() {
        return new FoodDescriptionComparator();
    }

    public static Comparator<Food> getFoodAmountComparator() {
        return new FoodAmountComparator();
    }

    public static Comparator<Food> getFoodUsernameComparator() {
        return new FoodUsernameComparator();
    }

    private static class FoodDescriptionComparator implements Comparator<Food> {
        @Override
        public int compare(Food o1, Food o2) {
            return o1.getDescription().compareTo(o2.getDescription());
        }
    }

    private static class FoodAmountComparator implements Comparator<Food> {
        @Override
        public int compare(Food o1, Food o2) {
            return o1.getAmount() - o2.getAmount();
        }
    }

    private static class FoodUsernameComparator implements Comparator<Food> {
        @Override
        public int compare(Food o1, Food o2) {
            return o1.getUsername().compareTo(o2.getUsername());
        }
    }
}
