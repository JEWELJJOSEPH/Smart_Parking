import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class VehicleEntryForm extends JFrame {
    private Connection dbConnection;

    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            dbConnection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/parking", 
                "root", 
                "" 
            );
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "MySQL JDBC Driver not found: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createTableIfNotExists() {
    String sql = "CREATE TABLE IF NOT EXISTS entries ("
        + "id INT AUTO_INCREMENT PRIMARY KEY,"
        + "vehicle_number VARCHAR(50),"
        + "owner_name VARCHAR(100),"
        + "phone_number VARCHAR(20),"
        + "vehicle_type VARCHAR(20),"
        + "slot VARCHAR(10)"
        + ")";
        try (Statement stmt = dbConnection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Table creation failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean insertVehicleEntry(String number, String owner, String phone, String type, String slot) {
        String sql = "INSERT INTO entries (vehicle_number, owner_name, phone_number, vehicle_type, slot) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
            pstmt.setString(1, number);
            pstmt.setString(2, owner);
            pstmt.setString(3, phone);
            pstmt.setString(4, type);
            pstmt.setString(5, slot);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    private final JTextField vehicleNumberField, ownerNameField, phoneNumberField;
    private final JComboBox<String> vehicleTypeBox;
    private final JButton registerButton;
    private final JButton[] slotButtons;
    private final boolean[] slotOccupied;
    private String selectedSlot = "";

    public VehicleEntryForm() {
    connectToDatabase();
    createTableIfNotExists();
        setTitle("Vehicle Entry");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(new Color(240, 255, 245));

        // Back Button + Title Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 255, 245));

        JButton backButton = new JButton("← Back to Dashboard");
    backButton.setFocusPainted(false);
    backButton.setBackground(new Color(230, 240, 230));
    backButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
    backButton.setPreferredSize(new Dimension(180, 40)); 
    headerPanel.add(backButton, BorderLayout.WEST);

        JPanel titlePanel = new JPanel();
    titlePanel.setBackground(new Color(240, 255, 245));
    titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
    JLabel title = new JLabel("Vehicle Entry");
    title.setFont(new Font("SansSerif", Font.BOLD, 26));
    title.setHorizontalAlignment(SwingConstants.CENTER);
    titlePanel.add(title);

        JLabel subtitle = new JLabel("Register a new vehicle entering the parking lot");
    subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
    subtitle.setForeground(Color.DARK_GRAY);
    subtitle.setHorizontalAlignment(SwingConstants.CENTER);
    JPanel subtitlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    subtitlePanel.setBackground(new Color(240, 255, 245));
    subtitlePanel.add(subtitle);

        JPanel titleContainer = new JPanel(new GridLayout(2, 1));
        titleContainer.setBackground(new Color(240, 255, 245));
        titleContainer.add(titlePanel);
        titleContainer.add(subtitlePanel);

        headerPanel.add(titleContainer, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 230, 200), 1, true),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel sectionLabel = new JLabel("Vehicle Information");
        sectionLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(sectionLabel, gbc);

        JLabel descLabel = new JLabel("Enter the details of the vehicle entering the parking lot");
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        descLabel.setForeground(Color.GRAY);
        gbc.gridy++;
        formPanel.add(descLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++; gbc.gridx = 0;
        formPanel.add(new JLabel("Vehicle Number *"), gbc);
        gbc.gridx = 1;
        vehicleNumberField = new JTextField();
        vehicleNumberField.setPreferredSize(new Dimension(200, 32));
        vehicleNumberField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        vehicleNumberField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 220, 180), 2, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        formPanel.add(vehicleNumberField, gbc);

        gbc.gridy++; gbc.gridx = 0;
        formPanel.add(new JLabel("Vehicle Type *"), gbc);
        gbc.gridx = 1;
        vehicleTypeBox = new JComboBox<>(new String[]{"Car", "Bike", "Truck"});
    vehicleTypeBox.setPreferredSize(new Dimension(200, 32));
    vehicleTypeBox.setFont(new Font("SansSerif", Font.PLAIN, 15));
    vehicleTypeBox.setBackground(new Color(245, 255, 245));
    formPanel.add(vehicleTypeBox, gbc);

        gbc.gridy++; gbc.gridx = 0;
        formPanel.add(new JLabel("Owner Name *"), gbc);
        gbc.gridx = 1;
        ownerNameField = new JTextField();
        ownerNameField.setPreferredSize(new Dimension(200, 32));
        ownerNameField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        ownerNameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 220, 180), 2, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        formPanel.add(ownerNameField, gbc);

        gbc.gridy++; gbc.gridx = 0;
        formPanel.add(new JLabel("Phone Number"), gbc);
        gbc.gridx = 1;
        phoneNumberField = new JTextField();
        phoneNumberField.setPreferredSize(new Dimension(200, 32));
        phoneNumberField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        phoneNumberField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 220, 180), 2, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        formPanel.add(phoneNumberField, gbc);

        gbc.gridy++; gbc.gridx = 0;
        formPanel.add(new JLabel("Select Parking Slot *"), gbc);
        gbc.gridx = 1;

        JPanel slotPanel = new JPanel(new GridLayout(1, 6, 10, 10));
        String[] slots = {"A5", "A12", "A18", "B23", "B31", "B37", "C42"};
    slotButtons = new JButton[slots.length];
    slotOccupied = new boolean[slots.length];
        for (int i = 0; i < slots.length; i++) {
            slotButtons[i] = new JButton(slots[i]);
            slotButtons[i].setBackground(new Color(230, 245, 230)); // green for free
            slotButtons[i].setFocusPainted(false);
            slotButtons[i].setFont(new Font("SansSerif", Font.BOLD, 15));
            slotButtons[i].setPreferredSize(new Dimension(70, 36));
            slotButtons[i].setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 153, 51), 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
            slotOccupied[i] = false;
            int index = i;
            slotButtons[i].addActionListener(_ -> selectSlot(index));
            slotPanel.add(slotButtons[i]);
        }
        formPanel.add(slotPanel, gbc);

        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2;
        registerButton = new JButton("Register Vehicle Entry");
        registerButton.setBackground(new Color(0, 153, 51));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("SansSerif", Font.BOLD, 17));
        registerButton.setFocusPainted(false);
        registerButton.setPreferredSize(new Dimension(220, 44));
        registerButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 153, 51), 2, true),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        registerButton.addActionListener(_ -> registerVehicle());
        formPanel.add(registerButton, gbc);

        add(formPanel, BorderLayout.CENTER);
    }

    private void selectSlot(int index) {
        if (slotOccupied[index]) {
            JOptionPane.showMessageDialog(this, "This slot is occupied!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (int i = 0; i < slotButtons.length; i++) {
            if (!slotOccupied[i]) {
                slotButtons[i].setBackground(new Color(230, 245, 230)); // green for free
                slotButtons[i].setForeground(Color.BLACK);
            } else {
                slotButtons[i].setBackground(Color.RED); // red for occupied
                slotButtons[i].setForeground(Color.WHITE);
            }
        }
        slotButtons[index].setBackground(new Color(0, 204, 102)); // highlight selected
        slotButtons[index].setForeground(Color.WHITE);
        selectedSlot = slotButtons[index].getText();
    }

    private void registerVehicle() {
        String number = vehicleNumberField.getText();
        String owner = ownerNameField.getText();
        String type = (String) vehicleTypeBox.getSelectedItem();

        if (number.isEmpty() || owner.isEmpty() || selectedSlot.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Save to database
        if (insertVehicleEntry(number, owner, phoneNumberField.getText(), type, selectedSlot)) {
            // Mark slot as occupied (red)
            for (int i = 0; i < slotButtons.length; i++) {
                if (slotButtons[i].getText().equals(selectedSlot)) {
                    slotOccupied[i] = true;
                    slotButtons[i].setBackground(Color.RED);
                    slotButtons[i].setForeground(Color.WHITE);
                }
            }
            JOptionPane.showMessageDialog(this,
                "Vehicle Registered Successfully!\n\n" +
                "Vehicle: " + number + "\n" +
                "Owner: " + owner + "\n" +
                "Type: " + type + "\n" +
                "Slot: " + selectedSlot,
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Database error! Could not save entry.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    // Call this method when a vehicle exits to free the slot
    public void freeSlot(String slotName) {
        for (int i = 0; i < slotButtons.length; i++) {
            if (slotButtons[i].getText().equals(slotName)) {
                slotOccupied[i] = false;
                slotButtons[i].setBackground(new Color(230, 245, 230)); // green for free
                slotButtons[i].setForeground(Color.BLACK);
            }
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new VehicleEntryForm().setVisible(true);
        });
    }
}
