/**
 * AlbumIterator.java
 * Description: Interface for the album iterator
 *
 * @author  Marlon Huynh
 * @version 1.0, 3/29/2025
 */

public interface AlbumIterator {
    boolean hasNext();      // check if there is a next element
    boolean hasPrevious();  // check if there is a previous element
    Photo current();        // get the Photo at the current position
    Photo next();           // get the Photo at the next position
    Photo previous();       // get the Photo at the previous position
}