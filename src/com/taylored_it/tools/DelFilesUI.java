package com.taylored_it.tools;

import com.taylored_it.TextAreaAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class DelFilesUI extends JDialog {
    public static Logger logger = Logger.getLogger(DelFilesUI.class.getName());
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField eFileMask;
    private JTextField eRootPath;
    private JTextArea textArea1;
    private JButton infoButton;
    private String sFileMAsk;
    private String sRootPath;
    private static DelFiles o = new DelFiles();
    private static Package pkg = o.getClass().getPackage();


    public DelFilesUI() {
        setContentPane(contentPane);
        Image img = new ImageIcon(DelFilesUI.class.getResource("taylored-it_logo_small.png")).getImage();
        setIconImage(img);
        setModal(true);
        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
        int w = contentPane.getSize().width;
        int h = contentPane.getSize().height;
        int x = (dim.width - w)/2;
        int y = (dim.height - h)/2;
        x = x / 2;
        y = y / 2;
        // Move the window
        //setBounds(x,y,w,h);
        setLocation(x, y);
        //setLocationRelativeTo(null);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onOK();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onInfo();
            }
        });
    }

    public static void main() {
        DelFilesUI dialog = new DelFilesUI();
        dialog.pack();
        dialog.setVisible(true);

        System.exit(0);
    }

    protected static void setupLog4JAppender(JTextArea jTextArea) {
        // This code attaches the appender to the text area
        TextAreaAppender.setTextArea(jTextArea);
        System.setProperty("DelFiles.log", DelFiles.StartPath + DelFiles.dirSeperator + "log" + DelFiles.dirSeperator);

        // Normally configuration would be done via a log4j.properties
        // file found on the class path, but here we will explicitly set
        // values to keep it simple.
        //
        // Great introduction to Log4J at http://logging.apache.org/log4j/docs/manual.html
        //
        // Could also have used straight code like: app.logger.setLevel(Level.INFO);
        Properties logProperties = new Properties();
        logProperties.put("log4j.rootLogger", "INFO, FILE, TEXTAREA");

        logProperties.put("log4j.appender.FILE", "org.apache.log4j.DailyRollingFileAppender"); // A standard console appender
        logProperties.put("log4j.appender.FILE.layout", "org.apache.log4j.PatternLayout"); //See: http://logging.apache.org/log4j/docs/api/org/apache/log4j/PatternLayout.html
        logProperties.put("log4j.appender.FILE.File", "${DelFiles.log}DelFiles.log");
        logProperties.put("log4j.appender.FILE.DatePattern", "'.'yyyy-MM-dd");
        logProperties.put("log4j.appender.FILE.Append", "true");
        logProperties.put("log4j.appender.FILE.layout.ConversionPattern", "%d{dd/MM/yyyy HH:mm:ss,SSS} %5p [%-20M] (%15F:%5L) - %m%n");

        logProperties.put("log4j.appender.TEXTAREA", "com.taylored_it.TextAreaAppender");  // Our custom appender
        logProperties.put("log4j.appender.TEXTAREA.layout", "org.apache.log4j.PatternLayout"); //See: http://logging.apache.org/log4j/docs/api/org/apache/log4j/PatternLayout.html
        logProperties.put("log4j.appender.TEXTAREA.layout.ConversionPattern", "%d{dd/MM/yyyy HH:mm:ss,SSS} %5p - %m%n");

        PropertyConfigurator.configure(logProperties);
    }

    private void onOK() throws IOException {
// add your code here

        setupLog4JAppender(textArea1);
        logger.info("======================================================================================");
        logger.info("Staring DelFiles run!");
        logger.debug("Package name:\t" + pkg.getName());
        logger.debug("Specification title:\t" + pkg.getSpecificationTitle());
        logger.debug("Specification vendor:\t" + pkg.getSpecificationVendor());
        logger.debug("Specification version:\t" + pkg.getSpecificationVersion());
        logger.info("Title:   " + pkg.getImplementationTitle());
        logger.info("Vendor:  " + pkg.getImplementationVendor());
        logger.info("Version: " + pkg.getImplementationVersion());
        logger.debug("File mask = " + eFileMask.getText());
        logger.debug("Base Directory = " + eRootPath.getText());
        Path vPath = Paths.get(eRootPath.getText());
        RecursiveList rl = new RecursiveList(vPath, eFileMask.getText());
        for (Path path : rl) {
            //System.out.print(path.getFileName().toString());
            if (Files.isRegularFile(path)) {
                logger.info("File: " + path.toAbsolutePath().toString() + " to be deleted.");
                boolean result = path.toAbsolutePath().toFile().delete();
                logger.info("File: " + path.toAbsolutePath().toString() + " delete result = " + result);
            }
        }
        logger.info("End of DelFiles run!");
        logger.info("======================================================================================");
        //dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    private void onInfo() {
// add your code here

        setupLog4JAppender(textArea1);
        logger.info("======================================================================================");
        logger.info("DelFiles Information!");
        logger.debug("Package name:\t" + pkg.getName());
        logger.debug("Specification title:\t" + pkg.getSpecificationTitle());
        logger.debug("Specification vendor:\t" + pkg.getSpecificationVendor());
        logger.debug("Specification version:\t" + pkg.getSpecificationVersion());
        logger.info("Title:   " + pkg.getImplementationTitle());
        logger.info("Vendor:  " + pkg.getImplementationVendor());
        logger.info("Version: " + pkg.getImplementationVersion());
        logger.debug("File mask = " + eFileMask.getText());
        logger.debug("Base Directory = " + eRootPath.getText());
        logger.info("End of DelFiles Information!");
        logger.info("======================================================================================");
        //dispose();
    }


    public String getSFileMAsk() {
        return sFileMAsk;
    }

    public void setsFileMAsk(final String sFileMAsk) {
        this.sFileMAsk = sFileMAsk;
    }

    public String getSRootPath() {
        return sRootPath;
    }

    public void setsRootPath(final String sRootPath) {
        this.sRootPath = sRootPath;
    }

    public void setData(DelFilesUI data) {
    }

    public void getData(DelFilesUI data) {
    }

    public boolean isModified(DelFilesUI data) {
        return false;
    }
}
