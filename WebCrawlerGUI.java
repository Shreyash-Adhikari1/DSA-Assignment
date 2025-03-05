// Scenario: A Multithreaded Web Crawler
// Problem:
// You need to crawl a large number of web pages to gather data or index content. Crawling each page
// sequentially can be time-consuming and inefficient.
// Goal:
// Create a web crawler application that can crawl multiple web pages concurrently using multithreading to
// improve performance.
// Tasks:
// Design the application:
// Create a data structure to store the URLs to be crawled.
// Implement a mechanism to fetch web pages asynchronously.
// Design a data storage mechanism to save the crawled data.
// Create a thread pool:
// Use the ExecutorService class to create a thread pool for managing multiple threads.
// Submit tasks:
// For each URL to be crawled, create a task (e.g., a Runnable or Callable object) that fetches the web page
// and processes the content.
// Submit these tasks to the thread pool for execution.
// Handle responses:
// Process the fetched web pages, extracting relevant data or indexing the content.
// Handle errors or exceptions that may occur during the crawling process.
// Manage the crawling queue:
// Implement a mechanism to manage the queue of URLs to be crawled, such as a priority queue or a
// breadth-first search algorithm.
// By completing these tasks, you will create a multithreaded web crawler that can efficiently crawl large
// numbers of web page

