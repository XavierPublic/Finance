package org.example.utils;

import java.util.regex.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionExtractor {

    public static List<Transaction> extractTransactions(String text) {
        List<Transaction> transactions = new ArrayList<>();

        String transactionRegex = "(\\d{2}/\\d{2}/\\d{4})\\s+([A-Z\\s]+)\\s+(\\d+,\\d{2})?\\s+(\\d+,\\d{2})?";
        Pattern pattern = Pattern.compile(transactionRegex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String date = matcher.group(1);
            String nature = matcher.group(2).trim();
            Double debit = matcher.group(3) != null ? Double.parseDouble(matcher.group(3).replace(",", ".")) : null;
            Double credit = matcher.group(4) != null ? Double.parseDouble(matcher.group(4).replace(",", ".")) : null;

            transactions.add(new Transaction(date, nature, debit, credit));
        }
        return transactions;
    }

}
