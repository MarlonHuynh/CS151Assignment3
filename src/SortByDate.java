/**
 * SortByDate.java
 *
 * Description: Sorting by date sorting strategy
 *
 * @author  Marlon Huynh
 * @version 1.0, 3/29/2025
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public class SortByDate implements SortingStrategy {
    /**
     * Sorts the album by date and return the sorted album
     *
     * @return the sorted album
     */
    @Override
    public ArrayList<Photo> sort(List<Photo> old) {
        ArrayList <Photo> sortedPhotos = new ArrayList<>(old);
        sortedPhotos.sort(Comparator.comparing(Photo::getDateAdded));
        return sortedPhotos;
    }
}