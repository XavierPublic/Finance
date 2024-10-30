package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        // Spécifie le chemin du dossier contenant les fichiers PDF
        String folderPath = "chemin/vers/votre/dossier";

        // Appel de la fonction pour extraire le texte de tous les PDF du dossier
        extractTextFromPDFsInFolder(folderPath);
    }

    public static void extractTextFromPDFsInFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.isDirectory()) {
            System.out.println("Le chemin spécifié n'est pas un dossier.");
            return;
        }

        // Liste tous les fichiers du dossier
        File[] files = folder.listFiles();
        if (files == null) {
            System.out.println("Erreur lors de la lecture du dossier.");
            return;
        }

        for (File file : files) {
            // Vérifie que le fichier est bien un PDF
            if (file.isFile() && file.getName().endsWith(".pdf")) {
                try {
                    String text = extractTextFromPDF(file);
                    System.out.println("Texte extrait du fichier " + file.getName() + " :");
                    System.out.println(text);
                } catch (IOException e) {
                    System.err.println("Erreur lors de l'extraction du texte du fichier " + file.getName() + ": " + e.getMessage());
                }
            }
        }
    }

    public static String extractTextFromPDF(File file) throws IOException {
        try (PDDocument document = Loader.loadPDF(file)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }
}
