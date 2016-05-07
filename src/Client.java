import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;


public class Client {

    ClientComms comms;
    public Client() throws Exception {
        Display window = new Display("eAuctions");
        window.init();
        comms = new ClientComms();
        comms.start();

    }

    public static void main(String[] args) throws Exception {
        new Client();
    }

    protected void sendRegisterMessage(String givenName, String familyName, String password) throws Exception {
        comms.sendRegisterMessage(givenName, familyName, password);
    }


    class Display extends JFrame {

        private JPanel panels;
        private CardLayout cardLayout;

        public Display(String title) {
            super(title);
        }

        protected void init() {
            this.setPreferredSize(new Dimension(1200, 600));
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

            cardLayout = (CardLayout) (panels.getLayout());
            cardLayout.show(panels, "sign in");

            container.add(panels);
            this.pack();
            this.setVisible(true);
            this.setResizable(false);

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
                userIDField.setMaximumSize(new Dimension(200, 25));

                JLabel passwordLabel = new JLabel("Password");
                JPasswordField passwordField = new JPasswordField(10);
                passwordField.setMaximumSize(new Dimension(200, 25));
                JButton signInButton = new JButton("Sign in");
                JLabel registerLabel = new JLabel("Not registered? Click to sign up");
                JButton registerButton = new JButton("Sign up");

                // Allows the user to navigate to register screen
                registerButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        cardLayout.show(panels, "register");
                    }
                });

                // Center components
                userIDLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                userIDField.setAlignmentX(Component.CENTER_ALIGNMENT);
                passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
                signInButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                registerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                bottomPanel.add(Box.createRigidArea(new Dimension(5, 50)));
                bottomPanel.add(userIDLabel);
                bottomPanel.add(Box.createRigidArea(new Dimension(5, 5)));
                bottomPanel.add(userIDField);
                bottomPanel.add(Box.createRigidArea(new Dimension(5, 5)));
                bottomPanel.add(passwordLabel);
                bottomPanel.add(Box.createRigidArea(new Dimension(5, 5)));
                bottomPanel.add(passwordField);
                bottomPanel.add(Box.createRigidArea(new Dimension(5, 5)));
                bottomPanel.add(signInButton);
                bottomPanel.add(Box.createRigidArea(new Dimension(5, 45)));
                bottomPanel.add(registerLabel);
                bottomPanel.add(Box.createRigidArea(new Dimension(5, 5)));
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
                passwordField.setMaximumSize(new Dimension(200, 25));
                JButton registerButton = new JButton("Register");

                // Send message to server to register user
                registerButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            //sendRegisterMessage(givenNameField.getText(), familyNameField.getText(), String.valueOf(passwordField.getPassword()));
                            sendRegisterMessage(givenNameField.getText(), familyNameField.getText(), String.valueOf(passwordField.getPassword()));
                        } catch (Exception e1) {e1.printStackTrace();}
                    }
                });

                // Center components
                givenNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                givenNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
                familyNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                familyNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
                passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
                registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                bottomPanel.add(Box.createRigidArea(new Dimension(5, 50)));
                bottomPanel.add(givenNameLabel);
                bottomPanel.add(Box.createRigidArea(new Dimension(5, 5)));
                bottomPanel.add(givenNameField);
                bottomPanel.add(Box.createRigidArea(new Dimension(5, 5)));
                bottomPanel.add(familyNameLabel);
                bottomPanel.add(Box.createRigidArea(new Dimension(5, 5)));
                bottomPanel.add(familyNameField);
                bottomPanel.add(Box.createRigidArea(new Dimension(5, 5)));
                bottomPanel.add(passwordLabel);
                bottomPanel.add(Box.createRigidArea(new Dimension(5, 5)));
                bottomPanel.add(passwordField);
                bottomPanel.add(Box.createRigidArea(new Dimension(5, 45)));
                bottomPanel.add(registerButton);

                this.add(bottomPanel, BorderLayout.CENTER);
            }
        }

        class MainClientUI extends JPanel {

            private JPanel topPanel;
            private JPanel itemPanelBottom;
            private JPanel itemSearchPanel;
            private JPanel itemDisplayPanel;

            public MainClientUI() {
                this.setLayout(new BorderLayout());
                init();
            }

            protected void init() {


                JTabbedPane tabbedPane = new JTabbedPane();
                this.add(tabbedPane);

                /* -----------VIEW BIDS TABBED PANE--------------*/
                JPanel viewBidsPanel = new JPanel();
                viewBidsPanel.setLayout(new BorderLayout());

                itemSearchPanel = new JPanel();
                itemSearchPanel.setLayout(new BoxLayout(itemSearchPanel, BoxLayout.Y_AXIS));
                itemSearchPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                itemSearchPanel.setBorder(new TitledBorder(new EtchedBorder(), "Item Search"));
                itemSearchPanel.setPreferredSize(new Dimension(195, 0));

                itemDisplayPanel = new JPanel();
                itemDisplayPanel.setLayout(new BoxLayout(itemDisplayPanel, BoxLayout.Y_AXIS));
                itemDisplayPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                itemDisplayPanel.setBorder(new TitledBorder(new EtchedBorder(), "Item Display"));
                itemDisplayPanel.setPreferredSize(new Dimension(990, 535));

                // Labels and fields for ID and created after:
                JLabel itemIDLabel = new JLabel("Item ID");
                itemIDLabel.setFont(new Font(itemIDLabel.getFont().getFontName(), Font.BOLD, 13));
                JTextField itemIDField = new JTextField();
                itemIDField.setMaximumSize(new Dimension(600, 20));

                JLabel createdAfterLabel = new JLabel("Created after");
                createdAfterLabel.setFont(new Font(createdAfterLabel.getFont().getFontName(), Font.BOLD, 13));
                JTextField createdAfterField = new JTextField();
                createdAfterField.setMaximumSize(new Dimension(600, 20));

                // Radio buttons for the categories:
                JLabel categoriesLabel = new JLabel("Categories");
                categoriesLabel.setFont(new Font(categoriesLabel.getFont().getFontName(), Font.BOLD, 13));
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

                // Search button:
                JButton searchButton = new JButton("Search");
                searchButton.setMaximumSize(new Dimension(175, 25));

                itemSearchPanel.add(Box.createRigidArea(new Dimension(0, 15)));
                itemSearchPanel.add(itemIDLabel);
                itemSearchPanel.add(Box.createRigidArea(new Dimension(5, 5)));
                itemSearchPanel.add(itemIDField);
                itemSearchPanel.add(Box.createRigidArea(new Dimension(5, 15)));
                itemSearchPanel.add(categoriesLabel);
                itemSearchPanel.add(homeRadio);
                itemSearchPanel.add(sportsRadio);
                itemSearchPanel.add(electronicsRadio);
                itemSearchPanel.add(jewelleryRadio);
                itemSearchPanel.add(gamesRadio);
                itemSearchPanel.add(clothingRadio);
                itemSearchPanel.add(booksRadio);
                itemSearchPanel.add(otherRadio);
                itemSearchPanel.add(Box.createRigidArea(new Dimension(5, 15)));
                itemSearchPanel.add(createdAfterLabel);
                itemSearchPanel.add(Box.createRigidArea(new Dimension(5, 5)));
                itemSearchPanel.add(createdAfterField);
                itemSearchPanel.add(Box.createRigidArea(new Dimension(5, 25)));
                itemSearchPanel.add(searchButton);

                // ITEM DISPLAY PANEL //
                ArrayList<Integer> users = new ArrayList<Integer>();


                String[] values = {"1", "2", "3"};
                //JList bids = new JList(listModel);
                String[] columnNames = {"ID", "Title", "Description", "Category", "Vendor ID", "Start time", "Close Time", "Reserve Price", "Current bids"};
                Object[][] data = {{new Integer(1), "random", "pretty awesome pretty awesomepretty awesome pretty awesome ", "Sports", "242", "01:42", "04:42", "£123.00", values}};

                // Makes jtable uneditable apart from the last bids comboBox which is only there to show a list of bids
                // Needs to be editable to see contents
                JTable table = new JTable(data, columnNames) {
                    public boolean isCellEditable(int r, int c) {
                        if (c == 8) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                };

                itemDisplayPanel.add(table.getTableHeader());
                itemDisplayPanel.add(table);

                table.getColumn("Current bids").setCellEditor(new BidComboBoxEditor(values));
                table.getColumn("Current bids").setCellRenderer(new BidComboBoxRenderer(values));
                table.setRowHeight(0, 20);
                table.getColumn("ID").setPreferredWidth(10);
                table.getColumn("Description").setPreferredWidth(250);


                viewBidsPanel.add(itemSearchPanel, BorderLayout.WEST);
                viewBidsPanel.add(itemDisplayPanel, BorderLayout.CENTER);
                // Created tabbed panes:

                tabbedPane.addTab("View bids", viewBidsPanel);


                /*--------SUBMIT ITEM TABBED PANE--------*/
                JPanel submitItemPanel = new JPanel();
                submitItemPanel.setLayout(new BorderLayout());

                JPanel itemPanelBottom = new JPanel();
                itemPanelBottom.setLayout(new BoxLayout(itemPanelBottom, BoxLayout.Y_AXIS));

                JLabel sellTitle = new JLabel("Got something to sell?");
                sellTitle.setFont(new Font(sellTitle.getFont().getFontName(), Font.BOLD, 40));
                sellTitle.setHorizontalAlignment(SwingConstants.CENTER);
                submitItemPanel.add(sellTitle, BorderLayout.NORTH);

                // Necessary components for registration screen:
                JLabel itemTitleLabel = new JLabel("Title - Keep this short and sweet");
                JTextField itemTitleField = new JTextField();
                itemTitleField.setMaximumSize(new Dimension(200, 25));
                JLabel itemDescriptionLabel = new JLabel("Full item description");
                JTextArea itemDescriptionField = new JTextArea();
                itemDescriptionField.setLineWrap(true);
                itemDescriptionField.setMaximumSize(new Dimension(200, 100));
                JLabel itemCategoryLabel = new JLabel("Category");

                // Creating categories JComboBox

                String[] categoriesArray = {"Home & Garden", "Sports", "Electronics", "Jewellery & Watches", "Toys & Games",
                        "Clothing", "Books & Comics", "Other"};
                JComboBox categoriesCombo = new JComboBox(categoriesArray);
                categoriesCombo.setMaximumSize(new Dimension(200, 25));

                JLabel itemReservePriceLabel = new JLabel("Reserve Price");
                JTextField itemReservePriceField = new JTextField("£");
                itemReservePriceField.setMaximumSize(new Dimension(200, 25));
                JButton submitItemButton = new JButton("Submit item");

                // Center components
                itemTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                itemTitleField.setAlignmentX(Component.CENTER_ALIGNMENT);
                itemDescriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                itemDescriptionField.setAlignmentX(Component.CENTER_ALIGNMENT);
                itemCategoryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                categoriesCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
                itemReservePriceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                itemReservePriceField.setAlignmentX(Component.CENTER_ALIGNMENT);
                submitItemButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                itemPanelBottom.add(Box.createRigidArea(new Dimension(5, 35)));
                itemPanelBottom.add(itemTitleLabel);
                itemPanelBottom.add(Box.createRigidArea(new Dimension(5, 5)));
                itemPanelBottom.add(itemTitleField);
                itemPanelBottom.add(Box.createRigidArea(new Dimension(5, 5)));
                itemPanelBottom.add(itemDescriptionLabel);
                itemPanelBottom.add(Box.createRigidArea(new Dimension(5, 5)));
                itemPanelBottom.add(itemDescriptionField);
                itemPanelBottom.add(Box.createRigidArea(new Dimension(5, 5)));
                itemPanelBottom.add(itemCategoryLabel);
                itemPanelBottom.add(Box.createRigidArea(new Dimension(5, 5)));
                itemPanelBottom.add(categoriesCombo);
                itemPanelBottom.add(Box.createRigidArea(new Dimension(5, 5)));
                itemPanelBottom.add(itemReservePriceLabel);
                itemPanelBottom.add(Box.createRigidArea(new Dimension(5, 5)));
                itemPanelBottom.add(itemReservePriceField);
                itemPanelBottom.add(Box.createRigidArea(new Dimension(5, 35)));
                itemPanelBottom.add(submitItemButton);

                submitItemPanel.add(itemPanelBottom, BorderLayout.CENTER);
                tabbedPane.addTab("Sell item", submitItemPanel);
            }

        }

        class BidComboBoxRenderer extends JComboBox implements TableCellRenderer {
            public BidComboBoxRenderer(String[] bids) {
                super(bids);
            }

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                if (isSelected) {
                    setForeground(table.getSelectionForeground());
                    super.setBackground(table.getSelectionBackground());
                } else {
                    setForeground(table.getForeground());
                    setBackground(table.getBackground());
                }
                setSelectedItem(value);
                return this;
            }
        }

        class BidComboBoxEditor extends DefaultCellEditor {
            public BidComboBoxEditor(String[] bids) {
                super(new JComboBox(bids));
            }
        }

    }
}