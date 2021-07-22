package com.example.checknut.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @version: 1.0
 * @author: faraway
 * @date: 2021-07-15 15:23
 */
@Data
@Component
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ReturnInfo {
    private int id;

    private Map<String,Integer> nutMap;

    private Map<String,Integer> boltMap;

    private Map<String,Integer> spotMap;

    private Map<String,Integer> lineMap;

}
