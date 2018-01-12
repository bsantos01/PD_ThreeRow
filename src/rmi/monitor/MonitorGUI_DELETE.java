/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi.monitor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import rmi.commons.RemoteServiceInterface;

/**
 *
 * @author simao
 */
public class MonitorGUI_DELETE extends JFrame {

    private Container cp;
    private JPanel left = new JPanel();
    private JPanel left_middle_up = new JPanel();
    private JPanel left_middle_down = new JPanel();
    private JPanel right = new JPanel();
    private JPanel right_middle = new JPanel();
    private JPanel middle = new JPanel();
    private JPanel middle_middle = new JPanel();
    private JTextField pass = new JTextField(12);
    private JTextField user = new JTextField(12);
    private JTextField path = new JTextField(40);
    private JButton login = new JButton("Entrar");
    private JButton register = new JButton("Registar");
    private JButton logout = new JButton("Sair");
    private JButton browse = new JButton("Procurar...");
    private JButton send = new JButton("Enviar ficheiro");
    private JLabel online_users = new JLabel();
    private JLabel authentication_error = new JLabel();
    private JLabel loggedInfo = new JLabel();
    private JPanel logged = new JPanel();
    private JTable allFilesTable = new JTable();
    private JTable myCopiesTable = new JTable();
    private JScrollPane allFiles = new JScrollPane(allFilesTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JScrollPane myCopies = new JScrollPane(myCopiesTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    //private JFileChooser browser;

//    private ServerSocket uploadServerSocket;
//    private ServerSocket downloadServerSocket;
    private MonitorGUI_DELETE.Listeners listeners;
    private String loggedUser;
    private String directory;
//    private HashMap<RepFile, String> myCopiesMap;
//    private ArrayList<Thread> openedThreads; //threads de envios de ficheiro para o servidor
    private RemoteServiceInterface service;
//    private RMI_ClientInterface client;

    public MonitorGUI_DELETE() {
        super("Repositorio - Programacao Distribuida");

        this.service = service;
//        this.client = client;
        this.directory = directory;
        cp = getContentPane();
        listeners = new MonitorGUI_DELETE.Listeners();
        loggedUser = null;
//        if (!new File(directory).exists()) {
//            new File(directory).mkdirs();
//        }
//        browser = new JFileChooser(directory);
//        myCopiesMap = new HashMap<>();
//        openedThreads = new ArrayList<>();

        buildWindow();
        registerListeners();

        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        validate();
    }

    public void setUsername(String user) {
        loggedUser = user;
    }

    public String getUsername() {
        return loggedUser;
    }

//    public void fileDeleted(String deletedFile) {
//        postResponse(Constants.WARNING + deletedFile + Constants.REMOVED_BY_AUTHOR);
//        new File(directory + deletedFile).delete();
//        myCopiesMap.remove(new RepFile(deletedFile));
//        updateMyCopies();
//    }
//    public void fileUpdated(String updatedFile) throws RemoteException {
//        postResponse(Constants.WARNING + updatedFile + Constants.UPDATED_BY_AUTHOR);
//        new FileDownload().start();
//        server.getReadCopy(client, updatedFile);
//    }
    public void logged(boolean b) {
        left_middle_up.setVisible(!b);
        left_middle_down.setVisible(b);
        logged.setVisible(b);
        middle_middle.setVisible(b);
        right_middle.setVisible(b);
        online_users.setText("Online");

        if (!b) {
            user.setText("");
            pass.setText("");
            authentication_error.setText("");
        } else {
            loggedInfo.setText("Logged as: " + loggedUser);
            path.setText("");
            left_middle_down.removeAll();
            left_middle_down.add(logged);
            left_middle_down.add(Box.createVerticalStrut(15));
            left_middle_down.setMaximumSize(new Dimension(220, 55));
//            updateMyCopies();
        }
    }

    public void postResponse(String info) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(info);
        label.setFont(new Font(Font.DIALOG, Font.BOLD, 9));
        panel.add(label);
        left_middle_down.add(panel);
        left_middle_down.setMaximumSize(new Dimension(220, left_middle_down.getMaximumSize().height + 26));
        validate();
    }

    public void postAuthenticationError(String error) {
        authentication_error.setText(error);
    }

//    public void updateOnlineUsers(ArrayList<String> users, HashMap<String, ArrayList<String>> editCopies, HashMap<String, ArrayList<String>> readCopies) {
//        online_users.setText(Constants.ONLINE_USERS + users.size());
//
//        int howMany = right_middle.getComponentCount();
//        for (int i = howMany - 1; i > 2; i--) {
//            right_middle.remove(i);
//        }
//        for (String s : users) {
//            if (s.equals(loggedUser)) {
//                right_middle.add(new JLabel("    " + s + " (eu)"));
//                continue;
//            }
//            right_middle.add(new JLabel("    " + s));
//            if (editCopies.containsKey(s)) {
//                for (String ec : editCopies.get(s)) {
//                    right_middle.add(new JLabel("        " + ec + " (A)"));
//                }
//            }
//            if (readCopies.containsKey(s)) {
//                for (String rc : readCopies.get(s)) {
//                    right_middle.add(new JLabel("        " + rc + " (L)"));
//                }
//            }
//        }
//    }
//    public void updateRepositoryFiles(ArrayList<RepFile> files) {
//        Object[][] info = new Object[files.size()][Constants.TABLE_COLUMNS_NUMBER];
//
//        for (int i = 0; i < info.length; i++) {
//            RepFile rf = files.get(i);
//            if (rf.getAuthor().equals(loggedUser)) {
//                info[i] = new Object[]{rf.getName(), rf.getSize(), rf.getCreationDate(), rf.getLastModificationDate(), UIManager.getIcon("OptionPane.questionIcon"), UIManager.getIcon("OptionPane.warningIcon"), UIManager.getIcon("OptionPane.errorIcon")};
//            } else {
//                info[i] = new Object[]{rf.getName(), rf.getSize(), rf.getCreationDate(), rf.getLastModificationDate(), UIManager.getIcon("OptionPane.questionIcon"), null, null};
//            }
//        }
//
//        MyTableModel model = new MyTableModel(Constants.REP_TABLE_COLUMNS_NAME, info);
//        allFilesTable = new JTable(model);
//        new ButtonColumn(allFilesTable, listeners.new getReadCopyActionListener(), 4);
//        new ButtonColumn(allFilesTable, listeners.new getEditCopyActionListener(), 5);
//        new ButtonColumn(allFilesTable, listeners.new deleteActionListener(), 6);
//        for (int i = 1; i < Constants.TABLE_COLUMNS_NUMBER; i++) {
//            if (i == 1) {
//                allFilesTable.getColumnModel().getColumn(i).setMinWidth(75);
//                allFilesTable.getColumnModel().getColumn(i).setMaxWidth(75);
//            }
//            if (i == 2 || i == 3) {
//                allFilesTable.getColumnModel().getColumn(i).setMinWidth(140);
//                allFilesTable.getColumnModel().getColumn(i).setMaxWidth(140);
//            }
//            if (i == 4 || i == 5 || i == 6) {
//                allFilesTable.getColumnModel().getColumn(i).setMinWidth(60);
//                allFilesTable.getColumnModel().getColumn(i).setMaxWidth(60);
//            }
//        }
//        allFiles.setViewportView(allFilesTable);
//    }
//    private void updateMyCopies() {
//        int i = 0;
//        Object[][] info = new Object[myCopiesMap.size()][Constants.TABLE_COLUMNS_NUMBER];
//
//        for (RepFile rf : myCopiesMap.keySet()) {
//            if (myCopiesMap.get(rf).equals(Constants.READ_COPY)) {
//                info[i] = new Object[]{rf.getName(), rf.getSize(), rf.getCreationDate(), rf.getLastModificationDate(), UIManager.getIcon("OptionPane.questionIcon"), null, UIManager.getIcon("OptionPane.errorIcon")};
//            } else {
//                info[i] = new Object[]{rf.getName(), rf.getSize(), rf.getCreationDate(), rf.getLastModificationDate(), UIManager.getIcon("OptionPane.questionIcon"), UIManager.getIcon("OptionPane.warningIcon"), UIManager.getIcon("OptionPane.errorIcon")};
//            }
//            i++;
//        }
//        MyTableModel model = new MyTableModel(Constants.MY_TABLE_COLUMNS_NAME, info);
//        myCopiesTable = new JTable(model);
//        new ButtonColumn(myCopiesTable, listeners.new openCopyActionListener(), 4);
//        new ButtonColumn(myCopiesTable, listeners.new updateEditCopyActionListener(), 5);
//        new ButtonColumn(myCopiesTable, listeners.new discardCopyActionListener(), 6);
//
//        for (i = 1; i < Constants.TABLE_COLUMNS_NUMBER; i++) {
//            if (i == 1) {
//                myCopiesTable.getColumnModel().getColumn(i).setMinWidth(75);
//                myCopiesTable.getColumnModel().getColumn(i).setMaxWidth(75);
//            }
//            if (i == 2 || i == 3) {
//                myCopiesTable.getColumnModel().getColumn(i).setMinWidth(140);
//                myCopiesTable.getColumnModel().getColumn(i).setMaxWidth(140);
//            }
//            if (i == 4 || i == 5 || i == 6) {
//                myCopiesTable.getColumnModel().getColumn(i).setMinWidth(60);
//                myCopiesTable.getColumnModel().getColumn(i).setMaxWidth(60);
//            }
//        }
//        myCopies.setViewportView(myCopiesTable);
//    }
//    private void clearMyCopies() {
//        File[] dir = new File(directory).listFiles();
//        for (File f : dir) {
//            f.delete();
//        }
//        myCopiesMap.clear();
//    }
    private void registerListeners() {
        login.addActionListener(listeners.new loginActionListener());
        register.addActionListener(listeners.new registerActionListener());
        logout.addActionListener(listeners.new logoutActionListener());
//        browse.addActionListener(listeners.new browseActionListener());
//        send.addActionListener(listeners.new sendActionListener());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
//                    service.logout(loggedUser);
//                    clearMyCopies();
                    setVisible(false);
//                    while (!openedThreads.isEmpty()) {
//                        Thread.sleep((long) (Math.random() * 5000));
//                    }
                    System.exit(0);
                } catch (Exception E) {
                    System.out.println("Em EXIT: " + E);
                }
            }
        });
    }

    private void buildWindow() {
        buildLeft();
        buildMiddle();
        buildRight();

        cp.setLayout(new BorderLayout());
        cp.add(middle, BorderLayout.CENTER);
        cp.add(left, BorderLayout.WEST);
        cp.add(right, BorderLayout.EAST);
        logged(false);
    }

    private void buildLeft() {
        JPanel left_middle = new JPanel();
        JPanel username = new JPanel(new FlowLayout());
        JPanel password = new JPanel(new FlowLayout());
        JPanel buttons = new JPanel(new FlowLayout());
        JPanel errorLabel = new JPanel(new FlowLayout());

        //LAYOUTS
        left.setLayout(new BorderLayout());
        left_middle.setLayout(new BoxLayout(left_middle, BoxLayout.Y_AXIS));
        left_middle_up.setLayout(new BoxLayout(left_middle_up, BoxLayout.Y_AXIS));
        left_middle_down.setLayout(new BoxLayout(left_middle_down, BoxLayout.Y_AXIS));
        logged.setLayout(new FlowLayout());

        left_middle_up.setBorder(BorderFactory.createLineBorder(Color.black));
        logged.setBorder(BorderFactory.createLineBorder(Color.black));
        left.setBorder(BorderFactory.createLineBorder(Color.black));
        left.setPreferredSize(new Dimension(250, 0));
        left_middle_up.setMaximumSize(new Dimension(220, 124));

        //COLOCACOES
        username.add(new JLabel("Username:"));
        username.add(user);
        password.add(new JLabel("Password:"));
        password.add(pass);
        buttons.add(login);
        buttons.add(register);
        errorLabel.add(authentication_error);
        logged.add(loggedInfo);
        logged.add(logout);

        left_middle_up.add(username);
        left_middle_up.add(password);
        left_middle_up.add(buttons);
        left_middle_up.add(errorLabel);

        left_middle.add(Box.createVerticalStrut(15));
        left_middle.add(left_middle_up);
        left_middle.add(left_middle_down);
        left_middle.add(Box.createVerticalStrut(15));

        left.add(Box.createHorizontalStrut(15), BorderLayout.WEST);
        left.add(left_middle, BorderLayout.CENTER);
        left.add(Box.createHorizontalStrut(15), BorderLayout.EAST);
    }

    private void buildRight() {
        //LAYOUTS
        right.setLayout(new BoxLayout(right, BoxLayout.X_AXIS));
        right_middle.setLayout(new BoxLayout(right_middle, BoxLayout.Y_AXIS));

        right.setPreferredSize(new Dimension(250, 0));
        right.setBorder(BorderFactory.createLineBorder(Color.black));
        right_middle.setAlignmentY(TOP_ALIGNMENT);

        right_middle.add(Box.createVerticalStrut(15));
        right_middle.add(online_users);
        right_middle.add(Box.createVerticalStrut(10));

        right.add(Box.createHorizontalStrut(15));
        right.add(right_middle);
        right.add(Box.createHorizontalStrut(15));
    }

    private void buildMiddle() {
        JPanel forLabel1 = new JPanel(new BorderLayout());
        JPanel forLabel2 = new JPanel(new BorderLayout());
        JPanel forLabel3 = new JPanel(new BorderLayout());
        JPanel browserPanel = new JPanel(new FlowLayout());

        //LAYOUTS
        middle.setLayout(new BoxLayout(middle, BoxLayout.X_AXIS));
        middle_middle.setLayout(new BoxLayout(middle_middle, BoxLayout.Y_AXIS));

        middle.setBorder(BorderFactory.createLineBorder(Color.black));
        forLabel1.setMaximumSize(new Dimension(1000, 10));
        forLabel2.setMaximumSize(new Dimension(1000, 10));
        forLabel3.setMaximumSize(new Dimension(1000, 10));

        //COLOCACOES
        forLabel1.add(new JLabel("Ficheiros no repositorio:", SwingConstants.LEFT), BorderLayout.WEST);
        forLabel2.add(new JLabel("As minhas copias actuais:", SwingConstants.LEFT), BorderLayout.WEST);
        forLabel3.add(new JLabel("Fazer upload de um novo ficheiro:", SwingConstants.LEFT), BorderLayout.WEST);
        browserPanel.add(path);
        browserPanel.add(browse);
        browserPanel.add(send);
        middle_middle.add(Box.createVerticalStrut(15));
        middle_middle.add(forLabel1);
        middle_middle.add(allFiles);
        middle_middle.add(Box.createVerticalStrut(20));
        middle_middle.add(forLabel3);
        middle_middle.add(browserPanel);
        middle_middle.add(Box.createVerticalStrut(20));
        middle_middle.add(forLabel2);
        middle_middle.add(myCopies);
        middle_middle.add(Box.createVerticalStrut(15));

        middle.add(Box.createHorizontalStrut(15));
        middle.add(middle_middle);
        middle.add(Box.createHorizontalStrut(15));
    }

