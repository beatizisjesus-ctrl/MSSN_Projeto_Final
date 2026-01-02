package Apps;
import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
    	java.net.URL imgURL = getClass().getResource(imagePath);
    	if (imgURL != null) {
            backgroundImage = new ImageIcon(imgURL).getImage();
        } else {
            System.out.println("Imagem não encontrada: " + imagePath);
        }
        setLayout(new BorderLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
