package eu.menzani.pdfmetadatacleaner;

import eu.menzani.error.GlobalExceptionHandler;
import eu.menzani.lang.UncaughtException;
import eu.menzani.struct.AppInfo;
import eu.menzani.struct.FileExtension;
import eu.menzani.swing.JConcurrentMessageBox;
import eu.menzani.swing.JMessageBox;
import eu.menzani.swing.SimpleFileFilter;
import eu.menzani.swing.Swing;
import eu.menzani.system.SystemPaths;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.pdmodel.PDDocument;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

class App extends Thread {
    public static void main(String[] args) {
        AppInfo.setName("PDF Metadata Cleaner");
        GlobalExceptionHandler.addErrorReport();
        Swing.setSystemLookAndFeel();

        Swing.run(() -> {
            JFileChooser fileChooser = new JFileChooser(SystemPaths.DOWNLOADS_FOLDER.toFile());
            fileChooser.setFileFilter(new SimpleFileFilter("PDF document", FileExtension.PDF));
            fileChooser.setAcceptAllFileFilterUsed(false);
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                new App(fileChooser.getSelectedFile()).start();
            } else {
                System.exit(0);
            }
        });
    }

    private final File file;

    private App(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        PDDocument document;
        try {
            document = PDDocument.load(file);
        } catch (IOException e) {
            throw new UncaughtException(e);
        }
        COSDictionary documentInformation = document.getDocumentInformation().getCOSObject();
        JMessageBox messageBox = new JConcurrentMessageBox();
        if (documentInformation.size() == 0) {
            messageBox.showPlain("The PDF document is already clean.");
        } else {
            documentInformation.clear();
            try {
                document.save(file);
            } catch (IOException e) {
                throw new UncaughtException(e);
            }
            messageBox.showPlain("The PDF document was cleaned.");
        }
    }
}