//    private class FileUpload extends Thread {
//
//        Socket uploadSocket;
//        File f;
//        String fileName;
//        Long fileLength;
//        Boolean update;
//        String response;
//        int bytesRead;
//        byte[] bytearray;
//        BufferedInputStream bis;
//        OutputStream os;
//        ObjectOutputStream oos;
//        ObjectInputStream ois;
//
//        public FileUpload(String filePath, boolean update) {
//            this.f = new File(filePath);
//            this.update = update;
//            this.fileLength = f.length();
//            this.fileName = f.getName();
//        }
//
//        @Override
//        public void run() {
//            try {
//                if (uploadServerSocket == null) {
//                    uploadServerSocket = new ServerSocket(Constants.TCP_PORT_FILE_UPLOAD);
//                    uploadServerSocket.setSoTimeout(Constants.SOCKET_TIME);
//                }
//                uploadSocket = uploadServerSocket.accept();
//
//                os = uploadSocket.getOutputStream();
//                oos = new ObjectOutputStream(os);
//                ois = new ObjectInputStream(uploadSocket.getInputStream());
//
//                oos.writeObject(fileName); //nome do ficheiro
//                oos.flush();
//                oos.writeObject(fileLength); //tamanho do ficheiro
//                oos.flush();
//                oos.writeObject(update); //se é ou não para actualizar
//                oos.flush();
//
//                if ((response = (String) ois.readObject()).equals(Constants.SUCCESS)) {
//                    postResponse(Constants.UPLOADING + fileName);
//                    bis = new BufferedInputStream(new FileInputStream(f));
//                    do {
//                        bytearray = new byte[Constants.TRANSFER_SIZE];
//                        bytesRead = bis.read(bytearray, 0, bytearray.length);
//                        //System.out.println(bytesRead);
//                        if (bytesRead != -1) {
//                            os.write(bytearray, 0, bytesRead);
//                            os.flush();
//                        }
//                    } while (bytesRead == Constants.TRANSFER_SIZE);
//
//                    System.out.println("Transferência terminada as " + new Date(System.currentTimeMillis()));
//
//                    bis.close();
//
//                    if (update) {
//                        f.delete();
//                    }
//                }
//            } catch (SocketTimeoutException ex) {
//                System.out.println("Acabou o tempo em upload... " + ex);
//            } catch (Exception ex) {
//                System.out.println("Em FILE_UPLOAD no CLIENT: as " + new Date(System.currentTimeMillis()) + " " + ex);
//            } finally {
//                try {
//                    ois.close();
//                    oos.close();
//                    os.close();
//                } catch (Exception e) {
//                    System.out.println(e);
//                }
//                openedThreads.remove(this);
//            }
//            System.out.println("Thread fechada as " + new Date(System.currentTimeMillis()));
//        }
//    }
//    private class FileDownload extends Thread {
//
//        private Socket downloadSocket;
//        private String type;
//        private long fileLength;
//        private RepFile rf;
//        private byte[] bytearray;
//        private int bytesRead;
//        private InputStream is;
//        private BufferedOutputStream bos;
//        private ObjectInputStream ois;
//
//        @Override
//        public void run() {
//            try {
//                if (downloadServerSocket == null) {
//                    downloadServerSocket = new ServerSocket(Constants.TCP_PORT_FILE_DOWNLOAD);
//                    downloadServerSocket.setSoTimeout(Constants.SOCKET_TIME);
//                }
//                downloadSocket = downloadServerSocket.accept();
//
//                is = downloadSocket.getInputStream();
//                ois = new ObjectInputStream(is);
//
//                rf = (RepFile) ois.readObject();
//                type = (String) ois.readObject();
//                fileLength = rf.getLength();
//
//                bos = new BufferedOutputStream(new FileOutputStream(directory + rf.getName()));
//
//                postResponse(Constants.DOWNLOADING + rf.getName());
//                do {
//                    bytearray = new byte[Constants.TRANSFER_SIZE];
//                    bytesRead = is.read(bytearray, 0, bytearray.length);
//                    fileLength -= bytesRead;
//                    //System.out.println(bytesRead + "   " + fileLength);
//                    if (bytesRead != -1) {
//                        bos.write(bytearray, 0, bytesRead);
//                        bos.flush();
//                    }
//                } while (fileLength != 0); // enquanto nao ler tudo
//
//                bos.close();
//                rf.setSize(new File(directory + rf.getName()).length());
//                myCopiesMap.put(rf, type);
//                postResponse(Constants.COPY_RECEIVED + rf.getName());
//                updateMyCopies();
//            } catch (SocketTimeoutException e) {
//                System.out.println("Acabou o tempo em download... " + e);
//            } catch (Exception ex) {
//                System.out.println("Em FILE_DOWNLOAD no CLIENT: as " + new Date(System.currentTimeMillis()) + " " + ex);
//            } finally {
//                try {
//                    System.out.println("Socket fechada às " + new Date(System.currentTimeMillis()));
//                    ois.close();
//                    is.close();
//                    downloadSocket.close();
//                } catch (Exception e) {
//                    System.out.println(e);
//                }
//            }
//            System.out.println("Thread fechada as " + new Date(System.currentTimeMillis()));
//        }
//    }
    private class Listeners {

        class logoutActionListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
//                    service.logout(loggedUser);
//                    clearMyCopies();
                } catch (Exception E) {
                    System.out.println("Em LOGOUT: " + E);
                }

                logged(false);
            }
        }

        class registerActionListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                String username = user.getText();
                String password = pass.getText();

                if (!username.equals("") && !password.equals("")) {
                    try {
//                        server.registerClient(client, username, password);
                    } catch (Exception E) {
                        System.out.println("Em REGISTER: " + E);
                    }
                } else {
                    postAuthenticationError("Exception");
                }
            }
        }

        class loginActionListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                String username = user.getText();
                String password = pass.getText();

                try {
//                    server.loginClient(client, username, password);
                } catch (Exception E) {
                    System.out.println("Em REGISTER: " + E);
                }
            }
        }

