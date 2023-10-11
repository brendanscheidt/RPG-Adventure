package rpggame;

import javax.swing.*;
import javax.swing.text.*;
import rpggame.Interfaces.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.List;

public class SwingUI implements GameUIInterface {
    private JFrame frame;
    private JTextPane textPane;
    private JTextField inputField;
    private JButton submitButton;
    private final BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    private final SimpleAttributeSet center;
    private JScrollPane scrollPane;

    public SwingUI() {
    // Initialize JFrame
    frame = new JFrame("CS214 Game");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Initialize JTextPane and set font
    textPane = new JTextPane();
    textPane.setEditable(false);
    Font font = new Font("Verdana", Font.BOLD, 16);
    textPane.setFont(font);

    // Initialize JTextField and center the text inside it
    inputField = new JTextField();
    inputField.setHorizontalAlignment(JTextField.CENTER);  // Center text in JTextField
    inputField.setPreferredSize(new Dimension(300, 30));  // Set preferred size


    // Initialize JButton
    submitButton = new JButton("Submit");
    
    // Initialize center attribute
    center = new SimpleAttributeSet();
    StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);

    ActionListener submitAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String userInput = inputField.getText();
            inputQueue.offer(userInput);  // Adds the input to the input queue
            inputField.setText("");
        }
    };
    
    submitButton.addActionListener(submitAction);

    // Add key listener to JTextField for Enter key
    inputField.addKeyListener(new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                submitAction.actionPerformed(null);
            }
        }
    });

    // Create a JPanel with a FlowLayout to center the inputField
    JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    inputPanel.add(inputField);

    // Add components to JFrame
    frame.setLayout(new BorderLayout());
    scrollPane = new JScrollPane(textPane);
    frame.add(scrollPane, BorderLayout.CENTER);
    frame.add(inputPanel, BorderLayout.NORTH);  // Add the panel instead of the field
    frame.add(submitButton, BorderLayout.SOUTH);

    // Center the frame
    frame.pack();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int height = screenSize.height;
    int width = screenSize.width;
    frame.setSize(width/2, height/2);
    frame.setLocationRelativeTo(null);

    // Show the JFrame
    frame.setVisible(true);
}


    @Override
    public void displayMessage(String message) {
      StyledDocument doc = textPane.getStyledDocument();
      try {
          doc.insertString(doc.getLength(), message + "\n", null);
          doc.setParagraphAttributes(0, doc.getLength(), center, false);
          
          // Auto-scroll to the end
          textPane.setCaretPosition(doc.getLength());
      } catch (BadLocationException e) {
          e.printStackTrace();
      }
    }
    
    public void displayInventory(EntityInterface entity) {
      List<ItemInterface> inventory = entity.exposeInventory();
      StyledDocument doc = textPane.getStyledDocument();

      try {
          if (entity instanceof Store) {
            StringBuilder inventoryString = new StringBuilder("\nStore Inventory:\n");
            if(inventory.isEmpty()) {
                inventoryString.append("Store inventory is empty.\n");
            }
              for (ItemInterface item : inventory) {
                  String itemInfo = item.getName() + " - $" + item.getPrice();
                  int numSpaces = 30 - itemInfo.length();
                  itemInfo = addSpaces(itemInfo, numSpaces);
                  itemInfo += "~~(" + item.getType() + ")";
                  inventoryString.append(itemInfo).append("\n");
              }
              doc.insertString(doc.getLength(), inventoryString.toString(), null);
          }

          if (entity instanceof Player) {
              StringBuilder inventoryString = new StringBuilder("\nPlayer Inventory:\n");
              if (inventory.isEmpty()) {
                  inventoryString.append("Player inventory is empty.\n");
              } else {
                  for (ItemInterface item : inventory) {
                      String itemInfo = item.getName();
                      int numSpaces = 20 - itemInfo.length();
                      itemInfo = addSpaces(itemInfo, numSpaces);
                      itemInfo += "~~(" + item.getType() + ")";
                      inventoryString.append(itemInfo).append("\n");
                  }
              }
              doc.insertString(doc.getLength(), inventoryString.toString(), null);
          }

          // Set the caret position to the end so it auto-scrolls
          textPane.setCaretPosition(doc.getLength());
      } catch (BadLocationException e) {
          e.printStackTrace();
      }
    }



    private String addSpaces(String str, int spaces) {
        for(int i = 0; i < spaces; i++) {
            str += " ";
        }
        return str;
    }

    public String getUserInput() {
        try {
            return inputQueue.take();  // Waits until an element becomes available
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
}
