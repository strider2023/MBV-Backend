package com.mbv.framework.util;

/**
 * Created by arindamnath on 02/03/16.
 */
public class KeyGenerator {

    private final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private final String NUMERIC_STRING = "0123456789";

    public KeyGenerator() {

    }

    public String generateTransactionCode(Long walletId, Long lastId) {
        StringBuilder builder = new StringBuilder();
        builder.append("MBTXN");
        builder.append(String.format("%07d", walletId.intValue()));
        builder.append(String.format("%08d", lastId.intValue() + 1));
        return builder.toString();
    }

    public String generateAccountCode(Long accountId, Long lastId) {
        StringBuilder builder = new StringBuilder();
        builder.append("MBAC");
        builder.append(String.format("%08d", accountId.intValue()));
        builder.append(String.format("%08d", lastId.intValue() + 1));
        return builder.toString();
    }

    public String generateWalletCode() {
        int count = 4;
        StringBuilder builder = new StringBuilder();
        builder.append("MBW");
        builder.append("-");
        builder.append(java.util.UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
        builder.append("-");
        while (count-- != 0) {
            int character = (int)(Math.random()*NUMERIC_STRING.length());
            builder.append(NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public String generateLoanCode() {
        int count = 4;
        StringBuilder builder = new StringBuilder();
        builder.append("MPL-");
        builder.append(String.valueOf(System.currentTimeMillis()));
        builder.append("-");
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public String generateCode(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}
