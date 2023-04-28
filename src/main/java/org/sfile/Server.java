package org.sfile;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class Server {
    static ArrayList<MyFile> files = new ArrayList<>();

    public static void main(String[] args) {
        int fileId = 0;
        JFrame frame = new JFrame("Server");
        frame.setSize(400, 400);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(scrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JLabel title = new JLabel("File receiver");
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setBorder(new EmptyBorder(20, 0, 10, 0));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        frame.add(title);
        frame.add(scrollPane);
        frame.setVisible(true);
    }
}