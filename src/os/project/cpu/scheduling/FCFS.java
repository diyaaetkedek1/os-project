package os.project.cpu.scheduling;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author diyaa
 */
public class FCFS {

    private ArrayList<Processes> Parr;
    int X;

    public FCFS(ArrayList<Processes> Parr, int X) {
        this.Parr = Parr;
        this.X = X;

    }

    public ArrayList<Processes> getParr() {
        return Parr;
    }

    public void setParr(ArrayList<Processes> Parr) {
        this.Parr = Parr;
    }

    public int getX() {
        return X;
    }

    public void setX(int X) {
        this.X = X;
    }

    public void sortarray() {
        Collections.sort(Parr, Comparator.comparingInt(Processes::getPAt));
    }

    public void printarray() {
        for (int i = 0; i < Parr.size(); i++) {
            System.out.println(Parr.get(i).getPid() + " " + Parr.get(i).getCBT());
        }
    }

    public void calcPFT() { // calculate the finish time of the processes
        int temp;
        temp = Parr.get(0).getPAt() + Parr.get(0).getCBT();
        Parr.get(0).setPFT(temp);
        for (int i = 1; i < Parr.size(); i++) {
            temp = Parr.get(i - 1).getPFT() + Parr.get(i).getCBT() + X;
            Parr.get(i).setPFT(temp);
        }
    }

    public void calcPTT() { // caculate process turn around time
        for (int i = 0; i < Parr.size(); i++) {
            int temp;
            temp = Parr.get(i).getPFT() - Parr.get(i).getPAt();
            Parr.get(i).setPTT(temp);
        }
    }

    public void calcPWT() { // caculate process wating time
        for (int i = 0; i < Parr.size(); i++) {
            int temp;
            temp = Parr.get(i).getPTT() - Parr.get(i).getCBT();
            Parr.get(i).setPWT(temp);
        }
    }

    public int calcTotalWT() { // calculate the total wating time
        int totalWT = 0;
        for (int i = 0; i < Parr.size(); i++) {
            totalWT += Parr.get(i).getPWT();
        }
        return totalWT;
    }

    public int calcTotalTT() { // calculate the total turn around time
        int totalTT = 0;
        for (int i = 0; i < Parr.size(); i++) {
            totalTT += Parr.get(i).getPTT();
        }
        return totalTT;
    }

    public int calcTotalFT() { // caculate the total finish time
        int totalFT = 0;
        for (int i = 0; i < Parr.size(); i++) {
            totalFT += Parr.get(i).getPFT();
        }
        return totalFT;
    }

    public int calcTotalCBT() { // caculate the total Burst time 
        int totalCBT = 0;
        for (int i = 0; i < Parr.size(); i++) {
            totalCBT += Parr.get(i).getCBT();
        }
        return totalCBT;
    }

    public void calcBegAndEndT() { // calculate the beginig and ending time of the process in the CPU
        int temp;
        for (int i = 0; i < Parr.size(); i++) {
            temp = Parr.get(i).getPFT() - Parr.get(i).getCBT();
            Parr.get(i).setPbegT(temp);
            temp = Parr.get(i).getPFT();
            Parr.get(i).setPendT(temp);
        }

    }

    public int calcMaxFT() { // caclulate the maximum Finish time
        int maxFT = Parr.get(0).getPFT();
        for (int i = 1; i < Parr.size(); i++) {
            if (Parr.get(i).getPFT() > maxFT) {
                maxFT = Parr.get(i).getPFT();
            }
        }
        return maxFT;
    }

    public void printGanttChart() {  // print the gantt chart 
        System.out.println("Gantt Chart: ");
        StringBuilder dashes = new StringBuilder();
        for (int i = 0; i < 20 * Parr.size(); i++) {
            dashes.append("-");
        }
        System.out.println(dashes.toString());

        for (int i = 0; i < Parr.size(); i++) {
            System.out.print("|   " + Parr.get(i).getPbegT() + "   P" + Parr.get(i).getPid() + "   " + Parr.get(i).getPendT() + "  ");
        }
        System.out.println("|\n" + dashes.toString());

        System.out.println("\n");
    }

    public void printProcessesAtt() { // print the processes attributes 
        System.out.println("processID\t\t Arrival time\t\t Burst time\t\t finish time\t\tturnaround time\t\t waiting time  ");
        for (int i = 0; i < Parr.size(); i++) {
            System.out.println(Parr.get(i).getPid() + "\t\t\t    " + Parr.get(i).getPAt() + "\t\t\t    " + Parr.get(i).getCBT() + "\t\t\t    " + Parr.get(i).getPFT() + "\t\t\t    "
                    + Parr.get(i).getPTT() + "\t\t\t    " + Parr.get(i).getPWT());

        }
        double temp;
        System.out.println("\n");
        temp = (double) calcTotalFT() / Parr.size();
        temp = Math.round(temp * 1000.0) / 1000.0;
        System.out.println("Average finish  time = " + temp);
        temp = (double) ((double) calcTotalWT() / Parr.size());
        temp = Math.round(temp * 1000.0) / 1000.0;
        System.out.println("Avarage Wating Time: " + temp);
        temp = (double) calcTotalTT() / Parr.size();
        temp = Math.round(temp * 1000.0) / 1000.0;
        System.out.println("Average turn around time = " + temp);
        temp = ((double) calcTotalCBT() / calcMaxFT()) * 100;
        temp = Math.round(temp * 1000.0) / 1000.0;
        System.out.println("Cpu Utilization : " + temp + "%");
        PCBfile();
    }

    public void PCBfile() {
        try {
            FileWriter writer = new FileWriter("PCBFile.txt");
            writer.write("at time 0:\n\t");
            for (int i = 0; i < Parr.size(); i++) {
                if (i == Parr.size() - 1) {
                    writer.write("Process " + Parr.get(i).getPid() + " is Ready\n");
                } else {
                    writer.write("Process " + Parr.get(i).getPid() + " is Ready\n\t");
                }

            }

            writer.write("when the simulation is done:\n\t");
            for (int i = 0; i < Parr.size(); i++) {
                writer.write("Process " + Parr.get(i).getPid() + " is Finished\n\t");

            }
            writer.close();
            ;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void run() { // run the program
        System.out.println("CS = " + this.X);
        sortarray();
        calcPFT();
        calcPTT();
        calcPWT();
        calcBegAndEndT();
        printGanttChart();
        printProcessesAtt();

    }
}
