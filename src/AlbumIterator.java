/**
 * AlbumIterator.java
 * Description: Interface for the album iterator
 *
 * @author  Marlon Huynh
 * @version 1.0, 3/29/2025
 */

public interface AlbumIterator {
    boolean hasNext();      // to check if there is a next element
    boolean hasPrevious();  // to check if there is a previous element
    Photo current();        // to get the photo at the current position
    Photo next();           // to advance the iterator to the next position
    Photo previous();       // to advance the iterator to the previous position
}