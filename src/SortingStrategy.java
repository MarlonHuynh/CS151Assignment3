/**
 * SortingStrategy.java
 * Description: Sorting strategy interface
 *
 * @author  Marlon Huynh
 * @version 1.0, 3/29/2025
 */

import java.util.ArrayList;
import java.util.List;

public interface SortingStrategy {
    ArrayList<Photo> sort(List<Photo> photos);
}
