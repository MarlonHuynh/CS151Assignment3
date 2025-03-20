public class PhotoAlbumController {
    private PhotoAlbumModel model;  // Reference to the model
    private PhotoAlbumView view;  // Reference to the view
    private int currentIndex = 0;

    // Constructor to initialize the view and model
    public PhotoAlbumController(PhotoAlbumModel model, PhotoAlbumView view) {
        this.model = model;
        this.view = view;
        this.view.addAddBtnListener(e -> addImage());
        this.view.addPrevBtnListener(e -> prevImage());
        this.view.addNextBtnListener(e -> nextImage());
        this.view.addExitBtnListener(e -> exitProgram());
        displayCurrentImage();
    }

    private void addImage() {
        System.out.println("Clicked add button!");
        view.changeScreenID(2);
    }

    public void prevImage(){
        if (currentIndex > 0) {
            currentIndex--;
            displayCurrentImage();
        }
    }
    public void nextImage(){
        if (currentIndex < model.getPhotos().size() - 1) {
            currentIndex++;
            displayCurrentImage();
        }
    }
    public void exitProgram(){
        System.exit(0);
    }

    private void displayCurrentImage() {
        //Photo p = model.getPhotos().get(currentIndex);
        //view.displayImage(p);
    }
}
