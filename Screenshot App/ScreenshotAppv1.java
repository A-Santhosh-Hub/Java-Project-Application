import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotApp extends JFrame {

    private JButton captureAreaButton;
    private JButton captureFullButton;
    private JButton autoCaptureButton;
    private volatile boolean autoCapturing = false;

    public ScreenshotApp() {
        setTitle("Screenshot Capturer");
        setSize(400, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        captureAreaButton = new JButton("📸 Capture Selected Area");
        captureFullButton = new JButton("🖥️ Capture Full Screen");
        autoCaptureButton = new JButton("🔁 Auto Capture (X sec)");

        captureAreaButton.addActionListener(e -> captureWithDelay(true));
        captureFullButton.addActionListener(e -> captureWithDelay(false));
        autoCaptureButton.addActionListener(e -> startAutoCapture());

        add(captureAreaButton);
        add(captureFullButton);
        add(autoCaptureButton);
    }

    private void captureWithDelay(boolean selectArea) {
        new Thread(() -> {
            try {
                JOptionPane.showMessageDialog(this, "Capturing in 3 seconds...");
                Thread.sleep(3000);
                Rectangle area = selectArea ? selectCaptureArea() : getFullScreenBounds();
                if (area != null) {
                    BufferedImage image = capture(area);
                    previewAndSave(image);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }).start();
    }

    private Rectangle getFullScreenBounds() {
        return new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
    }

    private BufferedImage capture(Rectangle area) throws Exception {
        Robot robot = new Robot();
        return robot.createScreenCapture(area);
    }

    private void previewAndSave(BufferedImage image) throws IOException {
        // Preview Image in dialog
        ImageIcon icon = new ImageIcon(image.getScaledInstance(600, -1, Image.SCALE_SMOOTH));
        int option = JOptionPane.showConfirmDialog(this, new JLabel(icon), "Preview Screenshot",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Screenshot");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            File defaultFile = new File("screenshot_" + sdf.format(new Date()) + ".png");
            fileChooser.setSelectedFile(defaultFile);

            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                ImageIO.write(image, "png", fileChooser.getSelectedFile());
                JOptionPane.showMessageDialog(this, "Saved to:\n" + fileChooser.getSelectedFile().getAbsolutePath());
            }
        }
    }

    private Rectangle selectCaptureArea() throws Exception {
        JWindow window = new JWindow();
        window.setAlwaysOnTop(true);
        window.setOpacity(0.4f);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setBounds(0, 0, screenSize.width, screenSize.height);

        Point[] points = new Point[2];

        window.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                points[0] = e.getPoint();
            }

            public void mouseReleased(MouseEvent e) {
                points[1] = e.getPoint();
                window.setVisible(false);
                window.dispose();
            }
        });

        window.setVisible(true);

        while (points[1] == null) {
            Thread.sleep(100);
        }

        int x = Math.min(points[0].x, points[1].x);
        int y = Math.min(points[0].y, points[1].y);
        int w = Math.abs(points[0].x - points[1].x);
        int h = Math.abs(points[0].y - points[1].y);
        return new Rectangle(x, y, w, h);
    }

    private void startAutoCapture() {
        String input = JOptionPane.showInputDialog(this, "Enter interval in seconds:");
        if (input == null) return;

        try {
            int seconds = Integer.parseInt(input);
            autoCapturing = true;
            JOptionPane.showMessageDialog(this, "Auto-capturing started! It will run in background.\nClick OK to stop.");

            new Thread(() -> {
                try {
                    while (autoCapturing) {
                        BufferedImage img = capture(getFullScreenBounds());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                        File file = new File("auto_screenshot_" + sdf.format(new Date()) + ".png");
                        ImageIO.write(img, "png", file);
                        System.out.println("Saved: " + file.getName());
                        Thread.sleep(seconds * 1000);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }).start();

            // Stop auto capture after dialog OK
            JOptionPane.showMessageDialog(this, "Auto-capturing paused.");
            autoCapturing = false;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ScreenshotApp app = new ScreenshotApp();
            app.setVisible(true);
        });
    }
}
