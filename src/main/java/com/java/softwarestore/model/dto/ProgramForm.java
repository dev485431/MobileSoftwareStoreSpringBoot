package com.java.softwarestore.model.dto;

import com.java.softwarestore.validation.ValidationConfig;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ProgramForm {

    @NotBlank(message = "{msg.empty}")
    @Size(min = ValidationConfig.LENGTH_PROGRAM_NAME_MIN, max = ValidationConfig.LENGTH_PROGRAM_NAME_MAX, message = "{msg.invalid.length}")
    @Pattern(regexp = ValidationConfig.REGEXP_ALPHANUMERIC, message = "{msg.invalid.program.name}")
    private String name;

    @NotNull(message = "{msg.null}")
    @Digits(integer = ValidationConfig.LENGTH_CATEGORY_ID_MAX, fraction = ValidationConfig.ZERO)
    private Integer categoryId;

    private MultipartFile file;

    @NotBlank(message = "{msg.empty}")
    @Size(min = ValidationConfig.LENGTH_DESCRIPTION_MIN, max = ValidationConfig.LENGTH_DESCRIPTION_MAX, message = "{msg.invalid.length}")
    @Pattern(regexp = ValidationConfig.REGEXP_ALPHANUMERIC, message = "{msg.invalid.program.description}")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ProgramForm{" +
                "name='" + name + '\'' +
                ", categoryId=" + categoryId +
                ", file=" + file +
                ", description='" + description + '\'' +
                '}';
    }
}
