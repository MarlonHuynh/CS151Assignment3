public class PhotoAlbumController {
    private PhotoAlbumModel model;  // Reference to the model
    private PhotoAlbumView view;  // Reference to the view

    // Constructor to initialize the view and model
    public PhotoAlbumController(PhotoAlbumModel model, PhotoAlbumView view) {
        this.model = model;
        this.view = view;
        this.view.addAddBtnListener(e -> addImage());
        this.view.addPrevBtnListener(e -> prevImage());
        this.view.addNextBtnListener(e -> nextImage());
        this.view.addExitBtnListener(e -> exitProgram());
    }

    private void addImage() {
        System.out.println("Clicked add button!");
        view.changeScreenID(2);
    }

    public void prevImage(){
    }
    public void nextImage(){
    }
    public void exitProgram(){
        System.exit(0);
    }
}
