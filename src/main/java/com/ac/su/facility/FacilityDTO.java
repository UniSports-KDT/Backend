package com.ac.su.facility;

import lombok.Data;

import java.util.List;
@Data
public class FacilityDTO {
    private String name;
    private String description;
    private String location;
    private String availableHours;
    private Double fee;
    private AttachmentFlag attachmentFlag;
    private List<String> attachmentNames;
}
