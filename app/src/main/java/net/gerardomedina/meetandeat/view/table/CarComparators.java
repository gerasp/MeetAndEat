package net.gerardomedina.meetandeat.view.table;

import net.gerardomedina.meetandeat.common.Food;

import java.util.Comparator;



public final class CarComparators {

    private CarComparators() {
        //no instance
    }

    public static Comparator<Food> getCarProducerComparator() {
        return new CarProducerComparator();
    }

    public static Comparator<Food> getCarPowerComparator() {
        return new CarPowerComparator();
    }

    public static Comparator<Food> getCarNameComparator() {
        return new CarNameComparator();
    }

    public static Comparator<Food> getCarPriceComparator() {
        return new CarPriceComparator();
    }


    private static class CarProducerComparator implements Comparator<Food> {

        @Override
        public int compare(final Food food1, final Food food2) {
            return food1.getProducer().getName().compareTo(food2.getProducer().getName());
        }
    }

    private static class CarPowerComparator implements Comparator<Food> {

        @Override
        public int compare(final Food food1, final Food food2) {
            return food1.getPs() - food2.getPs();
        }
    }

    private static class CarNameComparator implements Comparator<Food> {

        @Override
        public int compare(final Food food1, final Food food2) {
            return food1.getName().compareTo(food2.getName());
        }
    }

    private static class CarPriceComparator implements Comparator<Food> {

        @Override
        public int compare(final Food food1, final Food food2) {
            if (food1.getPrice() < food2.getPrice()) return -1;
            if (food1.getPrice() > food2.getPrice()) return 1;
            return 0;
        }
    }

}
