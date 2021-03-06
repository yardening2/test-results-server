package com.yarden.restServiceDemo.pojos;

import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class SdkResultRequestJson implements RequestInterface{

    @SerializedName("sdk")
    @Expose
    private String sdk;
    @SerializedName("group")
    @Expose
    private String group;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("sandbox")
    @Expose
    private Boolean sandbox;
    @SerializedName("results")
    @Expose
    private JsonArray results;
    @SerializedName("mandatory")
    @Expose
    private Boolean mandatory;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public String getSdk() {
        return sdk;
    }

    public void setSdk(String sdk) {
        this.sdk = sdk;
    }

    public String getId() {
        if (StringUtils.isEmpty(this.id)) {
            setId(UUID.randomUUID().toString().substring(0, 6));
        }
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getSandbox() {
        return sandbox;
    }

    public void setSandbox(Boolean sandbox) {
        this.sandbox = sandbox;
    }

    public JsonArray getResults() {
        return results;
    }

    public void setResults(JsonArray results) {
        this.results = results;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Boolean getMandatory() {
        if (this.mandatory == null) {
            return false;
        }
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}