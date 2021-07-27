package com.example.checknut.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * TODO
 * 零件公差要求，需提前录入数据库
 * @version: 1.0
 * @author: faraway
 * @date: 2021-07-08 15:07
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "parttolerance")
public class PartTolerance {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String partNum;

    private String itemNum;

    private Double upper;

    private Double lower;


    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Version
    @TableField(fill = FieldFill.INSERT)
    private Integer version;
}
