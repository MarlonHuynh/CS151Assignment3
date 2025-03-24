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
    private ArrayList<Photo> photos;
    private int index = 0;
    public PhotoAlbumModel() {
        photos = new ArrayList<>();
    }
    public void addPhoto(String photoPath){ // Assumes a valid photoPath
        File file = new File(photoPath);
        String name = file.getName();
        ImageIcon imageIcon = new ImageIcon(photoPath);
        Date lastModified = new Date();
        long fileSizeInBytes = file.length();
        Photo newPhoto = new Photo(name, photoPath, lastModified, fileSizeInBytes);
        photos.add(newPhoto);
    }
    public void addPhoto(Photo p){
        photos.add(p);
    }
    public boolean removePhoto(String s) {
        boolean found = false;
        int foundIndex = 0;
        for (int i = 0; i < photos.size(); i++) {
            // Check if the photo name matches the provided photo name
            if (photos.get(i).getName().equals(s)) {
                photos.remove(i);
                found = true;
                foundIndex = 0;
                break;  // Once we find and remove, we exit the loop
            }
        }
        if (foundIndex == photos.size() - 1){
            index --;
            if (index < 0){
                index = 0;
            }
        }
        if (found) {
            photos.trimToSize();
        }
        return found;
    }
    public void setPhotos(ArrayList<Photo> p){
        photos = p;
    }
    public ArrayList<Photo> getPhotos(){
        return photos;
    }
    public int getIndex(){
        return index;
    }
    // --- Interface Implementation ---
    public boolean hasNext() {
        return index < photos.size() - 1;
    }
    public boolean hasPrevious() {
        return index > 0;
    }
    public Photo current() {
        return photos.get(index);
    }
    public Photo next() {
        index ++;
        return photos.get(index);
    }
    public Photo previous() {
        index --;
        return photos.get(index);
    }
}
