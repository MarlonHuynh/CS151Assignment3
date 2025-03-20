import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class PhotoAlbumView extends JFrame{
    private PhotoAlbumModel model;

    private final int buttonPanelHeight = 100;      // Height of the button panel
    private int imageHeight;                        // Height of the image (determined at runtime)
    private final Timer resizeTimer;
    private int newState, oldState;
    private int screenID = 1;                       // 1 == Main Screen, 2 = Enter Image Path Screen
    // JPanels
    private JPanel mainImagePanel, mainImageTextPanel, btnPanel, leftPanel, innerLeftPanel;
    // JComponents
    private JButton addBtn, delBtn, nextBtn, prevBtn, sortNameBtn, sortDateBtn, sortSizeBtn, exitBtn, changeNameBtn, changeDateBtn;
    private JLabel mainImageLabel;  // Image
    private JLabel topLabel, bottomLabel, leftTextLabel;
    private JLabel enterPathLabel, statusLabel;
    // Other variables
    private Image mainImage, resizedImage;
    private ImageIcon scaledLeftIcons;

    public PhotoAlbumView(PhotoAlbumModel m){
        model = m;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.gc(); // Force garbage collection
            printMemoryStats(); // Print memory stats for debugging
        }));
        initializeView();
        // Timer to delay resize action
        resizeTimer = new Timer(200, new ActionListener() { @Override public void actionPerformed(ActionEvent e) { redrawScreen(screenID); } });
        resizeTimer.setRepeats(false);  // Only run the action once after the delay
    }

    public void initializeView() {
        // --------------------------------------------------
        //              ONE-TIME INITIALIZATION
        // --------------------------------------------------
        // App
        setLayout(new BorderLayout());
        setTitle("Marlon's Photo Album");
        ImageIcon appIcon = new ImageIcon("src/IconImage.PNG");
        setIconImage(appIcon.getImage());
        // Display the main frame
        setSize(1500, 1000);    // Default Size
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Define panels ---
        mainImagePanel = new JPanel(new BorderLayout());
        btnPanel = new JPanel();
        leftPanel = new JPanel();
        mainImageTextPanel = new JPanel();
        mainImageTextPanel.setLayout(new BoxLayout(mainImageTextPanel, BoxLayout.Y_AXIS));
        // Create Buttons
        addBtn = new JButton("Add");
        prevBtn = new JButton("Prev");
        nextBtn = new JButton("Next");
        delBtn = new JButton("Delete");
        exitBtn = new JButton("Exit");
        changeNameBtn = new JButton("Change Name");
        changeDateBtn = new JButton("Change Date");
        sortNameBtn = new JButton("Sort by Name");
        sortDateBtn = new JButton("Sort by Date");
        sortSizeBtn = new JButton("Sort by Size");
        // Set Colors
        setButtonColors(addBtn);
        setButtonColors(nextBtn);
        setButtonColors(prevBtn);
        setButtonColors(delBtn);
        setButtonColors(exitBtn);
        setButtonColors(sortNameBtn);
        setButtonColors(sortDateBtn);
        setButtonColors(sortSizeBtn);
        setButtonColors(changeNameBtn);
        setButtonColors(changeDateBtn);
        // Add buttons to the panel
        btnPanel.add(addBtn);
        btnPanel.add(changeNameBtn);
        btnPanel.add(changeDateBtn);
        btnPanel.add(delBtn);
        btnPanel.add(exitBtn);
        btnPanel.add(prevBtn);
        btnPanel.add(nextBtn);
        btnPanel.add(sortNameBtn);
        btnPanel.add(sortDateBtn);
        btnPanel.add(sortSizeBtn);
        // Set current image and label information
        topLabel = new JLabel("Upload something! [Current Photo's Title will be Displayed Here.]", JLabel.CENTER);
        bottomLabel = new JLabel("[Current Photo's Date will be Displayed Here.]", JLabel.CENTER);
        topLabel.setFont(new Font("Arial", Font.BOLD, 20));
        bottomLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Set position for the main image panel
        mainImageTextPanel.add(topLabel);
        mainImageTextPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainImageTextPanel.add(bottomLabel);
        mainImagePanel.add(mainImageTextPanel, BorderLayout.NORTH);
        // Create a new panel to center enterPathLabel
        add(mainImagePanel, BorderLayout.CENTER);
        // --------------------------------------------------
        //        DYNAMIC SIZING BASED ON WINDOW SIZE
        // --------------------------------------------------
        // Set main Image (right side)'s size dynamically
        mainImage = (new ImageIcon("src/IconImage.PNG")).getImage();
        imageHeight = getHeight() - buttonPanelHeight - 100;
        resizedImage = mainImage.getScaledInstance(imageHeight, imageHeight, Image.SCALE_SMOOTH); // Resizing image to square
        mainImageLabel = new JLabel(new ImageIcon(resizedImage));
        mainImagePanel.add(mainImageLabel, BorderLayout.CENTER);
        // Set left side panel for other photos in the album dynamically
        leftPanel.setLayout(new GridLayout(3, 3));
        leftPanel.setPreferredSize(new Dimension(getWidth() - imageHeight, getHeight() - buttonPanelHeight));
        for (int i = 0; i < 9; i++) {
            // Create the image icon and resize it
            scaledLeftIcons = new ImageIcon(mainImage.getScaledInstance((getWidth() - imageHeight) / 4, (getWidth() - imageHeight) / 4, Image.SCALE_SMOOTH)); // Adjust size as needed
            JLabel leftImagesLabel = new JLabel(scaledLeftIcons);
            // Create the text label
            leftTextLabel = new JLabel("No Image Title - No Image Date", JLabel.CENTER);
            leftTextLabel.setFont(new Font("Arial", Font.BOLD, 20));
            // Create a panel to hold the image and text
            innerLeftPanel = new JPanel(new BorderLayout());
            // Add the image and text to the panel
            innerLeftPanel.add(leftImagesLabel, BorderLayout.CENTER);       // Place image in the center
            innerLeftPanel.add(leftTextLabel, BorderLayout.NORTH);          // Place text above the image
            // Add the panel to the leftPanel
            leftPanel.add(innerLeftPanel);
        }
        add(leftPanel, BorderLayout.WEST);
        // Set buttons panel (bottom) dynamically
        btnPanel.setLayout(new GridLayout(2, 5));
        btnPanel.setPreferredSize(new Dimension(getWidth(), buttonPanelHeight));
        add(btnPanel, BorderLayout.SOUTH);
        // --------------------------------------------------
        //        Accounts for resizing window
        // --------------------------------------------------
        // METHOD 1: User drags the window to resize
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (!resizeTimer.isRunning()) { resizeTimer.restart(); }
            }
        });
        // METHOD 2: User clicks the min/max button
        addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                newState = e.getNewState();
                oldState = e.getOldState();
                // Check if the window was maximized
                if ((newState & Frame.MAXIMIZED_BOTH) != 0 && (oldState & Frame.MAXIMIZED_BOTH) == 0) { // Window was maximized, trigger resize
                    redrawScreen(screenID);
                }
                // Check if the window was restored
                if ((newState & Frame.MAXIMIZED_BOTH) == 0 && (oldState & Frame.MAXIMIZED_BOTH) != 0) { // Window was restored, trigger resize
                    redrawScreen(screenID);
                }
            }
        });
    }

    private void redrawScreen(int screenID) {
        System.out.println("Redrawing screen with ID: " + screenID);
        switch (screenID){
            case 1:
                redrawMainScreen();
                break;
            case 2:
                redrawEnterImagePathScreen();
                break;
            default:
                System.out.println("Error: Invalid screenID.");
                break;
        }
    }

    private void redrawMainScreen(){
        // Update the image size for the main image
        imageHeight = getHeight() - buttonPanelHeight - 100;
        resizedImage.flush();
        resizedImage = mainImage.getScaledInstance(imageHeight, imageHeight, Image.SCALE_SMOOTH);
        Icon currentIcon = mainImageLabel.getIcon();
        if (currentIcon != null) { ((ImageIcon) currentIcon).getImage().flush(); }
        mainImageLabel.setIcon(new ImageIcon(resizedImage));
        // Update the left panel layout and thumbnails
        leftPanel.setPreferredSize(new Dimension(getWidth() - imageHeight, getHeight() - buttonPanelHeight));
        // Resize existing thumbnails
        for (Component component : leftPanel.getComponents()) {
            if (component instanceof JPanel imageTextPanel) {
                for (Component innerComponent : imageTextPanel.getComponents()) {
                    if (innerComponent instanceof JLabel lbl) {
                        ImageIcon icon = (ImageIcon) lbl.getIcon();
                        if (icon != null) {
                            // Resize the image directly without retaining unnecessary references
                            Image imgIcon = icon.getImage();
                            imgIcon.flush();  // Ensure old image is cleared
                            // Nullify references to the old Image and ImageIcon

                            Image resizedImgIcon = imgIcon.getScaledInstance((getWidth() - imageHeight) / 4, (getWidth() - imageHeight) / 4, Image.SCALE_SMOOTH);
                            lbl.setIcon(new ImageIcon(resizedImgIcon));  // Set the resized thumbnail
                        }
                    }
                }
            }
        }
        // Revalidate and repaint
        leftPanel.revalidate();
        leftPanel.repaint();
        revalidate();
        repaint();
    }

    private void redrawEnterImagePathScreen() {
        // Remove all components before adding new ones
        getContentPane().removeAll();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        // Create a panel for entering the file path
        JPanel enterPathPanel = new JPanel(new BorderLayout());
        // Label
        enterPathLabel = new JLabel("Enter File Path: ", JLabel.CENTER);
        enterPathLabel.setFont(new Font("Arial", Font.BOLD, 25));
        // Status label (below input)
        statusLabel = new JLabel("[Status of Image Fetch will be Displayed Here]", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        // Text Field (for user input)
        JTextField pathTextField = new JTextField(30);
        pathTextField.setFont(new Font("Arial", Font.PLAIN, 20));
        // When user presses Enter, fetch image
        pathTextField.addActionListener(e -> {
            String photoPath = pathTextField.getText().trim(); // Get input path
            if (photoPath.isEmpty()) {
                statusLabel.setText("Error: Please enter a valid file path.");
                return;
            }
            // Try loading the image
            ImageIcon imageIcon = new ImageIcon(photoPath);
            if (imageIcon.getIconWidth() == -1) {
                statusLabel.setText("Error: Could not load image. Check the path.");
            } else {
                statusLabel.setText("Image loaded successfully!");
                model.addPhoto(photoPath);
            }
        });
        // Panel to hold label and text field
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        inputPanel.add(enterPathLabel);
        inputPanel.add(pathTextField);
        // Add components to the main panel
        enterPathPanel.add(inputPanel, BorderLayout.CENTER);
        enterPathPanel.add(statusLabel, BorderLayout.SOUTH);
        // Add panel to frame
        add(enterPathPanel, gbc);
        // Refresh UI
        revalidate();
        repaint();
    }



    public void changeScreenID(int id){
        screenID = id;
        redrawScreen(screenID);
    }
    // Set button colors for different states
    private void setButtonColors(JButton button) {
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

    // Custom class for creating icons with a solid color
    class ColorIcon implements Icon {
        private Color color;
        public ColorIcon(Color color) { this.color = color; }
        @Override public void paintIcon(Component c, Graphics g, int x, int y) { g.setColor(color); g.fillRect(x, y, getIconWidth(), getIconHeight()); }
        @Override public int getIconWidth() { return 100; }
        @Override public int getIconHeight() { return 30; }
    }
    // Helps debugs memory
    private static void printMemoryStats() {
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

    // private JButton addBtn, changeNameBtn, changeDateBtn, delBtn, exitBtn, prevBtn, nextBtn, sortNameBtn, sortDateBtn, sortSizeBtn;
    public void addAddBtnListener(ActionListener listener) {
        addBtn.addActionListener(e -> { listener.actionPerformed(e); });
    }
    public void addChangeNameBtnListener(ActionListener listener) {
        changeNameBtn.addActionListener(e -> { listener.actionPerformed(e); });
    }
    public void addChangeDateBtnListener(ActionListener listener) {
        changeDateBtn.addActionListener(e -> { listener.actionPerformed(e); });
    }
    public void addDelBtnListener(ActionListener listener) {
        delBtn.addActionListener(e -> { listener.actionPerformed(e); });
    }
    public void addExitBtnListener(ActionListener listener) {
        exitBtn.addActionListener(e -> { listener.actionPerformed(e); });
    }
    public void addPrevBtnListener(ActionListener listener) {
        prevBtn.addActionListener(e -> { listener.actionPerformed(e); });
    }
    public void addNextBtnListener(ActionListener listener) {
        addBtn.addActionListener(e -> { listener.actionPerformed(e); });
    }
    public void addSortNameBtnListener(ActionListener listener) {
        sortNameBtn.addActionListener(e -> { listener.actionPerformed(e); });
    }
    public void addSortDateBtnListener(ActionListener listener) {
        sortDateBtn.addActionListener(e -> { listener.actionPerformed(e); });
    }
    public void addSortSizeBtnListener(ActionListener listener) {
        sortSizeBtn.addActionListener(e -> {  listener.actionPerformed(e); });
    }

    public void displayImage(Photo p) {
        // Load the new image from the given path (assuming the photo is a file path)
        ImageIcon imageIcon = new ImageIcon(p.getFilePath());
        mainImage = imageIcon.getImage();

        // Resize the image to fit the current window size
        imageHeight = getHeight() - buttonPanelHeight - 100;
        resizedImage = mainImage.getScaledInstance(imageHeight, imageHeight, Image.SCALE_SMOOTH);

        // Update the main image label with the resized image
        mainImageLabel.setIcon(new ImageIcon(resizedImage));

        topLabel.setText("Title of the Image: " + p.getName());
        bottomLabel.setText("Date: " + p.getDateAdded());
    }

}
