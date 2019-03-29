package com.fans.pojo;

import com.fans.annotation.MyRule;
import com.fans.common.UserStatusEnum;
import com.fans.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class User {
    @ApiModelProperty(value = "用户ID", example = "0")
    @NotNull(message = "id不能为空")
    private Long id;
    @ApiModelProperty(value = "用户名")
    @NotEmpty(message = "用户名不能为空")
    private String username;
    @NotEmpty(message = "密码无能为空")
    private String password;
    @NotNull(message = "状态不能为空")
    private Integer status;
    @MyRule
    private String desc;

    private Date createTime;

    private Date updateTime;

    @JsonIgnore
    public String getUserStatusDesc() {
        return Objects.requireNonNull(EnumUtils.getByCode(status, UserStatusEnum.class)).getDesc();
    }


}