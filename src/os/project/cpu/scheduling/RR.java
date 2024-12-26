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
public class RR {

    private ArrayList<Processes> Parr;
    private ArrayList<Processes> Garr;
    private ArrayList<Processes> PAT;
    private int X;
    private int Q;

    public RR(ArrayList<Processes> Parr, int X, int Q) {
        this.Parr = Parr;
        this.Garr = new ArrayList<>();
        this.PAT = new ArrayList<>();
        for (int i = 0; i < Parr.size(); i++) {
            PAT.add(new Processes(Parr.get(i).getPid(), Parr.get(i).getPAt(), Parr.get(i).getCBT()));
        }
        this.X = X;
        this.Q = Q;
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

    public int getQ() {
        return Q;
    }

    public void setQ(int Q) {
        this.Q = Q;
    }

    public void fun() {
        Collections.sort(Parr, Comparator.comparingInt(Processes::getPAt));

        int temp;
        int time = 0;
        while (true) {
            boolean flag = true;
            for (int i = 0; i < Parr.size(); i++) {
                if (Parr.get(i).getPAt() <= time) {
                    if (Parr.get(i).getPAt() <= Q) {
                        if (Parr.get(i).getCBT() > 0) {
                            flag = false;
                            if (Parr.get(i).getCBT() > Q) { // if cpu burst time > quantum
                                temp = Parr.get(i).getPid();
                                Garr.add(new Processes(temp, time, (time + Q), 0));
                                time = time + Q + X;
                                temp = Parr.get(i).getCBT() - Q;
                                Parr.get(i).setCBT(temp);
                                temp = Parr.get(i).getPAt() + Q;
                                Parr.get(i).setPAt(temp);

                            } else { // if cpu burst time < quantum 
                                temp = Parr.get(i).getPid();
                                Garr.add(new Processes(temp, time, (time + Parr.get(i).getCBT()), 0));
                                time = time + Parr.get(i).getCBT() + X;
                                temp = time - Parr.get(i).getPAt();
                                Parr.get(i).setPFT(temp);
                                temp -= Parr.get(i).getCBT();
                                Parr.get(i).setPWT(temp);
                                Parr.get(i).setCBT(0);

                            }
                        }
                    } else if (Parr.get(i).getPAt() > Q) {
                        for (int j = 0; j < Parr.size(); j++) {
                            if (Parr.get(j).getPAt() < Parr.get(i).getPAt()) {
                                if (Parr.get(j).getCBT() > 0) {
                                    flag = false;
                                    if (Parr.get(j).getCBT() > Q) {
                                        Garr.add(new Processes(j, time, (time + Q), 0));
                                        time = time + Q + X;
                                        temp = Parr.get(j).getCBT() - Q;
                                        Parr.get(j).setCBT(temp);
                                        temp = Parr.get(j).getPAt() + Q;
                                        Parr.get(j).setPAt(temp);
                                    } else {
                                        temp = Parr.get(j).getPid();
                                        Garr.add(new Processes(temp, time, (time + Parr.get(j).getCBT()), 0));
                                        time = time + Parr.get(j).getCBT() + X;
                                        temp = time - Parr.get(j).getPAt();
                                        Parr.get(j).setPFT(temp);
                                        temp -= Parr.get(j).getCBT();
                                        Parr.get(j).setPWT(temp);
                                        Parr.get(j).setCBT(0);

                                    }
                                }
                            }

                        }
                        if (Parr.get(i).getCBT() > 0) { // iam hereeeeeeeeeeeeeeeeeeeeeeeee
                            flag = false;
                            if (Parr.get(i).getCBT() > Q) {
                                temp = Parr.get(i).getPid();
                                Garr.add(new Processes(temp, time, (time + Q), 0));
                                time = time + Q + X;
                                temp = Parr.get(i).getCBT() - Q;
                                Parr.get(i).setCBT(temp);
                                temp = Parr.get(i).getPAt() + Q;
                                Parr.get(i).setPAt(temp);
                            } else {
                                temp = Parr.get(i).getPid();
                                Garr.add(new Processes(temp, time, (time + Parr.get(i).getCBT()), 0));
                                time = time + Parr.get(i).getCBT() + X;
                                temp = time - Parr.get(i).getPAt();
                                Parr.get(i).setPFT(temp);
                                temp -= Parr.get(i).getCBT();
                                Parr.get(i).setPWT(temp);
                                Parr.get(i).setCBT(0);
                            }
                        }
                    }
                } else if (Parr.get(i).getPAt() > time) {
                    time++;
                    i--;
                }

            }
            if (flag) {
                break;
            }
        }
    }

    public int calcTotalWT() { // calculate the wating time
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

    public int calcTotalCBT() { // caculate the total finish time 
        int totalCBT = 0;
        for (int i = 0; i < Parr.size(); i++) {
            totalCBT += PAT.get(i).getCBT();
        }
        return totalCBT;
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

    public static ArrayList<Integer> findMatchingIndices(ArrayList<Processes> list) {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i).getPid() == list.get(i + 1).getPid()) {
                indices.add(i + 1);  // Add the index of the current person if the next person has the same ID
            }
        }
        return indices;
    }

