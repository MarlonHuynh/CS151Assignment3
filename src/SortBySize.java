/**
 * SortBySize.java
 *
 * Description: Sorting by size sorting strategy
 *
 * @author  Marlon Huynh
 * @version 1.0, 3/29/2025
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public class SortBySize implements SortingStrategy {
    /**
     * Sorts the album by size and return the sorted album
     *
     * @return the sorted album
     */
    @Override
    public ArrayList<Photo> sort(List<Photo> old) {
        ArrayList <Photo> sortedPhotos = new ArrayList<>(old);
        sortedPhotos.sort(Comparator.comparing(Photo::getFileSize));
        return sortedPhotos;
    }
}