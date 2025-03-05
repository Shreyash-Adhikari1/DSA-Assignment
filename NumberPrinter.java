// NumberPrinter class with methods to print 0, even, and odd numbers
class NumberPrinter {
    public void printZero() {
        System.out.print("0");
    }

    public void printEven(int num) {
        System.out.print(num);
    }

    public void printOdd(int num) {
        System.out.print(num);
    }
}

// ThreadController class to coordinate the three threads
class ThreadController {
    private final NumberPrinter printer = new NumberPrinter(); // Instance of NumberPrinter
    private int n; // maximum number to print up to
    private int current = 1; // current number to print
    private int turn = 0; // indicates which thread should print next (0: ZeroThread, 1: OddThread, 2:
                          // EvenThread)

    public ThreadController(int n) {
        this.n = n;
    }

    // method for ZeroThread
    public void zeroThread() throws InterruptedException {
        synchronized (this) {
            while (current <= n) {
                // wait until it's ZeroThread's turn
                while (turn != 0) {
                    wait();
                }
                // Print 0 if the sequence is not complete
                if (current <= n) {
                    printer.printZero();
                }
                // determinig the next thread's turn (OddThread or EvenThread)
                turn = (current % 2 == 0) ? 2 : 1;
                notifyAll(); // notifying all waiting threads
            }
        }
    }

    // method for EvenThread
    public void evenThread() throws InterruptedException {
        synchronized (this) {
            while (current <= n) {
                // Wait until it's EvenThread's turn
                while (turn != 2) {
                    wait();
                }
                // printing the even number if the sequence is not complete
                if (current <= n) {
                    printer.printEven(current);
                    current++; // moving to the next number
                }
                turn = 0; // setting the next turn to ZeroThread
                notifyAll(); // notifying all waiting threads
            }
        }
    }

    // Method for OddThread
    public void oddThread() throws InterruptedException {
        synchronized (this) {
            while (current <= n) {
                // wait until it's OddThread's turn
                while (turn != 1) {
                    wait();
                }
                // printing the odd number if the sequence is not complete
                if (current <= n) {
                    printer.printOdd(current);
                    current++; // Move to the next number
                }
                turn = 0; // setting the next turn to ZeroThread
                notifyAll(); // notifying all waiting threads
            }
        }
    }
}

// main class to test the solution
class Main {
    public static void main(String[] args) {
        int n = 5; // maximum number to print up to
        ThreadController controller = new ThreadController(n);

        // creating ZeroThread
        Thread zeroThread = new Thread(() -> {
            try {
                controller.zeroThread();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // creating OddThread
        Thread oddThread = new Thread(() -> {
            try {
                controller.oddThread();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // creating EvenThread
        Thread evenThread = new Thread(() -> {
            try {
                controller.evenThread();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // starting all threads
        zeroThread.start();
        oddThread.start();
        evenThread.start();

        // waiting for all threads to complete
        try {
            zeroThread.join();
            oddThread.join();
            evenThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}