//6b

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class WebCrawlerGUI extends JFrame {
    // GUI components
    private JTextField urlField;                    // Text field for seed URL input
    private JButton startButton;                    // Button to start crawling
    private JTextArea logArea;                      // Area to display crawl progress
    private JTextArea dataArea;                     // Area to display crawled data
    private JLabel statusLabel;                     // Label to show crawler status

    // Data structures for crawling
    private Queue<String> urlQueue = new LinkedBlockingQueue<>(); // Thread-safe queue for URLs
    private Set<String> visitedUrls = Collections.synchronizedSet(new HashSet<>()); // Thread-safe set for visited URLs
    private Map<String, String> crawledData = Collections.synchronizedMap(new HashMap<>()); // Thread-safe map for data
    private ExecutorService executorService;        // Thread pool for crawling tasks
    private AtomicInteger activeTasks = new AtomicInteger(0); // Counter for active tasks
    private final int MAX_URLS = 10;                // Maximum number of URLs to crawl
    private final int THREAD_POOL_SIZE = 4;         // Number of threads in the pool
    private volatile boolean isCrawling = false;    // Flag to track crawling state

    // Constructor to set up the GUI
    public WebCrawlerGUI() {
        setTitle("Web Crawler GUI");                // Set window title
        setSize(800, 600);                         // Set window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit application on window close
        setLayout(new BorderLayout());             // Use BorderLayout for layout

        // Control panel for URL input and start button
        JPanel controlPanel = new JPanel();        // Create panel for controls
        urlField = new JTextField("https://example.com", 20); // Text field with default URL
        startButton = new JButton("Start Crawling"); // Button to initiate crawling
        statusLabel = new JLabel("Status: Idle");  // Label to show current status
        controlPanel.add(new JLabel("Seed URL:")); // Add label for URL field
        controlPanel.add(urlField);                // Add URL input field
        controlPanel.add(startButton);             // Add start button
        controlPanel.add(statusLabel);             // Add status label
        add(controlPanel, BorderLayout.NORTH);     // Place control panel at top

        // Split pane for log and data display
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT); // Horizontal split pane
        logArea = new JTextArea(20, 30);           // Text area for logging crawl progress
        logArea.setEditable(false);                // Make log area read-only
        dataArea = new JTextArea(20, 30);          // Text area for displaying crawled data
        dataArea.setEditable(false);               // Make data area read-only
        splitPane.setLeftComponent(new JScrollPane(logArea)); // Add log area with scrollbar
        splitPane.setRightComponent(new JScrollPane(dataArea)); // Add data area with scrollbar
        add(splitPane, BorderLayout.CENTER);       // Place split pane in center

        // Action listener for start button
        startButton.addActionListener(e -> startCrawling()); // Call startCrawling when button clicked

        setVisible(true);                          // Make window visible
        executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE); // Initialize thread pool
    }

    // Method to start crawling based on user input
    private void startCrawling() {
        if (isCrawling) {                          // Check if already crawling
            JOptionPane.showMessageDialog(this, "Crawling is already in progress."); // Show warning
            return;                                // Exit if crawling
        }
        String seedUrl = urlField.getText().trim(); // Get seed URL from text field
        if (seedUrl.isEmpty()) {                   // Validate URL input
            JOptionPane.showMessageDialog(this, "Please enter a valid URL."); // Show error
            return;                                // Exit if invalid
        }
        urlQueue.clear();                          // Clear previous URLs
        visitedUrls.clear();                       // Clear visited URLs
        crawledData.clear();                       // Clear previous data
        logArea.setText("");                       // Clear log area
        dataArea.setText("");                      // Clear data area
        urlQueue.add(seedUrl);                     // Add seed URL to queue
        isCrawling = true;                         // Set crawling flag
        statusLabel.setText("Status: Crawling");   // Update status label
        startButton.setEnabled(false);             // Disable start button during crawl
        crawl();                                   // Start the crawling process
    }

    // Main crawling logic
    private void crawl() {
        while (!urlQueue.isEmpty() && visitedUrls.size() < MAX_URLS && !executorService.isShutdown()) {
            String url = urlQueue.poll();          // Get next URL from queue
            if (url != null && !visitedUrls.contains(url)) { // Check if URL hasn’t been visited
                visitedUrls.add(url);              // Mark URL as visited
                activeTasks.incrementAndGet();     // Increment active task counter
                executorService.submit(new CrawlTask(url)); // Submit crawl task to thread pool
            }
        }
        // Monitor tasks and shutdown when done
        new Thread(() -> {
            while (activeTasks.get() > 0) {        // Wait for all tasks to complete
                try {
                    Thread.sleep(1000);            // Sleep to avoid busy-waiting
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore interrupt status
                }
            }
            shutdown();                            // Shut down executor when tasks are done
        }).start();                                // Start shutdown thread
    }

    // Method to shut down the thread pool
    private void shutdown() {
        executorService.shutdown();                // Prevent new tasks from being submitted
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) { // Wait 60s for tasks to finish
                executorService.shutdownNow();     // Force shutdown if timeout occurs
                logArea.append("Forced shutdown due to timeout.\n"); // Log timeout
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();         // Force shutdown on interruption
            Thread.currentThread().interrupt();    // Restore interrupt status
        }
        isCrawling = false;                        // Reset crawling flag
        statusLabel.setText("Status: Idle");       // Update status to idle
        startButton.setEnabled(true);              // Re-enable start button
        logArea.append("Crawling completed. Total URLs crawled: " + visitedUrls.size() + "\n"); // Log completion
    }

    // Inner class for crawl task
    class CrawlTask implements Runnable {
        private String url;                        // URL to crawl

        CrawlTask(String url) {
            this.url = url;                        // Initialize with URL
        }

        @Override
        public void run() {
            try {
                String content = fetchPage(url);   // Fetch web page content
                processContent(url, content);      // Process and store content
                extractUrls(content);              // Extract new URLs from content
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> 
                    logArea.append("Error crawling " + url + ": " + e.getMessage() + "\n")); // Log error in GUI
            } finally {
                activeTasks.decrementAndGet();     // Decrement active task counter
            }
        }

        // Method to fetch web page content
        private String fetchPage(String urlString) throws Exception {
            URL url = new URL(urlString);          // Create URL object
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(url.openStream())); // Open stream to read page
            StringBuilder content = new StringBuilder(); // Builder for page content
            String line;
            while ((line = reader.readLine()) != null) { // Read each line
                content.append(line).append("\n"); // Append line to content
            }
            reader.close();                        // Close the reader
            return content.toString();             // Return content as string
        }

        // Method to process and store content
        private void processContent(String url, String content) {
            String snippet = content.substring(0, Math.min(content.length(), 100)); // Get first 100 chars
            crawledData.put(url, snippet);         // Store in crawled data map
            SwingUtilities.invokeLater(() -> {     // Update GUI on Event Dispatch Thread
                logArea.append("Crawled: " + url + " (Length: " + content.length() + ")\n"); // Log crawl
                dataArea.append("URL: " + url + "\nContent: " + snippet + "\n\n"); // Display data
            });
        }

        // Method to extract new URLs (simplified)
        private void extractUrls(String content) {
            String[] words = content.split("\\s+"); // Split content into words
            for (String word : words) {            // Check each word
                if (word.startsWith("http://") || word.startsWith("https://")) { // Identify URLs
                    if (visitedUrls.size() < MAX_URLS && !visitedUrls.contains(word)) { // Check limits
                        urlQueue.add(word);        // Add new URL to queue
                    }
                }
            }
        }
    }

    // Main method to launch the GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WebCrawlerGUI()); // Run GUI on EDT
    }
}