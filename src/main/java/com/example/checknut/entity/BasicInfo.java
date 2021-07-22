package com.example.checknut.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * TODO
 *
 * @version: 1.0
 * @author: faraway
 * @date: 2021-07-08 15:48
 */

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "basicinfo")
public class BasicInfo {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String partNum;

    private Integer checkItem;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Version
    @TableField(fill = FieldFill.INSERT)
    private Integer version;

    public BasicInfo(String partNum, Integer checkItem) {
        this.partNum = partNum;
        this.checkItem = checkItem;
    }
}
