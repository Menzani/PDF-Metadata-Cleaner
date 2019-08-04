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

        try (PDDocument document = PDDocument.load(file, password)) {
            COSDictionary documentInformation = document.getDocumentInformation().getCOSObject();
            if (documentInformation.size() == 0) {
                System.out.println("The PDF file is already clean: \"" + file.getAbsolutePath() + '"');
                System.exit(1);
            }
            documentInformation.clear();
            try {
                document.save(file);
            } catch (IOException e) {
                System.err.println("Could not overwrite the PDF file: " + e.getMessage());
                System.exit(4);
            }
        } catch (InvalidPasswordException e) {
            if (password.isEmpty()) {
                System.err.println("The PDF document requires a password.");
            } else {
                System.err.println("The PDF document password provided is incorrect.");
            }
            System.exit(3);
        } catch (IOException e) {
            System.err.println("Could not read or parse the PDF file: " + e.getMessage());
            System.exit(2);
        }
        System.out.println("Cleaned the PDF file: \"" + file.getAbsolutePath() + '"');
    }
}
