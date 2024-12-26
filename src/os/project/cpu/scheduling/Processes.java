package os.project.cpu.scheduling;

/**
 *
 * @author diyaa
 */
public class Processes {

    private int Pid; // process id
    private int PAt; // process arival time
    private int CBT; // process CPU burst time
    private int PFT; // process finish time
    private int PWT; // process wating time
    private int PTT; // Process turnaround time
    private int PbegT; // process begin time in cpu
    private int PendT; // process end time in cpu
    private boolean finshed = false;

    // constructor, setters and getters
    public Processes(int Pid, int PAt, int CBT) {
        this.Pid = Pid;
        this.PAt = PAt;
        this.CBT = CBT;
    }

    public Processes(int Pid, int PbegT, int PendT, int PAt) {
        this.Pid = Pid;
        this.PbegT = PbegT;
        this.PendT = PendT;
        this.PAt = PAt;

    }

    public boolean isFinshed() {
        return finshed;
    }

    public void setFinshed(boolean finshed) {
        this.finshed = finshed;
    }

    public int getPid() {
        return Pid;
    }

    public void setPid(int Pid) {
        this.Pid = Pid;
    }

    public int getPAt() {
        return PAt;
    }

    public void setPAt(int PAt) {
        this.PAt = PAt;
    }

    public int getCBT() {
        return CBT;
    }

    public void setCBT(int CBT) {
        this.CBT = CBT;
    }

    public int getPFT() {
        return PFT;
    }

    public void setPFT(int PFT) {
        this.PFT = PFT;
    }

    public int getPWT() {
        return PWT;
    }

    public void setPWT(int PWT) {
        this.PWT = PWT;
    }

    public int getPTT() {
        return PTT;
    }

    public void setPTT(int PTT) {
        this.PTT = PTT;
    }

    public int getPbegT() {
        return PbegT;
    }

    public void setPbegT(int PbegT) {
        this.PbegT = PbegT;
    }

    public int getPendT() {
        return PendT;
    }

    public void setPendT(int PendT) {
        this.PendT = PendT;
    }

}
