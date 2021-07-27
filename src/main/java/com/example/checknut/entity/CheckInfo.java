package com.example.checknut.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * TODO
 * 记录检验信息
 * @version: 1.0
 * @author: faraway
 * @date: 2021-07-08 14:15
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "checkinfo")
public class CheckInfo {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String partNum;

    private String partName;

    private Integer checkItem;

    private Integer checkStatus;

    private String valueUser;

    private String checkDate;

    private String checkTime;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Version
    @TableField(fill = FieldFill.INSERT)
    private Integer version;
}
