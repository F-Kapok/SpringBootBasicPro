package com.fans.pojo;

import com.fans.annotation.MyRule;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
    private String descn;
}