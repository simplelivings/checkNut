package com.example.checknut.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CheckInfoReturn {

    private String checkDate;

    private List<TempCheckInfo> checkInfoList;
}
