import java.util.Scanner;
import java.util.Arrays;
public class Banker {
    private static int customers=5;
    private static int resources=4;
    private static int[] available=new int[resources];
    private static int[][] max=new int[customers][resources];
    private static int[][] need=new int[customers][resources];
    private static int[][] allocation=new int[customers][resources];

    public static void main(String[] args) {
        Banker b=new Banker();
        b.fillMax(max);
        b.fillAvail(available);
        for(int i=0;i<customers;i++) {
            need[i] = Arrays.copyOf(max[i], resources);
        }
        Scanner in = new Scanner(System.in);
        System.out.println("Enter a command");
        String cmd = in.nextLine();
        while (!"q".equals(cmd)) {
            switch (cmd) {
                case "RQ":
                    b.request();
                    break;
                case "RL":
                    b.release();
                    break;
                case "*":
                    System.out.println("Available: ");
                    b.print(available);
                    System.out.println("Max: ");
                    b.print2D(max);
                    System.out.println("Allocation: ");
                    b.print2D(allocation);
                    System.out.println("Need: ");
                    b.print2D(need);
                    break;
                default:
                    System.out.println("available commands: request=RQ, release=RL, print all=*, quit=q");
                    break;
            }
            System.out.println("Enter a command");
            cmd = in.nextLine();
        }
    }

    private void fillMax(int[][] max){
        Scanner in=new Scanner(System.in);
        System.out.print("Fill max array: ");
        for(int i=0;i<customers;i++){
            for(int j=0; j<resources;j++){
                max[i][j]=in.nextInt();
            }
        }
    }
    private void fillAvail(int[] avail){
        Scanner in=new Scanner(System.in);
        System.out.print("Fill available array: ");
        for(int i=0;i<resources;i++){
            avail[i]=in.nextInt();
        }
    }
    private void request(){
        Scanner in=new Scanner(System.in);
        System.out.print("Customer #: ");
        int cust=in.nextInt();
        System.out.print("Requested Resources: ");
        int request[]=new int[resources];
        for(int i=0;i<resources;i++){
            request[i]=in.nextInt();
        }
        if(!checkNeed(cust, request)){
            System.out.println("Error, more requested than needed");
            return;
        }
        if(!checkAvail(request, available)){
            System.out.println("Error more requested than avaialabe");
            return;
        }
        if(!checkSafe(cust, request)){
            System.out.println("Error, system unsafe if request is processed");
            return;
        }
        updateAlloc(cust,request);
        updateNeed(cust, request);
        updateAvail(request);
        System.out.println("request processed succesfully");
    }
    private boolean checkNeed(int cust, int[] request){ ;
        for (int j = 0; j < resources; j++) {
            if (need[cust][j] < request[j]) {
                return false;
            }
        }
        return true;
    }
    private boolean checkAvail(int[] request, int[] available){
        for(int i=0;i<resources;i++){
            if(available[i]<request[i]){
                return false;
            }
        }
        return true;
    }
    public boolean checkSafe(int cust, int request[]) {
        boolean done[] = new boolean[customers];
        int[][] need1 = new int[customers][resources];
        for (int i = 0; i < customers; i++) {
            need1[i] = Arrays.copyOf(need[i], resources);
        }
            for (int i = 0; i < resources; i++) {
                need1[cust][i] = need1[cust][i] - request[i];
            }
            int[] avail1 = Arrays.copyOf(available, resources);
            for (int i = 0; i < resources; i++) {
                avail1[i] = avail1[i] - request[i];
            }
            int j = 0;
            while (j < customers) {
                boolean allocated = false;
                for (int i = 0; i < customers; i++) {
                    if (!done[i] && checkAvail(need1[i], avail1)) {
                        for (int k = 0; k < resources; k++) {
                            avail1[k] = avail1[k] - need1[i][k] + max[i][k];
                        }
                        allocated = done[i] = true;
                        j++;
                    }
                    if (!allocated) {
                        return false;
                    }
                }
            }
            return true;

    }
    private void updateAlloc(int i, int[] update){
        for(int j=0;j<resources;j++){
            allocation[i][j]=allocation[i][j]+update[j];
        }
    }
    private void updateNeed(int i, int[] update){
        for(int j=0;j<resources;j++){
            need[i][j]=need[i][j]-update[j];
        }
    }
    private void updateAvail(int[] update){
        for(int i=0; i<resources;i++){
            available[i]=available[i]-update[i];
        }
    }
    private void release(){
        System.out.print("Customer #: ");
        Scanner in=new Scanner(System.in);
        int cust=in.nextInt();
        System.out.print("Requested Resources: ");
        int release[]=new int[resources];
        for(int i=0;i<resources;i++){
            release[i]=(in.nextInt()*-1);
        }
        if(!checkRelease(release, allocation,cust)){
            System.out.println("customer does not have enough resources to release");
            return;
        }
        updateNeed(cust, release);
        updateAvail(release);
        updateAlloc(cust, release);
    }

    private void print2D(int[][] arr){
        for(int i=0;i<customers;i++){
            for(int j=0;j<resources;j++){
                System.out.print(arr[i][j] + " ");
            }
            System.out.print("\n");
        }
    }
    private void print(int[] arr){
        for(int i=0; i<resources;i++){
            System.out.print(arr[i]+" ");
        }
        System.out.print("\n");
    }
    private boolean checkRelease(int[] release, int[][] allocation, int cust){
        for(int i=0;i<resources;i++){
            if((release[i]*-1)>allocation[cust][i]){
                return false;
            }
        }
        return true;
    }
}
