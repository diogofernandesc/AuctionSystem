import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class Client {

    String activeUser;
    ClientComms comms;
    Display window;

    public Client() throws Exception {
        window = new Display("eAuctions");
        window.init();
        comms = new ClientComms(this, window);
        comms.startConnection();
    }

    public static void main(String[] args) throws Exception {
        new Client();
    }

    protected void setActiveUser(String user) {
        this.activeUser = user;
    }


    class Display extends JFrame {

        private JPanel panels;
        private CardLayout cardLayout;
        SignInPanel signInPanel;
        RegistrationPanel registrationPanel;
        MainClientUI mainUIPanel;


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

            signInPanel = new SignInPanel();
            registrationPanel = new RegistrationPanel();
            mainUIPanel = new MainClientUI();

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

        public void receiveRegisterMessage(String message) {
            if (message.equals("invalid")) {
                JOptionPane.showMessageDialog(window, "Username already in use", "Invalid username",JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(window, "You are registered under: "+ message);
                cardLayout.show(panels, "sign in");
            }
        }

        public void receiveSignInMessage(String message) {
            String[] successMessage = message.split(",");
            if (successMessage[0].equals("success")) {
                cardLayout.show(panels, "main ui");
                activeUser = successMessage[1];

            } else if (message.equals("wrong password")) {
                JOptionPane.showMessageDialog(window, "The password you entered is incorrect", "Invalid password", JOptionPane.ERROR_MESSAGE);

            } else if (message.equals("wrong username")) {
                JOptionPane.showMessageDialog(window, "The username you entered does not exist", "Invalid username", JOptionPane.ERROR_MESSAGE);
            }
        }

        public void receiveSellItemMessage(SellItemMessage message) {
            mainUIPanel.addItem(message.getItemID(), message.getTitle(), message.getDescription(), message.getCatKeyword(), activeUser, message.getStartTime(),
                    message.getCloseTime(), message.getReservePrice(), message.getBidList());
        }

        public void receiveViewItemMessage(ViewItemMessage message) {
            ArrayList<Item> items = message.getSearchedItems();
            mainUIPanel.newSearchTable(items);

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

                signInButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        comms.sendSignInMessage(userIDField.getText(), String.valueOf(passwordField.getPassword()));
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

            public JPanel getBottomPanel() {
                return bottomPanel;
            }

            protected void init() {
                JLabel title = new JLabel("Registration");
                title.setFont(new Font("Serif", Font.BOLD, 72));
                title.setHorizontalAlignment(SwingConstants.CENTER);
                this.add(title, BorderLayout.NORTH);

                bottomPanel = new JPanel();
                bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

                // Necessary components for registration screen:
                JLabel usernameLabel = new JLabel("Username");
                JTextField usernameField = new JTextField();
                usernameField.setMaximumSize(new Dimension(200,25));
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
                            //comms.startConnection();
                            comms.sendRegisterMessage(usernameField.getText(),givenNameField.getText(), familyNameField.getText(), String.valueOf(passwordField.getPassword()));

                        } catch (Exception e1) {e1.printStackTrace();}
                    }
                });

                // Center components
                usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
                givenNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                givenNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
                familyNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                familyNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
                passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
                registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                bottomPanel.add(Box.createRigidArea(new Dimension(5, 50)));
                bottomPanel.add(usernameLabel);
                bottomPanel.add(Box.createRigidArea(new Dimension(5, 5)));
                bottomPanel.add(usernameField);
                bottomPanel.add(Box.createRigidArea(new Dimension(5, 5)));
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
                bottomPanel.add(Box.createRigidArea(new Dimension(5, 15)));

                this.add(bottomPanel, BorderLayout.CENTER);
            }
        }

        class MainClientUI extends JPanel {

            private JPanel topPanel;
            private JPanel itemPanelBottom;
            private JPanel itemSearchPanel;
            private JPanel itemDisplayPanel;
            DefaultTableModel tableModel;
            JTable table;

            public MainClientUI() {
                this.setLayout(new BorderLayout());
                init();
            }

            protected void init() {


                JTabbedPane tabbedPane = new JTabbedPane();
                this.add(tabbedPane);

                /* -----------VIEW BIDS TABBED PANE--------------*/
                JPanel viewItemsPanel = new JPanel();
                viewItemsPanel.setLayout(new BorderLayout());

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
                itemIDLabel.setMaximumSize(new Dimension(200,20));
                itemIDLabel.setFont(new Font(itemIDLabel.getFont().getFontName(), Font.BOLD, 13));
                JTextField itemIDField = new JTextField();
                itemIDField.setMaximumSize(new Dimension(600, 20));

                JLabel createdAfterLabel = new JLabel("Created after");
                createdAfterLabel.setMaximumSize(new Dimension(200,20));
                createdAfterLabel.setFont(new Font(createdAfterLabel.getFont().getFontName(), Font.BOLD, 13));

                // Created after JSpinner for time and date:
                Date date = new Date();
                SpinnerDateModel cdm = new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY);
                JSpinner createdDateSpinner = new JSpinner(cdm);
                JSpinner.DateEditor cde = new JSpinner.DateEditor(createdDateSpinner, "dd/MM/yyyy HH:mm:ss");
                createdDateSpinner.setEditor(cde);
                createdDateSpinner.setMaximumSize(new Dimension(300,20));


                // Radio buttons for the categories:
                JLabel categoriesLabel = new JLabel("Categories");
                categoriesLabel.setFont(new Font(categoriesLabel.getFont().getFontName(), Font.BOLD, 13));
                JRadioButton homeRadio = new JRadioButton("Home & Garden");
                homeRadio.setMaximumSize(new Dimension(300,20));
                JRadioButton sportsRadio = new JRadioButton("Sports");
                sportsRadio.setMaximumSize(new Dimension(300,20));
                JRadioButton electronicsRadio = new JRadioButton("Electronics");
                electronicsRadio.setMaximumSize(new Dimension(300,20));
                JRadioButton jewelleryRadio = new JRadioButton("Jewellery & Watches");
                jewelleryRadio.setMaximumSize(new Dimension(300,20));
                JRadioButton gamesRadio = new JRadioButton("Toys & Games");
                gamesRadio.setMaximumSize(new Dimension(300,20));
                JRadioButton clothingRadio = new JRadioButton("Clothing");
                clothingRadio.setMaximumSize(new Dimension(300,20));
                JRadioButton booksRadio = new JRadioButton("Books & Comics");
                booksRadio.setMaximumSize(new Dimension(300,20));
                JRadioButton otherRadio = new JRadioButton("Other");
                otherRadio.setMaximumSize(new Dimension(300,20));

                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
                buttonPanel.setMaximumSize(new Dimension(300,190));
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
                searchButton.setMaximumSize(new Dimension(250, 25));

                searchButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            String category = categoryButtons.getSelection().getActionCommand();
                            Date date = (Date) createdDateSpinner.getValue();
                            comms.sendViewItemMessage(itemIDField.getText(), category, date);
                        } catch (NullPointerException e1) {}
                    }
                });

                // Reset search button:

                JButton resetSearchButton = new JButton("Reset search");
                resetSearchButton.setMaximumSize(new Dimension(250,25));

                itemIDLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
                itemIDField.setAlignmentX(Component.CENTER_ALIGNMENT);
                categoriesLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
                homeRadio.setAlignmentX(Component.RIGHT_ALIGNMENT);
                sportsRadio.setAlignmentX(Component.RIGHT_ALIGNMENT);
                electronicsRadio.setAlignmentX(Component.RIGHT_ALIGNMENT);
                jewelleryRadio.setAlignmentX(Component.RIGHT_ALIGNMENT);
                gamesRadio.setAlignmentX(Component.RIGHT_ALIGNMENT);
                clothingRadio.setAlignmentX(Component.RIGHT_ALIGNMENT);
                booksRadio.setAlignmentX(Component.RIGHT_ALIGNMENT);
                otherRadio.setAlignmentX(Component.RIGHT_ALIGNMENT);
                createdAfterLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
                createdDateSpinner.setAlignmentX(Component.CENTER_ALIGNMENT);
                searchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                resetSearchButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                itemSearchPanel.add(Box.createRigidArea(new Dimension(0, 15)));
                itemSearchPanel.add(itemIDLabel);
                itemSearchPanel.add(Box.createRigidArea(new Dimension(5, 5)));
                itemSearchPanel.add(itemIDField);
                itemSearchPanel.add(Box.createRigidArea(new Dimension(5, 15)));
                itemSearchPanel.add(categoriesLabel);
                itemSearchPanel.add(Box.createRigidArea(new Dimension(5, 5)));
                buttonPanel.add(homeRadio);
                buttonPanel.add(sportsRadio);
                buttonPanel.add(electronicsRadio);
                buttonPanel.add(jewelleryRadio);
                buttonPanel.add(gamesRadio);
                buttonPanel.add(clothingRadio);
                buttonPanel.add(booksRadio);
                buttonPanel.add(otherRadio);
                itemSearchPanel.add(buttonPanel);
                itemSearchPanel.add(Box.createRigidArea(new Dimension(5, 15)));
                itemSearchPanel.add(createdAfterLabel);
                itemSearchPanel.add(Box.createRigidArea(new Dimension(5, 5)));
                itemSearchPanel.add(createdDateSpinner);
                itemSearchPanel.add(Box.createRigidArea(new Dimension(5, 25)));
                itemSearchPanel.add(searchButton);
                itemSearchPanel.add(Box.createRigidArea(new Dimension(5, 10)));
                itemSearchPanel.add(resetSearchButton);

                // ITEM DISPLAY PANEL //
                ArrayList<Integer> users = new ArrayList<Integer>();

                //JList bids = new JList(listModel);
                String[] columnNames = {"ID", "Title", "Description", "Category", "Vendor ID", "Start time", "Close Time", "Reserve Price"};
