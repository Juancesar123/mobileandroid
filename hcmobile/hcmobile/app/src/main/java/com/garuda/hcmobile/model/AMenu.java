package com.garuda.hcmobile.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 5/18/2016.
 */
public class AMenu {
    @SerializedName("module_code")
    private int module_code;
    @SerializedName("module_id")
    private String module_id;
    @SerializedName("module_description")
    private String module_description;
    @SerializedName("module_text")
    private String module_text;
    @SerializedName("module_leaf")
    private String module_leaf;
    @SerializedName("module_tabtitle")
    private String module_tabtitle;
    @SerializedName("module_config")
    private String module_config;
    @SerializedName("module_isactive")
    private String module_isactive;
    @SerializedName("module_type")
    private String module_type;
    @SerializedName("mod_sequence")
    private String mod_sequence;
    @SerializedName("mod_icon")
    private int mod_icon;
    @SerializedName("mod_status")
    private int mod_status;

    public int getMod_status() {
        return mod_status;
    }

    public void setMod_status(int mod_status) {
        this.mod_status = mod_status;
    }

    public String getMod_sequence() {
        return mod_sequence;
    }

    public void setMod_sequence(String mod_sequence) {
        this.mod_sequence = mod_sequence;
    }

    public int getMod_icon() {
        return mod_icon;
    }

    public void setMod_icon(int mod_icon) {
        this.mod_icon = mod_icon;
    }

    public String getModule_leaf() {
        return module_leaf;
    }

    public void setModule_leaf(String module_leaf) {
        this.module_leaf = module_leaf;
    }

    public int getModule_code() {
        return module_code;
    }

    public void setModule_code(int module_code) {
        this.module_code = module_code;
    }

    public String getModule_id() {
        return module_id;
    }

    public void setModule_id(String module_id) {
        this.module_id = module_id;
    }

    public String getModule_description() {
        return module_description;
    }

    public void setModule_description(String module_description) {
        this.module_description = module_description;
    }

    public String getModule_text() {
        return module_text;
    }

    public void setModule_text(String module_text) {
        this.module_text = module_text;
    }

    public String getModule_tabtitle() {
        return module_tabtitle;
    }

    public void setModule_tabtitle(String module_tabtitle) {
        this.module_tabtitle = module_tabtitle;
    }

    public String getModule_config() {
        return module_config;
    }

    public void setModule_config(String module_config) {
        this.module_config = module_config;
    }

    public String getModule_isactive() {
        return module_isactive;
    }

    public void setModule_isactive(String module_isactive) {
        this.module_isactive = module_isactive;
    }

    public String getModule_type() {
        return module_type;
    }

    public void setModule_type(String module_type) {
        this.module_type = module_type;
    }
}
