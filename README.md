Delete Files
==============

Delete Files is a simple Java program to delete files from a directory tree based on a file mask.

It can be executed either as a command line utility or as a GUI program.

From the commandline
--------------------

    usage: java -jar DelFiles.jar [-debug] -filemask <File Mask> [-help] -rootpath <Path> [-version]

Options:
    -debug                  enable debug logging
    -filemask <File Mask>   The file mask of the files to delete. e.g. *.pdf
    -help                   print this help information
    -rootpath <Path>        The root path to start searching for files. e.g. c:\data or /home/user1
    -version                print the version information and exits

