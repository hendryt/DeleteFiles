package com.taylored_it.tools;

import org.apache.commons.cli.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created with IntelliJ IDEA.
 * User: Hendry
 * Date: 01/06/13
 * Time: 00:15
 * Package com.taylored_it.tools
 */
public class DelFiles {

    public static Logger logger = Logger.getLogger(DelFiles.class.getName());
    public static String StartPath;
    private static String mRootPath;
    private static String mFileMask;
    public static String dirSeperator;
    public static commonUtils commUtil = new commonUtils();
    public static String mVersion;
    public static String mPackage;
    // create Options object
    public static final Options options = new Options();
    // create the command line parser
    public static final CommandLineParser parser = new GnuParser();
    public static CommandLine line;

    public static void main(String[] args) throws IOException {

        DelFiles o = new DelFiles();
        Package pkg = o.getClass().getPackage();
        mVersion = pkg.getImplementationVersion();
        mPackage = pkg.getName();
        checkOS();
        getClassUrl();

        System.setProperty("DelFiles.log", StartPath + dirSeperator + "log" + dirSeperator);
        PropertyConfigurator.configure(StartPath + dirSeperator + "conf" + dirSeperator + "log4j.properties");

        defineOptions(false);
        processOptions(args);

        logger.info("======================================================================================");
        logger.info("Staring DelFiles!");
        logger.debug("Package name:\t" + pkg.getName());
        logger.debug("Specification title:\t" + pkg.getSpecificationTitle());
        logger.debug("Specification vendor:\t" + pkg.getSpecificationVendor());
        logger.debug("Specification version:\t" + pkg.getSpecificationVersion());
        logger.info("Title:   " + pkg.getImplementationTitle());
        logger.info("Vendor:  " + pkg.getImplementationVendor());
        logger.info("Version: " + pkg.getImplementationVersion());

        logger.debug("File mask = " + mFileMask);
        logger.debug("Base Directory = " + mRootPath);

        Path vPath = Paths.get(mRootPath);
        RecursiveList rl = new RecursiveList(vPath, mFileMask);
        for (Path path : rl) {
            //System.out.print(path.getFileName().toString());
            if (Files.isRegularFile(path)) {
                logger.info("File: " + path.toAbsolutePath().toString() + " to be deleted.");
                boolean result = path.toAbsolutePath().toFile().delete();
                logger.info("File: " + path.toAbsolutePath().toString() + " delete result = " + result);
            }
        }
        logger.info("shutting down DelFiles program!");
        logger.info("======================================================================================");
    }

    /**
     * Gets the path of the class.<p>This gets the path of the class file that was executed.
     */
    private static void getClassUrl() {
        ClassLoader loader = DelFiles.class.getClassLoader();
        if (loader == null) {
            System.err.println("Cannot load the class");
        } else {
            File f = null;
            try {
                f = new File(new URI(DelFiles.class.getProtectionDomain().getCodeSource().getLocation().toString()));
            } catch (Exception e) {
                System.err.println("Exception: " + commUtil.getStackTraceAsString(e));
                System.exit(1);
            }
            StartPath = f.getParent();
        }
    }

    /**
     * Checks which OS the class is being executed on.<p>
     * This then sets the path separator, the directory
     * separator characters and then sets the temp file names and paths.
     */
    private static void checkOS() {
        if (commonUtils.isWindows()) {
            //System.out.println("This is Windows");
            dirSeperator = "\\";
        } else if (commonUtils.isMac()) {
            //System.out.println("This is Mac");
            dirSeperator = "/";
        } else if (commonUtils.isUnix()) {
            //System.out.println("This is Unix or Linux");
            dirSeperator = "/";
        } else {
            System.err.println("Your OS is not supported!!");
        }
    }

    public static void processOptions(String[] opts) {
        try {
            // parse the command line arguments
            line = parser.parse(options, opts);
        } catch (UnrecognizedOptionException e) {
            usage("\n\nUnrecognised Option: " + e.getMessage() + "\n");
            System.exit(1);
        } catch (MissingOptionException | MissingArgumentException e) {
            usage("\n\n" + e.getMessage() + "\n");
            System.exit(1);
        } catch (ParseException exp) {
            // oops, something went wrong
            usage("\n\nParse option error: " + exp.getMessage());
            System.exit(1);
        }
        if (line.hasOption("help")) {
            usage("");
            System.exit(0);
        } else if (line.hasOption("version")) {
            System.out.println("\n\n\tDelfiles\n");
            System.out.println("\tVersion: " + mVersion + "\n\n");
            System.exit(0);
        } else if ( line.hasOption("rootpath") && line.hasOption("filemask")) {
            if (line.hasOption("debug")) {
                logger.setLevel(Level.DEBUG);
                logger.debug("Logging level = " + logger.getLevel());
            }
            //readProperties(line.getOptionValue( "config" ) );
            mRootPath = line.getOptionValue("rootpath");
            mFileMask = line.getOptionValue("filemask");
            // List retieved properties in log file.
            logger.info("Root path parameter value: " + mRootPath);
            logger.info("File mask parameter value: " + mFileMask);

        } else {
            DelFilesUI.main();
            usage("\n\n\tNot all options specified! or Invalid options specified!\n");
            System.exit(2);
        }
    }

    public static void defineOptions(boolean usage) {
        // create the Options
        Option help = new Option("help", "print this help information");
        Option version = new Option("version", "print the version information and exits");
        Option debug = new Option("debug", "enable debug logging");
        Option rootpath;
        Option filemask;
        if (usage) {
            OptionBuilder.withArgName("Path");
            OptionBuilder.isRequired();
            OptionBuilder.hasArg();
            OptionBuilder.withDescription("The root path to start searching for files. e.g. c:\\data or /home/user1");
            rootpath = OptionBuilder.create("rootpath");

            OptionBuilder.withArgName("File Mask");
            OptionBuilder.isRequired();
            OptionBuilder.hasArg();
            OptionBuilder.withDescription("The file mask of the files to delete. e.g. *.pdf ");
            filemask = OptionBuilder.create("filemask");
        } else {
            OptionBuilder.withArgName("Path");
            OptionBuilder.hasArg();
            OptionBuilder.withDescription("The root path to start searching for files. e.g. c:\\data or /home/user1");
            rootpath = OptionBuilder.create("rootpath");

            OptionBuilder.withArgName("File Mask");
            OptionBuilder.hasArg();
            OptionBuilder.withDescription("The file mask of the files to delete. e.g. *.pdf ");
            filemask = OptionBuilder.create("filemask");
        }
        options.addOption(help);
        options.addOption(version);
        options.addOption(debug);
        options.addOption(rootpath);
        options.addOption(filemask);
    }

    public static void usage(String msg) {
        System.out.println();
        defineOptions(true);
        if (msg.isEmpty()) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(100, "java DelFiles", "\nOptions:", options, "\n", true);
        } else {
            System.out.println(msg);
            System.out.println();
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(100, "java DelFiles", "\nOptions:", options, "\n", true);
        }
    }

}
