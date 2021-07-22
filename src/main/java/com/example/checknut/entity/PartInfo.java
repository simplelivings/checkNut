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
 * @date: 2021-07-08 14:50
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "partinfo")
public class PartInfo {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String partNum;

    private String partName;

    private Integer photoNum;

    private Integer nut1;
    private Integer nut2;
    private Integer nut3;
    private Integer nut4;
    private Integer nut5;
    private Integer nut6;
    private Integer nut7;
    private Integer nut8;
    private Integer nut9;
    private Integer nut10;

    private Integer bolt1;
    private Integer bolt2;
    private Integer bolt3;
    private Integer bolt4;
    private Integer bolt5;
    private Integer bolt6;
    private Integer bolt7;
    private Integer bolt8;
    private Integer bolt9;
    private Integer bolt10;

    private Integer spot1;
    private Integer spot2;
    private Integer spot3;
    private Integer spot4;
    private Integer spot5;
    private Integer spot6;
    private Integer spot7;
    private Integer spot8;
    private Integer spot9;
    private Integer spot10;

    private Integer line1;
    private Integer line2;
    private Integer line3;
    private Integer line4;
    private Integer line5;
    private Integer line6;
    private Integer line7;
    private Integer line8;
    private Integer line9;
    private Integer line10;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Version
    @TableField(fill = FieldFill.INSERT)
    private Integer version;
}
