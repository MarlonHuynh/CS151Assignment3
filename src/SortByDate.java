import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public class SortByDate implements SortingStrategy {
    @Override
    public List<Photo> sort(List<Photo> old) {
        List <Photo> sortedPhotos = new ArrayList<>(old);
        sortedPhotos.sort(Comparator.comparing(Photo::getDateAdded));
        return sortedPhotos;
    }
}