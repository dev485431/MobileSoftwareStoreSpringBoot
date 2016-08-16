package com.dataart.softwarestore.utils;

import com.dataart.softwarestore.model.dto.ProgramTextDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

@Component
public class ProgramInfoHandler {

    @Value("${program.zip.txt.key.name}")
    private String txtNameKey;
    @Value("${program.zip.txt.key.package}")
    private String txtPackageKey;
    @Value("${program.zip.txt.key.picture_128}")
    private String txtPicture128Key;
    @Value("${program.zip.txt.key.picture_512}")
    private String txtPicture512Key;


    public ProgramTextDetails getProgramTextDetails(File programTextFile) throws IOException {
        Properties prop = loadProgramProperties(programTextFile);
        return new ProgramTextDetails(Optional.ofNullable(prop.getProperty(txtNameKey)), Optional.ofNullable(prop.getProperty(txtPackageKey)),
                Optional.ofNullable(prop.getProperty(txtPicture128Key)), Optional.ofNullable(prop.getProperty(txtPicture512Key)));
    }

    private Properties loadProgramProperties(File programTextFile) throws IOException {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(programTextFile)) {
            prop.load(input);
        }
        return prop;
    }


}
