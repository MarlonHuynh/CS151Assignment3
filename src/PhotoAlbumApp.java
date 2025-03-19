public class PhotoAlbumApp {
    public static void main(String[] args) {
        PhotoAlbumModel model = new PhotoAlbumModel();
        PhotoAlbumView view = new PhotoAlbumView();
        PhotoAlbumController controller = new PhotoAlbumController(model, view);
        view.setVisible(true);
    }
}