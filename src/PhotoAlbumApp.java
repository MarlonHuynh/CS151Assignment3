/**
 * PhotoAlbumApp.java
 *
 * Description: Creates a photo album app using the MVC pattern and implementing Strategy pattern.
 *
 * @author  Marlon Huynh
 * @version 1.0, 3/29/2025
 */

public class PhotoAlbumApp {
    public static void main(String[] args) {
        PhotoAlbumModel model = new PhotoAlbumModel();
        PhotoAlbumView view = new PhotoAlbumView(model);
        PhotoAlbumController controller = new PhotoAlbumController(model, view);
        view.setVisible(true);
    }
}