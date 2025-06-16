import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

class MovieTicketBookingGUI extends JFrame {

    private JComboBox<String> movieDropdown, timeDropdown;
    private JTextField nameField, seatField;
    private JTextArea outputArea;
    private JButton bookButton;

    private final String fileName = "bookings.txt";
    private Set<String> bookedSeats = new HashSet<>();

    private final String[] movies = {"Inception", "Interstellar", "Joker"};
    private final String[] times = {"10:00 AM", "1:00 PM", "6:00 PM"};

    public MovieTicketBookingGUI() {
        setTitle("🎬 Movie Ticket Booking");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(7, 1, 10, 5));

        loadBookedSeats();

        // UI Components
        nameField = new JTextField();
        seatField = new JTextField();
        movieDropdown = new JComboBox<>(movies);
        timeDropdown = new JComboBox<>(times);
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        bookButton = new JButton("Book Ticket");

        add(new JLabel("Enter Name:"));
        add(nameField);
        add(new JLabel("Select Movie:"));
        add(movieDropdown);
        add(new JLabel("Select Showtime:"));
        add(timeDropdown);
        add(new JLabel("Seat (e.g., A1):"));
        add(seatField);
        add(bookButton);
        add(new JScrollPane(outputArea));

        bookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bookTicket();
            }
        });

        setVisible(true);
    }

    private void bookTicket() {
        String name = nameField.getText().trim();
        String movie = (String) movieDropdown.getSelectedItem();
        String time = (String) timeDropdown.getSelectedItem();
        String seat = seatField.getText().trim().toUpperCase();

        if (name.isEmpty() || seat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "⚠️ Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (bookedSeats.contains(seat)) {
            JOptionPane.showMessageDialog(this, "❌ Seat already booked. Choose another one.", "Seat Taken", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            saveBooking(name, movie, time, seat);
            bookedSeats.add(seat);

            outputArea.setText("✅ Booking Confirmed!\n"
                    + "Name: " + name + "\n"
                    + "Movie: " + movie + "\n"
                    + "Time: " + time + "\n"
                    + "Seat: " + seat);

            nameField.setText("");
            seatField.setText("");

        } catch (IOException ex) {
            outputArea.setText("❌ Error saving booking.");
        }
    }

    private void saveBooking(String name, String movie, String time, String seat) throws IOException {
        FileWriter fw = new FileWriter(fileName, true);
        fw.write(name + "," + movie + "," + time + "," + seat + "\n");
        fw.close();
    }

    private void loadBookedSeats() {
        File file = new File(fileName);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    bookedSeats.add(data[3].toUpperCase());
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load previous bookings.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MovieTicketBookingGUI());
    }
}
