package com.example.checknut.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * TODO
 *
 * @version: 1.0
 * @author: faraway
 * @date: 2021-07-21 14:10
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class TempCheckInfo {
    private String partNum;

    private String checkItem;

    private String checkDate;

    private int checkNum;

    private String checkResult;

    private String checkNote;
}
