package Lab_05;

abstract class Employee {
    String name;
    int id;

    Employee(String name, int id) {
        this.name = name;
        this.id = id;
    }

    abstract double monthlySalary();
}

class FullTime extends Employee {
    double salary;

    FullTime(String name, int id, double salary) {
        super(name, id);
        this.salary = salary;
    }

    double monthlySalary() {
        return salary;
    }
}

class PartTime extends Employee {
    int hours;
    double rate;

    PartTime(String name, int id, int hours, double rate) {
        super(name, id);
        this.hours = hours;
        this.rate = rate;
    }

    double monthlySalary() {
        return hours * rate;
    }
}

class Intern extends Employee {
    double stipend;

    Intern(String name, int id, double stipend) {
        super(name, id);
        this.stipend = stipend;
    }

    double monthlySalary() {
        return stipend;
    }
}

public class PayRoll {
    public static void main(String[] args) {
        Employee[] employees = {
            new FullTime("Amit", 101, 40000),
            new PartTime("Riya", 102, 80, 200),
            new Intern("Jay", 103, 8000)
        };

        double total = 0;

        for (Employee e : employees) {
            double salary = e.monthlySalary();

            System.out.println("Name: " + e.name);
            System.out.println("ID: " + e.id);
            System.out.println("Monthly salary: " + salary);

            if (e instanceof Intern) {
                System.out.println("Note: Intern employee");
            }

            total += salary;
            System.out.println();
        }

        System.out.println("Total payroll: " + total);
    }
}