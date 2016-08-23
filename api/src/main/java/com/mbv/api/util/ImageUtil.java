package com.mbv.api.util;

import org.apache.commons.codec.binary.Hex;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * Created by arindamnath on 25/02/16.
 */
public class ImageUtil {

    public Point isValidImageType(InputStream is) throws Exception {
        Point imageCoords = new Point();

        int c1 = is.read();
        int c2 = is.read();
        int c3 = is.read();

        if (c1 == 'G' && c2 == 'I' && c3 == 'F') { // GIF
            is.skip(3);
            imageCoords.x = readInt(is, 2, false);
            imageCoords.y = readInt(is, 2, false);
            return imageCoords;
        } else if (c1 == 0xFF && c2 == 0xD8) { // JPG
            while (c3 == 255) {
                int marker = is.read();
                int len = readInt(is, 2, true);
                if (marker == 192 || marker == 193 || marker == 194) {
                    is.skip(1);
                    imageCoords.y = readInt(is, 2, true);
                    imageCoords.x = readInt(is, 2, true);
                    break;
                }
                is.skip(len - 2);
                c3 = is.read();
            }
            return imageCoords;
        } else if (c1 == 137 && c2 == 80 && c3 == 78) { // PNG
            is.skip(15);
            imageCoords.x = readInt(is, 2, true);
            is.skip(2);
            imageCoords.y = readInt(is, 2, true);
            return imageCoords;
        } else
            return null;
    }

    private int readInt(InputStream is, int noOfBytes, boolean bigEndian) throws Exception {
        int ret = 0;
        int sv = bigEndian ? ((noOfBytes - 1) * 8) : 0;
        int cnt = bigEndian ? -8 : 8;
        for (int i = 0; i < noOfBytes; i++) {
            ret |= is.read() << sv;
            sv += cnt;
        }
        return ret;
    }

    private byte[] createChecksum(InputStream fis) throws Exception {
        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;
        do {
            numRead = fis.read(buffer);
            if (numRead > 0)
                complete.update(buffer, 0, numRead);
        } while (numRead != -1);
        fis.close();
        return complete.digest();
    }

    public String getMD5Checksum(InputStream fis) {
        try {
            byte[] b = createChecksum(fis);
            //DatatypeConverter.printHexBinary(b).toUpperCase();
            String result = "";
            for (int i = 0; i < b.length; i++)
                result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public String getMD5Checksum(byte[] bytes) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.reset();
        messageDigest.update(bytes);
        return new String(Hex.encodeHex(messageDigest.digest()));
    }

    public InputStream[] cloneIS(InputStream inputStream, int numTimes) {
        InputStream[] inputStreams = new InputStream[numTimes];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n = 0;
        try {
            while ((n = inputStream.read(buf)) >= 0)
                baos.write(buf, 0, n);
            byte[] content = baos.toByteArray();
            for (int i = 0; i < numTimes; i++)
                inputStreams[i] = new ByteArrayInputStream(content);
            return inputStreams;
        } catch (Exception e) {
            return null;
        }
    }
}
