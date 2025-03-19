public class PhotoAlbumController {
    private PhotoAlbumModel model;  // Reference to the model
    private PhotoAlbumView view;  // Reference to the view

    // Constructor to initialize the view and model
    public PhotoAlbumController(PhotoAlbumModel model, PhotoAlbumView view) {
        this.model = model;
        this.view = view;
        this.view.addExitBtnListener(e -> exitProgram());
    }

    public void exitProgram(){
        System.exit(0);
    }
}
