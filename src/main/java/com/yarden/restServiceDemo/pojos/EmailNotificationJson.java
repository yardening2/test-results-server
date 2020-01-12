package com.yarden.restServiceDemo.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmailNotificationJson {

    @SerializedName("sdk")
    @Expose
    private String sdk;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("changeLog")
    @Expose
    private String changeLog;

    @SerializedName("testCoverageGap")
    @Expose
    private String testCoverageGap;
    @SerializedName("reportTitle")
    @Expose
    private String reportTitle;
    @SerializedName("mailTextPart")
    @Expose
    private String mailTextPart;
    @SerializedName("isTestRequest")
    @Expose
    private boolean isTestRequest;
    @SerializedName("specificRecipient")
    @Expose
    private String specificRecipient;

    public EmailNotificationJson(boolean isTestRequest) {
        this.isTestRequest = isTestRequest;
    }

    public String getSdk() {
        return sdk;
    }

    public void setSdk(String sdk) {
        this.sdk = sdk;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getChangeLog() {
        return changeLog;
    }

    public void setChangeLogUrl(String changeLog) {
        this.changeLog = changeLog;
    }

    public String getTestCoverageGap() {
        return testCoverageGap;
    }

    public void setTestCoverageGap(String testCoverageGap) {
        this.testCoverageGap = testCoverageGap;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getMailTextPart() {
        return mailTextPart;
    }

    public void setMailTextPart(String mailTextPart) {
        this.mailTextPart = mailTextPart;
    }

    public boolean isTestRequest() {
        return isTestRequest;
    }

    public void setTestRequest(boolean testRequest) {
        isTestRequest = testRequest;
    }

    public String getSpecificRecipient() {
        return specificRecipient;
    }

    public void setSpecificRecipient(String specificRecipient) {
        this.specificRecipient = specificRecipient;
    }
}