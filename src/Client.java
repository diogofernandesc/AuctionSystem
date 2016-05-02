import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;


public class Client {

    public static void main(String[] args) {
        Display window = new Display("eAuctions");
        window.init();
    }
}

class Display extends JFrame {

    private JPanel panels;

    public Display(String title) {
        super(title);
    }

    protected void init() {
        this.setPreferredSize(new Dimension(800, 600));
        this.setResizable(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new FlowLayout());
        //container.setLayout(new BorderLayout());

        SignInPanel signInPanel = new SignInPanel();
        RegistrationPanel registrationPanel = new RegistrationPanel();
        MainClientUI mainUIPanel = new MainClientUI();

        // Contains the different 'screens'
        panels = new JPanel();
        panels.setLayout(new CardLayout());

        // Adding different panels to the main Card Layout Panel
        panels.add(signInPanel, "sign in");
        panels.add(registrationPanel, "register");
        panels.add(mainUIPanel, "main ui");

        CardLayout cardLayout = (CardLayout)(panels.getLayout());
        cardLayout.show(panels, "main ui");

        container.add(panels);
        this.pack();
        this.setVisible(true);

    }

    class SignInPanel extends JPanel {

        private JPanel bottomPanel;

        public SignInPanel() {
            this.setLayout(new BorderLayout());
            init();
        }
        protected void init() {
            // Title for the log in screen
            JLabel title = new JLabel("eAuctions");
            title.setFont(new Font("Serif", Font.BOLD, 72));
            title.setHorizontalAlignment(SwingConstants.CENTER);
            this.add(title, BorderLayout.NORTH);

            bottomPanel = new JPanel();
            bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
            // Necessary components for the sign in screen
            JLabel userIDLabel = new JLabel("User ID");
            JTextField userIDField = new JTextField();
            userIDField.setMaximumSize(new Dimension(200,25));

            JLabel passwordLabel = new JLabel("Password");
            JPasswordField passwordField = new JPasswordField(10);
            passwordField.setMaximumSize(new Dimension(200,25));
            JButton signInButton = new JButton("Sign in");
            JLabel registerLabel = new JLabel("Not registered? Click to sign up");
            JButton registerButton = new JButton("Sign up");

            // Center components
            userIDLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            userIDField.setAlignmentX(Component.CENTER_ALIGNMENT);
            passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
            signInButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            registerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            bottomPanel.add(Box.createRigidArea(new Dimension(5,50)));
            bottomPanel.add(userIDLabel);
            bottomPanel.add(Box.createRigidArea(new Dimension(5,5)));
            bottomPanel.add(userIDField);
            bottomPanel.add(Box.createRigidArea(new Dimension(5,5)));
            bottomPanel.add(passwordLabel);
            bottomPanel.add(Box.createRigidArea(new Dimension(5,5)));
            bottomPanel.add(passwordField);
            bottomPanel.add(Box.createRigidArea(new Dimension(5,5)));
            bottomPanel.add(signInButton);
            bottomPanel.add(Box.createRigidArea(new Dimension(5,45)));
            bottomPanel.add(registerLabel);
            bottomPanel.add(Box.createRigidArea(new Dimension(5,5)));
            bottomPanel.add(registerButton);

            this.add(bottomPanel, BorderLayout.CENTER);
        }
    }
    class RegistrationPanel extends JPanel {

        private JPanel bottomPanel;

        public RegistrationPanel() {
            this.setLayout(new BorderLayout());
            init();
        }

        protected void init() {
            JLabel title = new JLabel("Registration");
            title.setFont(new Font("Serif", Font.BOLD, 72));
            title.setHorizontalAlignment(SwingConstants.CENTER);
            this.add(title, BorderLayout.NORTH);

            bottomPanel = new JPanel();
            bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

            // Necessary components for registration screen:
            JLabel givenNameLabel = new JLabel("Given name");
            JTextField givenNameField = new JTextField();
            givenNameField.setMaximumSize(new Dimension(200, 25));
            JLabel familyNameLabel = new JLabel("Family name");
            JTextField familyNameField = new JTextField();
            familyNameField.setMaximumSize(new Dimension(200, 25));
            JLabel passwordLabel = new JLabel("Password");
            JPasswordField passwordField = new JPasswordField();
            passwordField.setMaximumSize(new Dimension(200,25));
            JButton registerButton = new JButton("Register");

            // Center components
            givenNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            givenNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
            familyNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            familyNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
            passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
            registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            bottomPanel.add(Box.createRigidArea(new Dimension(5,50)));
            bottomPanel.add(givenNameLabel);
            bottomPanel.add(Box.createRigidArea(new Dimension(5,5)));
            bottomPanel.add(givenNameField);
            bottomPanel.add(Box.createRigidArea(new Dimension(5,5)));
            bottomPanel.add(familyNameLabel);
            bottomPanel.add(Box.createRigidArea(new Dimension(5,5)));
            bottomPanel.add(familyNameField);
            bottomPanel.add(Box.createRigidArea(new Dimension(5,5)));
            bottomPanel.add(passwordLabel);
            bottomPanel.add(Box.createRigidArea(new Dimension(5,5)));
            bottomPanel.add(passwordField);
            bottomPanel.add(Box.createRigidArea(new Dimension(5,45)));
            bottomPanel.add(registerButton);

            this.add(bottomPanel, BorderLayout.CENTER);
        }
    }

