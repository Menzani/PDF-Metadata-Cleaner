package it.fmenza.pdfmetadatacleaner;

import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

import java.io.File;
import java.io.IOException;

public class PDFMetadataCleaner {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Arguments: \"<path to PDF file>\" [PDF document password]");
            System.exit(5);
        }
        File file = new File(args[0]);
        String password;
        if (args.length == 1) {
            password = "";
        } else {
            password = args[1];
        }

        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.defaultlog", "trace");

        PDDocument document;
        try {
            document = PDDocument.load(file, password);
        } catch (InvalidPasswordException e) {
            if (args.length == 1) {
                System.err.println("The PDF document requires a password.");
            } else {
                System.err.println("The PDF document password provided is incorrect.");
            }
            System.exit(3);
            return;
        } catch (IOException e) {
            System.err.println("Could not read or parse the PDF file: " + e.getMessage());
            System.exit(2);
            return;
        }
        COSDictionary documentInformation = document.getDocumentInformation().getCOSObject();
        if (documentInformation.size() == 0) {
            System.out.println("The PDF file is already clean: \"" + tryGetCanonicalPath(file) + '"');
            System.exit(1);
        }
        documentInformation.clear();
        try {
            document.save(file);
        } catch (IOException e) {
            System.err.println("Could not overwrite the PDF file: " + e.getMessage());
            System.exit(4);
        }
        System.out.println("Cleaned the PDF file: \"" + tryGetCanonicalPath(file) + '"');
    }

    private static String tryGetCanonicalPath(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            return file.getAbsolutePath();
        }
    }
}
