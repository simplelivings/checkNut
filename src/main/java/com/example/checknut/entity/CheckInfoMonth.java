package com.example.checknut.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * TODO
 * 检验信息月度汇总
 * @version: 1.0
 * @author: faraway
 * @date: 2021-07-19 11:12
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "checkinfomonth")
public class CheckInfoMonth {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String partNum;

    private Integer conformNum;

    private Integer unConformNum;

    private Integer checkItem;

    private String valueUser;

    private Integer month;

    private Integer year;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Version
    @TableField(fill = FieldFill.INSERT)
    private Integer version;
}
