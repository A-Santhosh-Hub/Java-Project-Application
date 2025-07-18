import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

class LoanEMICalculator extends Application {

    TextField amountField, rateField, termField;
    Label emiLabel, totalInterestLabel, totalPaymentLabel;
    TableView<AmortizationRow> table;
    PieChart pieChart;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Loan EMI Calculator");

        // Input Fields
        amountField = new TextField();
        rateField = new TextField();
        termField = new TextField();
        amountField.setPromptText("Loan Amount");
        rateField.setPromptText("Annual Interest Rate (%)");
        termField.setPromptText("Loan Term (Years)");

        Button calculateButton = new Button("Calculate");
        calculateButton.setOnAction(e -> calculateEMI());

        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.setPadding(new Insets(20));
        inputGrid.add(new Label("Loan Amount:"), 0, 0);
        inputGrid.add(amountField, 1, 0);
        inputGrid.add(new Label("Annual Interest Rate (%):"), 0, 1);
        inputGrid.add(rateField, 1, 1);
        inputGrid.add(new Label("Loan Term (Years):"), 0, 2);
        inputGrid.add(termField, 1, 2);
        inputGrid.add(calculateButton, 1, 3);

        // Output Labels
        emiLabel = new Label("Monthly EMI: ₹0");
        totalInterestLabel = new Label("Total Interest: ₹0");
        totalPaymentLabel = new Label("Total Payment: ₹0");

        VBox outputBox = new VBox(10, emiLabel, totalInterestLabel, totalPaymentLabel);
        outputBox.setPadding(new Insets(20));

        // Pie Chart
        pieChart = new PieChart();
        pieChart.setTitle("Principal vs Interest");

        // Amortization Table
        table = new TableView<>();
        TableColumn<AmortizationRow, Integer> monthCol = new TableColumn<>("Month");
        TableColumn<AmortizationRow, Double> emiCol = new TableColumn<>("EMI");
        TableColumn<AmortizationRow, Double> principalCol = new TableColumn<>("Principal");
        TableColumn<AmortizationRow, Double> interestCol = new TableColumn<>("Interest");
        TableColumn<AmortizationRow, Double> balanceCol = new TableColumn<>("Balance");

        monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
        emiCol.setCellValueFactory(new PropertyValueFactory<>("emi"));
        principalCol.setCellValueFactory(new PropertyValueFactory<>("principal"));
        interestCol.setCellValueFactory(new PropertyValueFactory<>("interest"));
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));

        table.getColumns().addAll(monthCol, emiCol, principalCol, interestCol, balanceCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox mainBox = new VBox(10, inputGrid, outputBox, pieChart, new Label("Amortization Schedule:"), table);
        mainBox.setPadding(new Insets(20));
        ScrollPane scrollPane = new ScrollPane(mainBox);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 900, 700);
        stage.setScene(scene);
        stage.show();
    }

    private void calculateEMI() {
        try {
            double principal = Double.parseDouble(amountField.getText());
            double annualRate = Double.parseDouble(rateField.getText());
            int years = Integer.parseInt(termField.getText());

            double monthlyRate = annualRate / 12 / 100;
            int months = years * 12;

            double emi = (principal * monthlyRate * Math.pow(1 + monthlyRate, months)) /
                    (Math.pow(1 + monthlyRate, months) - 1);
            double totalPayment = emi * months;
            double totalInterest = totalPayment - principal;

            emiLabel.setText(String.format("Monthly EMI: ₹%.2f", emi));
            totalInterestLabel.setText(String.format("Total Interest: ₹%.2f", totalInterest));
            totalPaymentLabel.setText(String.format("Total Payment: ₹%.2f", totalPayment));

            // Pie Chart Update
            ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                    new PieChart.Data("Principal", principal),
                    new PieChart.Data("Interest", totalInterest)
            );
            pieChart.setData(pieData);

            // Amortization Schedule
            ObservableList<AmortizationRow> rows = FXCollections.observableArrayList();
            double balance = principal;
            for (int i = 1; i <= months; i++) {
                double interest = balance * monthlyRate;
                double principalPart = emi - interest;
                balance -= principalPart;
                rows.add(new AmortizationRow(i, emi, principalPart, interest, Math.max(balance, 0)));
            }
            table.setItems(rows);

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Please enter valid numeric values.");
            alert.showAndWait();
        }
    }

    public static class AmortizationRow {
        private final int month;
        private final double emi;
        private final double principal;
        private final double interest;
        private final double balance;

        public AmortizationRow(int month, double emi, double principal, double interest, double balance) {
            this.month = month;
            this.emi = emi;
            this.principal = principal;
            this.interest = interest;
            this.balance = balance;
        }

        public int getMonth() { return month; }
        public double getEmi() { return emi; }
        public double getPrincipal() { return principal; }
        public double getInterest() { return interest; }
        public double getBalance() { return balance; }
    }
}
