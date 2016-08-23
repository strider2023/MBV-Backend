package com.mbv.framework.props;

/**
 * Created by arindamnath on 24/02/16.
 */
public class AmazonS3Props {

    private String accessKeyId;

    private String secretKey;

    private String rootBucket;

    private String imageBucket;

    private String receiptBucket;

    AmazonS3Props(){ }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getRootBucket() {
        return rootBucket;
    }

    public void setRootBucket(String rootBucket) {
        this.rootBucket = rootBucket;
    }

    public String getImageBucket() {
        return imageBucket;
    }

    public void setImageBucket(String imageBucket) {
        this.imageBucket = imageBucket;
    }

    public String getReceiptBucket() {
        return receiptBucket;
    }

    public void setReceiptBucket(String receiptBucket) {
        this.receiptBucket = receiptBucket;
    }
}
