import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Iterator;

public class PhotoAlbumModel {
    private List<Photo> photos;

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

    public List<Photo> getPhotos() {
        return photos;
    }

    public Photo getPhoto(int i){
        if (i < photos.size()){
            return photos.get(i);
        }
        else{
            return photos.get(0);
        }
    }
    public void removePhoto(String photo) {

    }
}
