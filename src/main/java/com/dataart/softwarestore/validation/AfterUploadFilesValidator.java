package com.dataart.softwarestore.validation;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

@Component
public class AfterUploadFilesValidator {

    public boolean areThereEmptyFiles(Map<String, File> files) throws IOException {
        for (Map.Entry<String, File> file : files.entrySet()) {

            try (FileInputStream fis = new FileInputStream(file.getValue().getAbsolutePath())) {
                if (fis.available() == 0) {
                    return true;
                }
            }
        }
        return false;
    }

}
