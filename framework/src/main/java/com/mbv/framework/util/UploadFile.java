package com.mbv.framework.util;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.mbv.framework.props.AmazonS3Props;
import com.sun.jersey.core.header.FormDataContentDisposition;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;

/**
 * Created by arindamnath on 24/02/16.
 */
public class UploadFile {

    @Autowired
    private AmazonS3Props amazonS3Props;

    public UploadFile() { }

    public AmazonS3Props getAmazonS3Props() {
        return amazonS3Props;
    }

    public void setAmazonS3Props(AmazonS3Props amazonS3Props) {
        this.amazonS3Props = amazonS3Props;
    }

    private String uploadFileToAmazonS3(String bucket, String fileName,
                                        String contentType, InputStream uploadedInputStream,
                                        boolean setFileSize) {
        try {
            System.setProperty("org.xml.sax.driver",
                    "org.xmlpull.v1.sax2.Driver");
            String keyName = bucket + "/" + fileName;
            AWSCredentials awsc = new AWSCredentials() {
                @Override
                public String getAWSAccessKeyId() {
                    return amazonS3Props.getAccessKeyId();
                }

                @Override
                public String getAWSSecretKey() {
                    return amazonS3Props.getSecretKey();
                }
            };
            AmazonS3 s3Client = new AmazonS3Client(awsc);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(contentType);
            if (setFileSize) {
                objectMetadata
                        .setContentLength(uploadedInputStream.available());
            }
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    amazonS3Props.getRootBucket(), keyName, uploadedInputStream, objectMetadata);
            putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
            PutObjectResult result = s3Client.putObject(putObjectRequest);
            return result.getETag();
        } catch (AmazonClientException e) {
            e.printStackTrace();
            return "ERROR : " + e;
        } catch (Exception ex) {
            return "ERROR : " + ex;
        }
    }

    public String uploadImage(InputStream uploadedInputStream,
                                  FormDataContentDisposition fileDetail, String fileName) {
        String ext = fileDetail.getFileName().split("\\.")[1].toString();
        return uploadFileToAmazonS3(amazonS3Props.getImageBucket(), fileName, "image/" + ext,
                uploadedInputStream, false);
    }

    public String uploadAttachment(InputStream uploadedInputStream, String fileName) {
        return uploadFileToAmazonS3(amazonS3Props.getReceiptBucket(), fileName, "application/octet-stream", uploadedInputStream,
                true);
    }
}
