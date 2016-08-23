package com.mbv.framework.util;

import com.mbv.framework.data.PDFLoanData;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by arindamnath on 02/03/16.
 */
public class PDFUtil {

    private static final int HEADER_SIZE = 18;
    private static final int BODY_SIZE = 10;
    private final int keyLength = 128;

    private final float margin = 72;

    private PDFLoanData PDFLoanData;

    private float startY = 0, midX = 0;
    private PDFont header, body;
    private AccessPermission ap;
    private StandardProtectionPolicy spp;
    private PDDocument document;
    private PDPage page;
    private PDPageContentStream contentStream;

    private SimpleDateFormat dateFormat;

    public PDFUtil() {
        header = PDType1Font.HELVETICA_BOLD;
        body = PDType1Font.HELVETICA;
        ap = new AccessPermission();
        this.dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
    }

    public PDFLoanData getPDFLoanData() {
        return PDFLoanData;
    }

    public void setPDFLoanData(PDFLoanData PDFLoanData) {
        this.PDFLoanData = PDFLoanData;
    }

    public byte[] createBorrowerPDF(boolean isSecure, boolean isLender) {
        try {
            document = new PDDocument();
            page = new PDPage();
            document.addPage(page);
            contentStream = new PDPageContentStream(document, page);

            startY = page.getMediaBox().getUpperRightY() - margin;
            midX = page.getMediaBox().getWidth() / 2;

            contentStream.beginText();
            contentStream.setFont(header, HEADER_SIZE);
            contentStream.moveTextPositionByAmount(margin, startY);
            contentStream.drawString("mPokket Receipt");
            contentStream.endText();

            startY = startY - (HEADER_SIZE + 25);
            contentStream.beginText();
            contentStream.moveTextPositionByAmount(margin, startY);
            contentStream.setFont(header, BODY_SIZE);
            contentStream.drawString("Borrower: ");
            contentStream.setFont(body, BODY_SIZE);
            contentStream.drawString(PDFLoanData.getBorrowerData().getName());
            contentStream.endText();

            contentStream.beginText();
            contentStream.moveTextPositionByAmount(midX, startY);
            contentStream.setFont(header, BODY_SIZE);
            contentStream.drawString("Lender: ");
            contentStream.setFont(body, BODY_SIZE);
            contentStream.drawString(PDFLoanData.getLenderData().getName());
            contentStream.endText();

            startY = startY - (BODY_SIZE + 5);
            contentStream.beginText();
            contentStream.moveTextPositionByAmount(margin, startY);
            contentStream.drawString(PDFLoanData.getBorrowerData().getAddress());
            contentStream.endText();

            contentStream.beginText();
            contentStream.moveTextPositionByAmount(midX, startY);
            contentStream.drawString(PDFLoanData.getLenderData().getAddress());
            contentStream.endText();

            startY = startY - (BODY_SIZE + 5);
            contentStream.beginText();
            contentStream.moveTextPositionByAmount(margin, startY);
            contentStream.drawString(PDFLoanData.getBorrowerData().getCity());
            contentStream.endText();

            contentStream.beginText();
            contentStream.moveTextPositionByAmount(midX, startY);
            contentStream.drawString(PDFLoanData.getLenderData().getCity());
            contentStream.endText();

            startY = startY - (BODY_SIZE + 5);
            contentStream.beginText();
            contentStream.moveTextPositionByAmount(margin, startY);
            contentStream.drawString(PDFLoanData.getBorrowerData().getState() + " - " + PDFLoanData.getBorrowerData().getPincode());
            contentStream.endText();

            contentStream.beginText();
            contentStream.moveTextPositionByAmount(midX, startY);
            contentStream.drawString(PDFLoanData.getLenderData().getState() + " - " + PDFLoanData.getLenderData().getPincode());
            contentStream.endText();

            startY = startY - (BODY_SIZE + 25);
            contentStream.beginText();
            contentStream.moveTextPositionByAmount(margin, startY);
            contentStream.setFont(header, BODY_SIZE);
            contentStream.drawString("Transaction details : ");
            contentStream.endText();

            startY = startY - (BODY_SIZE + 15);
            contentStream.beginText();
            contentStream.moveTextPositionByAmount(margin, startY);
            contentStream.setFont(header, BODY_SIZE);
            contentStream.drawString("Reference ID : ");
            contentStream.setFont(body, BODY_SIZE);
            contentStream.drawString(PDFLoanData.getReferenceId());
            contentStream.endText();

            startY = startY - (BODY_SIZE + 5);
            contentStream.beginText();
            contentStream.moveTextPositionByAmount(margin, startY);
            contentStream.setFont(header, BODY_SIZE);
            contentStream.drawString("Period : ");
            contentStream.setFont(body, BODY_SIZE);
            contentStream.drawString(PDFLoanData.getPeriod() + " Month");
            contentStream.endText();

            startY = startY - (BODY_SIZE + 5);
            contentStream.beginText();
            contentStream.moveTextPositionByAmount(margin, startY);
            contentStream.setFont(header, BODY_SIZE);
            contentStream.drawString((isLender) ? "mPokket money lent : " : "mPokket money availed : ");
            contentStream.setFont(body, BODY_SIZE);
            contentStream.drawString(PDFLoanData.getAmount() + " INR");
            contentStream.endText();

            if(!isLender) {
                startY = startY - (BODY_SIZE + 5);
                contentStream.beginText();
                contentStream.moveTextPositionByAmount(margin, startY);
                contentStream.setFont(header, BODY_SIZE);
                contentStream.drawString("mPokket money received : ");
                contentStream.setFont(body, BODY_SIZE);
                contentStream.drawString(PDFLoanData.getDisbursementAmount() + " INR");
                contentStream.endText();
            }

            startY = startY - (BODY_SIZE + 5);
            contentStream.beginText();
            contentStream.moveTextPositionByAmount(margin, startY);
            contentStream.setFont(header, BODY_SIZE);
            contentStream.drawString("Request Issued On : ");
            contentStream.setFont(body, BODY_SIZE);
            contentStream.drawString(PDFLoanData.getRequestedOn());
            contentStream.endText();

            startY = startY - (BODY_SIZE + 5);
            contentStream.beginText();
            contentStream.moveTextPositionByAmount(margin, startY);
            contentStream.setFont(header, BODY_SIZE);
            contentStream.drawString("Repayment On : ");
            contentStream.setFont(body, BODY_SIZE);
            contentStream.drawString(PDFLoanData.getRepaymentOn());
            contentStream.endText();

            startY = startY - (BODY_SIZE + 50);
            contentStream.beginText();
            contentStream.moveTextPositionByAmount(margin, startY);
            contentStream.setFont(header, BODY_SIZE);
            contentStream.drawString((isLender) ? "Total Amount : " : "Borrow Amount : ");
            contentStream.setFont(body, BODY_SIZE);
            contentStream.drawString(PDFLoanData.getAmount() + " INR");
            contentStream.endText();

            if (isLender) {
                startY = startY - (BODY_SIZE + 5);
                contentStream.beginText();
                contentStream.moveTextPositionByAmount(margin, startY);
                contentStream.setFont(header, BODY_SIZE);
                contentStream.drawString("Loyalty Fee : ");
                contentStream.setFont(body, BODY_SIZE);
                contentStream.drawString(PDFLoanData.getInterest() + " INR");
                contentStream.endText();
            } else {
                startY = startY - (BODY_SIZE + 5);
                contentStream.beginText();
                contentStream.moveTextPositionByAmount(margin, startY);
                contentStream.setFont(header, BODY_SIZE);
                contentStream.drawString("Service Tax + Cess : ");
                contentStream.setFont(body, BODY_SIZE);
                contentStream.drawString(PDFLoanData.getTax() + " INR");
                contentStream.endText();

                startY = startY - (BODY_SIZE + 5);
                contentStream.beginText();
                contentStream.moveTextPositionByAmount(margin, startY);
                contentStream.setFont(header, BODY_SIZE);
                contentStream.drawString("App Usage Fee : ");
                contentStream.setFont(body, BODY_SIZE);
                contentStream.drawString(PDFLoanData.getUsageFee() + " INR");
                contentStream.endText();

                startY = startY - (BODY_SIZE + 5);
                contentStream.beginText();
                contentStream.moveTextPositionByAmount(margin, startY);
                contentStream.setFont(header, BODY_SIZE);
                contentStream.drawString("Total (Principal + Tax + Usage Fee) : ");
                contentStream.setFont(body, BODY_SIZE);
                contentStream.drawString((PDFLoanData.getAmount() + PDFLoanData.getTax() + PDFLoanData.getUsageFee()) + " INR");
                contentStream.endText();
            }

            startY = startY - (BODY_SIZE + 5);
            contentStream.beginText();
            contentStream.moveTextPositionByAmount(margin, startY);
            contentStream.setFont(header, BODY_SIZE);
            contentStream.drawString((isLender) ? "Paid : " : "Paid (Tax + Usage Fee) : ");
            contentStream.setFont(body, BODY_SIZE);
            contentStream.drawString("-" + ((isLender) ? PDFLoanData.getAmount() : (PDFLoanData.getTax() + PDFLoanData.getUsageFee())) + " INR");
            contentStream.endText();

            if (isLender) {
                startY = startY - (BODY_SIZE + 5);
                contentStream.beginText();
                contentStream.moveTextPositionByAmount(margin, startY);
                contentStream.setFont(header, BODY_SIZE);
                contentStream.drawString("Total Repayment (Loyalty + Paid Amt) : ");
                contentStream.setFont(body, BODY_SIZE);
                contentStream.drawString((PDFLoanData.getInterest() + PDFLoanData.getAmount()) + " INR");
                contentStream.endText();
            } else {
                startY = startY - (BODY_SIZE + 5);
                contentStream.beginText();
                contentStream.moveTextPositionByAmount(margin, startY);
                contentStream.setFont(header, BODY_SIZE);
                contentStream.drawString("Pending Payment : ");
                contentStream.setFont(body, BODY_SIZE);
                contentStream.drawString(PDFLoanData.getAmount() + " INR");
                contentStream.endText();
            }

            if (!isLender) {
                startY = startY - (BODY_SIZE + 15);
                contentStream.beginText();
                contentStream.moveTextPositionByAmount(margin, startY);
                contentStream.setFont(header, BODY_SIZE);
                contentStream.drawString("Note: A default rate of 5 INR/day will be applicable if repayment not received by " + PDFLoanData.getRepaymentOn() + ".");
                contentStream.endText();
            }

            contentStream.beginText();
            contentStream.moveTextPositionByAmount(margin, margin + BODY_SIZE + 5);
            contentStream.setFont(header, BODY_SIZE);
            contentStream.drawString("This is a system generated receipt and does not require a signature.");
            contentStream.endText();

            contentStream.beginText();
            contentStream.moveTextPositionByAmount(margin, margin);
            contentStream.setFont(header, BODY_SIZE);
            contentStream.drawString("Generated on " + dateFormat.format(new Date()));
            contentStream.endText();

            contentStream.close();

            if (isSecure) {
                spp = new StandardProtectionPolicy("12345", "", ap);
                spp.setEncryptionKeyLength(keyLength);
                spp.setPermissions(ap);
                document.protect(spp);
            }
            //document.save("/Users/arindamnath/Desktop/mPokket Receipt " + System.currentTimeMillis() +
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            //pdStream = page.getContents();
            document.close();
            System.out.println(out.toByteArray().length);
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            PDFUtil pdfUtil = new PDFUtil();
            pdfUtil.setPDFLoanData(new PDFLoanData());
            pdfUtil.createBorrowerPDF(false, false);
        } catch (Exception e) {

        }
    }
}
