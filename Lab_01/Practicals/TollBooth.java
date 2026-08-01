import java.util.Scanner;

record Vehicle(String number, String type) {}

public class TollBooth {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int totalToll = 0;
        int bikeCount = 0;
        int carCount = 0;
        int truckCount = 0;

        while (true) {

            System.out.print("Enter Vehicle Number : ");
            String vehicleNumber = sc.next();

            if (vehicleNumber.equalsIgnoreCase("done")) {
                break;
            }

            System.out.print("Enter Vehicle Type (bike, car, truck): ");
            String type = sc.next().toLowerCase();

            Vehicle vehicle = new Vehicle(vehicleNumber, type);

            switch (vehicle.type()) {

                case "bike":
                    totalToll += 20;
                    bikeCount++;
                    break;

                case "car":
                    totalToll += 50;
                    carCount++;
                    break;

                case "truck":
                    totalToll += 150;
                    truckCount++;
                    break;
            }
        }

        System.out.println("Total Toll: " + totalToll);

        if (bikeCount >= carCount && bikeCount >= truckCount) {
            System.out.println("Most Frequent: bike");
        } 
        else if (carCount >= bikeCount && carCount >= truckCount) {
            System.out.println("Most Frequent: car");
        } 
        else {
            System.out.println("Most Frequent: truck");
        }

        sc.close();
    }
}