//        class browseActionListener implements ActionListener {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                int response = browser.showOpenDialog(MonitorGUI_DELETE.this);
//
//                if (response == JFileChooser.APPROVE_OPTION) {
//                    path.setText(browser.getSelectedFile().getPath());
//                }
//            }
//        }
//        class sendActionListener implements ActionListener {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String filePath = path.getText();
//                try {
//                    Thread t = new FileUpload(filePath, false);
//                    openedThreads.add(t);
//                    t.start();
//                    server.uploadFile(client);
//                } catch (Exception E) {
//                    System.out.println("Em UPLOAD_FILE: " + E);
//                }
//            }
//        }
        class deleteActionListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                String file = (String) allFilesTable.getValueAt(Integer.valueOf(e.getActionCommand()), 0);
                try {
//                    server.deleteFile(file);
                } catch (Exception E) {
                    System.out.println("Em DELETE_FILE: " + E);
                }
            }
        }

//        class getReadCopyActionListener implements ActionListener {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String fileName = (String) allFilesTable.getValueAt(Integer.valueOf(e.getActionCommand()), 0);
//
//                if (myCopiesMap.containsKey(new RepFile(fileName))) {
//                    postResponse(Constants.WARNING + Constants.ALREADY_HAVE + fileName);
//                    return;
//                }
//                try {
//                    new FileDownload().start();
//                    server.getReadCopy(client, fileName);
//                } catch (Exception E) {
//                    System.out.println("Em GET_READ_COPY_FILE: " + E);
//                }
//            }
//        }
//        class getEditCopyActionListener implements ActionListener {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String fileName = (String) allFilesTable.getValueAt(Integer.valueOf(e.getActionCommand()), 0);
//
//                if (myCopiesMap.containsKey(new RepFile(fileName))) {
//                    postResponse(Constants.WARNING + Constants.ALREADY_HAVE + fileName);
//                    return;
//                }
//                try {
//                    new FileDownload().start();
//                    server.getEditCopy(client, fileName);
//                } catch (Exception E) {
//                    System.out.println("Em GET_EDIT_COPY_FILE: " + E);
//                }
//            }
//        }
        class openCopyActionListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = (String) myCopiesTable.getValueAt(Integer.valueOf(e.getActionCommand()), 0);
                try {
                    Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + new File(directory + fileName).getAbsolutePath());
                } catch (Exception E) {
                    System.out.println("Em OPEN_COPY_FILE: " + E);
                }
            }
        }

