import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class PhotoAlbumModel {
    private List<String> photos;

    public PhotoAlbumModel() { photos = new ArrayList<>();  }
    public List<String> getPhotos() { return photos; }
    public void addPhoto(String photo) { photos.add(photo); }
    public void removePhoto(String photo) { photos.remove(photo); }

    // Iterator implementation
    public Iterator<String> iterator() { return new PhotoAlbumIterator(); }
    private class PhotoAlbumIterator implements Iterator<String> {
        private int index = 0; // Specific to each iterator!
        @Override
        public boolean hasNext() {
            return index < photos.size();
        }
        @Override
        public String next() {
            if (hasNext()) {
                return photos.get(index++);
            }
            throw new IndexOutOfBoundsException("No more photos.");
        }
    }
}
