package com.example.checknut.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * TODO
 * 返回检验结果的累计信息
 * @version: 1.0
 * @author: faraway
 * @date: 2021-07-21 16:26
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "totalnum")
public class TotalNum {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String partNum;

    private Integer totalNum;

    private Integer conformNum;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Version
    @TableField(fill = FieldFill.INSERT)
    private Integer version;
}
