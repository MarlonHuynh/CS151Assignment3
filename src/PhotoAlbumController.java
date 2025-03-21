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
        this.view.addDelBtnListener(e -> delImage());
    }

    private void addImage() {
        System.out.println("Clicked add button!");
        view.changeScreenID(2);
    }

    private void delImage() {
        System.out.println("Clicked del button!");
    }

    public void prevImage(){
        System.out.println("Clicked prev button!");
        if (model.hasPrevious()){
            Photo p = model.previous();
            view.displayImage(p);

        }
    }
    public void nextImage(){
        System.out.println("Clicked next button!.");
        if (model.hasNext()){
            Photo p = model.next();
            view.displayImage(p);
        }
        else{
            System.out.println("No next image.");
        }
    }
    public void exitProgram(){
        System.exit(0);
    }
}
