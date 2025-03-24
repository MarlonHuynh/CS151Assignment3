/**
 * PhotoAlbumController.java
 *
 * Description: Acts as an intermediate between the View and Model, and helps manages the interactivity of the program.
 *
 * @author  Marlon Huynh
 * @version 1.0, 3/29/2025
 */

public class PhotoAlbumController {
    private PhotoAlbumModel model;  // Reference to the model
    private PhotoAlbumView view;  // Reference to the view

    // Constructor to initialize the view and model
    public PhotoAlbumController(PhotoAlbumModel m, PhotoAlbumView v) {
        model = m;
        view = v;
        view.addAddBtnListener(e -> addImage());
        view.addPrevBtnListener(e -> prevImage());
        view.addNextBtnListener(e -> nextImage());
        view.addExitBtnListener(e -> exitProgram());
        view.addDelBtnListener(e -> delImage());
        view.addSortNameBtnListener(e -> sortName());
        view.addSortDateBtnListener(e -> sortDate());
        view.addSortSizeBtnListener(e -> sortSize());
        view.addChangeNameBtnListener(e -> changeName());
        view.addChangeDateBtnListener(e -> changeDate());
    }
    private void changeName() {
        System.out.println("Clicked change name button!");
        view.changeToChangeNameScreen();
    }
    private void changeDate() {
        System.out.println("Clicked change date button!");
        view.changeToChangeDateScreen();
    }
    private void sortSize() {
        System.out.println("Clicked sort size button!");
        SortBySize sortObj = new SortBySize();
        model.setPhotos(sortObj.sort(model.getPhotos()));
        view.updateLeftPanel(model.getPhotos(), model.getIndex());
        view.displayImage(model.current());
        view.drawMainScreen();
    }
    private void sortDate() {
        System.out.println("Clicked sort date button!");
        SortByDate sortObj = new SortByDate();
        model.setPhotos(sortObj.sort(model.getPhotos()));
        view.updateLeftPanel(model.getPhotos(), model.getIndex());
        view.displayImage(model.current());
        view.drawMainScreen();
    }
    private void sortName() {
        System.out.println("Clicked sort name button!");
        SortByName sortObj = new SortByName();
        model.setPhotos(sortObj.sort(model.getPhotos()));
        view.updateLeftPanel(model.getPhotos(), model.getIndex());
        view.displayImage(model.current());
        view.drawMainScreen();
    }
    private void addImage() {
        System.out.println("Clicked add button!");
        view.changeToAddScreen();
    }
    private void delImage() {
        System.out.println("Clicked del button!");
        view.changeToDeleteScreen();
    }
    public void prevImage(){
        System.out.println("Clicked prev button!");
        if (model.hasPrevious()){
            Photo p = model.previous();
            view.displayImage(p);
            view.updateLeftPanel(model.getPhotos(), model.getIndex());
        }
    }
    public void nextImage(){
        System.out.println("Clicked next button!");
        if (model.hasNext()){
            Photo p = model.next();
            view.displayImage(p);
            view.updateLeftPanel(model.getPhotos(), model.getIndex());
        }
    }
    public void exitProgram(){
        System.exit(0);
    }
}
