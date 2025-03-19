import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class PhotoAlbumView extends JFrame{
    JButton addBtn, delBtn, nextBtn, prevBtn, nameBtn, dateBtn, sizeBtn, exitBtn;
    int buttonPanelHeight = 100; // Height of the button panel
    int imageHeight;

    public PhotoAlbumView(){ drawScreen(); }

    public void drawScreen(){
        // App
        setLayout(new BorderLayout());
        setTitle("Marlon's Super Cool Photo Album App");
        ImageIcon appIcon = new ImageIcon("src/IconImage.PNG");
        setIconImage(appIcon.getImage());
        // Define panels
        JPanel imagePanel = new JPanel();
        JPanel btnPanel = new JPanel();
        // Display the main frame
        setSize(1000, 1000);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Set Image
        imageHeight = getHeight() - buttonPanelHeight;
        ImageIcon mainImage = new ImageIcon("src/IconImage.PNG");
        Image img = mainImage.getImage();
        Image resizedImage = img.getScaledInstance(imageHeight, imageHeight, Image.SCALE_SMOOTH); // Resizing image to square
        JLabel imageLabel = new JLabel(new ImageIcon(resizedImage));
        imagePanel.add(imageLabel);
        add(imagePanel, BorderLayout.EAST);
        // Set Buttons
        btnPanel.setLayout(new GridLayout(2, 4));
        btnPanel.setPreferredSize(new Dimension(getWidth(), buttonPanelHeight));
        add(btnPanel, BorderLayout.SOUTH);
        // Buttons with different actions
        addBtn = new JButton("Add");
        delBtn = new JButton("Delete");
        prevBtn = new JButton("Prev");
        nextBtn = new JButton("Next");
        nameBtn = new JButton("Sort by Name");
        dateBtn = new JButton("Sort by Date");
        sizeBtn = new JButton("Sort by Size");
        exitBtn = new JButton("Exit");
        // Set Colors
        setButtonColors(addBtn);
        setButtonColors(delBtn);
        setButtonColors(nextBtn);
        setButtonColors(prevBtn);
        setButtonColors(nameBtn);
        setButtonColors(dateBtn);
        setButtonColors(sizeBtn);
        setButtonColors(exitBtn);
        // Add buttons to the panel
        btnPanel.add(addBtn);
        btnPanel.add(nextBtn);
        btnPanel.add(prevBtn);
        btnPanel.add(delBtn);
        btnPanel.add(nameBtn);
        btnPanel.add(dateBtn);
        btnPanel.add(sizeBtn);
        btnPanel.add(exitBtn);
        // Add a component listener to handle resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                imageHeight = getHeight() - buttonPanelHeight;
                Image img = mainImage.getImage();
                Image resizedImage = img.getScaledInstance(imageHeight, imageHeight, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(resizedImage)); // Create new image icon
                // Destroys previous image and replace with new image
                imagePanel.removeAll();     // Clear any previous images
                imagePanel.add(imageLabel);
                imagePanel.revalidate();    // Refresh the panel
                imagePanel.repaint();       // Redraw the panel with the new image
            }
        });
    }
    // Set button colors for different states
    private void setButtonColors(JButton button, Color text, Color normal, Color rollover, Color pressed, Color released) {
        button.setBackground(normal);
        button.setForeground(text);
        button.setFocusPainted(false);
        button.setRolloverEnabled(true);
        button.setRolloverIcon(new ColorIcon(rollover));
        button.setPressedIcon(new ColorIcon(pressed));
        button.setDisabledIcon(new ColorIcon(released));
    }
    // Set button colors for different states
    private void setButtonColors(JButton button) {
        Font buttonFont = new Font("Arial", Font.BOLD, 25);
        button.setFont(buttonFont);
        button.setBackground(Color.BLACK);  // Default background color
        button.setForeground(Color.WHITE);  // Text color
        button.setFocusPainted(false);  // Remove focus border
        button.setFocusable(false);  // Disable focus behavior
        button.setContentAreaFilled(false);  // Enable default button press effects
        button.setRolloverEnabled(true);  // Enable rollover effect
        button.setOpaque(true);
        button.setBorder(new EmptyBorder(10, 25, 10, 25));
        // Override up a MouseListener to handle color changes
        button.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { button.setBackground(Color.decode("#45001f")); } // Hover color
            @Override public void mouseExited(MouseEvent e) { button.setBackground(Color.BLACK); } // Color when exiting
            @Override public void mousePressed(MouseEvent e) { button.setBackground(Color.decode("#45001f").brighter()); } // Color when pressed
            @Override public void mouseReleased(MouseEvent e) { button.setBackground(Color.decode("#45001f")); } // Color after release
            @Override public void mouseClicked(MouseEvent e) { button.setBackground(Color.decode("#45001f").darker()); }
        });
    }
    public void addExitBtnListener(ActionListener listener) {
        exitBtn.addActionListener(e -> {
            System.out.println("DEBUG: Exit button clicked!");
            listener.actionPerformed(e);
        });
    }
    // Custom class for creating icons with a solid color
    class ColorIcon implements Icon {
        private Color color;
        public ColorIcon(Color color) { this.color = color; }
        @Override public void paintIcon(Component c, Graphics g, int x, int y) { g.setColor(color); g.fillRect(x, y, getIconWidth(), getIconHeight()); }
        @Override public int getIconWidth() { return 100; }
        @Override public int getIconHeight() { return 30; }
    }
}
