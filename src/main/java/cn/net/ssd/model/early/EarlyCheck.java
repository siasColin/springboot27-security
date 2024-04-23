package cn.net.ssd.model.early;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

/**
 * @author SXF
 * @version 1.0
 * @description: TODO
 * @date 2024/4/11 13:49
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel
public class EarlyCheck {

    @ApiModelProperty(value = "信息类型", notes = "Actual：正式预警，Test：测试预警", position = 1,allowableValues="Actual,Test", example = "Test")
    private String alertStatus;
    @ApiModelProperty(value = "预警信息状态", notes = "Alert：首发，Update：更新，Cancel：解除", position = 2,allowableValues="Alert,Update,Cancel", example = "Alert")
    private String msgType;
    @ApiModelProperty(value = "灾种编码", notes = "国突标准灾种编码", position = 3, example = "11B03")
    private String eventType;
    @ApiModelProperty(value = "灾种级别", notes = "灾种级别，1:红色，2:橙色，3:黄色，4:蓝色，10:提示", position = 4,allowableValues="1,2,3,4,10", example = "4")
    private String severity;
    @ApiModelProperty(value = "预警发布时间", notes = "预警发布时间，格式 yyyy-MM-dd HH:mm:ss", position =5, example = "2024-04-11 10:36:00")
    private String relieaseTime;
    @ApiModelProperty(value = "预警标题", notes = "预警标题", position = 6, example = "新疆维吾尔自治区气象台发布暴雨蓝色预警[Ⅳ级/一般]")
    private String headline;
    @ApiModelProperty(value = "内容分组标记",notes="数据集合中的内容（data.content）分组标记，相同内容避免重复校验",hidden = true)
    private Map<Integer,List<Integer>> contentGroupMark;
    
    @ApiModelProperty(value = "数据集合 " +
            "数据结构是List<DataDTO> 的格式;", position = 7)
    private List<DataDTO> data;

    public String getAlertStatus() {
        return alertStatus;
    }

    public void setAlertStatus(String alertStatus) {
        this.alertStatus = alertStatus;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getRelieaseTime() {
        return relieaseTime;
    }

    public void setRelieaseTime(String relieaseTime) {
        this.relieaseTime = relieaseTime;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public Map<Integer, List<Integer>> getContentGroupMark() {
        return contentGroupMark;
    }

    public void setContentGroupMark(Map<Integer, List<Integer>> contentGroupMark) {
        this.contentGroupMark = contentGroupMark;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DataDTO {
        @ApiModelProperty(value = "渠道编码", notes = "格式，channel_01，其中channel_是固定前缀", position = 8, example = "channel_01")
        private String eleKey;
        @ApiModelProperty(value = "渠道名称", notes = "渠道名称", position = 9, example = "短信")
        private String eleName;
        @ApiModelProperty(value = "质控内容", notes = "质控内容", position = 10, example = "新疆维吾尔自治区气象台2024年04月11日10时36分发布暴雨蓝色预警信号：未来12小时降雨量将达到50毫米以上，请加强防范。")
        private String content;

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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

    }
}
