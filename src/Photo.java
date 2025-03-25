/**
 * Photo.java
 * Description: Definition of the Photo object, each with a name, file path, date added, and file size.
 *
 * @author  Marlon Huynh
 * @version 1.0, 3/29/2025
 */

import java.util.Date;

public class Photo {
    private String name;
    private String filePath;
    private Date dateAdded;
    private long fileSize; // In bytes
    // Constructor, getters, and setters
    public Photo(String n, String f, Date d, long s) {
        name = n;
        filePath = f;
        dateAdded = d;
        fileSize = s;
    }
    // Getters
    public String getName() { return name; }
    public String getFilePath() { return filePath; }
    public Date getDateAdded() { return dateAdded; }
    public long getFileSize() { return fileSize; }
    // Setters
    public void setName(String n) { name = n; }
    public void setFilePath(String f) { filePath = f; }
    public void setDateAdded(Date d) { dateAdded = d; }
    public void setFileSize(long s) { fileSize = s; }
}
