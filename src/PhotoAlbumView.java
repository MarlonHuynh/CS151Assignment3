/**
 * PhotoAlbumView.java
 * Description: Manages the display of the photo album.
 *
 * @author  Marlon Huynh
 * @version 1.0, 3/29/2025
 */

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PhotoAlbumView extends JFrame{
    // State and constant Variables
    private final int buttonPanelHeight = 100;      // Height of the button panel
    private int imageHeight;                        // Height of the main image (determined at runtime)
    private int screenID = 1;                       // Two Screens: 1 == Main Screen, 2 = Input Screens
    private int inputID = 1;                        // Screen states for Input Screen: 1 == Enter image, 2 == Change Name, 3 == Change Date, 4 == Delete
    private Timer resizeTimer;                      // Timer for resizing window so too many calls aren't made
    // Main Screen JPanels
    private JPanel mainImagePanel, mainImageTextPanel, btnPanel, leftPanel, innerLeftPanel;
    // Main Screen JComponents
    private JButton addBtn, delBtn, nextBtn, prevBtn, sortNameBtn, sortDateBtn, sortSizeBtn, exitBtn, changeNameBtn, changeDateBtn;
    private JLabel mainImageLabel;          // Image
    private JLabel topLabel, bottomLabel;
    private final ArrayList<JLabel> leftImages = new ArrayList<>();
    private final ArrayList<JTextArea> leftTexts = new ArrayList<>();
    private Image mainImage, resizedImage;
    private BufferedImage whiteSquare;      // Default image
    // Enter Screens Components
    private JLayeredPane layeredPane;
    private JPanel inputPanel;
    private JLabel enterLabel;
    private JTextArea statusLabel;
    private JTextField fieldLabel = new JTextField();
    /* -----------------------------------------------------------------
                    INITIALIZATION & NAVIGATIONAL LOGIC
     -----------------------------------------------------------------*/
    /**
     * Constructor of the photo album. Initializes the starting view.
     */
    public PhotoAlbumView(){
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.gc();                        // Force garbage collection
            HelperFunctions.printMemoryStats(); // Print memory stats for debugging
        }));
        initializeView();
        this.setVisible(true);
    }
    /**
     * Initializes the starting view of the program and all variables associated.
     */
    public void initializeView() {
        System.out.println(fieldLabel.getText() + "--is initialized!");
        // --------------------------------------------------
        //              ONE-TIME INITIALIZATION
        // --------------------------------------------------
        // Timer for resizing app (prevents too many resize calls)
        resizeTimer = new Timer(200, new ActionListener() { @Override public void actionPerformed(ActionEvent e) { setScreen(screenID); } });
        resizeTimer.setRepeats(false);  // Only run the action once after the delay
        // App
        setLayout(new BorderLayout());
        setTitle("Marlon's Photo Album");
        // Display the main frame
        setSize(1200, 800);    // Default Size
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
        setButtonsLock(true);
        // Set Colors
        HelperFunctions.setButtonColors(addBtn);
        HelperFunctions.setButtonColors(nextBtn);
        HelperFunctions.setButtonColors(prevBtn);
        HelperFunctions.setButtonColors(delBtn);
        HelperFunctions.setButtonColors(exitBtn);
        HelperFunctions.setButtonColors(sortNameBtn);
        HelperFunctions.setButtonColors(sortDateBtn);
        HelperFunctions.setButtonColors(sortSizeBtn);
        HelperFunctions.setButtonColors(changeNameBtn);
        HelperFunctions.setButtonColors(changeDateBtn);
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
        // Create a BufferedImage of the specified width and height
        whiteSquare = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics g = whiteSquare.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 100, 100);
        mainImage = whiteSquare;
        imageHeight = getHeight() - buttonPanelHeight - 100;
        resizedImage = mainImage.getScaledInstance(imageHeight, imageHeight, Image.SCALE_SMOOTH); // Resizing image to square
        mainImageLabel = new JLabel(new ImageIcon(resizedImage));
        mainImagePanel.add(mainImageLabel, BorderLayout.CENTER);
        // Set left side panel for other photos in the album dynamically
        leftPanel.setLayout(new GridLayout(3, 3));
        leftPanel.setPreferredSize(new Dimension(getWidth() - imageHeight, getHeight() - buttonPanelHeight));
        for (int i = 0; i < 9; i++) {
            // Create the image icon and resize it
            ImageIcon scaledLeftIcons = new ImageIcon(mainImage.getScaledInstance((getWidth() - imageHeight) / 4, (getWidth() - imageHeight) / 4, Image.SCALE_SMOOTH)); // Adjust size as needed
            JLabel leftImagesLabel = new JLabel(scaledLeftIcons);
            // Create the text label
            JTextArea leftTextArea = new JTextArea(2, 20);
            leftTextArea.setText("No image displayed. Please add an image.");
            leftTextArea.setLineWrap(true);  // Enable line wrapping
            leftTextArea.setWrapStyleWord(true);  // Wrap at word boundaries, not in the middle of words
            leftTextArea.setMargin(new Insets(10, 10, 10, 10));
            leftTextArea.setFont(new Font("Arial", Font.BOLD, 20));
            leftTextArea.setEditable(false);
            // Create a panel to hold the image and text
            innerLeftPanel = new JPanel(new BorderLayout());
            // Add the image and text to the panel
            innerLeftPanel.add(leftImagesLabel, BorderLayout.CENTER);
            innerLeftPanel.add(leftTextArea, BorderLayout.NORTH);
            if (i == 0){
                innerLeftPanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
            }
            // Add the images and texts to ArrayList for later accessibility
            leftImages.add(leftImagesLabel);
            leftTexts.add(leftTextArea);
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
                int newState = e.getNewState();
                int oldState = e.getOldState();
                // Check if the window was maximized
                if ((newState & Frame.MAXIMIZED_BOTH) != 0 && (oldState & Frame.MAXIMIZED_BOTH) == 0) { // Window was maximized, trigger resize
                    setScreen(screenID);
                }
                // Check if the window was restored
                if ((newState & Frame.MAXIMIZED_BOTH) == 0 && (oldState & Frame.MAXIMIZED_BOTH) != 0) { // Window was restored, trigger resize
                    setScreen(screenID);
                }
            }
        });
    }
    /**
     * Changes the UI of the program to the specified screen
     *
     * @param screenID the ID of the screen to set to
     */
    private void setScreen(int screenID) {
        System.out.println("Redrawing screen with ID: " + screenID);
        switch (screenID){
            case 1: // Removes the input panel if its there and update the main screen
                if (inputPanel != null) {
                    inputPanel.setVisible(false);
                }
                drawMainScreen();
                break;
            case 2:
                setInputPanel();
                break;
            default:
                System.out.println("Error: Invalid screenID.");
                break;
        }
    }
    /**
     * Draws the main screen of the program (with the left panels, right image, and buttons)
     */
    public void drawMainScreen(){
        // Update the image size for the main image
        imageHeight = getHeight() - buttonPanelHeight - 100;
        resizedImage.flush();
        resizedImage = mainImage.getScaledInstance(imageHeight, imageHeight, Image.SCALE_SMOOTH);
        Icon currentIcon = mainImageLabel.getIcon();
        if (currentIcon != null) { ((ImageIcon) currentIcon).getImage().flush(); }
        mainImageLabel.setIcon(new ImageIcon(resizedImage));
        // Update the left panel layout and thumbnails
        leftPanel.setPreferredSize(new Dimension(getWidth() - imageHeight, getHeight() - buttonPanelHeight));
        // Resize existing thumbnails to appropriate size
        for (Component component : leftPanel.getComponents()) {
            if (component instanceof JPanel imageTextPanel) {
                for (Component innerComponent : imageTextPanel.getComponents()) {
                    if (innerComponent instanceof JLabel label) {
                        ImageIcon icon = (ImageIcon) label.getIcon();
                        if (icon != null) {
                            Image imgIcon = icon.getImage();
                            imgIcon.flush();                                // Ensure old image is cleared
                            Image resizedImgIcon = imgIcon.getScaledInstance((getWidth() - imageHeight) / 4, (getWidth() - imageHeight) / 4, Image.SCALE_SMOOTH);
                            label.setIcon(new ImageIcon(resizedImgIcon));   // Set the resized thumbnail
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
    /**
     * Draws the input panel, which allows the user to input a string (for actions such as add, delete, change name, and change date)
     */
    private void setInputPanel() {
        if (inputPanel != null){        // If it exists, unhide it
            unhideInputPanel();
        }
        else {  // If it doesn't exist, create it
            initializeInputPanel();
        }
    }
    /**
     * Initializes the input panel.
     */
    public void initializeInputPanel(){
        inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setOpaque(true);
        // Label
        enterLabel = new JLabel(inputID + " - Enter File Path: ", JLabel.CENTER);
        enterLabel.setFont(new Font("Arial", Font.BOLD, 25));
        enterLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Status label (below input)
        statusLabel = new JTextArea(5, 30);
        statusLabel.setLineWrap(true);          // Enable line wrapping
        statusLabel.setWrapStyleWord(true);     // Wrap at word boundaries, not in the middle of words
        statusLabel.setMargin(new Insets(10, 10, 10, 10));
        statusLabel.setText("To add, enter a valid [name,file path,date], [file path], or [folder path]. Enter nothing to return to main screen. Date is to be formatted in 'MM/DD/YYYY HH:MM [Time Zone]'. For example, 'Apple,F:/Art/Apple.png,12/22/2003 6:30 PST'");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 25));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Text Field (for user input)
        fieldLabel = new JTextField("Enter input here!", 30);
        fieldLabel.setFont(new Font("Arial", Font.BOLD, 20));
        // Colors
        Color fColor = Color.BLACK;
        enterLabel.setForeground(fColor);
        statusLabel.setForeground(fColor);
        Color bColor = Color.WHITE;
        enterLabel.setBackground(bColor);
        statusLabel.setBackground(bColor);
        // Panel to hold label and text field
        JPanel tempPanel = new JPanel(new GridBagLayout()); // Center components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.CENTER;
        tempPanel.add(enterLabel, gbc);
        tempPanel.add(fieldLabel, gbc);
        tempPanel.add(statusLabel, gbc);
        inputPanel.add(tempPanel, BorderLayout.CENTER);
        // Add the Enter Path Panel to the layered pane at a higher layer
        layeredPane = getLayeredPane();
        inputPanel.setBounds(0, 0, getWidth(), getHeight());
        inputPanel.setBackground(Color.RED);
        inputPanel.setVisible(true);
        layeredPane.add(inputPanel, JLayeredPane.MODAL_LAYER); // Add to the modal layer (topmost layer)
        // Refresh the UI
        revalidate();
        repaint();
    }
    /**
     * Updates and unhide the input panel.
     */
    public void unhideInputPanel(){
        inputPanel.setVisible(true);
        switch (inputID){ // Makes sure the panel has correct info
            case 1: // Enter
                enterLabel.setText(inputID + " - Enter File Path: ");
                statusLabel.setText("To add, enter a valid [name, file path, date], [file path], or [folder path]. Enter nothing to return to main screen. Date is to be formatted in 'MM/DD/YYYY HH:MM [Time Zone]'. For example, 'Apple, F:/Art/Apple.png, 12/22/2003 6:30 PST'");
                break;
            case 2: // Change Name
                enterLabel.setText(inputID + " - Enter New Name: ");
                statusLabel.setText("[To add, enter a name. Enter nothing to return to main screen.]");
                break;
            case 3: // Change Date
                enterLabel.setText(inputID + " - Enter New Date: ");
                statusLabel.setText("[To add, enter a date in the form 'MM/DD/YYYY HH:MM [Time Zone]'. Enter nothing to return to main screen.]");
                break;
            case 4:
                enterLabel.setText(inputID + " - Enter file name to delete: ");
                statusLabel.setText("[To delete, enter a name of an existing photo. Enter nothing to return to main screen.");
            default:
                break;
        }
        inputPanel.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.setLayer(inputPanel, JLayeredPane.MODAL_LAYER);
        inputPanel.revalidate();
        inputPanel.repaint();
    }
    /* -----------------------------------------------------------------
                    HELPER METHODS FOR CHANGING DISPLAY
     -----------------------------------------------------------------*/
    // --- Changing Screen ---
    /**
     * Changes to the 'Add' input screen
     */
    public void changeToAddScreen(){
        changeInputID(1);
        changeScreenID(2);
    }
    /**
     * Changes to the 'Change Name' input screen
     */
    public void changeToChangeNameScreen(){
        changeInputID(2);
        changeScreenID(2);
    }
    /**
     * Changes to the 'Change Date' input screen
     */
    public void changeToChangeDateScreen(){
        changeInputID(3);
        changeScreenID(2);
    }
    /**
     * Changes to the 'Delete' input screen
     */
    public void changeToDeleteScreen(){
        changeInputID(4);
        changeScreenID(2);
    }
    /**
     * Changes the screenID to the specified number and sets the appropriate screen
     * Here are the screenIDs used:
     * 1 - Main Screen
     * 2 - Input Screens
     *
     * @param id the ID of the screen ID to change to
     */
    public void changeScreenID(int id) {
        screenID = id;
        setScreen(screenID);
    }
    /**
     * Changes the inputID to the specified number to determine what Input Screen should be displayed, if it were to be displayed
     * Here are the inputID used:
     * 1 - Add Input
     * 2 - Change Name Input
     * 3 - Change Date Input
     * 4 - Delete Input
     *
     * @param i the ID of the input screen to change to
     */
    public void changeInputID(int i){
        inputID = i;
    }
    // --- Changing Images ---
    /**
     * Changes the right side main image to the specified Photo
     *
     * @param p the Photo to display on the main image
     */
    public void displayImage(Photo p) {
        mainImage = new ImageIcon(p.getFilePath()).getImage();
        imageHeight = getHeight() - buttonPanelHeight - 100;
        resizedImage = mainImage.getScaledInstance(imageHeight, imageHeight, Image.SCALE_SMOOTH);
        mainImageLabel.setIcon(new ImageIcon(resizedImage));
        topLabel.setText("Title: " + p.getName());
        bottomLabel.setText("Date: " + p.getDateAdded());
        setButtonsLock(false);
    }
    /**
     * Changes the right side main image to an empty image
     */
    public void displayEmptyMainImage() {
        // Set to a blank or placeholder image
        mainImage = whiteSquare;
        resizedImage = mainImage.getScaledInstance(imageHeight, imageHeight, Image.SCALE_SMOOTH);
        mainImageLabel.setIcon(new ImageIcon(resizedImage));
        topLabel.setText("Upload something! [Current Photo's Title will be Displayed Here.]");
        bottomLabel.setText("[Current Photo's Date will be Displayed Here.]");
        setButtonsLock(true);
    }
    /**
     * Updates all the left panels using the album and current index
     *
     * @param masterAlbum the Photo arrayList to display
     * @param currentIndex the Photo arrayList's current index
     */
    public void updateLeftPanel(ArrayList<Photo> masterAlbum, int currentIndex) {
        // Clears panels if album is empty
        if (masterAlbum == null || masterAlbum.isEmpty()) {
            for (int i = 0; i < leftImages.size(); i++) {
                JPanel innerPanel = (JPanel) leftPanel.getComponent(i);
                JLabel label1 = (JLabel) innerPanel.getComponent(0);
                label1.setIcon(null);
                JTextArea label2 = (JTextArea) innerPanel.getComponent(1);
                label2.setText("");
            }
            return;
        }
        // Update panels otherwise
        for (int i = 0; i < leftImages.size(); i++) {
            if (currentIndex + i >= 0 && currentIndex + i < masterAlbum.size()) { // Checks if the access is within bounds of the panel's indexes (0 to 8)
                Photo masterAlbumPhoto = (masterAlbum.get(currentIndex + i));
                ImageIcon imageIcon = new ImageIcon(masterAlbumPhoto.getFilePath());
                Image resizedImgIcon = imageIcon.getImage().getScaledInstance((getWidth() - imageHeight) / 4, (getWidth() - imageHeight) / 4, Image.SCALE_SMOOTH);
                JPanel innerPanel = (JPanel) leftPanel.getComponent(i);
                JLabel label1 = (JLabel) innerPanel.getComponent(0);
                label1.setIcon(new ImageIcon(resizedImgIcon));
                JTextArea label2 = (JTextArea) innerPanel.getComponent(1);
                label2.setText(masterAlbumPhoto.getName() + "\n" + masterAlbumPhoto.getFileSize() + " bytes " + "\n" + masterAlbumPhoto.getDateAdded().toString());
            }
            else { // Else sets the null image
                JPanel innerPanel = (JPanel) leftPanel.getComponent(i);
                JLabel label1 = (JLabel) innerPanel.getComponent(0);
                label1.setIcon(null);
                JTextArea label2 = (JTextArea) innerPanel.getComponent(1);
                label2.setText("");
            }
        }
        drawMainScreen();
    }
    // --- Changing Buttons' State ---
    /**
     * Updates the buttons to lock out certain buttons if there are no images in the album
     * as it is impossible to do actions like Next, Change Date, etc. if there are no images.
     *
     * @param b the state of the buttons. With false being majority locked and true being
     *          all buttons available.
     */
    public void setButtonsLock(boolean b){
        if (b) {
            addBtn.setEnabled(true);
            prevBtn.setEnabled(false);
            nextBtn.setEnabled(false);
            delBtn.setEnabled(false);
            exitBtn.setEnabled(true);
            changeNameBtn.setEnabled(false);
            changeDateBtn.setEnabled(false);
            sortNameBtn.setEnabled(false);
            sortDateBtn.setEnabled(false);
            sortSizeBtn.setEnabled(false);
        }
        else {
            addBtn.setEnabled(true);
            prevBtn.setEnabled(true);
            nextBtn.setEnabled(true);
            delBtn.setEnabled(true);
            exitBtn.setEnabled(true);
            changeNameBtn.setEnabled(true);
            changeDateBtn.setEnabled(true);
            sortNameBtn.setEnabled(true);
            sortDateBtn.setEnabled(true);
            sortSizeBtn.setEnabled(true);
        }
    }
    /* -----------------------------------------------------------------
                                GETTERS
     -----------------------------------------------------------------*/
    /**
     * Adds a button listener to the respective button.
     */
    public JButton getAddBtn() { return addBtn; }
    public JButton getChangeNameBtn() { return changeNameBtn; }
    public JButton getChangeDateBtn() { return changeDateBtn; }
    public JButton getDelBtn() { return delBtn; }
    public JButton getExitBtn() { return exitBtn; }
    public JButton getPrevBtn() { return prevBtn; }
    public JButton getNextBtn() { return nextBtn; }
    public JButton getSortNameBtn() { return sortNameBtn; }
    public JButton getSortDateBtn() { return sortDateBtn; }
    public JButton getSortSizeBtn() { return sortSizeBtn; }

    public JTextField getFieldLabel(){ return fieldLabel; }
    public JTextArea getStatusLabel(){ return statusLabel; }
    public int getInputID(){ return inputID; }

}
