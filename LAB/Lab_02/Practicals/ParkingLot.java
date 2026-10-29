public class ParkingLot {

    private int twoWheelers;
    private int fourWheelers;

    private final int twoCap;
    private final int fourCap;

    private static long revenue = 0;

    public ParkingLot(int twoCap, int fourCap) {
        this.twoCap = twoCap;
        this.fourCap = fourCap;
        this.twoWheelers = 0;
        this.fourWheelers = 0;
    }

    public void park(String type) {

        if (type.equalsIgnoreCase("two")) {

            if (twoWheelers < twoCap) {
                twoWheelers++;
                revenue += 20;
                System.out.println("Two-wheeler parked.");
            } else {
                System.out.println("Two-wheeler section Full");
            }

        } else if (type.equalsIgnoreCase("four")) {

            if (fourWheelers < fourCap) {
                fourWheelers++;
                revenue += 40;
                System.out.println("Four-wheeler parked.");
            } else {
                System.out.println("Four-wheeler section Full");
            }

        } else {
            System.out.println("Invalid vehicle type.");
        }
    }

    public void leave(String type) {

        if (type.equalsIgnoreCase("two")) {

            if (twoWheelers > 0) {
                twoWheelers--;
                System.out.println("Two-wheeler left.");
            } else {
                System.out.println("No two-wheelers to leave.");
            }

        } else if (type.equalsIgnoreCase("four")) {

            if (fourWheelers > 0) {
                fourWheelers--;
                System.out.println("Four-wheeler left.");
            } else {
                System.out.println("No four-wheelers to leave.");
            }

        } else {
            System.out.println("Invalid vehicle type.");
        }
    }

    public int getTwoWheelers() {
        return twoWheelers;
    }

    public int getFourWheelers() {
        return fourWheelers;
    }

    public static long getRevenue() {
        return revenue;
    }

    public static void main(String[] args) {

        ParkingLot lot = new ParkingLot(3, 2);

        lot.park("two");
        lot.park("two");
        lot.park("two");
        lot.park("two");

        lot.park("four");
        lot.park("four");
        lot.park("four");

        lot.leave("two");
        lot.leave("four");

        lot.park("two");
        lot.park("four");

        System.out.println();

        System.out.println("Final Two-Wheelers: " + lot.getTwoWheelers());
        System.out.println("Final Four-Wheelers: " + lot.getFourWheelers());
        System.out.println("Total Revenue: " + ParkingLot.getRevenue());
    }
}