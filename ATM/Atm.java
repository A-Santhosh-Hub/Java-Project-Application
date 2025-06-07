import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
class ATMGui extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private double balance = 1000.0;
    private final int correctPin = 1234;

    public ATMGui() {
        setTitle("Simple ATM Interface");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        addLoginPanel();
        addMenuPanel();

        add(mainPanel);
        setVisible(true);
    }

    private void addLoginPanel() {
        JPanel loginPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        JLabel pinLabel = new JLabel("Enter 4-digit PIN:");
        JPasswordField pinField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        loginPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        loginPanel.add(pinLabel);
        loginPanel.add(pinField);
        loginPanel.add(loginButton);

        loginButton.addActionListener(e -> {
            String pinText = new String(pinField.getPassword());
            try {
                int enteredPin = Integer.parseInt(pinText);
                if (enteredPin == correctPin) {
                    cardLayout.show(mainPanel, "menu");
                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect PIN!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        mainPanel.add(loginPanel, "login");
    }

    private void addMenuPanel() {
        JPanel menuPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JButton checkBalanceBtn = new JButton("Check Balance");
        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton logoutBtn = new JButton("Logout");

        checkBalanceBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, String.format("Your balance is ₹%.2f", balance), "Balance", JOptionPane.INFORMATION_MESSAGE);
        });

        depositBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Enter amount to deposit:");
            try {
                double amount = Double.parseDouble(input);
                if (amount > 0) {
                    balance += amount;
                    JOptionPane.showMessageDialog(this, "Deposit successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Enter a valid amount!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        withdrawBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
            try {
                double amount = Double.parseDouble(input);
                if (amount > 0 && amount <= balance) {
                    balance -= amount;
                    JOptionPane.showMessageDialog(this, "Withdrawal successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid amount or insufficient balance!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        logoutBtn.addActionListener(e -> {
            cardLayout.show(mainPanel, "login");
        });

        menuPanel.add(checkBalanceBtn);
        menuPanel.add(depositBtn);
        menuPanel.add(withdrawBtn);
        menuPanel.add(logoutBtn);

        mainPanel.add(menuPanel, "menu");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ATMGui::new);
    }
}
