import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Main {
	// 2D arrays that store the ATT in the first row and AWT in the second row for
	// all scheduling algorithms repeated 100, 1000, 10000 and 100000 times
	public static double[][] it100 = new double[2][4];
	public static double[][] it1000 = new double[2][4];
	public static double[][] it10000 = new double[2][4];
	public static double[][] it100000 = new double[2][4];
	public static final int RR_QUANTUM = 20; // quantum time for round robin
	public static final int MLFQ_QUANTUM1 = 10; // quantum time for round robin 1 in MLFQ
	public static final int MLFQ_QUANTUM2 = 50; // quantum time for round robin 2 in MLFQ
	public static final int NUM_OF_PROCESSES = 8; // number of processes in ready queue
	static int[] at = new int[NUM_OF_PROCESSES]; // arrival time
	static int[] bt = new int[NUM_OF_PROCESSES]; // burst time
	static int[] wt = new int[NUM_OF_PROCESSES]; // waiting time
	static int[] tt = new int[NUM_OF_PROCESSES]; // turn around time
	static int[] ct = new int[NUM_OF_PROCESSES]; // completion time
	static int[] rt = new int[NUM_OF_PROCESSES]; // remaining time
	static int[] iterations = { 100, 1000, 10000, 100000 };

	public static void main(String[] args) {
		for (int i = 0; i < iterations.length; i++) {
			fillArrays(iterations[i]);
		}

		System.out.println("|------------------------#FCFS#--------------------------");
		System.out.println("|       |    100    |    1000    |   10000  |   100000   |");
		System.out.println("|-------|-----------|------------|----------|------------|");

		// print fcfs table
		System.out.printf("| %-5s | %9.4f | %10.4f | %8.4f | %10.4f |\n", "ATT", it100[0][0], it1000[0][0], it10000[0][0], it100000[0][0]);
		System.out.printf("| %-5s | %9.4f | %10.4f | %8.4f | %10.4f |\n", "AWT", it100[1][0], it1000[1][0], it10000[1][0], it100000[1][0]);

		
		System.out.println("|------------------------#SRTF#--------------------------|");

		// print srtf table
		System.out.printf("| %-5s | %9.4f | %10.4f | %8.4f | %10.4f |\n", "ATT", it100[0][1], it1000[0][1], it10000[0][1], it100000[0][1]);
		System.out.printf("| %-5s | %9.4f | %10.4f | %8.4f | %10.4f |\n", "AWT", it100[1][1], it1000[1][1], it10000[1][1], it100000[1][1]);

		System.out.println("|--------------------------#RR#--------------------------|");

		// print rr table
		System.out.printf("| %-5s | %9.4f | %10.4f | %8.4f | %10.4f |\n", "ATT", it100[0][2], it1000[0][2], it10000[0][2], it100000[0][2]);
		System.out.printf("| %-5s | %9.4f | %10.4f | %8.4f | %10.4f |\n", "AWT", it100[1][2], it1000[1][2], it10000[1][2], it100000[1][2]);

		System.out.println("|-------------------------#MLFQ#-------------------------|");

		// print mlfq table
		System.out.printf("| %-5s | %9.4f | %10.4f | %8.4f | %10.4f |\n", "ATT", it100[0][3], it1000[0][3], it10000[0][3], it100000[0][3]);
		System.out.printf("| %-5s | %9.4f | %10.4f | %8.4f | %10.4f |\n", "AWT", it100[1][3], it1000[1][3], it10000[1][3], it100000[1][3]);

		System.out.println("----------------------------------------------------------");

	}

	// method to generate 8 processes
	public static void createProcesses() {
		Random atr = new Random();
		// generate arrival time randomly, the range of the arrival time is between ten
		// units starting from [0,9], for each new process the range increases by ten
		// for both upper and lower bound
		for (int i = 0; i < NUM_OF_PROCESSES; i++) {
			int lowerBound = i * 10;
			int upperBound = (i * 10) + 9;
			int r = atr.nextInt(upperBound - lowerBound + 1) + lowerBound; // generate a random number between the
			                                                               // specified range
			at[i] = r; // assign the random number for arrival time
		}
		Random btr = new Random();
		for (int i = 0; i < NUM_OF_PROCESSES; i++) {
			int r = btr.nextInt(96) + 5; // generate a random number between [5-100]
			bt[i] = r; // assign it to the burst time
		}
	}

	// FCFS simulation implementation
	public static void FCFSSimulation() {
		// this simulator represent the time while executing the processes and is used
		// to calculate completion time of all processes
		int simulator = 0;
		// Go through all processes in order
		for (int i = 0; i < NUM_OF_PROCESSES; i++) {
			if (at[i] > simulator) {
				// if the process hasn't arrived yet, the cpu is idle until some process arrives
				simulator = at[i];
			}
			// when the process arrives its time to compute completion time
			ct[i] = simulator + bt[i];// add the burst time of the process to the simulator to find it's completion
			                          // time
			simulator = ct[i];// move the simulator to the end time of the process
		}
		// this for loop calculate turn around time and waiting time for all processes
		for (int i = 0; i < NUM_OF_PROCESSES; i++) {
			tt[i] = ct[i] - at[i];// turn around time equals (completion time - arrival time)
			wt[i] = tt[i] - bt[i];// waiting time equals (turn around time - burst time)
		}
	}

	// SRTF (SJF preemptive) simulation implementation
	public static void SRTFSimulation() {
		// this simulator represent the time while executing the processes and is used
		// to calculate completion time of all processes
		int simulator = 0;
		int n = NUM_OF_PROCESSES; // n represents the number of undone processes
		System.arraycopy(bt, 0, rt, 0, bt.length); // copying the burst time array to the remaining time array
		// while loop, as long as the number of undone processes is greater than zero,
		// continue the simulation
		while (n > 0) {
			int executing = -1; // initially there is no executing process

			// go through all existing processes that are undone and has arrived, find the
			// one with
			// the shortest remaining time
			for (int i = 0; i < NUM_OF_PROCESSES; i++) {
				if (rt[i] > 0 && at[i] <= simulator) {
					if (executing == -1 || rt[i] < rt[executing]) {
						executing = i; // assign the value of the process with shortest time to the variable executing
						               // to start executing it
					}
				}
			}
			if (executing == -1) { // if no process was assigned to execute (no process has arrived or no process
			                       // has remaining time) then the cpu remains idle waiting a process to execute
				simulator++;
			} else { // if there is an executing process
				simulator++; // move the simulator by only one time unit
				rt[executing]--; // decrease the process's remaining time by one time unit

				// if the process is done (it's remaining time is zero)
				if (rt[executing] == 0) {
					n--; // decrease the number of undone processes
					ct[executing] = simulator;// assign the completion time to the process to where the simulator has
					                          // reached
				}
			}
		}

		// this for loop calculate turn around time and waiting time for all processes
		for (int i = 0; i < NUM_OF_PROCESSES; i++) {
			tt[i] = ct[i] - at[i];// turn around time equals (completion time - arrival time)
			wt[i] = tt[i] - bt[i];// waiting time equals (turn around time - burst time)
		}
	}

	// Round Robin simulation implementation
	public static void RRSimulation(int quantum) {
		// this simulator represent the time while executing the processes and is used
		// to calculate completion time of all processes
		int simulator = 0;
		int n = NUM_OF_PROCESSES; // n represents the number of undone processes
		System.arraycopy(bt, 0, rt, 0, bt.length); // copying the burst time array to the remaining time array
		Queue<Integer> processes = new LinkedList<>(); // a queue that stores processes in the order they will be
		                                               // executed
		int i = 0;// represent the process index
		while (!processes.isEmpty() || n > 0) { // as long as there is still processes in the queue or the number of
		                                        // undone processes exceeds zero, the loop goes on
			while (i < NUM_OF_PROCESSES && at[i] <= simulator) { // add all processes that has arrived to the queue
				processes.add(i);
				i++; // increase the process index to check the next process
			}
			if (processes.isEmpty()) { // if the queue is empty (which means no new processes has arrived) the cpu
			                           // remains idle and the simulator continues to count time
				simulator = at[i];
			} else { // if there are newly arrived processes
				int executing = processes.poll(); // take the first process
				// check the remaining time of the process if it is smaller than the quantum
				// execute it and end the process, if it is equal or larger than quantum execute
				// the quantum time of the process
				int q = 0;
				if (rt[executing] < quantum) {
					q = rt[executing];
				} else {
					q = quantum;
				}
				simulator += q;
				rt[executing] -= q;
				// after executing the process, there might be newly arrived processes, add them
				// to the queue
				while (i < NUM_OF_PROCESSES && at[i] <= simulator) {
					processes.add(i);
					i++;
				}
				if (rt[executing] > 0) { // if the executing process is not done yet
					processes.add(executing);// re add the process to the end of the queue
				} else {// if the process is done executing
					ct[executing] = simulator;// assign the completion time for the process
					n--; // decrease the number of undone processes
				}

			}
		}

		// this for loop calculate turn around time and waiting time for all processes
		for (int j = 0; j < NUM_OF_PROCESSES; j++) {
			tt[j] = ct[j] - at[j];// turn around time equals (completion time - arrival time)
			wt[j] = tt[j] - bt[j];// waiting time equals (turn around time - burst time)
		}
	}

	// MLFQ simulation implementation
	static void MLFQSimulation(int q1, int q2) {
		// this simulator represent the time while executing the processes and is used
		// to calculate completion time of all processes
		int simulator = 0;
		int n = NUM_OF_PROCESSES; // n represents the number of existing processes
		System.arraycopy(bt, 0, rt, 0, bt.length); // copying the burst time array to the remaining time array
		Queue<Integer> processes = new LinkedList<>(); // queue for executing round robin with quantum 10
		Queue<Integer> processes2 = new LinkedList<>();// queue for executing round robin with quantum 50
		Queue<Integer> processes3 = new LinkedList<>();// queue for executing fcfs
		int i = 0; // represent the process index
		int done = 0; // number of done processes
		while (done < n) { // as long as there is undone processes keep the loop going
			while (i < NUM_OF_PROCESSES && at[i] <= simulator) { // add newly arrived processes to the first queue
				processes.add(i);
				i++;
			}

			if (processes.isEmpty()) { // check if the first queue is empty
				if (processes2.isEmpty()) {// check if the second queue is empty
					if (processes3.isEmpty()) {// check if the third queue is empty
						simulator++; // if all queues are empty it means there are no arrived processes to execute so
						             // the cpu remains idle
					}
					// if both round robin 1 and 2 are done, execute all remaining processes in fcfs
					// this means that first and second queue are empty and that all remaining
					// processes are in the third queue
					for (int m = 0; m < processes3.size(); m++) {
						int executing = processes3.poll(); // get the process from the queue
						simulator += rt[executing]; // execute its remaining time
						ct[executing] = simulator;// assign the calculated completion time
						done++; // increase the number of executed processes
					}
				}

				// if the second queue is not empty execute the following loop until it is
				while (!processes2.isEmpty()) {
					int executing = processes2.poll(); // get the process from the queue
					int q = 0;
					// check the remaining time of the process if it is smaller than the quantum
					// execute it and end the process, if it is equal or larger than quantum execute
					// the quantum time of the process
					if (rt[executing] < q2) {
						q = rt[executing];
					} else {
						q = q2;
					}
					simulator += q;
					rt[executing] -= q;
					// after executing the process, there might be newly arrived processes, add them
					// to the first queue
					while (i < NUM_OF_PROCESSES && at[i] <= simulator) {
						processes.add(i);
						i++;
					}

					if (rt[executing] > 0) { // if the executing process from second queue is not done, add it to the
					                         // third queue
						processes3.add(executing);
					} else {// if it is done, assign it's completion time and increase the number of
					        // executed processes
						ct[executing] = simulator;
						done++;
					}
					// break if a new process arrived to the first queue
					if (!processes.isEmpty()) {
						break;
					}
				}
			} else { // if there are processes in the first queue
				int executing = processes.poll();
				int q = 0;
				// check the remaining time of the process if it is smaller than the quantum
				// execute it and end the process, if it is equal or larger than quantum execute
				// the quantum time of the process
				if (rt[executing] < q1) {
					q = rt[executing];
				} else {
					q = q1;
				}
				simulator += q;
				rt[executing] -= q;
				// after executing the process, there might be newly arrived processes, add them
				// to the first queue
				while (i < NUM_OF_PROCESSES && at[i] <= simulator) {
					processes.add(i);
					i++;
				}

				if (rt[executing] > 0) { // if the processes is not finished yet add at to the second queue
					processes2.add(executing);
				} else { // if process done
					ct[executing] = simulator; // assign the completion time of the process
					done++; // increase the number of executed processes
				}
			}
			// main loop is done, all processes are executed
		}
		// this for loop calculate turn around time and waiting time for all processes
		for (int j = 0; j < NUM_OF_PROCESSES; j++) {
			tt[j] = ct[j] - at[j];// turn around time equals (completion time - arrival time)
			wt[j] = tt[j] - bt[j];// waiting time equals (turn around time - burst time)
		}
	}

	// method to calculate the average
	public static double calcAvg(int[] array) {
		int total = 0;
		// find the summation of all array elements
		for (int i = 0; i < array.length; i++) {
			total += array[i];
		}
		return total * 1.0 / array.length; // find the average by dividing the summation on the number of array elements
	}

	// method to fill the ATT and AWT arrays
	public static void fillArrays(int iter) {
		// total variables for both ATT and AWT for all scheduling algorithms
		double totalAwtFCFS = 0;
		double totalAttFCFS = 0;
		double totalAwtSRTF = 0;
		double totalAttSRTF = 0;
		double totalAwtRR = 0;
		double totalAttRR = 0;
		double totalAwtMLFQ = 0;
		double totalAttMLFQ = 0;
		// for loop that repeats the creation of processes and simulation for all
		// scheduling algorithms for the specified iterations count
		for (int i = 0; i < iter; i++) {

			createProcesses();

			// average variables for both ATT and AWT for all scheduling algorithms
			double awtFCFS = 0;
			double attFCFS = 0;
			double awtSRTF = 0;
			double attSRTF = 0;
			double awtRR = 0;
			double attRR = 0;
			double awtMLFQ = 0;
			double attMLFQ = 0;

			// start the fcfs simulation
			FCFSSimulation();

			// calculate AWT and ATT for the processes executed on fcfs
			awtFCFS = calcAvg(wt);
			attFCFS = calcAvg(tt);

			// add the average to the average total variable
			totalAwtFCFS += awtFCFS;
			totalAttFCFS += attFCFS;

			// start the srtf simulation
			SRTFSimulation();

			// calculate AWT and ATT for the processes executed on srtf
			awtSRTF = calcAvg(wt);
			attSRTF = calcAvg(tt);

			// add the average to the average total variable
			totalAwtSRTF += awtSRTF;
			totalAttSRTF += attSRTF;

			// start the rr simulation
			RRSimulation(RR_QUANTUM);

			// calculate AWT and ATT for the processes executed on rr
			awtRR = calcAvg(wt);
			attRR = calcAvg(tt);

			// add the average to the average total variable
			totalAwtRR += awtRR;
			totalAttRR += attRR;

			// start the mlfq simulation
			MLFQSimulation(MLFQ_QUANTUM1, MLFQ_QUANTUM2);

			// calculate AWT and ATT for the processes executed on mlfq
			awtMLFQ = calcAvg(wt);
			attMLFQ = calcAvg(tt);

			// add the average to the average total variable
			totalAwtMLFQ += awtMLFQ;
			totalAttMLFQ += attMLFQ;

		}
		// fill the arrays according to the iterations count by dividing the total of
		// averages on the number of iterations
		if (iter == 100) {
			it100[0][0] = totalAttFCFS * 1.0 / iter;
			it100[0][1] = totalAttSRTF * 1.0 / iter;
			it100[0][2] = totalAttRR * 1.0 / iter;
			it100[0][3] = totalAttMLFQ * 1.0 / iter;
			it100[1][0] = totalAwtFCFS * 1.0 / iter;
			it100[1][1] = totalAwtSRTF * 1.0 / iter;
			it100[1][2] = totalAwtRR * 1.0 / iter;
			it100[1][3] = totalAwtMLFQ * 1.0 / iter;
		} else if (iter == 1000) {
			it1000[0][0] = totalAttFCFS * 1.0 / iter;
			it1000[0][1] = totalAttSRTF * 1.0 / iter;
			it1000[0][2] = totalAttRR * 1.0 / iter;
			it1000[0][3] = totalAttMLFQ * 1.0 / iter;
			it1000[1][0] = totalAwtFCFS * 1.0 / iter;
			it1000[1][1] = totalAwtSRTF * 1.0 / iter;
			it1000[1][2] = totalAwtRR * 1.0 / iter;
			it1000[1][3] = totalAwtMLFQ * 1.0 / iter;
		} else if (iter == 10000) {
			it10000[0][0] = totalAttFCFS * 1.0 / iter;
			it10000[0][1] = totalAttSRTF * 1.0 / iter;
			it10000[0][2] = totalAttRR * 1.0 / iter;
			it10000[0][3] = totalAttMLFQ * 1.0 / iter;
			it10000[1][0] = totalAwtFCFS * 1.0 / iter;
			it10000[1][1] = totalAwtSRTF * 1.0 / iter;
			it10000[1][2] = totalAwtRR * 1.0 / iter;
			it10000[1][3] = totalAwtMLFQ * 1.0 / iter;
		} else {
			it100000[0][0] = totalAttFCFS * 1.0 / iter;
			it100000[0][1] = totalAttSRTF * 1.0 / iter;
			it100000[0][2] = totalAttRR * 1.0 / iter;
			it100000[0][3] = totalAttMLFQ * 1.0 / iter;
			it100000[1][0] = totalAwtFCFS * 1.0 / iter;
			it100000[1][1] = totalAwtSRTF * 1.0 / iter;
			it100000[1][2] = totalAwtRR * 1.0 / iter;
			it100000[1][3] = totalAwtMLFQ * 1.0 / iter;
		}
	}

}