/**
 * PhotoAlbumController.java
 * Description: Acts as an intermediate between the View and Model, and helps manages the interactivity of the program.
 *
 * @author  Marlon Huynh
 * @version 1.0, 3/29/2025
 */

public class PhotoAlbumController {
    private final PhotoAlbumModel model;  // Reference to the model
    private final PhotoAlbumView view;  // Reference to the view
    /**
     * Constructor
     *
     * @param m The Photo Album Model
     * @param v The Photo Album View
     */
    public PhotoAlbumController(PhotoAlbumModel m, PhotoAlbumView v) {
        model = m;
        view = v;
        view.addAddBtnListener(e -> addImage());
        view.addPrevBtnListener(e -> prevImage());
        view.addNextBtnListener(e -> nextImage());
        view.addDelBtnListener(e -> delImage());
        view.addExitBtnListener(e -> exitProgram());
        view.addSortNameBtnListener(e -> sortName());
        view.addSortDateBtnListener(e -> sortDate());
        view.addSortSizeBtnListener(e -> sortSize());
        view.addChangeNameBtnListener(e -> changeName());
        view.addChangeDateBtnListener(e -> changeDate());
    }
    /**
     * Performs add image(s) action
     */
    private void addImage() {
        view.changeToAddScreen();
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
