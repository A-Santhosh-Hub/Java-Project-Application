import javax.swing.*;
import java.awt.event.*;
import java.io.FileWriter;

class FencingWorkCalculatorGUI {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Fencing Work Calculator");
        frame.setSize(400, 450);
        frame.setLayout(null);

        JLabel stoneLabel = new JLabel("Select Stone Height:");
        stoneLabel.setBounds(20, 20, 200, 25);
        frame.add(stoneLabel);

        String[] stoneOptions = {"6ft - ₹300", "7ft - ₹400", "8ft - ₹500"};
        JComboBox<String> stoneDropdown = new JComboBox<>(stoneOptions);
        stoneDropdown.setBounds(180, 20, 180, 25);
        frame.add(stoneDropdown);

        JLabel quantityLabel = new JLabel("Number of Stones:");
        quantityLabel.setBounds(20, 60, 200, 25);
        frame.add(quantityLabel);

        JTextField quantityField = new JTextField();
        quantityField.setBounds(180, 60, 180, 25);
        frame.add(quantityField);

        JLabel chainHeightLabel = new JLabel("Chain Height (5/6 ft):");
        chainHeightLabel.setBounds(20, 100, 200, 25);
        frame.add(chainHeightLabel);

        JTextField chainHeightField = new JTextField();
        chainHeightField.setBounds(180, 100, 180, 25);
        frame.add(chainHeightField);

        JLabel chainLengthLabel = new JLabel("Chain Length (ft):");
        chainLengthLabel.setBounds(20, 140, 200, 25);
        frame.add(chainLengthLabel);

        JTextField chainLengthField = new JTextField();
        chainLengthField.setBounds(180, 140, 180, 25);
        frame.add(chainLengthField);

        JTextArea resultArea = new JTextArea();
        resultArea.setBounds(20, 220, 340, 140);
        resultArea.setEditable(false);
        frame.add(resultArea);

        JButton calcBtn = new JButton("Calculate");
        calcBtn.setBounds(20, 180, 160, 30);
        frame.add(calcBtn);

        JButton saveBtn = new JButton("Save as Text File");
        saveBtn.setBounds(200, 180, 160, 30);
        frame.add(saveBtn);

        calcBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] stonePrices = {300, 400, 500};
                int[] chainRates = {20, 25};

                int stoneIndex = stoneDropdown.getSelectedIndex();
                int stonePrice = stonePrices[stoneIndex];
                String stoneType = stoneOptions[stoneIndex].split(" ")[0];

                int quantity = Integer.parseInt(quantityField.getText());
                int totalStone = stonePrice * quantity;

                int chainHeight = Integer.parseInt(chainHeightField.getText());
                int chainLength = Integer.parseInt(chainLengthField.getText());

                int chainRate = (chainHeight == 5) ? chainRates[0] :
                        (chainHeight == 6) ? chainRates[1] : 0;

                if (chainRate == 0) {
                    resultArea.setText("Invalid chain height. Enter 5 or 6.");
                    return;
                }

                int chainArea = chainHeight * chainLength;
                int chainCost = chainArea * chainRate;
                int totalCost = totalStone + chainCost;

                String result = "Stone: " + stoneType + "\n" +
                        "Qty: " + quantity + " ➜ ₹" + totalStone + "\n" +
                        "Chain: " + chainHeight + "ft x " + chainLength + "ft ➜ ₹" + chainCost + "\n" +
                        "--------------------------------\n" +
                        "Total: ₹" + totalCost;

                resultArea.setText(result);
            }
        });

        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    FileWriter fw = new FileWriter("fencing_estimate_gui.txt");
                    fw.write(resultArea.getText());
                    fw.close();
                    JOptionPane.showMessageDialog(frame, "Saved to fencing_estimate_gui.txt");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                }
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
