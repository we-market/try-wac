package com.wemarket.wac.biz.dto;

/**
 * Created by justinli on 2019/3/5
 **/
public class LabelDTO {
    private String labelId;
    private String labelName;
    private String templateOriginPrice;
    private String templateSuggestPrice;
    private int isPopular;
    private int isRecentPopular;

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getTemplateOriginPrice() {
        return templateOriginPrice;
    }

    public void setTemplateOriginPrice(String templateOriginPrice) {
        this.templateOriginPrice = templateOriginPrice;
    }

    public String getTemplateSuggestPrice() {
        return templateSuggestPrice;
    }

    public void setTemplateSuggestPrice(String templateSuggestPrice) {
        this.templateSuggestPrice = templateSuggestPrice;
    }

    public int getIsPopular() {
        return isPopular;
    }

    public void setIsPopular(int isPopular) {
        this.isPopular = isPopular;
    }

    public int getIsRecentPopular() {
        return isRecentPopular;
    }

    public void setIsRecentPopular(int isRecentPopular) {
        this.isRecentPopular = isRecentPopular;
    }
}
