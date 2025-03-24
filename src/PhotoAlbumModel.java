/**
 * PhotoAlbumModel.java
 *
 * Description: Stores and manipulates the photo album.
 *
 * @author  Marlon Huynh
 * @version 1.0, 3/29/2025
 */

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class PhotoAlbumModel implements AlbumIterator {
    private ArrayList<Photo> album;
    private int index = 0;
    /**
     * Constructor
     */
    public PhotoAlbumModel() {
        album = new ArrayList<>();
    }
    /**
     * Adds a specified photo to the album
     *
     * @param photoPath the path of the Photo to add
     */
    public void addPhoto(String photoPath){ // Assumes a valid photoPath
        File file = new File(photoPath);
        String name = file.getName();
        ImageIcon imageIcon = new ImageIcon(photoPath);
        Date lastModified = new Date();
        long fileSizeInBytes = file.length();
        Photo newPhoto = new Photo(name, photoPath, lastModified, fileSizeInBytes);
        album.add(newPhoto);
    }
    /**
     * Adds a specified photo to the album
     *
     * @param p the Photo to add
     */
    public void addPhoto(Photo p){
        album.add(p);
    }
    /**
     * Removes a specified photo of album
     *
     * @param s the name of the Photo
     * @return a boolean if its true or not whether the photo has been found and removed
     */
    public boolean removePhoto(String s) {
        boolean found = false;
        int foundIndex = 0;
        for (int i = 0; i < album.size(); i++) {
            // Check if the photo name matches the provided photo name
            if (album.get(i).getName().equals(s)) {
                album.remove(i);
                found = true;
                foundIndex = 0;
                break;  // Once we find and remove, we exit the loop
            }
        }
        if (found) {
            // Decrement the index if found to account for taking a photo out
            if (foundIndex == album.size() - 1) {
                index--;
                if (index < 0) {
                    index = 0;
                }
            }
            album.trimToSize();
        }
        return found;
    }
    /**
     * Sets the album to a specified album
     *
     * @param a The album to set to
     */
    public void setAlbum(ArrayList<Photo> a){
        album = a;
    }
    /**
     * Returns the album
     *
     * @return ArrayList of Photos of the album
     */
    public ArrayList<Photo> getAlbum(){
        return album;
    }
    /**
     * Returns the album's index
     *
     * @return int of the index
     */
    public int getIndex(){
        return index;
    }
    // --- Interface Implementation ---
    /**
     * Returns next index in the album
     *
     * @return a boolean whether it's true or not there is a next index in the album
     */
    public boolean hasNext() {
        return index < album.size() - 1;
    }
    /**
     * Returns previous index in the album
     *
     * @return a boolean whether it's true or not there is a previous index in the album
     */
    public boolean hasPrevious() {
        return index > 0;
    }
    /**
     * Returns current photo
     *
     * @return the current photo of the album
     */
    public Photo current() {
        return album.get(index);
    }
    /**
     * Returns the next photo
     *
     * @return the next photo of the album
     */
    public Photo next() {
        index ++;
        return album.get(index);
    }
    /**
     * Returns the previous photo
     *
     * @return the previous photo of the album
     */
    public Photo previous() {
        index --;
        return album.get(index);
    }
}
