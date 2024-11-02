package org.example.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.opencsv.CSVWriter;

public class TransactionCSVGenerator {

    public static void generateCSV(List<Transaction> transactions, String filePath) throws Exception {
        // Grouper les transactions par mois
        Map<String, List<Transaction>> transactionsParMois = new LinkedHashMap<>();

        for (Transaction transaction : transactions) {
            String mois = transaction.getMonth();
            transactionsParMois.putIfAbsent(mois, new ArrayList<>());
            transactionsParMois.get(mois).add(transaction);
        }

        // Créer le fichier CSV
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            // En-tête avec chaque mois et ses sous-colonnes
            List<String> header = new ArrayList<>();
            for (String month : transactionsParMois.keySet()) {
                header.add(month + " - Intitulé");
                header.add(month + " - Montant");
            }
            writer.writeNext(header.toArray(new String[0]));

            // Déterminer le nombre maximal de transactions dans un mois
            int maxTransactions = transactionsParMois.values().stream().mapToInt(List::size).max().orElse(0);

            // Remplir les lignes du CSV
            for (int i = 0; i < maxTransactions; i++) {
                List<String> row = new ArrayList<>();
                for (String month : transactionsParMois.keySet()) {
                    List<Transaction> monthlyTransactions = transactionsParMois.get(month);
                    if (i < monthlyTransactions.size()) {
                        Transaction transaction = monthlyTransactions.get(i);
                        row.add(transaction.nature);
                        if(transaction.credit != null){
                            row.add(transaction.credit.toString());
                        }else {
                            row.add(transaction.debit.toString());
                        }
                    } else {
                        row.add(""); // Intitulé vide si pas de transaction pour cet index
                        row.add(""); // Montant vide si pas de transaction pour cet index
                    }
                }
                writer.writeNext(row.toArray(new String[0]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

