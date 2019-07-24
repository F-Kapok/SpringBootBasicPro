package com.fans.pojo;

import com.fans.annotation.MyRule;
import com.fans.common.UserStatusEnum;
import com.fans.serializer.Date2LongSerializer;
import com.fans.utils.EnumUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class User {
    @Id
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
    /**
     * 注意关键字要进行自定义起名
     */
    @Column(name = "`desc`")
    private String desc;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    @CreationTimestamp
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;

    @JsonSerialize(using = Date2LongSerializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateTime;

    @JsonIgnore
    public String getUserStatusDesc() {
        return Objects.requireNonNull(EnumUtils.getByCode(status, UserStatusEnum.class)).getDesc();
    }


}