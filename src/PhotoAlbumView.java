import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PhotoAlbumView extends JFrame{
    private PhotoAlbumModel model;
    // State and constant Variables
    private final int buttonPanelHeight = 100;      // Height of the button panel
    private int imageHeight;                        // Height of the main image (determined at runtime)
    private int screenID = 1;                       // Two Screens: 1 == Main Screen, 2 = Input Screens
    private int inputID = 1;                        // Screen states for Input Screen: 1 == Enter image, 2 == Change Name, 3 == Change Date, 4 == Delete
    private boolean isProcessing = false;           // Timer boolean for input processing so too many calls aren't made
    private final Timer resizeTimer;                // Timer for resizing window so too many calls aren't made
    // Main Screen JPanels
    private JPanel mainImagePanel, mainImageTextPanel, btnPanel, leftPanel, innerLeftPanel;
    // Main Screen JComponents
    private JButton addBtn, delBtn, nextBtn, prevBtn, sortNameBtn, sortDateBtn, sortSizeBtn, exitBtn, changeNameBtn, changeDateBtn;
    private JLabel mainImageLabel;          // Image
    private JLabel topLabel, bottomLabel;
    private ArrayList<JLabel> leftImages = new ArrayList<>();
    private ArrayList<JTextArea> leftTexts = new ArrayList<>();
    private Image mainImage, resizedImage;
    private ImageIcon scaledLeftIcons;
    private BufferedImage whiteSquare;      // Default image
    // Enter Screens Components
    private JPanel inputPanel;
    private JLabel enterLabel;
    private JTextArea statusLabel;
    private JTextField fieldLabel;
    /* -----------------------------------------------------------------
                    INITIALIZATION & NAVIGATIONAL LOGIC
     -----------------------------------------------------------------*/
    public PhotoAlbumView(PhotoAlbumModel m){
        model = m;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.gc(); // Force garbage collection
            HelperFunctions.printMemoryStats(); // Print memory stats for debugging
        }));
        initializeView();
        // Timer to delay resize action
        resizeTimer = new Timer(200, new ActionListener() { @Override public void actionPerformed(ActionEvent e) { setScreen(screenID); } });
        resizeTimer.setRepeats(false);  // Only run the action once after the delay
    }
    public void initializeView() {
        // --------------------------------------------------
        //              ONE-TIME INITIALIZATION
        // --------------------------------------------------
        // App
        setLayout(new BorderLayout());
        setTitle("Marlon's Photo Album");
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
            scaledLeftIcons = new ImageIcon(mainImage.getScaledInstance((getWidth() - imageHeight) / 4, (getWidth() - imageHeight) / 4, Image.SCALE_SMOOTH)); // Adjust size as needed
            JLabel leftImagesLabel = new JLabel(scaledLeftIcons);
            // Create the text label
            JTextArea leftTextArea = new JTextArea(2, 20);
            leftTextArea.setText("No image displayed. Please add an image.");
            leftTextArea.setLineWrap(true);  // Enable line wrapping
            leftTextArea.setWrapStyleWord(true);  // Wrap at word boundaries, not in the middle of words
            leftTextArea.setMargin(new Insets(10, 10, 10, 10));
            leftTextArea.setFont(new Font("Arial", Font.BOLD, 20));
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
    private void setScreen(int screenID) {
        System.out.println("Redrawing screen with ID: " + screenID);
        switch (screenID){
            case 1:
                if (inputPanel != null) { // Removes the input panel if its there and update the main screen
                    // Remove the Input Panel when switching to the Main Screen
                    JLayeredPane layeredPane = getLayeredPane();
                    layeredPane.remove(inputPanel);
                    inputPanel.setVisible(false);
                    // Update the panels
                    updateLeftPanel(model.getPhotos(), model.getIndex());
                    if (model.getPhotos().isEmpty()){ displayEmptyImage(); }
                    else{ displayImage(model.current()); }
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
    private void setInputPanel() {
        if (inputPanel != null){        // If it exists, unhide it
            unhideInputPanel();
        }
        else if (inputPanel == null) {  // If it doesn't exist, create it
            initializeInputPanel();
        }
    }
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
        fieldLabel = new JTextField(30);
        fieldLabel.setFont(new Font("Arial", Font.BOLD, 20));
        // Colors
        Color fColor = Color.BLACK;
        enterLabel.setForeground(fColor);
        statusLabel.setForeground(fColor);
        Color bColor = Color.WHITE;
        enterLabel.setBackground(bColor);
        statusLabel.setBackground(bColor);
        // Panel to hold label and text field
        JPanel tempPanel = new JPanel();
        tempPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        tempPanel.add(enterLabel);
        tempPanel.add(fieldLabel);
        tempPanel.add(statusLabel);
        inputPanel.add(tempPanel, BorderLayout.CENTER);
        // Add the Enter Path Panel to the layered pane at a higher layer
        JLayeredPane layeredPane = getLayeredPane();
        inputPanel.setBounds(0, 0, getWidth(), getHeight());
        inputPanel.setBackground(Color.RED);
        inputPanel.setVisible(true);
        layeredPane.add(inputPanel, JLayeredPane.MODAL_LAYER); // Add to the modal layer (topmost layer)
        // When user presses Enter, perform action
        fieldLabel.addActionListener(e -> {
            if (isProcessing) { return; }   // Prevent duplicate triggers
            isProcessing = true;            // Set flag to indicate action is in progress
            String inputString = fieldLabel.getText().trim(); // Get input path
            if (inputString.isEmpty()) { // Switch to main screen
                changeScreenID(1);
            }
            else if (inputID == 1) { // Add new file logic
                // --- Method 1: [name, file path, date] format ---
                String[] parts = inputString.split(",", 3);
                if (parts.length == 3) {
                    File defaultFile = new File(parts[1]);
                    if (defaultFile.isFile()) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm z");
                        Date date = new Date();
                        try {
                            date = dateFormat.parse(parts[2]);
                        } catch (ParseException p) {
                            statusLabel.setText("Error: Invalid input parse for change date! enter a valid [name,file path,date], [file path], or [folder path] or blank to go back.");
                            isProcessing = false;
                            return;
                        }
                        // All parts requirement met for [name, file path, date] format
                        Photo p = new Photo(parts[0], parts[1], date, defaultFile.length());
                        statusLabel.setText(inputString + " is parsed and added!");
                        model.addPhoto(p);
                        isProcessing = false;
                        return;
                    }
                    else{
                        statusLabel.setText("Error: Invalid input parse for change date! enter a valid [name,file path,date], [file path], or [folder path] or blank to go back.");
                        isProcessing = false;
                        return;
                    }
                }
                // --- Method 2: [directory path] format ---
                File file = new File(inputString);
                if (file.isDirectory()) {
                    File[] imageFiles = file.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".jpeg") || name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".gif"));
                    if (imageFiles == null || imageFiles.length == 0) {
                        statusLabel.setText("No images found in the folder. Try again or enter blank to go back.");
                        isProcessing = false;
                        return;
                    }
                    for (File imageFile : imageFiles) {
                        model.addPhoto(imageFile.getAbsolutePath());
                        System.out.println("Added " + imageFile.getAbsolutePath());
                    }
                    statusLabel.setText(imageFiles.length + " images at " + inputString + " loaded successfully! Enter more or enter blank to go back.");
                }
                // --- Method 3: [file path] format ---
                else if (file.isFile() &&
                        (inputString.toLowerCase().endsWith(".jpg") || inputString.toLowerCase().endsWith(".jpeg") || inputString.toLowerCase().endsWith(".png") || inputString.toLowerCase().endsWith(".gif"))) {
                    model.addPhoto(file.getAbsolutePath());
                    System.out.println("Added " + file.getAbsolutePath());
                    statusLabel.setText("Image at " + inputString + " loaded successfully! Enter blank to go back.");
                } else {
                    statusLabel.setText("Error: Invalid file or directory. Please enter a valid image file or folder.");
                }
            }
            else if (inputID == 2) { // Change Name
                model.current().setName(inputString);
                statusLabel.setText("Changed name to " + inputString + "Enter blank to go back.");
            }
            else if (inputID == 3) { // Change Date
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm z");
                Date date = new Date();
                try {
                    date = dateFormat.parse(inputString);
                    statusLabel.setText(date + " Date parsed!");
                }
                catch (ParseException p) {
                    statusLabel.setText("Error: Invalid input parse for change date! Enter 'MM/DD/YYYY HH:MM [Time Zone] or blank to go back.");
                }
                model.current().setDateAdded(date);
                System.out.println("Set date to " + date);
            }
            else if (inputID == 4) { // Delete
                boolean removed = model.removePhoto(inputString);
                if (removed) { statusLabel.setText(inputString + " successfully removed!"); }
                else { statusLabel.setText("Error: No such photo found. Enter name to try again or blank to go back."); }
            }

            isProcessing = false;  // Reset flag to allow the next input
        });
        // Refresh the UI
        revalidate();
        repaint();
    }
    public void unhideInputPanel(){
        JLayeredPane layeredPane = getLayeredPane();
        layeredPane.add(inputPanel);
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
    }
    /* -----------------------------------------------------------------
                    HELPER METHODS FOR CHANGING DISPLAY
     -----------------------------------------------------------------*/
    // --- Changing Screen ---
    public void changeToAddScreen(){
        changeInputID(1);
        changeScreenID(2);
    }
    public void changeToChangeNameScreen(){
        changeInputID(2);
        changeScreenID(2);
    }
    public void changeToChangeDateScreen(){
        changeInputID(3);
        changeScreenID(2);
    }
    public void changeToDeleteScreen(){
        changeInputID(4);
        changeScreenID(2);
    }
    public void changeInputID(int i){
        inputID = i;
    }
    public void changeScreenID(int id) {
        screenID = id;
        setScreen(screenID);
    }
    // --- Changing Images ---
    public void displayImage(Photo p) {
        mainImage = new ImageIcon(p.getFilePath()).getImage();
        imageHeight = getHeight() - buttonPanelHeight - 100;
        resizedImage = mainImage.getScaledInstance(imageHeight, imageHeight, Image.SCALE_SMOOTH);
        mainImageLabel.setIcon(new ImageIcon(resizedImage));
        topLabel.setText("Title: " + p.getName());
        bottomLabel.setText("Date: " + p.getDateAdded());
        setButtonsLock(false);
    }
    public void displayEmptyImage() {
        // Set to a blank or placeholder image
        mainImage = whiteSquare;
        resizedImage = mainImage.getScaledInstance(imageHeight, imageHeight, Image.SCALE_SMOOTH);
        mainImageLabel.setIcon(new ImageIcon(resizedImage));
        topLabel.setText("Upload something! [Current Photo's Title will be Displayed Here.]");
        bottomLabel.setText("[Current Photo's Date will be Displayed Here.]");
        updateLeftPanel(model.getPhotos(), model.getIndex());
        setButtonsLock(true);
    }
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
                        BUTTON ACTION LISTENERS
     -----------------------------------------------------------------*/
    public void addAddBtnListener(ActionListener listener) { addBtn.addActionListener(e -> { listener.actionPerformed(e); }); }
    public void addChangeNameBtnListener(ActionListener listener) { changeNameBtn.addActionListener(e -> { listener.actionPerformed(e); }); }
    public void addChangeDateBtnListener(ActionListener listener) { changeDateBtn.addActionListener(e -> { listener.actionPerformed(e); }); }
    public void addDelBtnListener(ActionListener listener) { delBtn.addActionListener(e -> { listener.actionPerformed(e); }); }
    public void addExitBtnListener(ActionListener listener) { exitBtn.addActionListener(e -> { listener.actionPerformed(e); }); }
    public void addPrevBtnListener(ActionListener listener) { prevBtn.addActionListener(e -> { listener.actionPerformed(e); }); }
    public void addNextBtnListener(ActionListener listener) { nextBtn.addActionListener(e -> { listener.actionPerformed(e); }); }
    public void addSortNameBtnListener(ActionListener listener) { sortNameBtn.addActionListener(e -> { listener.actionPerformed(e); }); }
    public void addSortDateBtnListener(ActionListener listener) { sortDateBtn.addActionListener(e -> { listener.actionPerformed(e); }); }
    public void addSortSizeBtnListener(ActionListener listener) { sortSizeBtn.addActionListener(e -> {  listener.actionPerformed(e); }); }
}
