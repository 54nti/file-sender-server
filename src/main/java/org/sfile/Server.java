package org.sfile;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    static ArrayList<MyFile> files = new ArrayList<>();

    public static void main(String[] args) throws IOException {
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

        ServerSocket serverSocket = new ServerSocket(1234);
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                int fileNameLength = dataInputStream.readInt();
                if (fileNameLength > 0) {
                    byte[] fileNameBytes = new byte[fileNameLength];
                    dataInputStream.readFully(fileNameBytes, 0, fileNameBytes.length);
                    String filename = new String(fileNameBytes);
                    int fileContentLength = dataInputStream.readInt();
                    if (fileContentLength > 0) {
                        byte[] fileContentBytes = new byte[fileContentLength];
                        dataInputStream.readFully(fileContentBytes, 0, fileContentLength);

                        JPanel fileRow = new JPanel();
                        fileRow.setLayout(new BoxLayout(fileRow, BoxLayout.Y_AXIS));

                        JLabel lFilename = new JLabel(filename);
                        lFilename.setFont(new Font("Arial", Font.BOLD, 20));
                        lFilename.setBorder(new EmptyBorder(10, 0, 10, 0));
                        lFilename.setAlignmentX(Component.CENTER_ALIGNMENT);
                        if (getFileExt((filename)).equalsIgnoreCase("txt")) {
                            fileRow.setName(String.valueOf(fileId));
                            fileRow.addMouseListener(getMyMouseListener());

                            fileRow.add(lFilename);
                            panel.add(fileRow);
                            frame.validate();
                        } else {
                            fileRow.setName(String.valueOf(fileId));
                            fileRow.addMouseListener(getMyMouseListener());

                            fileRow.add(lFilename);
                            panel.add(fileRow);

                            frame.validate();
                        }
                        files.add(new MyFile(fileId, filename, fileContentBytes, getFileExt(filename)));
                        fileId++;
                    }
                }
            } catch(IOException error) {
                error.printStackTrace();
            }
        }
    }

    // it should be static because we are using it in the main
    public static String getFileExt(String filename) {
        // would not work with .tar.gz
        int i = filename.lastIndexOf('.');
        if (0 < i) {
            return filename.substring(i + 1);
        } else {
            return "No file extension found";
        }
    }

    public static JFrame createFrame(String filename, byte[] data, String extension) {
        JFrame frame =  new JFrame("File downloader");
        frame.setSize(400, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("File downloader");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setBorder(new EmptyBorder(20, 0, 10, 0));

        JLabel prompt = new JLabel("Are you sure you want to download" + filename);
        prompt.setAlignmentX(Component.CENTER_ALIGNMENT);
        prompt.setFont(new Font("Arial", Font.BOLD, 20));
        prompt.setBorder(new EmptyBorder(20, 0, 10, 0));

        JButton yes = new JButton("Yes");
        yes.setPreferredSize(new Dimension(150, 75));
        yes.setFont(new Font("Arial", Font.BOLD, 20));

        JButton no = new JButton("No");
        no.setPreferredSize(new Dimension(150, 75));
        no.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel fileContent = new JLabel();
        fileContent.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttons = new JPanel();
        buttons.setBorder(new EmptyBorder(20, 0, 10, 0));
        buttons.add(yes);
        buttons.add(no);

        if (extension.equalsIgnoreCase("txt")) {
            fileContent.setText("<html>" + new String(data) + "</html>");
        } else {
            fileContent.setIcon(new ImageIcon(data));
        }

        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File fileToDownload = new File(filename);
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload);
                    fileOutputStream.write(data);
                    fileOutputStream.close();
                    frame.dispose();
                } catch(IOException error) {
                    error.printStackTrace();
                }
            }
        });

        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        panel.add(title);
        panel.add(prompt);
        panel.add(fileContent);
        panel.add(buttons);

        frame.add(panel);

        return frame;
    }

    // it should be static because we are using it in the main
    public static MouseListener getMyMouseListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel panel = (JPanel) e.getSource();
                int fileId = Integer.parseInt(panel.getName());
                for (MyFile file : files) {
                    if (file.getId() == fileId) {
                        JFrame  preview = createFrame(file.getName(), file.getData(), file.getExtension());
                        preview.setVisible(true);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
    }
}