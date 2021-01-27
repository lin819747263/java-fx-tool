package com.kehua.xml.model;

import lombok.Data;

@Data
public class BlockPoint implements Comparable<BlockPoint>{

    private String funCode;

    private Integer addr;

    private String regType;

    private String regParam;

    private String userType;

    @Override
    public int compareTo(BlockPoint o) {
        return this.getAddr().compareTo(o.getAddr());
    }
}
