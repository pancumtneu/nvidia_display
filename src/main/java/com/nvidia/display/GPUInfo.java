package com.nvidia.display;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class GPUInfo {
    private Integer number;
    private String name;
    private String totalMemory;
    private String usedMemory;
    private String useableMemory;
    private Double usageRate;


    public GPUInfo(Integer number, String name, String totalMemory, String usedMemory, String useableMemory, Double usageRate) {
        this.number = number;
        this.name = name;
        this.totalMemory = totalMemory;
        this.usedMemory = usedMemory;
        this.useableMemory = useableMemory;
        this.usageRate = usageRate;
    }

    public GPUInfo() {
    }
}


