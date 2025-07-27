import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A Java Swing GUI application to determine loan eligibility.
 * This program provides a graphical interface for the user to enter their details.
 */
class LoanEligibilityGUI extends JFrame {

    // --- Eligibility Criteria Constants ---
    private static final int MINIMUM_AGE = 21;
    private static final double MINIMUM_INCOME = 20000.00;
    private static final int MINIMUM_CREDIT_SCORE = 650;

    // --- GUI Components ---
    private JTextField ageField;
    private JTextField incomeField;
    private JTextField creditScoreField;
    private JLabel resultLabel; // To display the final outcome.

    /**
     * Constructor to set up the GUI.
     */
    public LoanEligibilityGUI() {
        // --- Frame Setup ---
        setTitle("Loan Eligibility Checker");
        setSize(400, 250); // Set window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit on close
        setLocationRelativeTo(null); // Center the window on the screen
        setLayout(new BorderLayout(10, 10)); // Use BorderLayout with gaps

        // --- Input Panel ---
        // A panel to hold the labels and text fields for user input.
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5)); // 3 rows, 2 columns, with gaps
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // 1. Age Input
        inputPanel.add(new JLabel("Enter your current age:"));
        ageField = new JTextField();
        inputPanel.add(ageField);

        // 2. Income Input
        inputPanel.add(new JLabel("Enter your total annual income:"));
        incomeField = new JTextField();
        inputPanel.add(incomeField);

        // 3. Credit Score Input
        inputPanel.add(new JLabel("Enter your credit score:"));
        creditScoreField = new JTextField();
        inputPanel.add(creditScoreField);

        // --- Button Panel ---
        // A panel for the action button.
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton checkButton = new JButton("Check Eligibility");
        buttonPanel.add(checkButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));


        // --- Add panels to the frame ---
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Add Action Listener to the Button ---
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkEligibility();
            }
        });
    }

    /**
     * This method contains the logic to check for loan eligibility.
     * It is called when the "Check Eligibility" button is clicked.
     */
    private void checkEligibility() {
        int userAge;
        double userIncome;
        int userCreditScore;

        // --- Parse User Input with Error Handling ---
        try {
            userAge = Integer.parseInt(ageField.getText());
            userIncome = Double.parseDouble(incomeField.getText());
            userCreditScore = Integer.parseInt(creditScoreField.getText());
        } catch (NumberFormatException ex) {
            // Show an error message if the input is not a valid number.
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return; // Stop further execution
        }

        // --- Check Eligibility Logic ---
        boolean isEligible = true;
        StringBuilder reasons = new StringBuilder("<html>We're sorry, but you are not eligible for a loan at this time.<br><br><b>Reasons:</b><br>");

        if (userAge < MINIMUM_AGE) {
            isEligible = false;
            reasons.append(" - You must be at least ").append(MINIMUM_AGE).append(" years old.<br>");
        }

        if (userIncome < MINIMUM_INCOME) {
            isEligible = false;
            reasons.append(" - Your annual income must be at least $").append(String.format("%,.2f", MINIMUM_INCOME)).append(".<br>");
        }

        if (userCreditScore < MINIMUM_CREDIT_SCORE) {
            isEligible = false;
            reasons.append(" - Your credit score must be at least ").append(MINIMUM_CREDIT_SCORE).append(".<br>");
        }
        reasons.append("</html>");

        // --- Display Result in a JOptionPane ---
        if (isEligible) {
            JOptionPane.showMessageDialog(this, "Congratulations! You are eligible for a loan.", "Eligibility Result", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, reasons.toString(), "Eligibility Result", JOptionPane.WARNING_MESSAGE);
        }
    }


    /**
     * The main method to run the application.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // It's best practice to create and show the GUI on the Event Dispatch Thread (EDT).
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoanEligibilityGUI().setVisible(true);
            }
        });
    }
}
