import java.util.ArrayList;
import java.util.List;

public class PhotoAlbumModel {
    private List<String> photos;
    public PhotoAlbumModel() {
        photos = new ArrayList<>();
    }
    public List<String> getPhotos() { return photos; }
    public void addPhoto(String photo) { photos.add(photo); }
    public void removePhoto(String photo) { photos.remove(photo); }
}
