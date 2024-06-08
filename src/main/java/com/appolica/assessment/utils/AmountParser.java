package com.appolica.assessment.utils;

public class AmountParser {
    public static double parseToDouble(String line) throws NumberFormatException {
        int index = line.indexOf(".");
        if (line.length() > 1) {
            isNotAllowedZeroLeadNumber(line.charAt(0), line.charAt(1));
        }

        isNotAllowedEndWithDot(line);

        double amount = Double.parseDouble(line);
        String amountStr = String.valueOf(amount);
        isFractionalPartTooLong(amountStr, index);

        String finalAmount = String.format("%.2f", amount);
        return Double.parseDouble(finalAmount);
    }

    private static void isNotAllowedZeroLeadNumber(char firstChar, char secondChar) throws NumberFormatException {
        boolean isNotAllowedZeroLeadNumber = firstChar == '0' && secondChar != '.';
        if (isNotAllowedZeroLeadNumber) {
            throw new NumberFormatException();
        }
    }

    private static void isNotAllowedEndWithDot(String line) {
        boolean isNotAllowedEndWithDot = line.charAt(line.length() - 1) == '.';
        if (isNotAllowedEndWithDot) {
            throw new NumberFormatException();
        }
    }

    private static void isFractionalPartTooLong(String amountStrLength, int index) {
        boolean isFractionalPartTooLong = (amountStrLength.length() - (index + 1) > 2);
        boolean dotExists = index > -1;
        if (dotExists) {
            if (isFractionalPartTooLong) {
                throw new NumberFormatException();
            }
        }

    }


}
