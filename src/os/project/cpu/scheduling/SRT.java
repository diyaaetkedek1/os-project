package os.project.cpu.scheduling;

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author diyaa
 */
public class SRT {

    private ArrayList<Processes> Parr;
    private int X;

    public SRT(ArrayList<Processes> Parr, int X) {
        this.Parr = Parr;
        this.X = X;
    }

    public void run() {
        System.out.println("CS = " + this.X);
        int n;

        n = Parr.size();

        int[] arrivalTime = new int[n];
        int[] burstTime = new int[n];
        int[] completionTime = new int[n];
        int[] waitingTime = new int[n];
        int[] turnaroundTime = new int[n];
        int[] remainingTime = new int[n];
        int[] processNum = new int[n];
        List<List<Integer>> startTimes = new ArrayList<>();
        int completed = 0;
        int totalWaitingTime = 0, totalTurnaroundTime = 0;

        for (int i = 0; i < n; i++) {
            processNum[i] = Parr.get(i).getPid();

            arrivalTime[i] = Parr.get(i).getPAt();
            burstTime[i] = Parr.get(i).getCBT();
            remainingTime[i] = burstTime[i];
            startTimes.add(new ArrayList<>());
        }

        int currTime = 0;
        int lastProcess = -1;
        while (completed != n) {
            int shortestTime = Integer.MAX_VALUE;
            int shortestProcess = -1;
            for (int i = 0; i < n; i++) {
                if (arrivalTime[i] <= currTime && remainingTime[i] < shortestTime && remainingTime[i] > 0) {
                    shortestTime = remainingTime[i];
                    shortestProcess = i;
                }
            }
            if (shortestProcess == -1) {
                // If no process is ready, increment time by 1 unit.
                currTime++;
            } else {
                if (lastProcess != shortestProcess) {
                    if (lastProcess != -1) {
                        // Apply context switch time when switching from one process to another
                        currTime += X;
                    }
                    startTimes.get(shortestProcess).add(currTime);
                    lastProcess = shortestProcess;
                }
                // Simulate the execution of the process for one time unit
                remainingTime[shortestProcess]--;
                currTime++;
                if (remainingTime[shortestProcess] == 0) {
                    completed++;
                    completionTime[shortestProcess] = currTime;
                    waitingTime[shortestProcess] = completionTime[shortestProcess] - arrivalTime[shortestProcess] - burstTime[shortestProcess];
                    turnaroundTime[shortestProcess] = completionTime[shortestProcess] - arrivalTime[shortestProcess];
                }
            }
        }

        printGanttChart(processNum, startTimes, completionTime);
        System.out.println("\n\n");

        System.out.println("Process ID\t\t Arrival Time\t\t Burst Time\t\t Finish Time\t\t Waiting Time\t\t Turnaround Time");
        for (int i = 0; i < n; i++) {
            System.out.println(processNum[i] + "\t\t\t    " + arrivalTime[i] + "\t\t\t    " + burstTime[i] + "\t\t\t    " + completionTime[i] + "\t\t\t    " + waitingTime[i] + "\t\t\t    " + turnaroundTime[i]);
            totalWaitingTime += waitingTime[i];
            totalTurnaroundTime += turnaroundTime[i];
        }
        System.out.println("\n\n");
        double temp;
        temp = calcTotalFT(completionTime) / n;
        temp = Math.round(temp * 1000.0) / 1000.0;
        System.out.println("Avarage Finish Time = " + temp);
        temp = (double) totalWaitingTime / n;
        temp = Math.round(temp * 1000.0) / 1000.0;
        System.out.println("Average Waiting Time = " + temp);
        temp = (double) totalTurnaroundTime / n;
        temp = Math.round(temp * 1000.0) / 1000.0;
        System.out.println("Average Turnaround Time = " + temp);
        temp = ((double) calcTotalCBT() / calcMaxFT(completionTime)) * 100;
        temp = Math.round(temp * 1000.0) / 1000.0;
        System.out.println("Cpu Utilization = " + temp + "%");
        PCBfile();
    }

    public int calcMaxFT(int[] completionTime) { // caclulate the maximum Finish time
        int maxFT = completionTime[0];
        for (int i = 1; i < completionTime.length; i++) {
            if (completionTime[i] > maxFT) {
                maxFT = completionTime[i];
            }
        }
        return maxFT;
    }

    public int calcTotalCBT() { // caculate the total Burst time 
        int totalCBT = 0;
        for (int i = 0; i < Parr.size(); i++) {
            totalCBT += Parr.get(i).getCBT();
        }

        return totalCBT;
    }

    public int calcTotalFT(int[] completionTime) {
        int totalFT = 0;
        for (int i = 0; i < completionTime.length; i++) {
            totalFT += completionTime[i];
        }
        return totalFT;
    }

    private void printGanttChart(int[] processNum, List<List<Integer>> startTimes, int[] completionTime) {

        StringBuilder dashes = new StringBuilder();
        for (int i = 0; i < calcTotalCBT() * 10; i++) {
            dashes.append("-");
        }

        System.out.println("Gantt Chart:");
        ArrayList<Processes> Garr = new ArrayList<>();
        for (int i = 0; i < processNum.length; i++) {
            for (int start : startTimes.get(i)) {

                Garr.add(new Processes(processNum[i], start, completionTime[i], 0));
            }
        }

        Collections.sort(Garr, Comparator.comparingInt(Processes::getPbegT));

        Stack<Integer> duplicateIndices = findDuplicateIndices(Garr);
        while (!duplicateIndices.isEmpty()) {
            int index = duplicateIndices.pop();

            Garr.get(index).setPendT(Garr.get(index + 1).getPbegT() - X);
        }
        System.out.println(dashes.toString());
        for (int i = 0; i < Garr.size(); i++) {
            System.out.print("|   " + Garr.get(i).getPbegT() + "   P" + Garr.get(i).getPid() + "   " + Garr.get(i).getPendT() + "  ");
        }
        System.out.println('|' + "");
        System.out.println(dashes.toString());

    }

    public static Stack<Integer> findDuplicateIndices(ArrayList<Processes> array) {
        Map<Integer, Integer> lastOccurrenceMap = new HashMap<>();
        Stack<Integer> stack = new Stack<>();

        // Populate the map with the last occurrence of each element
        for (int i = array.size() - 1; i >= 0; i--) {
            if (!lastOccurrenceMap.containsKey(array.get(i).getPid())) {
                lastOccurrenceMap.put(array.get(i).getPid(), i);
            }
        }

        // Check earlier indices for duplicates and push to stack
        for (int i = array.size() - 1; i >= 0; i--) {
            if (lastOccurrenceMap.get(array.get(i).getPid()) != i) { // if this index is not the last occurrence
                stack.push(i);
            }
        }

        return stack;
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
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
