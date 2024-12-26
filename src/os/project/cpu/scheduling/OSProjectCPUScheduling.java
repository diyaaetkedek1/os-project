package os.project.cpu.scheduling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

/**
 *
 * @author diyaa
 */
public class OSProjectCPUScheduling {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    boolean exit = false;

    public static void main(String[] args) throws FileNotFoundException {

      //  loadDefultFileValues(); // please uncomint this comment if you have any problem with the processes file
        OSProjectCPUScheduling menu = new OSProjectCPUScheduling();
        menu.runMenu();

    }

    public void runMenu() throws FileNotFoundException {
        printHeader();
        while (!exit) {
            printMenu();
            int choice = getMenuChoice();
            performAction(choice);
        }

    }

    private void printHeader() {

        System.out.println("*************************************");
        System.out.println("|      Diyaa Etkedek     217023 |");
        System.out.println("*************************************");

    }

    private void printMenu() {
        displayHeader("Please Enter a selection: ");
        System.out.println("1)  Modify the Processes file");
        System.out.println("2)  Simulate FCFS");
        System.out.println("3)  Simulate RR");
        System.out.println("4)  Simulate SRT");
        System.out.println("5)  Exit");
    }

    private int getMenuChoice() {
        Scanner keyboard = new Scanner(System.in);
        int choice = -1;
        do {
            System.out.print("Enter your choice: ");
            try {
                choice = Integer.parseInt(keyboard.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid selection, only Numbers  please.");
            }
            if (choice < 0 || choice > 6) {
                System.out.println("Choice outside of range, Please choose again.");
            }
        } while (choice < 0 || choice > 6);
        return choice;
    }

    private void performAction(int choice) throws FileNotFoundException {
        int Q, X; // define the quantim and context switch time
        ArrayList<Processes> Parr = new ArrayList<>();
        File file = new File("Processes.txt");
        Scanner fin = new Scanner(file);
        Q = fin.nextInt();
        X = fin.nextInt();
        while (fin.hasNextInt()) {
            Parr.add(new Processes(fin.nextInt(), fin.nextInt(), fin.nextInt()));
        }
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i <= 200; i++) {
            stars.append("*");
        }
        switch (choice) {

            case 1 -> {
                modifyTheContentOfTheFIle("Processes.txt");
                System.out.println("\n" + stars.toString());

            }

            case 2 -> {
                System.out.println("\n" + stars.toString());

                System.out.println("\nFirst Come First Served\n");
                FCFS f = new FCFS(Parr, X);
                f.run();

            }
            case 3 -> {
                System.out.println("\n" + stars.toString());

                System.out.println("Round Robin\n");
                RR r = new RR(Parr, X, Q);
                r.run();
            }
            case 4 -> {
                System.out.println("\n" + stars.toString());

                System.out.println("Shortast Remaning Time\n");
                SRT S = new SRT(Parr, X);
                S.run();
            }
            case 5 -> {
                System.out.println("******************************************");
                System.out.println("| Good Bey thinks for running my Code :) |");
                System.out.println("******************************************");

                try {
                    FileWriter writer = new FileWriter("PCBFile.txt");
                    writer.write("");

                    writer.close();
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }

                System.exit(0);
            }

            default ->
                System.out.println("Unknown error has occurred.");

        }
    }

    private void displayHeader(String message) {

        System.out.println();
        int width = message.length() + 6;
        StringBuilder sb = new StringBuilder();
        sb.append("=");
        for (int i = 0; i < width; ++i) {
            sb.append("=");
        }
        sb.append("=");
        System.out.println(sb.toString());
        System.out.println("|   " + message + "   |");
        System.out.println(sb.toString());

    }

    public void modifyTheContentOfTheFIle(String filePath) {
        Scanner in = new Scanner(System.in);
        try (FileWriter fileWriter = new FileWriter(filePath, false)) { // 'false' to overwrite the file
            fileWriter.write(""); // Writing an empty string to clear the file
        } catch (IOException e) {
            System.out.println("Error occurred while clearing the file: " + e.getMessage());
            return; // Exit if file clearing fails
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            System.out.println("Enter the RoundRobin Qauntum(Q): ");
            int Q = in.nextInt();
            System.out.println("Enter the Context Switch time(X): ");
            int X = in.nextInt();
            writer.write(Q + "\n");
            writer.write(X + "\n");
            System.out.println("Enter How many Processes would you like to schedule:");
            int N = in.nextInt();
            for (int i = 0; i < N; i++) {

                if (i == 0) {
                    System.out.print("Enter the 1st Process id: ");
                    writer.write(in.nextInt() + "\t");
                    System.out.println("");
                    System.out.print("Enter the 1st Process Arrive Time: ");
                    writer.write(in.nextInt() + "\t");
                    System.out.println("");
                    System.out.print("Enter the 1st Process Burst time: ");
                    writer.write(in.nextInt() + "\n");
                    System.out.println("");
                } else if (i == 1) {
                    System.out.print("Enter the 2nd Process id: ");
                    writer.write(in.nextInt() + "\t");
                    System.out.println("");
                    System.out.print("Enter the 2nd Process Arrive Time: ");
                    writer.write(in.nextInt() + "\t");
                    System.out.println("");
                    System.out.print("Enter the 2nd Process Burst time: ");
                    writer.write(in.nextInt() + "\n");
                    System.out.println("");
                } else if (i == 2) {
                    System.out.print("Enter the 3rd Process id: ");
                    writer.write(in.nextInt() + "\t");
                    System.out.println("");
                    System.out.print("Enter the 3rd Process Arrive Time: ");
                    writer.write(in.nextInt() + "\t");
                    System.out.println("");
                    System.out.print("Enter the 3rd Process Burst time: ");
                    writer.write(in.nextInt() + "\n");
                    System.out.println("");
                } else {
                    System.out.print("Enter the " + (i + 1) + "th Process id: ");
                    writer.write(in.nextInt() + "\t");
                    System.out.println("");
                    System.out.print("Enter the " + (i + 1) + "th Process Arrive Time: ");
                    writer.write(in.nextInt() + "\t");
                    System.out.println("");
                    System.out.print("Enter the " + (i + 1) + "th Process Burst time: ");
                    writer.write(in.nextInt() + "\n");
                    System.out.println("");
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

    }

    public static void loadDefultFileValues() {
        try (FileWriter fileWriter = new FileWriter("Processes.txt", false)) {
            fileWriter.write("4"
                    + "\n1"
                    + "\n1\t0\t8"
                    + "\n2\t1\t4"
                    + "\n3\t2\t9"
                    + "\n4\t3\t5");
        } catch (IOException e) {
            System.out.println("Error occurred while load a defult value for the file: " + e.getMessage());
            return; // Exit if file clearing fails
        }
    }
}
