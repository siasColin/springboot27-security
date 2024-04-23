package cn.net.ssd.model.early;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author SXF
 * @version 1.0
 * @description: TODO
 * @date 2024/4/15 17:58
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel
public class CheckResult {
    @ApiModelProperty(value = "质控方法",notes="质控方法，用于区分不同类型的质控（如比：敏感词质控、关键词质控、叠词质控等）", position = 1)
    private String checkMethod;
    @ApiModelProperty(value = "质控方法中文名称",notes="质控方法中文名称，用于区分不同类型的质控（如比：敏感词质控、关键词质控、叠词质控等）", position = 1)
    private String checkMethodName;
    @ApiModelProperty(value = "质控级别",notes="-1 关闭，0 禁止发布，1 温馨提示", position = 2)
    private Integer checkLevel;
    @ApiModelProperty(value = "质控状态", notes = "质控状态，0:质控通过，-1:质控未通过，-2:质控超时", position = 3)
    private Integer checkStatus;
    @ApiModelProperty(value = "预警标题质控提醒", notes = "预警标题质控提醒", position = 4, example = "预警标题不完整，应该以发布单位开头！")
    private String headlinePrompt;
    @ApiModelProperty(value = "预警标题质控标注", notes = "预警标题质控标注", position = 5, example = "<span class='search-key'>新疆维吾尔自治区</span>发布暴雨蓝色预警[Ⅳ级/一般]")
    private String headlineMark;
    @ApiModelProperty(value = "质控修改后的预警标题", notes = "质控修改后的预警标题", position = 6, example = "新疆维吾尔自治区气象台发布暴雨蓝色预警[Ⅳ级/一般]")
    private String headlineModified;



    @ApiModelProperty(value = "数据集合 " +
            "数据结构是List<ResultDTO> 的格式;", position = 4)
    private List<CheckResult.ResultDTO> data;

    public String getCheckMethod() {
        return checkMethod;
    }

    public void setCheckMethod(String checkMethod) {
        this.checkMethod = checkMethod;
    }

    public String getCheckMethodName() {
        return checkMethodName;
    }

    public void setCheckMethodName(String checkMethodName) {
        this.checkMethodName = checkMethodName;
    }

    public Integer getCheckLevel() {
        return checkLevel;
    }

    public void setCheckLevel(Integer checkLevel) {
        this.checkLevel = checkLevel;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getHeadlinePrompt() {
        return headlinePrompt;
    }

    public void setHeadlinePrompt(String headlinePrompt) {
        this.headlinePrompt = headlinePrompt;
    }

    public String getHeadlineMark() {
        return headlineMark;
    }

    public void setHeadlineMark(String headlineMark) {
        this.headlineMark = headlineMark;
    }

    public String getHeadlineModified() {
        return headlineModified;
    }

    public void setHeadlineModified(String headlineModified) {
        this.headlineModified = headlineModified;
    }

    public List<CheckResult.ResultDTO> getData() {
        return data;
    }

    public void setData(List<CheckResult.ResultDTO> data) {
        this.data = data;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ResultDTO {
        @ApiModelProperty(value = "渠道编码", notes = "格式，channel_01，其中channel_是固定前缀", position = 5)
        private String eleKey;
        @ApiModelProperty(value = "渠道名称", notes = "渠道名称", position = 6)
        private String eleName;
        @ApiModelProperty(value = "质控状态", notes = "质控状态，0:质控通过，-1:质控未通过", position = 7)
        private Integer checkStatus;
        @ApiModelProperty(value = "内容质控提醒", notes = "内容质控提醒", position = 8)
        private String contentPrompt;
        @ApiModelProperty(value = "内容质控标注", notes = "内容质控标注", position = 9)
        private String contentMark;
        @ApiModelProperty(value = "质控修改后的内容", notes = "质控修改后的内容", position = 10)
        private String contentModified;

        public String getEleKey() {
            return eleKey;
        }

        public void setEleKey(String eleKey) {
            this.eleKey = eleKey;
        }

        public String getEleName() {
            return eleName;
        }

        public void setEleName(String eleName) {
            this.eleName = eleName;
        }

        public Integer getCheckStatus() {
            return checkStatus;
        }

        public void setCheckStatus(Integer checkStatus) {
            this.checkStatus = checkStatus;
        }

        public String getContentPrompt() {
            return contentPrompt;
        }

        public void setContentPrompt(String contentPrompt) {
            this.contentPrompt = contentPrompt;
        }

        public String getContentMark() {
            return contentMark;
        }

        public void setContentMark(String contentMark) {
            this.contentMark = contentMark;
        }

        public String getContentModified() {
            return contentModified;
        }

        public void setContentModified(String contentModified) {
            this.contentModified = contentModified;
        }
    }
}
