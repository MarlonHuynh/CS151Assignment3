import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public class SortBySize implements SortingStrategy {
    @Override
    public ArrayList<Photo> sort(List<Photo> old) {
        ArrayList <Photo> sortedPhotos = new ArrayList<>(old);
        sortedPhotos.sort(Comparator.comparing(Photo::getFileSize));
        return sortedPhotos;
    }
}