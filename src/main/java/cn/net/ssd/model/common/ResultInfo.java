package cn.net.ssd.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by sxf on 2019-5-15.
 * 统一的结果返回类
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel
public class ResultInfo implements Serializable {
    private static final long serialVersionUID = 2448561359229882274L;
    /**
     * 结果码
     */
    @ApiModelProperty(value = "结果码", notes = "0:成功，其他均为失败", position = 1, example = "0")
    private String code;

    /**
     * 结果码描述
     */
    @ApiModelProperty(value = "返回结果描述", position = 2, example = "")
    private String msg;

    /**
     * 返回记录条数
     */
    @ApiModelProperty(value = "返回记录条数", position = 3, example = "")
    private Long total;

    /**
     * 返回结果数据
     */
    @ApiModelProperty(value = "返回结果数据", position = 4, example = "")
    private Object data;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Long getTotal() {
        return total;
    }

    public Object getData() {
        return data;
    }

    public static ResultInfoBuilder builder() {
        return new ResultInfoBuilder();
    }

    private ResultInfo(ResultInfoBuilder builder) {
        code = builder.code;
        msg = builder.msg;
        total = builder.total;
        data = builder.data;
    }


    public static final class ResultInfoBuilder {
        private String code;
        private String msg;
        private Long total;
        private Object data;

        private ResultInfoBuilder() {
        }

        public ResultInfoBuilder resultCode(ResultCode resultCode) {
            code = resultCode.getCode();
            msg = resultCode.getMsg();
            return this;
        }

        public ResultInfoBuilder code(String val) {
            code = val;
            return this;
        }

        public ResultInfoBuilder msg(String val) {
            msg = val;
            return this;
        }

        public ResultInfoBuilder total(Long val) {
            total = val;
            return this;
        }

        public ResultInfoBuilder data(Object val) {
            data = val;
            return this;
        }

        public ResultInfo build() {
            return new ResultInfo(this);
        }
    }
}