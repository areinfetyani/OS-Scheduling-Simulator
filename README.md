# OS-Scheduling-Simulator

A Java-based simulator for evaluating and comparing the performance of four popular CPU scheduling algorithms: **FCFS (First-Come First-Served)**, **SRTF (Shortest Remaining Time First)**, **Round Robin**, and **MLFQ (Multi-Level Feedback Queue)**. Developed for the Operating Systems course project.


## 📚 Project Description

This project simulates how different CPU scheduling algorithms affect process execution in terms of:

- **Average Waiting Time (AWT)**
- **Average Turnaround Time (ATT)**

Each algorithm is tested across varying simulation scales: **100, 1000, 10000, and 100000 process sets** — all randomly generated. The results are printed in table format for easy comparison.


## ⚙️ Features

- Fully implemented simulations of:
  - **FCFS** — Non-preemptive, first-come-first-served scheduling
  - **SRTF** — Preemptive shortest-job-first (based on remaining time)
  - **Round Robin** — With a fixed quantum of `20`
  - **MLFQ** — With 3 levels:
    - Quantum 10 (RR1)
    - Quantum 50 (RR2)
    - FCFS fallback

- Dynamically generates process arrival and burst times
- Outputs well-formatted tables of average metrics per algorithm and per iteration count


## 🧪 Sample Output 

![Screenshot 2025-06-13 165410](https://github.com/user-attachments/assets/301807fc-e017-49d7-b2eb-809393586005)


## 🧑‍💻 How It Works

- `createProcesses()` generates processes with:
  - **Random arrival times**, spread in 10-unit intervals
  - **Random burst times**, in the range of 5–100 units
- Each scheduling method simulates process execution and collects:
  - Completion time
  - Turnaround time
  - Waiting time
- Average results for each method are calculated using:
  - `calcAvg()`
  - `fillArrays()`


## 📁 Structure

- `Main.java` — Contains all scheduling methods and the main simulation loop
- Algorithms:
  - `FCFSSimulation()`
  - `SRTFSimulation()`
  - `RRSimulation(int quantum)`
  - `MLFQSimulation(int q1, int q2)`
- Data Arrays:
  - `at[]`, `bt[]`, `ct[]`, `tt[]`, `wt[]`, `rt[]` — Arrival, Burst, Completion, Turnaround, Waiting, Remaining


## 🛠️ Requirements

- Java 8+
- Run via any IDE or command line:

```bash
javac Main.java
java Main
````


## 📘 Course Information

* **Course**: Operating Systems
* **Project Topic**: CPU Scheduling Simulation and Evaluation
* **University**: \[Birzeit University]
* **Instructor**: \[Dr. Ali Jaber]

---

## 📝 License

This project is for educational purposes only. Feel free to fork and extend for academic use.

