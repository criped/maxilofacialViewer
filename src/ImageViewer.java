import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.mortennobel.imagescaling.ResampleOp;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ImageViewer extends JFrame implements ActionListener {
  public ImageViewer() {
    setTitle("ImageViewer");
    setSize(500, 500);
    
    zoom = 1.0; //zoom factor
    image = null;
    
    JMenuBar mbar = new JMenuBar();
    JMenu m = new JMenu("File");
    openItem = new JMenuItem("Open");
    openItem.addActionListener(this);
    m.add(openItem);
    exitItem = new JMenuItem("Exit");
    exitItem.addActionListener(this);
    m.add(exitItem);
    mbar.add(m);
    setJMenuBar(mbar);
        
    label = new JLabel();
    jSP = new JScrollPane(label);
    
    getContentPane().add(jSP, BorderLayout.CENTER);
    //setSize(300, 250);
    setVisible(true);
    
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    
    panel.addMouseWheelListener(new MouseWheelListener() {
        public void mouseWheelMoved(MouseWheelEvent e) {
            int notches = e.getWheelRotation();
            double temp = zoom - (notches * 0.2);
            // minimum zoom factor is 1.0
            temp = Math.max(temp, 1.0);
            if (temp != zoom) {
                zoom = temp;
                resizeImage();
            }
        }
    });
    
    jSP.setViewportView(panel);
    
    panel.add(label, BorderLayout.CENTER);
  }
  
  public void resizeImage() {
//      System.out.println(zoom);
      ResampleOp resampleOp = new ResampleOp((int)(image.getWidth()*zoom), (int)(image.getHeight()*zoom));
      BufferedImage resizedIcon = resampleOp.filter(image, null);
      Icon imageIcon = new ImageIcon(resizedIcon);
      label.setIcon(imageIcon);
   }

  public void actionPerformed(ActionEvent evt) {
    Object source = evt.getSource();
    if (source == openItem) {
      JFileChooser chooser = new JFileChooser();
      chooser.setCurrentDirectory(new File("."));

      chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
        public boolean accept(File f) {
          return f.getName().toLowerCase().endsWith(".jpg")
              || f.isDirectory();
        }

        public String getDescription() {
          return "JPG Images";
        }
      });

      int r = chooser.showOpenDialog(this);
      if (r == JFileChooser.APPROVE_OPTION) {
        String name = chooser.getSelectedFile().getName();
        try {
			image = ImageIO.read(new File(chooser.getSelectedFile().toString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
        label.setIcon(new ImageIcon(chooser.getSelectedFile().toString()));
        label.setHorizontalAlignment(JLabel.CENTER);

      }
    } else if (source == exitItem)
      System.exit(0);
  }
  

  public static void main(String[] args) {
    JFrame frame = new ImageViewer();
    frame.show();
  }

  private JLabel label;
  
  private JScrollPane jSP;

  private JMenuItem openItem;

  private JMenuItem exitItem;
  
  private javax.swing.JPanel jPanel1;
  
  private double zoom;
  
  private BufferedImage image;

  
}
