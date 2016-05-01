import java.awt.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;


public class Client {

    public static void main(String[] args) {
        Display window = new Display("eAuctions");
        window.init();


    }
}

class Display extends JFrame {

    private JPanel bottomPanel;


    public Display(String title) {
        super(title);
    }

    protected void init() {
        this.setPreferredSize(new Dimension(1200, 800));
        this.setResizable(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());
        container.setBackground(Color.LIGHT_GRAY);

        // Title for the log in screen
        JLabel title = new JLabel("eAuctions");
        title.setFont(new Font("Serif", Font.PLAIN, 14));
        container.add(title, BorderLayout.NORTH);

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        // Necessary components for the sign in screen
        JTextField userIDField = new JTextField("Your user ID");
        userIDField.setPreferredSize(new Dimension(5,5));
        userIDField.setLayout(new FlowLayout());
        JPasswordField passwordField = new JPasswordField(10);
        JButton signInButton = new JButton("Sign in");
        JLabel registerLabel = new JLabel("Not registered? Click to sign up:");
        JButton registerButton = new JButton("Register");

        // Center components
        userIDField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        signInButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        bottomPanel.add(userIDField);
        bottomPanel.add(passwordField);
        bottomPanel.add(signInButton);
        bottomPanel.add(registerLabel);
        bottomPanel.add(registerButton);

        container.add(bottomPanel, BorderLayout.CENTER);

        this.pack();
        this.setVisible(true);

    }

    class RegistrationPanel extends JPanel {

        private JPanel bottomPanel;

        public RegistrationPanel() {
            this.setLayout(new BorderLayout());
        }

        protected void init() {
            JLabel title = new JLabel("Registration");
            title.setFont(new Font("Serif", Font.PLAIN, 14));
            this.add(title, BorderLayout.NORTH);

            bottomPanel = new JPanel();
            bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

            // Necessary components for registration screen:
            JLabel givenNameLabel = new JLabel("Given name:");
            JTextField givenNameField = new JTextField();
            JLabel familyNameLabel = new JLabel("Family name:");
            JTextField familyNameField = new JTextField();
            JLabel passwordLabel = new JLabel("Password");
            JPasswordField passwordField = new JPasswordField(10);
            JButton registerButton = new JButton("Register");

            // Center components
            givenNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            givenNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
            familyNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            familyNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
            passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
            registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            bottomPanel.add(givenNameLabel);
            bottomPanel.add(givenNameField);
            bottomPanel.add(familyNameLabel);
            bottomPanel.add(familyNameField);
            bottomPanel.add(passwordLabel);
            bottomPanel.add(passwordField);
            bottomPanel.add(registerButton);

            this.add(bottomPanel, BorderLayout.CENTER);
        }
    }

    class MainClientUI extends JPanel {

        private JPanel bottomPanel;
        private JPanel itemSearchPanel;
        private JPanel itemDisplayPanel;

        public MainClientUI() {
            this.setLayout(new BorderLayout());
        }

        protected void init() {
            JLabel title = new JLabel("Hello user [userID here]");
            title.setFont(new Font("Serif", Font.PLAIN, 10));
            this.add(title, BorderLayout.NORTH);

            bottomPanel = new JPanel();
            bottomPanel.setLayout(new BorderLayout());

            itemSearchPanel = new JPanel();
            itemSearchPanel.setLayout(new GridLayout(2,2));
            itemSearchPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            itemSearchPanel.setBorder(new TitledBorder(new EtchedBorder(), "Item Search"));

            itemDisplayPanel = new JPanel();
            itemDisplayPanel.setLayout(new FlowLayout());
            itemDisplayPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            itemDisplayPanel.setBorder(new TitledBorder(new EtchedBorder(), "Item Display"));

            bottomPanel.add(itemSearchPanel,BorderLayout.WEST);
            bottomPanel.add(itemDisplayPanel,BorderLayout.EAST);

            JLabel itemIDLabel = new JLabel("Item ID:");
            JTextField itemIDField = new JTextField();

            // Radio buttons for the categories:
            JLabel categoriesLabel = new JLabel("Categories");
            JRadioButton homeRadio = new JRadioButton("Home & Garden");
            JRadioButton sportsRadio = new JRadioButton("Sports");
            JRadioButton electronicsRadio = new JRadioButton("Electronics");
            JRadioButton jewelleryRadio = new JRadioButton("Jewellery & Watches");
            JRadioButton gamesRadio = new JRadioButton("Toys & Games");
            JRadioButton clothingRadio = new JRadioButton("Clothing");
            JRadioButton booksRadio = new JRadioButton("Books & Comics");
            JRadioButton otherRadio = new JRadioButton("Other");

            ButtonGroup categoryButtons = new ButtonGroup();
            categoryButtons.add(homeRadio);
            categoryButtons.add(sportsRadio);
            categoryButtons.add(electronicsRadio);
            categoryButtons.add(jewelleryRadio);
            categoryButtons.add(gamesRadio);
            categoryButtons.add(clothingRadio);
            categoryButtons.add(booksRadio);
            categoryButtons.add(otherRadio);

            JLabel createdAfterLabel = new JLabel("Created after:");
            JTextField createdAfterField = new JTextField();

            JButton searchButton = new JButton("Search");

            itemSearchPanel.add(itemIDLabel);
            itemSearchPanel.add(itemIDField);
            itemSearchPanel.add(categoriesLabel);
            itemSearchPanel.add(homeRadio);
            itemSearchPanel.add(sportsRadio);
            itemSearchPanel.add(electronicsRadio);
            itemSearchPanel.add(jewelleryRadio);
            itemSearchPanel.add(gamesRadio);
            itemSearchPanel.add(clothingRadio);
            itemSearchPanel.add(booksRadio);
            itemSearchPanel.add(otherRadio);
            itemSearchPanel.add(createdAfterLabel);
            itemSearchPanel.add(createdAfterField);
            itemSearchPanel.add(searchButton);

            //// ITEM DISPLAY ////

//            JList<Item> itemList = new JList();
//            DefaultListModel model = new DefaultListModel();
//
//
//            this.add(bottomPanel, BorderLayout.CENTER);
        }

    }
}