    class MainClientUI extends JPanel {

        private JPanel bottomPanel;
        private JPanel itemSearchPanel;
        private JPanel itemDisplayPanel;

        public MainClientUI() {
           this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            init();
        }

        protected void init() {

            JLabel title = new JLabel("Hello user [userID here]");
            title.setFont(new Font("Serif", Font.BOLD, 15));
            title.setHorizontalAlignment(SwingConstants.CENTER);
//            this.add(title, BorderLayout.NORTH);
            this.add(title);

            bottomPanel = new JPanel();
            bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

            itemSearchPanel = new JPanel();
            itemSearchPanel.setLayout(new BoxLayout(itemSearchPanel, BoxLayout.Y_AXIS));
            itemSearchPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            itemSearchPanel.setBorder(new TitledBorder(new EtchedBorder(), "Item Search"));

            itemDisplayPanel = new JPanel();
            itemDisplayPanel.setLayout(new BoxLayout(itemDisplayPanel, BoxLayout.Y_AXIS));
            itemDisplayPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            itemDisplayPanel.setBorder(new TitledBorder(new EtchedBorder(), "Item Display"));

            // Panel for unique ID
            JPanel uniqueIDPanel = new JPanel();
            uniqueIDPanel.setLayout(new BoxLayout(uniqueIDPanel, BoxLayout.X_AXIS));
            JLabel itemIDLabel = new JLabel("Item ID:");
            JTextField itemIDField = new JTextField();
            itemIDField.setMaximumSize(new Dimension(1000,25));

            uniqueIDPanel.add(itemIDLabel);
            uniqueIDPanel.add(itemIDField);

            // Panel for created after
            JPanel createdAfterPanel = new JPanel();
            createdAfterPanel.setLayout(new BoxLayout(createdAfterPanel, BoxLayout.X_AXIS));
            JLabel createdAfterLabel = new JLabel("Created after:");
            JTextField createdAfterField = new JTextField();
            createdAfterField.setMaximumSize(new Dimension(1000,25));
            createdAfterPanel.add(createdAfterLabel);
            createdAfterPanel.add(createdAfterField);

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

            JButton searchButton = new JButton("Search");
            searchButton.setHorizontalAlignment(SwingConstants.CENTER);

            itemSearchPanel.add(Box.createRigidArea(new Dimension(5,15)));
            itemSearchPanel.add(uniqueIDPanel);
            itemSearchPanel.add(Box.createRigidArea(new Dimension(5,5)));
            itemSearchPanel.add(categoriesLabel);
            itemSearchPanel.add(homeRadio);
            itemSearchPanel.add(sportsRadio);
            itemSearchPanel.add(electronicsRadio);
            itemSearchPanel.add(jewelleryRadio);
            itemSearchPanel.add(gamesRadio);
            itemSearchPanel.add(clothingRadio);
            itemSearchPanel.add(booksRadio);
            itemSearchPanel.add(otherRadio);
            itemSearchPanel.add(Box.createRigidArea(new Dimension(5,5)));
            itemSearchPanel.add(createdAfterPanel);
            itemSearchPanel.add(Box.createRigidArea(new Dimension(5,25)));
            itemSearchPanel.add(searchButton);

            bottomPanel.add(itemSearchPanel);
            bottomPanel.add(itemDisplayPanel);

            this.add(bottomPanel);
//            bottomPanel.add(itemSearchPanel,BorderLayout.WEST);
//            bottomPanel.add(itemDisplayPanel,BorderLayout.EAST);

//            this.add(bottomPanel, BorderLayout.CENTER);
            //// ITEM DISPLAY ////

//            JList<Item> itemList = new JList();
//            DefaultListModel model = new DefaultListModel();
//
//
//            this.add(bottomPanel, BorderLayout.CENTER);
        }

    }
}