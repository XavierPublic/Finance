package org.example.utils;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.example.Main.extractTextFromPDF;

public class TransactionParser {

    private static final String FOLDER_PATH = "/home/xavier/Bureau/finances";
    private static final String FILE_CSV_NAME = "transactions_par_mois.csv";
    private static final List<String> ORDERED_MONTH = Arrays.asList("janvier", "février", "mars", "avril", "mai", "juin", "juillet", "août", "septembre", "octobre", "novembre", "décembre");


    public static void main(String[] args) throws IOException {
        File folder = new File(FOLDER_PATH);
        File[] files = folder.listFiles();
        if(files == null){
            System.out.println("No files found");
            return;
        }
        Map<String, List<String[]>> transactionsByMonth = new HashMap<>();

        for (File file : files) {
            String text = extractTextFromPDF(file);
            text = text.replaceAll("\\r?\\n", " ");
            String[] transactionsTab = text.split("(?=\\d{2}/\\d{2}/\\d{4}\\s+\\d{2}/\\d{2}/\\d{4})");

            for (String transaction : transactionsTab) {
                System.out.println("Transaction: " + transaction.trim());
                Pattern pattern = Pattern.compile(
                        "(\\d{2}/\\d{2}/\\d{4})\\s+\\d{2}/\\d{2}/\\d{4}\\s+(.+?)\\s+(\\d+\\,\\d{2}|\\d+\\.\\d{2})"
                );

                Matcher matcher = pattern.matcher(text);
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM");
                while (matcher.find()) {
                    String dateStr = matcher.group(1);
                    String title = matcher.group(2).trim();
                    String amount = matcher.group(3).replace(",", ".");

                    System.out.println(dateStr);

                    LocalDate date = LocalDate.parse(dateStr, inputFormatter);
                    String month = date.format(monthFormatter);

                    // Ajouter la transaction à la liste du mois correspondant
                    transactionsByMonth
                            .computeIfAbsent(month, k -> new ArrayList<>())
                            .add(new String[]{title, amount});
                }
            }

            // Extraire les informations des transactions



        }

        // Créer le fichier CSV
        try (CSVWriter writer = new CSVWriter(new FileWriter(FOLDER_PATH + "/" + FILE_CSV_NAME))) {
            // Écrire l'en-tête
            List<String> header = new ArrayList<>();
            for (String month : transactionsByMonth.keySet()) {
                header.add(month + " - Intitulé");
                header.add(month + " - Montant");
            }
            System.out.println(header);
            writer.writeNext(header.toArray(new String[0]));

            // Déterminer le nombre maximal de transactions pour remplir toutes les lignes
            int maxRows = transactionsByMonth.values().stream().mapToInt(List::size).max().orElse(0);

            // Écrire les transactions dans les colonnes du CSV
            for (int i = 0; i < maxRows; i++) {
                List<String> row = new ArrayList<>();
                for (String month : transactionsByMonth.keySet()) {
                    List<String[]> monthTransactions = transactionsByMonth.get(month);
                    if (i < monthTransactions.size()) {
                        String[] transaction = monthTransactions.get(i);
                        row.add(transaction[0]);  // Intitulé
                        row.add(transaction[1]);  // Montant
                    } else {
                        row.add("");
                        row.add("");
                    }
                }
                writer.writeNext(row.toArray(new String[0]));
            }

            System.out.println("Fichier transactions_par_mois.csv créé avec succès.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

