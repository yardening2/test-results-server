package com.yarden.restServiceDemo.pojos;

import org.apache.commons.lang3.StringUtils;
import com.yarden.restServiceDemo.mailService.HTMLTableBuilder;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.json.JSONArray;

public class ReportMailData {

    private String mailTextPart;
    private HTMLTableBuilder detailedMissingTestsTable = null;
    private HTMLTableBuilder highLevelReportTable = null;
    private HTMLTableBuilder detailedPassedTestsTable = null;
    private String reportTitle = "";
    private String changeLog = "";
    private String version = "";
    private String coverageGap = "";
    private JSONArray recipientsJsonArray = null;
    private String htmlReportS3BucketName = "";

    public String getReportTitle() {
        return reportTitle;
    }

    public ReportMailData setReportTitle(String reportTitle) {
        this.reportTitle = fixNewLineForHtml(reportTitle);
        return this;
    }

    public String getChangeLog() {
        return changeLog;
    }

    public ReportMailData setChangeLog(String changeLog) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(changeLog);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        this.changeLog = renderer.render(document);
        return this;
    }

    public String getVersion() {
        return version;
    }

    public ReportMailData setVersion(String version) {
        this.version = version.replaceAll("RELEASE_CANDIDATE-", "").replaceAll("RELEASE_CANDIDATE", "");
        return this;
    }

    public String getCoverageGap() {
        return coverageGap;
    }

    public ReportMailData setCoverageGap(String coverageGap) {
        this.coverageGap = fixNewLineForHtml(multilineCapitalize(coverageGap));
        return this;
    }

    public String getMailTextPart() {
        return mailTextPart;
    }

    public ReportMailData setMailTextPart(String mailTextPart) {
        this.mailTextPart = mailTextPart;
        return this;
    }

    public HTMLTableBuilder getDetailedMissingTestsTable() {
        return detailedMissingTestsTable;
    }

    public ReportMailData setDetailedMissingTestsTable(HTMLTableBuilder detailedMissingTestsTable) {
        this.detailedMissingTestsTable = detailedMissingTestsTable;
        return this;
    }

    public HTMLTableBuilder getHighLevelReportTable() {
        return highLevelReportTable;
    }

    public ReportMailData setHighLevelReportTable(HTMLTableBuilder highLevelReportTable) {
        this.highLevelReportTable = highLevelReportTable;
        return this;
    }

    public HTMLTableBuilder getDetailedPassedTestsTable() {
        return detailedPassedTestsTable;
    }

    public ReportMailData setDetailedPassedTestsTable(HTMLTableBuilder detailedPassedTestsTable) {
        this.detailedPassedTestsTable = detailedPassedTestsTable;
        return this;
    }

    public JSONArray getRecipientsJsonArray() {
        return recipientsJsonArray;
    }

    public ReportMailData setRecipientsJsonArray(JSONArray recipientsJsonArray) {
        this.recipientsJsonArray = recipientsJsonArray;
        return this;
    }

    private String fixNewLineForHtml(String string){
        return string.replace("\n", "<br/>").replace(" ", "&nbsp;");
    }

    private String multilineCapitalize(String text){
        String result = "";
        String[] lines = text.split("\n");
        for (String line: lines) {
            result = result + StringUtils.capitalize(line) + "\n";
        }
        return result;
    }

    public String getHtmlReportS3BucketName() {
        return htmlReportS3BucketName;
    }

    public ReportMailData setHtmlReportS3BucketName(String htmlReportS3BucketName) {
        this.htmlReportS3BucketName = htmlReportS3BucketName;
        return this;
    }
}
