import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public class SortByName implements SortingStrategy {
    @Override
    public List<Photo> sort(List<Photo> old) {
        List <Photo> sortedPhotos = new ArrayList<>(old);
        sortedPhotos.sort(Comparator.comparing(Photo::getName));
        return sortedPhotos;
    }
}