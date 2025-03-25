/**
 * HelperFunctions.java
 * Description: Additional Helper methods used in the program
 *
 * @author  Marlon Huynh
 * @version 1.0, 3/29/2025
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class HelperFunctions {
    // Helps debugs memory
    public static void printMemoryStats() {
        // Print Runtime memory stats
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;

        System.out.println("Memory Stats:");
        System.out.println("Total Memory: " + totalMemory / (1024 * 1024) + " MB");
        System.out.println("Free Memory: " + freeMemory / (1024 * 1024) + " MB");
        System.out.println("Used Memory: " + usedMemory / (1024 * 1024) + " MB");

        // Optional: Use MemoryMXBean for more detailed memory stats
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        System.out.println("Heap Memory - Used: " + heapMemoryUsage.getUsed() / (1024 * 1024) + " MB");
        System.out.println("Heap Memory - Max: " + heapMemoryUsage.getMax() / (1024 * 1024) + " MB");
    }

    // Set button default UI
    public static void setButtonColors(JButton button) {
        Font buttonFont = new Font("Arial", Font.BOLD, 20);
        button.setFont(buttonFont);
        button.setBackground(Color.BLACK);  // Default background color
        button.setForeground(Color.WHITE);  // Text color
        button.setFocusPainted(false);  // Remove focus border
        button.setFocusable(false);  // Disable focus behavior
        button.setContentAreaFilled(false);  // Enable default button press effects
        button.setRolloverEnabled(true);  // Enable rollover effect
        button.setOpaque(true);
        button.setBorder(new EmptyBorder(10, 25, 10, 25));
        // Override up a MouseListener to handle color changes
        button.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { button.setBackground(Color.decode("#45001f")); } // Hover color
            @Override public void mouseExited(MouseEvent e) { button.setBackground(Color.BLACK); } // Color when exiting
            @Override public void mousePressed(MouseEvent e) { button.setBackground(Color.decode("#45001f").brighter()); } // Color when pressed
            @Override public void mouseReleased(MouseEvent e) { button.setBackground(Color.decode("#45001f")); } // Color after release
            @Override public void mouseClicked(MouseEvent e) { button.setBackground(Color.decode("#45001f").darker()); }
        });
    }
}