//        class updateEditCopyActionListener implements ActionListener {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String fileName = (String) myCopiesTable.getValueAt(Integer.valueOf(e.getActionCommand()), 0);
//                try {
//                    new FileUpload(directory + fileName, true).start();
//                    server.uploadFile(client);
//                    myCopiesMap.remove(new RepFile(fileName));
//                    updateMyCopies();
//                } catch (Exception E) {
//                    System.out.println("Em UPDATE_EDIT_COPY_FILE: " + E);
//                }
//            }
//        }
//        class discardCopyActionListener implements ActionListener {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                Integer row = Integer.valueOf(e.getActionCommand());
//                String fileName = (String) myCopiesTable.getValueAt(row, 0);
//                if (new File(directory + fileName).delete()) {
//                    try {
//                        if (myCopiesTable.getValueAt(row, 5) == null) {
//                            server.discardReadCopy(loggedUser, fileName);
//                        } else {
//                            server.discardEditCopy(loggedUser, fileName);
//                        }
//                        myCopiesMap.remove(new RepFile(fileName));
//                        postResponse(Constants.COPY_DISCARDED + fileName);
//                        updateMyCopies();
//                    } catch (Exception E) {
//                        System.out.println("Em DISCARD_COPY_FILE: " + E);
//                    }
//                } else {
//                    postResponse(Constants.WARNING + fileName + Constants.IS_OPEN);
//                }
//            }
//        }
//    }
    }

}