//                tableData = {{new Integer(1), "random", "pretty awesome pretty awesomepretty awesome pretty awesome ", "Sports", "242", "01:42", "04:42", "£123.00", values}};

                tableModel = new DefaultTableModel(columnNames, 0);
                // Makes jtable uneditable apart from the last bids comboBox which is only there to show a list of bids
                // Needs to be editable to see contents
                table = new JTable(tableModel) {
                    public boolean isCellEditable(int r, int c) {
                        if (c == 8) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                };

                TableCellRenderer tableCellRenderer = new DefaultTableCellRenderer() {

                    SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                    public Component getTableCellRendererComponent(JTable table,
                                                                   Object value, boolean isSelected, boolean hasFocus,
                                                                   int row, int column) {
                        if( value instanceof Date) {
                            value = f.format(value);
                        }
                        return super.getTableCellRendererComponent(table, value, isSelected,
                                hasFocus, row, column);
                    }
                };

//                ((DefaultTableModel)table.getModel()).removeRow(rowToRemove);
                table.getColumnModel().getColumn(5).setCellRenderer(tableCellRenderer);
                table.getColumnModel().getColumn(6).setCellRenderer(tableCellRenderer);



                table.setRowHeight(0, 20);
                table.getColumn("ID").setPreferredWidth(10);
                table.getColumn("Title").setPreferredWidth(150);
                table.getColumn("Description").setPreferredWidth(250);
                table.getColumn("Reserve Price").setPreferredWidth(20);

                itemDisplayPanel.add(table.getTableHeader());
                itemDisplayPanel.add(table);

                viewItemsPanel.add(itemSearchPanel, BorderLayout.WEST);
                viewItemsPanel.add(itemDisplayPanel, BorderLayout.CENTER);
                // Created tabbed panes:

                tabbedPane.addTab("View items", viewItemsPanel);


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
                itemDescriptionField.setMaximumSize(new Dimension(200, 50));
                JLabel itemCategoryLabel = new JLabel("Category");

                // Creating categories JComboBox

                String[] categoriesArray = {"Home & Garden", "Sports", "Electronics", "Jewellery & Watches", "Toys & Games",
                        "Clothing", "Books & Comics", "Other"};
                JComboBox categoriesCombo = new JComboBox(categoriesArray);
                categoriesCombo.setMaximumSize(new Dimension(200, 25));

                JLabel itemReservePriceLabel = new JLabel("Reserve Price");
                JTextField itemReservePriceField = new JTextField();
                itemReservePriceField.setMaximumSize(new Dimension(200, 25));
                JLabel startTimeLabel = new JLabel("Start time");

                // Set start date and time with JSpinner:
                SpinnerDateModel sdm = new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY);
                JSpinner startDateSpinner = new JSpinner(sdm);
                JSpinner.DateEditor sde = new JSpinner.DateEditor(startDateSpinner, "dd/MM/yyyy HH:mm:ss");
                startDateSpinner.setEditor(sde);
                startDateSpinner.setMaximumSize(new Dimension(200,25));

                JLabel endTimeLabel = new JLabel("End time");
                // Set end date and time with JSpinner:
                SpinnerDateModel edm = new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY);
                JSpinner endDateSpinner = new JSpinner(edm);
                JSpinner.DateEditor ede = new JSpinner.DateEditor(endDateSpinner, "dd/MM/yyyy HH:mm:ss");
                endDateSpinner.setEditor(ede);
                endDateSpinner.setMaximumSize(new Dimension(200,25));


                JButton submitItemButton = new JButton("Submit item");
                submitItemButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String category = (String)categoriesCombo.getSelectedItem();
                        Date startDate = (Date) startDateSpinner.getValue();
                        Date endDate = (Date) endDateSpinner.getValue();
                        comms.sendSellItemMessage(itemTitleField.getText(), itemDescriptionField.getText(),category, activeUser,
                                startDate, endDate, Integer.parseInt(itemReservePriceField.getText()));
                    }
                });

                // Center components
                itemTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                itemTitleField.setAlignmentX(Component.CENTER_ALIGNMENT);
                itemDescriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                itemDescriptionField.setAlignmentX(Component.CENTER_ALIGNMENT);
                itemCategoryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                categoriesCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
                itemReservePriceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                itemReservePriceField.setAlignmentX(Component.CENTER_ALIGNMENT);
                startTimeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                startDateSpinner.setAlignmentX(Component.CENTER_ALIGNMENT);
                endTimeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                endDateSpinner.setAlignmentX(Component.CENTER_ALIGNMENT);
                submitItemButton.setAlignmentX(Component.CENTER_ALIGNMENT);


                itemPanelBottom.add(Box.createRigidArea(new Dimension(5, 25)));
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
                itemPanelBottom.add(Box.createRigidArea(new Dimension(5, 5)));
                itemPanelBottom.add(startTimeLabel);
                itemPanelBottom.add(Box.createRigidArea(new Dimension(5, 5)));
                itemPanelBottom.add(startDateSpinner);
                itemPanelBottom.add(Box.createRigidArea(new Dimension(5, 5)));
                itemPanelBottom.add(endTimeLabel);
                itemPanelBottom.add(Box.createRigidArea(new Dimension(5, 5)));
                itemPanelBottom.add(endDateSpinner);
                itemPanelBottom.add(Box.createRigidArea(new Dimension(5, 25)));
                itemPanelBottom.add(submitItemButton);

                submitItemPanel.add(itemPanelBottom, BorderLayout.CENTER);
                tabbedPane.addTab("Sell item", submitItemPanel);
            }

            protected void addItem(int itemID, String title, String description, String catKeyword,
                                   String vendorID,Date startTime, Date closeTime, int reservePrice, ArrayList<Bid> bidList) {

                Object[] objs = {itemID, title, description, catKeyword, vendorID, startTime, closeTime, reservePrice, bidList};

                tableModel.addRow(objs);
            }

            protected void newSearchTable(ArrayList<Item> items) {
                tableModel.removeRow(0);
               // tableModel.setRowCount(0);
//                for (Item item : items) {
//                    Object[] objs = {item.getItemID(), item.getTitle(), item.getDescription(), item.getCatKeyword(), item.getVendorID(),
//                            item.getStartTime(), item.getCloseTime(), item.getReservePrice()};
//                    tableModel.addRow(objs);
//
//                }
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