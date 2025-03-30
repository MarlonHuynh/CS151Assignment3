/**
 * PhotoAlbumController.java
 * Description: Acts as an intermediate between the View and Model, and helps manages the interactivity of the program.
 *
 * @author  Marlon Huynh
 * @version 1.0, 3/29/2025
 */

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoAlbumController {
    private final PhotoAlbumModel model;  // Reference to the model
    private final PhotoAlbumView view;  // Reference to the view
    private boolean isProcessing = false;           // Timer boolean for input processing so too many calls aren't made
    /**
     * Constructor
     *
     * @param m The Photo Album Model
     * @param v The Photo Album View
     */
    public PhotoAlbumController(PhotoAlbumModel m, PhotoAlbumView v) {
        model = m;
        view = v;
        // Add all the action listeners for buttons
        view.getAddBtn().addActionListener(e -> addImage());
        view.getPrevBtn().addActionListener(e -> prevImage());
        view.getNextBtn().addActionListener(e -> nextImage());
        view.getDelBtn().addActionListener(e -> delImage());
        view.getExitBtn().addActionListener(e -> exitProgram());
        view.getSortNameBtn().addActionListener(e -> sortName());
        view.getSortDateBtn().addActionListener(e -> sortDate());
        view.getSortSizeBtn().addActionListener(e -> sortSize());
        view.getChangeNameBtn().addActionListener(e -> changeName());
        view.getChangeDateBtn().addActionListener(e -> changeDate());
    }
    /**
     * Performs add image(s) action and adds a listener to the text field if needed
     */
    private void addImage() {
        view.changeToAddScreen();
        // Only add Action Listener to field label if none exist
        if (view.getFieldLabel().getActionListeners().length == 0) {
            view.getFieldLabel().addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) { // Only perform action when key is lifted and no other action is processing to ensure only 1 input goes through each time
                    if (e.getKeyCode() == KeyEvent.VK_ENTER && !isProcessing) {
                        inputActions();
                    }
                }
            });
        }
    }

    /**
     * Logic for all text input within the program
     */
    public void inputActions(){
        if (isProcessing) { return; }   // Prevent duplicate triggers

        isProcessing = true;            // Set flag to indicate action is in progress
        String inputString = view.getFieldLabel().getText().trim(); // Get input path

        if (inputString.isEmpty()) { // Switch to main screen if empty
            // Sets the input screen invisible
            view.changeScreenID(1);
            // Updates the main screen
            view.updateLeftPanel(model.getAlbum(), model.getIndex());
            if (model.getAlbum().isEmpty()) { view.displayEmptyMainImage(); }
            else { view.displayImage(model.current()); }
            isProcessing = false;
            return;
        }

        switch (view.getInputID()) {
        case 1:  // Add new file logic
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
                        view.getStatusLabel().setText("Error: Invalid input parse for change date! enter a valid [name,file path,date], [file path], or [folder path] or blank to go back.");
                        isProcessing = false;
                        return;
                    }
                    // All parts requirement met for [name, file path, date] format
                    Photo p = new Photo(parts[0], parts[1], date, defaultFile.length());
                    view.getStatusLabel().setText(inputString + " is parsed and added!");
                    model.addPhoto(p);
                    isProcessing = false;
                    return;
                } else {
                    view.getStatusLabel().setText("Error: Invalid input parse for change date! enter a valid [name,file path,date], [file path], or [folder path] or blank to go back.");
                    isProcessing = false;
                    return;
                }
            }
            // --- Method 2: [directory path] format ---
            File file = new File(inputString);
            if (file.isDirectory()) {
                File[] imageFiles = file.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".jpeg") || name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".gif"));
                if (imageFiles == null || imageFiles.length == 0) {
                    view.getStatusLabel().setText("No images found in the folder. Try again or enter blank to go back.");
                    isProcessing = false;
                    return;
                }
                for (File imageFile : imageFiles) {
                    model.addPhoto(imageFile.getAbsolutePath());
                    System.out.println("Added " + imageFile.getAbsolutePath());
                }
                view.getStatusLabel().setText(imageFiles.length + " images at " + inputString + " loaded successfully! Enter more or enter blank to go back.");
            }
            // --- Method 3: [file path] format ---
            else if (file.isFile() && (inputString.toLowerCase().endsWith(".jpg") || inputString.toLowerCase().endsWith(".jpeg") || inputString.toLowerCase().endsWith(".png") || inputString.toLowerCase().endsWith(".gif"))) {
                model.addPhoto(file.getAbsolutePath());
                System.out.println("Added " + file.getAbsolutePath());
                view.getStatusLabel().setText("Image at " + inputString + " loaded successfully! Enter blank to go back.");
            } else {
                view.getStatusLabel().setText("Error: Invalid file or directory. Please enter a valid image file or folder.");
            }
            break;
        case 2:  // Change Name
            model.current().setName(inputString);
            view.getStatusLabel().setText("Changed name to " + inputString + ". Enter blank to go back.");
            break;
        case 3: // Change Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm z");
            Date date = new Date();
            try {
                date = dateFormat.parse(inputString);
                view.getStatusLabel().setText(date + " Date parsed!");
            } catch (ParseException p) {
                view.getStatusLabel().setText("Error: Invalid input parse for change date! Enter 'MM/DD/YYYY HH:MM [Time Zone] or blank to go back.");
            }
            model.current().setDateAdded(date);
            System.out.println("Set date to " + date);
            break;

        case 4: // Delete
            boolean removed = model.removePhoto(inputString);
            if (removed) {
                view.getStatusLabel().setText(inputString + " successfully removed!");
            } else {
                view.getStatusLabel().setText("Error: No such photo found. Enter name to try again or blank to go back.");
            }
            break;
        }
        isProcessing = false;  // Reset flag to allow the next input
        // Sort by date after adding
        if (!model.getAlbum().isEmpty()) {
            SortByDate sortObj = new SortByDate();
            model.setAlbum(sortObj.sort(model.getAlbum()));
            view.updateLeftPanel(model.getAlbum(), model.getIndex());
            view.displayImage(model.current());
            view.drawMainScreen();
        }

    }
    /**
     * Performs change to previous image action
     */
    public void prevImage(){
        if (model.hasPrevious()){
            Photo p = model.previous();
            view.displayImage(p);
            view.updateLeftPanel(model.getAlbum(), model.getIndex());
        }
    }
    /**
     * Performs change to next image action
     */
    public void nextImage(){
        if (model.hasNext()){
            Photo p = model.next();
            view.displayImage(p);
            view.updateLeftPanel(model.getAlbum(), model.getIndex());
        }
    }
    /**
     * Performs delete current image action
     */
    private void delImage() {
        view.changeToDeleteScreen();
    }
    /**
     * Performs exit program action
     */
    public void exitProgram(){
        System.exit(0);
    }
    /**
     * Performs change name action
     */
    private void changeName() {
        view.changeToChangeNameScreen();
    }
    /**
     * Performs change date action
     */
    private void changeDate() {
        view.changeToChangeDateScreen();
    }
    /**
     * Performs sort by size action
     */
    private void sortSize() {
        SortBySize sortObj = new SortBySize();
        model.setAlbum(sortObj.sort(model.getAlbum()));
        view.updateLeftPanel(model.getAlbum(), model.getIndex());
        view.displayImage(model.current());
        view.drawMainScreen();
    }
    /**
     * Performs sort by date action
     */
    private void sortDate() {
        SortByDate sortObj = new SortByDate();
        model.setAlbum(sortObj.sort(model.getAlbum()));
        view.updateLeftPanel(model.getAlbum(), model.getIndex());
        view.displayImage(model.current());
        view.drawMainScreen();
    }
    /**
     * Performs sort by name action
     */
    private void sortName() {
        SortByName sortObj = new SortByName();
        model.setAlbum(sortObj.sort(model.getAlbum()));
        view.updateLeftPanel(model.getAlbum(), model.getIndex());
        view.displayImage(model.current());
        view.drawMainScreen();
    }
}
