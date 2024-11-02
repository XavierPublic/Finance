package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.example.utils.Transaction;
import org.example.utils.TransactionCSVGenerator;
import org.example.utils.TransactionExtractor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        // Spécifie le chemin du dossier contenant les fichiers PDF
        String folderPath = "/home/xavier/Bureau/finances";

        // Appel de la fonction pour extraire le texte de tous les PDF du dossier
        extractTextFromPDFsInFolder(folderPath);
    }

    public static void extractTextFromPDFsInFolder(String folderPath) throws Exception {
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

        List<Transaction> transactionsTotal = new ArrayList<>();
        for (File file : files) {
            // Vérifie que le fichier est bien un PDF
            if (file.isFile() && file.getName().endsWith(".pdf")) {
                try {
                    String text = extractTextFromPDF(file);
                    if(text != null && !text.isEmpty()){
                        text = text.substring(text.indexOf("RELEVÉ DES OPÉRATIONS"));
                        System.out.println(text);
                        String[] lines = text.split("\n");
                        for(String line : lines){
                            transactionsTotal.addAll(TransactionExtractor.extractTransactions(line));
                        }
                        for (Transaction transaction : transactionsTotal) {
                            System.out.println(transaction);
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Erreur lors de l'extraction du texte du fichier " + file.getName() + ": " + e.getMessage());
                }
            }
        }
        //CSV Final
        String filePath = "transactions.csv";
        if(!transactionsTotal.isEmpty()){
            TransactionCSVGenerator.generateCSV(transactionsTotal, filePath);
            System.out.println("Fichier CSV généré : " + filePath);
        }
    }

    public static String extractTextFromPDF(File file) throws IOException {
        try (PDDocument document = Loader.loadPDF(file)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }
}


