import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class VehicleExit extends JFrame {
    private VehicleEntryForm entryForm;
    private Connection dbConnection;

    public VehicleExit() {
        setTitle("Vehicle Exit");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(255, 240, 245));
        setLayout(new BorderLayout(20, 20));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 240, 245));
        JButton backButton = new JButton("\u2190 Back to Dashboard");
        backButton.setFocusPainted(false);
        backButton.setBackground(new Color(230, 240, 230));
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        backButton.setPreferredSize(new Dimension(200, 40));
        headerPanel.add(backButton, BorderLayout.WEST);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(255, 240, 245));
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Vehicle Exit");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel subtitle = new JLabel("Process vehicle exit and calculate parking charges");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 18));
        subtitle.setForeground(Color.DARK_GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(8));
        titlePanel.add(subtitle);
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(255, 240, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 20, 0);

        // Find Vehicle Card
        JPanel findPanel = new JPanel();
        findPanel.setBackground(Color.WHITE);
        findPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(24, 24, 24, 24)));
        findPanel.setLayout(new BoxLayout(findPanel, BoxLayout.Y_AXIS));
        findPanel.setMaximumSize(new Dimension(800, 140));
        findPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel findLabel = new JLabel("Find Vehicle");
        findLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        findLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        findPanel.add(findLabel);
        findPanel.add(Box.createVerticalStrut(8));
        JLabel findDesc = new JLabel("Search by vehicle number or slot number");
        findDesc.setFont(new Font("SansSerif", Font.PLAIN, 15));
        findDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        findPanel.add(findDesc);
        findPanel.add(Box.createVerticalStrut(16));

        JPanel searchBarPanel = new JPanel(new BorderLayout(10, 0));
        searchBarPanel.setOpaque(false);
        JTextField searchField = new JTextField();
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        searchField.setPreferredSize(new Dimension(500, 40));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        searchBarPanel.add(searchField, BorderLayout.CENTER);
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(255, 120, 120));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        searchButton.setFocusPainted(false);
        searchButton.setPreferredSize(new Dimension(100, 40));
        searchBarPanel.add(searchButton, BorderLayout.EAST);
        findPanel.add(searchBarPanel);

        mainPanel.add(findPanel, gbc);
        gbc.gridy++;

        // Quick Search Card
        JPanel quickPanel = new JPanel();
        quickPanel.setBackground(Color.WHITE);
        quickPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(24, 24, 24, 24)));
        quickPanel.setLayout(new BoxLayout(quickPanel, BoxLayout.Y_AXIS));
        quickPanel.setMaximumSize(new Dimension(800, 180));
        quickPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel quickLabel = new JLabel("Quick Search");
        quickLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        quickLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        quickPanel.add(quickLabel);
        quickPanel.add(Box.createVerticalStrut(8));
        JLabel quickDesc = new JLabel("Try searching for these sample vehicles");
        quickDesc.setFont(new Font("SansSerif", Font.PLAIN, 15));
        quickDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        quickPanel.add(quickDesc);
        quickPanel.add(Box.createVerticalStrut(16));

        JPanel cardsPanel = new JPanel();
        cardsPanel.setOpaque(false);
        cardsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 24, 0));

        // Sample vehicle cards
        cardsPanel.add(createVehicleCard("\uD83D\uDE97", new Color(33, 150, 243), "ABC1234", "15", "09:30 AM", "John Doe"));
        cardsPanel.add(createVehicleCard("\uD83D\uDEB2", new Color(76, 175, 80), "XYZ5678", "3", "11:15 AM", "Jane Smith"));
        cardsPanel.add(createVehicleCard("\uD83D\uDE9A", new Color(156, 39, 176), "TRK9999", "47", "08:00 AM", "Mike Johnson"));

        quickPanel.add(cardsPanel);
        mainPanel.add(quickPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);

        connectToDatabase();
    }

    private JPanel createVehicleCard(String icon, Color iconColor, String number, String slot, String entry, String owner) {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(16, 20, 16, 20)));
        card.setPreferredSize(new Dimension(220, 110));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setForeground(iconColor);
        card.add(iconLabel);

        JLabel numLabel = new JLabel(number);
        numLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        card.add(numLabel);

        JLabel slotLabel = new JLabel("Slot: " + slot);
        slotLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        card.add(slotLabel);

        JLabel entryLabel = new JLabel("Entry: " + entry);
        entryLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        card.add(entryLabel);

        JLabel ownerLabel = new JLabel("Owner: " + owner);
        ownerLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        card.add(ownerLabel);

        return card;
    }

    private void addFreeSlotButton(JPanel parent) {
        JButton freeSlotButton = new JButton("Free Slot A5");
        freeSlotButton.setBackground(new Color(0, 153, 51));
        freeSlotButton.setForeground(Color.WHITE);
        freeSlotButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        freeSlotButton.addActionListener(e -> {
            String slot = "A5"; // Replace "A5" with selected slot
            boolean dbFreed = false;

            // Try freeing in database if connected
            if (dbConnection != null) {
                dbFreed = freeSlotInDatabase(slot);
            }

            // Also notify entry form if available to update UI/state
            if (entryForm != null) {
                entryForm.freeSlot(slot); // Replace with proper API of VehicleEntryForm
            }

            // Notify user
            if (dbFreed || entryForm != null) {
                JOptionPane.showMessageDialog(this, "Slot " + slot + " freed!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to free slot " + slot, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        parent.add(freeSlotButton);
    }

    public VehicleExit(VehicleEntryForm entryForm) {
        this(); // call default constructor for UI setup
        this.entryForm = entryForm;
    }

    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            dbConnection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/parking", // Update as needed
                "root", // Update as needed
                "" // Update as needed
            );
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "MySQL JDBC Driver not found: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Search for a vehicle by number or slot
    private ResultSet searchVehicle(String query) {
        try {
            String sql = "SELECT * FROM entries WHERE vehicle_number = ? OR slot = ?";
            PreparedStatement pstmt = dbConnection.prepareStatement(sql);
            pstmt.setString(1, query);
            pstmt.setString(2, query);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Search failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // Free a slot in the database (delete entry)
    private boolean freeSlotInDatabase(String slotName) {
        try {
            String sql = "DELETE FROM entries WHERE slot = ?";
            PreparedStatement pstmt = dbConnection.prepareStatement(sql);
            pstmt.setString(1, slotName);
            int affected = pstmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to free slot: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VehicleExit().setVisible(true));
    }
}
