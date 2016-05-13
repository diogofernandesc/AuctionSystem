import com.sun.org.glassfish.external.statistics.annotations.Reset;

import java.awt.*;
import java.awt.event.*;

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
    ArrayList<Item> activeUserItems;

    public Client() throws Exception {
        activeUserItems = new ArrayList<Item>();
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

    // Class responsible for GUI
    class Display extends JFrame {

        private JPanel panels;
        private CardLayout cardLayout;
        SignInPanel signInPanel;
        RegistrationPanel registrationPanel;
        MainClientUI mainUIPanel;
        JTextField currentBidField;
        JFrame windowFrame;
        DefaultTableModel seeTableModel;
        DefaultTableModel auctionsTableModel;
        ArrayList<Object[]> objectList = new ArrayList<Object[]>();
        JLabel welcomeLabel;

        public Display(String title) {
            super(title);
        }

        protected void init() {
            this.setPreferredSize(new Dimension(1200, 600));
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);

            Container container = this.getContentPane();
            container.setLayout(new FlowLayout());

            signInPanel = new SignInPanel();
            registrationPanel = new RegistrationPanel();
            mainUIPanel = new MainClientUI();

            // Contains the different 'screens'
            panels = new JPanel();
            panels.setLayout(new CardLayout());

            // Adding different panels to the main Card Layout Panel
            // CardLayout allows different "screens" on the same frame
            panels.add(signInPanel, "sign in");
            panels.add(registrationPanel, "register");
            panels.add(mainUIPanel, "main ui");

            cardLayout = (CardLayout) (panels.getLayout());
            // Sets initial "screen" to the sign in screen
            cardLayout.show(panels, "sign in");

            container.add(panels);
            this.pack();
            this.setVisible(true);
            this.setResizable(false);

        }

        /*
         * Message either invalid or the username
         */
        public void receiveRegisterMessage(String message) {
            if (message.equals("invalid")) {
                JOptionPane.showMessageDialog(window, "Username already in use", "Invalid username",JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(window, "You are registered under: "+ message);
                cardLayout.show(panels, "sign in");
            }
        }

        /*
         * The message can be either wrong password
         * or wrong username
         * Depending on which it is an option pane is opened with the suitable error
         * If successful, the welcome label in the main gui is set to have the user's username
         * Panel is also changed to the main ui panel
         */
        public void receiveSignInMessage(String message) {
            String[] successMessage = message.split(",");
            if (successMessage[0].equals("success")) {
                cardLayout.show(panels, "main ui");
                activeUser = successMessage[1];
                welcomeLabel.setText("Welcome, "+activeUser);

            } else if (message.equals("wrong password")) {
                JOptionPane.showMessageDialog(window, "The password you entered is incorrect", "Invalid password", JOptionPane.ERROR_MESSAGE);

            } else if (message.equals("wrong username")) {
                JOptionPane.showMessageDialog(window, "The username you entered does not exist", "Invalid username", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Explained in addItem
        public void receiveSellItemMessage(SellItemMessage message) {
            mainUIPanel.addItem(message.getItemID(), message.getTitle(), message.getDescription(), message.getCatKeyword(), activeUser, message.getStartTime(),
                    message.getCloseTime(), message.getReservePrice(), message.getBidList());
        }

        // Explained in newSearchTable
        public void receiveViewItemMessage(ViewItemMessage message) {
            ArrayList<Item> items = message.getSearchedItems();
            mainUIPanel.newSearchTable(items);
        }

        // Explained in newSearchTable
        public void receiveResetTableMessage(ResetTableMessage message) {
            ArrayList<Item> items = message.getResetList();
            mainUIPanel.newSearchTable(items);

        }

        /*
         * The message gotten represents whether the bid was successful or not
         * If the response is successful then the bid field is set to the new highest bid
         * The table representing the user's own bids is also updated with the new item they've bid on
         * A pop up message is also displayed with the appropriate message
         */
        public void receiveBidMessage(BidMessage message) {
            if(message.getResponse().equals("success")) {
                currentBidField.setText("£"+String.valueOf(message.getBidAmount()));
                Item item = message.getItem();
                activeUserItems.add(item);
                Object[] objs = {item.getItemID(), item.getTitle(), item.getReservePrice(), message.getBidAmount(), item.getCloseTime()};
                seeTableModel.addRow(objs);
                JOptionPane.showMessageDialog(windowFrame, "Bid successful", "Bid successful", JOptionPane.INFORMATION_MESSAGE);

            }
            else if (message.getResponse().equals("fail")) {
                JOptionPane.showMessageDialog(windowFrame, "Bid amount too low", "Bid amount too low",JOptionPane.ERROR_MESSAGE);
            }
        }

        /*
         * This JTable is responsible for keeping track of all the items the user has put up for sale
         * This simply adds a row with the designated information to that table
         */
        public void receiveAddToSellMessage(AddToSellListMessage message) {

            Object[] objs = {message.getItemID(), message.getItemTitle(), message.getStatus(), message.getItemRP(), message.getCurrentBid(), message.getItemCloseTime()};
            auctionsTableModel.addRow(objs);
        }

        /*
         * Simply shows a message depending on whether the user has won, lost, the auction is ongoing or the auction finished with no bids
         * This occurs when the user presses the notifications button in the main ui page
         */
        public void receiveWinMessage(WinMessage message) {
            if (message.getResponse().equals("won")) {
                JOptionPane.showMessageDialog(window, "You have the item: "+message.getItem().getTitle(), "You have won!", JOptionPane.OK_OPTION);

            } else if (message.getResponse().equals("lost")) {
                JOptionPane.showMessageDialog(window, "You have lost the item: "+message.getItem().getTitle(), "You have lost!", JOptionPane.ERROR_MESSAGE);

            } else if (message.getResponse().equals("ongoing")) {
                JOptionPane.showMessageDialog(window, "Auction ongoing", "Auction ongoing", JOptionPane.INFORMATION_MESSAGE);

            } else if (message.getResponse().equals("no bid")) {
                JOptionPane.showMessageDialog(window, "No bids happened", "No bids happened", JOptionPane.INFORMATION_MESSAGE);
            }
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

                // ActionListener to send a sign in message
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
            JTable seeTable;
            JTable auctionsTable;
            String category;
            Date startDate;
            Date endDate;
            String title;
            String description;
            double reservePrice;


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

                /* ------ Notifications --------- */

                JPanel notificationPanel = new JPanel();
                notificationPanel.setLayout(new BoxLayout(notificationPanel, BoxLayout.X_AXIS));

                welcomeLabel = new JLabel("Welcome, "+activeUser);
                JButton checkNotifications = new JButton("Check for notifications");
                JButton logOutButton = new JButton("Log out");
                logOutButton.setForeground(Color.WHITE);
                logOutButton.setBackground(Color.RED);

                // Checks the status of the last item the user has won, lost
                // Also checks the auctions the user has up
                checkNotifications.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (activeUserItems.size() == 1) {
                            comms.sendWinMessage(activeUserItems.get(activeUserItems.size() - 1), activeUser);
                        } else {
                            comms.sendWinMessage(activeUserItems.get(activeUserItems.size()), activeUser);
                        }

                    }
                });

                // Takes user to login screen
                logOutButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        cardLayout.show(panels, "sign in");
                    }
                });

                notificationPanel.add(Box.createRigidArea(new Dimension(5, 0)));
                notificationPanel.add(welcomeLabel);
                notificationPanel.add(Box.createRigidArea(new Dimension(500, 0)));
                notificationPanel.add(checkNotifications);
                notificationPanel.add(Box.createRigidArea(new Dimension(300, 0)));
                notificationPanel.add(logOutButton);

                viewItemsPanel.add(notificationPanel, BorderLayout.NORTH);

                /* ----- Item search and view */

                itemSearchPanel = new JPanel();
                itemSearchPanel.setLayout(new BoxLayout(itemSearchPanel, BoxLayout.Y_AXIS));

                itemSearchPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                itemSearchPanel.setBorder(new TitledBorder(new EtchedBorder(), "Item Search"));
                itemSearchPanel.setPreferredSize(new Dimension(195, 0));

                itemDisplayPanel = new JPanel();
                itemDisplayPanel.setLayout(new BoxLayout(itemDisplayPanel, BoxLayout.Y_AXIS));
                itemDisplayPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                itemDisplayPanel.setBorder(new TitledBorder(new EtchedBorder(), "Item Display"));
                itemDisplayPanel.setPreferredSize(new Dimension(990, 510)); //535

                // Labels and fields for ID and created after:
                JLabel itemIDLabel = new JLabel("Item ID");
                itemIDLabel.setFont(new Font(itemIDLabel.getFont().getFontName(), Font.BOLD, 13));
                JTextField itemIDField = new JTextField();
                itemIDField.setMaximumSize(new Dimension(150, 20));

                // Vendor ID:

                JLabel vendorIDLabel = new JLabel("Vendor ID");
                vendorIDLabel.setFont(new Font(vendorIDLabel.getFont().getFontName(), Font.BOLD, 13));
                JTextField vendorIDField = new JTextField();
                vendorIDField.setMaximumSize(new Dimension(150,20));

                JLabel createdAfterLabel = new JLabel("Created after");
                createdAfterLabel.setFont(new Font(createdAfterLabel.getFont().getFontName(), Font.BOLD, 13));

                // Created after JSpinner for time and date:
                Date date = new Date();
                SpinnerDateModel cdm = new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY);
                JSpinner createdDateSpinner = new JSpinner(cdm);
                JSpinner.DateEditor cde = new JSpinner.DateEditor(createdDateSpinner, "dd/MM/yyyy HH:mm:ss");
                createdDateSpinner.setEditor(cde);
                createdDateSpinner.setMaximumSize(new Dimension(150,20));

                // Categories JComboBox:
                JLabel categoriesLabel = new JLabel("Categories");
                categoriesLabel.setFont(new Font(categoriesLabel.getFont().getFontName(), Font.BOLD, 13));
                String[] categoriesList = {"Home & Garden", "Sports", "Electronics", "Jewellery & Watches", "Toys & Games",
                        "Clothing", "Books & Comics", "Other"};
                JComboBox categoriesComboBox = new JComboBox(categoriesList);
                categoriesComboBox.setMaximumSize(new Dimension(150, 20));


                // Search button:
                JButton searchButton = new JButton("Search");
                searchButton.setMaximumSize(new Dimension(125, 25));

                /*
                 * Gets the necessary information the user wants to search items for
                 * Updates the table accordingly
                 */
                searchButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String itemID = itemIDField.getText();
                        String vendorID = vendorIDField.getText();

                        if (itemID == null) {
                            itemID= "";
                        }

                        if (vendorID == null) {
                            vendorID = "";
                        }
                        String category = (String)categoriesComboBox.getSelectedItem();
                        Date date = (Date) createdDateSpinner.getValue();
                        comms.sendViewItemMessage(itemID, vendorID, category, date);

                    }
                });

                // Reset search button:

                JButton resetSearchButton = new JButton("Reset search");
                resetSearchButton.setMaximumSize(new Dimension(125,25));

                // Send a reset table message to bring back the table to how it was originally
                resetSearchButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        comms.sendResetTableMessage();
                    }
                });


                itemIDLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                itemIDField.setAlignmentX(Component.CENTER_ALIGNMENT);
                vendorIDLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                vendorIDField.setAlignmentX(Component.CENTER_ALIGNMENT);
                categoriesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                categoriesComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
                createdAfterLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                createdDateSpinner.setAlignmentX(Component.CENTER_ALIGNMENT);
                searchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                resetSearchButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                itemSearchPanel.add(Box.createRigidArea(new Dimension(0, 35)));
                itemSearchPanel.add(itemIDLabel);
                itemSearchPanel.add(Box.createRigidArea(new Dimension(5, 5)));
                itemSearchPanel.add(itemIDField);
                itemSearchPanel.add(Box.createRigidArea(new Dimension(5, 15)));
                itemSearchPanel.add(vendorIDLabel);
                itemSearchPanel.add(Box.createRigidArea(new Dimension(5, 5)));
                itemSearchPanel.add(vendorIDField);
                itemSearchPanel.add(Box.createRigidArea(new Dimension(5, 15)));
                itemSearchPanel.add(categoriesLabel);
                itemSearchPanel.add(Box.createRigidArea(new Dimension(5, 5)));
                itemSearchPanel.add(categoriesComboBox);
                itemSearchPanel.add(Box.createRigidArea(new Dimension(5, 15)));
                itemSearchPanel.add(createdAfterLabel);
                itemSearchPanel.add(Box.createRigidArea(new Dimension(5, 5)));
                itemSearchPanel.add(createdDateSpinner);
                itemSearchPanel.add(Box.createRigidArea(new Dimension(5, 45)));
                itemSearchPanel.add(searchButton);
                itemSearchPanel.add(Box.createRigidArea(new Dimension(5, 10)));
                itemSearchPanel.add(resetSearchButton);

                // ITEM DISPLAY PANEL //


                String[] columnNames = {"ID", "Title", "Description", "Category", "Seller ID", "Start Time", "Close Time", "Reserve Price"};

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

                // Makes it so the dates on the JTable are formatted properly
                TableCellRenderer tableCellRenderer = new DefaultTableCellRenderer() {

                    SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss dd/MM/yy");

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

                table.getColumnModel().getColumn(5).setCellRenderer(tableCellRenderer);
                table.getColumnModel().getColumn(6).setCellRenderer(tableCellRenderer);
                table.setRowHeight(0, 20);
                table.getColumn("ID").setPreferredWidth(5);
                table.getColumn("Title").setPreferredWidth(100);
                table.getColumn("Description").setPreferredWidth(150);
                table.getColumn("Category").setPreferredWidth(60);
                table.getColumn("Seller ID").setPreferredWidth(50);
                table.getColumn("Start Time").setPreferredWidth(50);
                table.getColumn("Close Time").setPreferredWidth(50);
                table.getColumn("Reserve Price").setPreferredWidth(20);

                // Opens up a small window frame which allows the user to bid on items
                table.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            JTable target = (JTable)e.getSource();
                            int row = target.getSelectedRow();
                            String itemTitle = (String)target.getValueAt(row,1);
                            int itemID = (Integer)target.getValueAt(row,0);
                            String description = (String)target.getValueAt(row,2);
                            String category = (String)target.getValueAt(row,3);
                            double rp = (Double)target.getValueAt(row,7);
                            Date startTime = (Date)target.getValueAt(row, 5);
                            Date closeTime = (Date)target.getValueAt(row, 6);
                            String sellerID = (String)target.getValueAt(row,4);

                            createFrame(itemTitle, itemID, description, category, sellerID ,rp, startTime, closeTime);
                        }
                    }
                });

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

                // Gets the necessary information from the fields in the JPanel the user uses to sell an item
                // Clicking the button collects information and sets an item up for auction
                // Pop up window to tell the user the item is up for action also appears upon click
                submitItemButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        category = (String)categoriesCombo.getSelectedItem();
                        startDate = (Date) startDateSpinner.getValue();
                        endDate = (Date) endDateSpinner.getValue();
                        title = itemTitleField.getText();
                        description = itemDescriptionField.getText();
                        reservePrice = Double.parseDouble(itemReservePriceField.getText());
                        comms.sendSellItemMessage(title, description,category, activeUser,
                                startDate, endDate, reservePrice);

                        JOptionPane.showMessageDialog(window, "Your item is now up to for auction!", "Auction started", JOptionPane.INFORMATION_MESSAGE);
                        tabbedPane.setSelectedIndex(0);

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


                // --- SEE ACTIVE BIDS PANE --- //

                // JTable responsible for the active user's bids
                JPanel activeBidsPanel = new JPanel();
                activeBidsPanel.setLayout(new BoxLayout(activeBidsPanel, BoxLayout.Y_AXIS));
                tabbedPane.add("My bids", activeBidsPanel);

                String[] seeBidsColumns = {"Item ID", "Title", "Reserve Price", "My bid", "Close Time"};

                seeTableModel = new DefaultTableModel(seeBidsColumns, 0);
                seeTable = new JTable(seeTableModel) {
                    public boolean isCellEditable(int r, int c) {
                        if (c == 8) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                };

                seeTable.getColumnModel().getColumn(4).setCellRenderer(tableCellRenderer);

                seeTable.setRowHeight(0, 20);

                activeBidsPanel.add(seeTable.getTableHeader());
                activeBidsPanel.add(seeTable);


                // ------- My auctions Pane ------ //

                // JTable responsible for the status of the auctions the user has started
                // Refreshing it collects new auctions the user may have started in the mean time
                JPanel myAuctionsPanel = new JPanel();
                myAuctionsPanel.setLayout(new BoxLayout(myAuctionsPanel, BoxLayout.Y_AXIS));
                tabbedPane.add("My Auctions", myAuctionsPanel);

                JButton refreshButton = new JButton("Refresh");
                refreshButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                myAuctionsPanel.add(refreshButton);
                refreshButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        comms.sendAddToSellListMessage(title, reservePrice, endDate);
                    }
                });
                //JList bids = new JList(listModel);
                String[] auctionsColumns = {"Item ID", "Title", "Status", "Reserve Price", "Current Bid", "Close Time"};

                auctionsTableModel = new DefaultTableModel(auctionsColumns, 0);
                // Makes jtable uneditable apart from the last bids comboBox which is only there to show a list of bids
                // Needs to be editable to see contents
                auctionsTable = new JTable(auctionsTableModel) {
                    public boolean isCellEditable(int r, int c) {
                        if (c == 8) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                };

//                ((DefaultTableModel)table.getModel()).removeRow(rowToRemove);
                auctionsTable.getColumnModel().getColumn(5).setCellRenderer(tableCellRenderer);

                auctionsTable.setRowHeight(0, 20);

                myAuctionsPanel.add(auctionsTable.getTableHeader());
                myAuctionsPanel.add(auctionsTable);

            }

            // ------ OPTION PANE TO BID ON ITEMS ------- //
            public void createFrame(String itemTitle, int itemID, String description, String category, String sellerID, double reservePrice, Date startTime, Date closeTime) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        windowFrame = new JFrame("Bid");
                        windowFrame.setPreferredSize(new Dimension(500,500));
                        windowFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        JPanel bidPanel = new JPanel();
                        windowFrame.setContentPane(bidPanel);
                        bidPanel.setLayout(new BorderLayout());
                        JPanel bidBottom = new JPanel();
                        bidBottom.setLayout(new BoxLayout(bidBottom, BoxLayout.Y_AXIS));
                        JLabel bidTitleLabel = new JLabel("You are about to bid on..");
                        bidTitleLabel.setFont(new Font(bidTitleLabel.getFont().getFontName(), Font.BOLD, 20));
                        bidTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        bidPanel.add(bidTitleLabel, BorderLayout.NORTH);

                        JLabel bidItemTitle = new JLabel(itemTitle);
                        bidItemTitle.setFont(new Font(bidItemTitle.getFont().getFontName(), Font.BOLD,15));
                        JLabel bidItemID = new JLabel("Item ID: "+String.valueOf(itemID));
                        JLabel bidSellerID = new JLabel("You are buying from: "+ sellerID);
                        JLabel currentBid = new JLabel("Current bid");
                        currentBidField = new JTextField("No initial bid placed yet");
                        currentBidField.setMaximumSize(new Dimension(150,20));
                        currentBidField.setEditable(false);
                        JLabel bidItemCategory = new JLabel("Category: "+category);
                        JLabel itemDescriptionLabel = new JLabel("Description");
                        JTextArea itemDescriptionArea = new JTextArea(description);
                        itemDescriptionArea.setMaximumSize(new Dimension(225,100));
                        itemDescriptionArea.setLineWrap(true);
                        SimpleDateFormat dt = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

                        JLabel bidStartTime = new JLabel("Start time: "+ dt.format(startTime));
                        JLabel bidCloseTime = new JLabel("Close time: "+ dt.format(closeTime));
                        bidCloseTime.setForeground(Color.RED);
                        JLabel bidItemRP = new JLabel("Reserve Price: £"+reservePrice);

                        JButton submitBidButton = new JButton("Submit");
                        JTextField bidAmountField = new JTextField("Enter bid amount here");
                        bidAmountField.setMaximumSize(new Dimension(150,20));

                        // Submits bid and closes window
                        submitBidButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String itemID = bidItemID.getText().split(": ")[1];
                                comms.sendBidMessage(Integer.parseInt(itemID),Double.parseDouble(bidAmountField.getText()),activeUser);
                                windowFrame.dispatchEvent(new WindowEvent(windowFrame, WindowEvent.WINDOW_CLOSING));
                            }
                        });


                        bidItemTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
                        bidItemID.setAlignmentX(Component.CENTER_ALIGNMENT);
                        bidSellerID.setAlignmentX(Component.CENTER_ALIGNMENT);
                        currentBid.setAlignmentX(Component.CENTER_ALIGNMENT);
                        currentBidField.setAlignmentX(Component.CENTER_ALIGNMENT);
                        bidItemCategory.setAlignmentX(Component.CENTER_ALIGNMENT);
                        itemDescriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                        itemDescriptionArea.setAlignmentX(Component.CENTER_ALIGNMENT);
                        bidStartTime.setAlignmentX(Component.CENTER_ALIGNMENT);
                        bidCloseTime.setAlignmentX(Component.CENTER_ALIGNMENT);
                        bidItemRP.setAlignmentX(Component.CENTER_ALIGNMENT);
                        bidAmountField.setAlignmentX(Component.CENTER_ALIGNMENT);
                        submitBidButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                        bidBottom.add(Box.createRigidArea(new Dimension(5, 15)));
                        bidBottom.add(bidItemTitle);
                        bidBottom.add(Box.createRigidArea(new Dimension(5, 5)));
                        bidBottom.add(bidItemID);
                        bidBottom.add(Box.createRigidArea(new Dimension(5, 10)));
                        bidBottom.add(currentBid);
                        bidBottom.add(Box.createRigidArea(new Dimension(5, 5)));
                        bidBottom.add(currentBidField);
                        bidBottom.add(Box.createRigidArea(new Dimension(5, 25)));
                        bidBottom.add(bidSellerID);
                        bidBottom.add(Box.createRigidArea(new Dimension(5, 5)));
                        bidBottom.add(bidItemCategory);
                        bidBottom.add(Box.createRigidArea(new Dimension(5, 5)));
                        bidBottom.add(itemDescriptionLabel);
                        bidBottom.add(itemDescriptionArea);
                        bidBottom.add(Box.createRigidArea(new Dimension(5, 5)));
                        bidBottom.add(bidStartTime);
                        bidBottom.add(Box.createRigidArea(new Dimension(5, 5)));
                        bidBottom.add(bidCloseTime);
                        bidBottom.add(Box.createRigidArea(new Dimension(5, 5)));
                        bidBottom.add(bidItemRP);
                        bidBottom.add(Box.createRigidArea(new Dimension(5, 20)));
                        bidBottom.add(bidAmountField);
                        bidBottom.add(Box.createRigidArea(new Dimension(5, 5)));
                        bidBottom.add(submitBidButton);
                        bidBottom.add(Box.createRigidArea(new Dimension(5, 10)));

                        bidPanel.add(bidBottom, BorderLayout.CENTER);

                        windowFrame.pack();
                        windowFrame.setVisible(true);
                        windowFrame.setResizable(false);

                    }
                });
            }

            /*
             * Creates an object array with multiple items wihich is then passed to the table model to populate the JTable
             * The JTable in question is the one the user sees when searching for items
             */
            protected void addItem(int itemID, String title, String description, String catKeyword,
                                   String vendorID,Date startTime, Date closeTime, double reservePrice, ArrayList<Bid> bidList) {

                Object[] objs = {itemID, title, description, catKeyword, vendorID, startTime, closeTime, reservePrice};

                tableModel.addRow(objs);
            }

            // This creates a new tableModel and set the JTable to it
            // This JTable is representative of what the user would search for
            protected void newSearchTable(ArrayList<Item> items) {
                tableModel.setRowCount(0);
                for (Item item : items) {
                    Object[] objs = {item.getItemID(), item.getTitle(), item.getDescription(), item.getCatKeyword(), item.getVendorID(),
                            item.getStartTime(), item.getCloseTime(), item.getReservePrice()};
                    tableModel.addRow(objs);
                }
            }

        }
    }
}