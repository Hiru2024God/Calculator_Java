package gui;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;



public class calculator extends JFrame {

    private JTextField display;
    private final JLabel jlabel;
    private double firstNumber = 0;
    private String operator = "";
    private boolean isOperatorClicked = false;       //if button click or not

    public calculator() {
        setTitle("My Calculator");
        setSize(360, 500);
        setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        jlabel = new JLabel("Standard Calculator");
        jlabel.setFont(new Font("Arial", Font.BOLD, 20));
        jlabel.setHorizontalAlignment(JLabel.LEFT);
        topPanel.add(jlabel, BorderLayout.NORTH);

        display = new JTextField("0") {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque() && getBorder() instanceof RoundedBorder) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        display.setFont(new Font("Arial", Font.BOLD, 24));
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(15),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        topPanel.add(display, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        JPanel buttonPanelWrapper = new JPanel(new BorderLayout());
        buttonPanelWrapper.setBorder(new EmptyBorder(0, 15, 15, 15));

        JPanel buttonPanel = new JPanel(new GridLayout(6, 4, 5, 5));

        buttonPanel.add(createButton("CE", Color.WHITE));
        buttonPanel.add(createButton("C", Color.WHITE));
        buttonPanel.add(createButton("⌫", Color.WHITE));
        buttonPanel.add(createButton("%", Color.WHITE));

        buttonPanel.add(createButton("1/x", Color.WHITE));
        buttonPanel.add(createButton("x²", Color.WHITE));
        buttonPanel.add(createButton("√x", Color.WHITE));
        buttonPanel.add(createButton("/", Color.WHITE));

        buttonPanel.add(createButton("7", Color.WHITE));
        buttonPanel.add(createButton("8", Color.WHITE));
        buttonPanel.add(createButton("9", Color.WHITE));
        buttonPanel.add(createButton("×", Color.WHITE));

        buttonPanel.add(createButton("4", Color.WHITE));
        buttonPanel.add(createButton("5", Color.WHITE));
        buttonPanel.add(createButton("6", Color.WHITE));
        buttonPanel.add(createButton("-", Color.WHITE));

        buttonPanel.add(createButton("1", Color.WHITE));
        buttonPanel.add(createButton("2", Color.WHITE));
        buttonPanel.add(createButton("3", Color.WHITE));
        buttonPanel.add(createButton("+", Color.WHITE));

        buttonPanel.add(createButton("±", Color.WHITE));
        buttonPanel.add(createButton("0", Color.WHITE));
        buttonPanel.add(createButton(".", Color.WHITE));
        buttonPanel.add(createButton("=", new Color(0, 150, 255)));

        buttonPanelWrapper.add(buttonPanel, BorderLayout.CENTER);
        add(buttonPanelWrapper, BorderLayout.CENTER);
        setVisible(true);
    }

    static class RoundedBorder extends AbstractBorder {

        public RoundedBorder(int radius) {}

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 150, 255));
            g2.drawRoundRect(x, y, width - 1, height - 1, 10, 10);
        }
    }

    class RoundedButton extends JButton {
        private static final int RADIUS = 10;

        public RoundedButton(String text) {
            super(text);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFont(new Font("Arial", Font.BOLD, 18));
            setMargin(new Insets(10, 20, 10, 20));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(getBackground());
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), RADIUS, RADIUS);
            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.GRAY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS, RADIUS);
            g2.dispose();
        }
    }

    private JButton createButton(String text, Color bg) {
        RoundedButton button = new RoundedButton(text);
        button.setBackground(bg);
        button.addActionListener(new ButtonClickListener());
        return button;
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.matches("[0-9]")) {
                if (isOperatorClicked || display.getText().equals("0")) {
                    display.setText(command);
                    isOperatorClicked = false;
                } else {
                    display.setText(display.getText() + command);
                }
            } else if (command.equals("C") || command.equals("CE")) {
                display.setText("0");
                firstNumber = 0;
                operator = "";
            } else if (command.equals("+") || command.equals("-") || command.equals("×") || command.equals("/")) {
                firstNumber = Double.parseDouble(display.getText());
                operator = command;
                isOperatorClicked = true;
            } else if (command.equals("=")) {
                if (!operator.isEmpty()) {
                    double secondNumber = Double.parseDouble(display.getText());
                    double result = calculate(firstNumber, secondNumber, operator);
                    display.setText(String.valueOf(result));
                    operator = "";
                    isOperatorClicked = true;
                }
            } else if (command.equals("√x")) {
                double num = Double.parseDouble(display.getText());
                display.setText(num >= 0 ? String.valueOf(Math.sqrt(num)) : "Error");
            } else if (command.equals("x²")) {
                double num = Double.parseDouble(display.getText());
                display.setText(String.valueOf(num * num));
            } else if (command.equals("1/x")) {
                double num = Double.parseDouble(display.getText());
                display.setText(num != 0 ? String.valueOf(1 / num) : "Error");
            } else if (command.equals("%")) {
                double num = Double.parseDouble(display.getText());
                display.setText(String.valueOf(num / 100));
            } else if (command.equals("±")) {
                double num = Double.parseDouble(display.getText());
                display.setText(String.valueOf(num * -1));
            } else if (command.equals("⌫")) {
                String current = display.getText();
                if (current.length() > 1)
                    display.setText(current.substring(0, current.length() - 1));
                else
                    display.setText("0");
            } else if (command.equals(".")) {
                if (!display.getText().contains(".")) {
                    display.setText(display.getText() + ".");
                }
            }
        }

        private double calculate(double a, double b, String op) {
            switch (op) {
                case "+": 
                    return a + b;
                case "-": 
                    return a - b;
                case "×": 
                    return a * b;
                case "/": 
                    return b != 0 ? a / b : Double.NaN;
                default: 
                    return 0;
            }
        }
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        new calculator();
    }
}
