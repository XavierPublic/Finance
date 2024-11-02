package org.example.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Transaction {
    String date;
    String nature;
    Double debit;
    Double credit;

    public Transaction(String date, String nature, Double debit, Double credit) {
        this.date = date;
        this.nature = nature;
        this.debit = debit;
        this.credit = credit;
    }

    public String getMonth() throws Exception {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date dateObj = inputFormat.parse(this.date);
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.FRENCH);
        return monthFormat.format(dateObj);
    }

    @Override
    public String toString() {
        return "Date: " + date + ", Nature: " + nature + ", Débit: " + debit + ", Crédit: " + credit;
    }
}