    public void printGanttChart() {  // print the gantt chart and its true

        ArrayList<Integer> matchingIndices = findMatchingIndices(Garr);
        for (int i = 0; i < matchingIndices.size(); i++) {
            int j = matchingIndices.get(i);
            int ss = (Garr.get(j).getPbegT() - Garr.get(j - 1).getPendT());
            Garr.get(j).setPbegT(Garr.get(j).getPbegT() - ss);
            Garr.get(j).setPendT(Garr.get(j).getPendT() - ss);
        }

        System.out.println("Gantt Chart: ");
        StringBuilder dashes = new StringBuilder();
        for (int i = 0; i < 2 * Q * calcTotalCBT(); i++) {
            dashes.append("-");
        }
        System.out.println(dashes.toString());
        for (int i = 0; i < Garr.size(); i++) {
            System.out.print("|   " + Garr.get(i).getPbegT() + "   P" + Garr.get(i).getPid() + "   " + Garr.get(i).getPendT() + "  ");
        }
        System.out.println("");
        System.out.println(dashes.toString());
    }

    public int findLastIndexOf(ArrayList<Processes> array, int target) {
        int lastIndex = -1; // Default value if the element is not found
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getPid() == target) {
                lastIndex = i; // Update lastIndex to the current index each time the target is found
            }
        }
        return lastIndex;
    }

    public static ArrayList<Integer> findIndicesOfId(ArrayList<Processes> list, int id) {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getPid() == id) {
                indices.add(i);  // Add the index if the person's ID matches the search ID
            }
        }
        return indices;
    }

    public void printProcessesAtt() { // print the processes attributes and some is wrong

        for (int i = 0; i < Garr.size(); i++) {
            int index = findLastIndexOf(Garr, Garr.get(i).getPid());

            if (index != -1) {
                int eeee = findLastIndexOf(Parr, Garr.get(index).getPid());
                Parr.get(eeee).setPFT(Garr.get(index).getPendT());
            }

        }

        // turn around time
        for (int i = 0; i < PAT.size(); i++) {
            Parr.get(i).setPAt(PAT.get(i).getPAt());
            Parr.get(i).setPTT(Parr.get(i).getPFT() - Parr.get(i).getPAt());
        }

        // wating time
        for (int i = 0; i < Parr.size(); i++) {
            int ss = 0;
            ArrayList<Integer> indices = findIndicesOfId(Garr, Parr.get(i).getPid());
            if (indices.isEmpty()) {
                System.out.println("No persons with ID " + Parr.get(i).getPid() + " found.");
            } else {
                ss = Garr.get(indices.get(0)).getPbegT() - Parr.get(i).getPAt();

                for (int j = 1; j < indices.size(); j++) {
                    ss += Garr.get(indices.get(j)).getPbegT() - Garr.get(indices.get(j - 1)).getPendT();

                }
            }
            Parr.get(i).setPWT(ss);
        }

        System.out.println("processID\t\t Arrival time\t\t Burst time\t\t finish time\t\t turnaround time\t waiting time  ");
        for (int i = 0; i < Parr.size(); i++) {
            System.out.println(Parr.get(i).getPid() + "\t\t\t    " + PAT.get(i).getPAt() + "\t\t\t    " + PAT.get(i).getCBT() + "\t\t\t    " + Parr.get(i).getPFT() + "\t\t\t    "
                    + Parr.get(i).getPTT() + "\t\t\t    " + Parr.get(i).getPWT());

        }
        double temp;

        System.out.println("\n\n");
        temp = (double) calcTotalFT() / Parr.size();
        temp = Math.round(temp * 1000.0) / 1000.0;
        System.out.println("Avarage Finish Time = " + temp);
        temp = (double) calcTotalWT() / Parr.size();
        temp = Math.round(temp * 1000.0) / 1000.0;
        System.out.println("Avarage Wating Time = " + temp);
        temp = (double) calcTotalTT() / Parr.size();
        temp = Math.round(temp * 1000.0) / 1000.0;
        System.out.println("Average Turnaround time = " + temp); // all below is wrong
        temp = ((double) calcTotalCBT() / calcMaxFT()) * 100;
        temp = Math.round(temp * 1000.0) / 1000.0;
        System.out.println("Cpu Utilization : " + temp + "%");

        for (int i = 0; i < Parr.size(); i++) {
            Parr.get(i).setCBT(PAT.get(i).getCBT());
        }
    }

    

    public void run() {
        System.out.println("CS = " + this.X + "\t,\tQ = " + this.Q);
        fun();
        printGanttChart();
        printProcessesAtt();
    }
}
