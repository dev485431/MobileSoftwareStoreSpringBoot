package com.dataart.softwarestore.validation;

import com.dataart.softwarestore.model.dto.ProgramTextDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ProgramTextDetailsValidator {

    @Value("${program.zip.txt.images.extension}")
    private String imagesNamesExpectedExtension;

    public boolean isValid(ProgramTextDetails programTextDetails) throws IOException {
        return (hasRequiredNonEmptyFields(programTextDetails) && hasExpectedImagesExtensions(programTextDetails) &&
                hasUniqueImagesNames(programTextDetails)) ? true : false;
    }

    private boolean hasRequiredNonEmptyFields(ProgramTextDetails programTextDetails) {
        return programTextDetails.getProgramName().isPresent() && !programTextDetails.getProgramName().get().isEmpty()
                && programTextDetails.getPackageName().isPresent() && !programTextDetails.getPackageName().get()
                .isEmpty();
    }

    private boolean hasExpectedImagesExtensions(ProgramTextDetails programTextDetails) {
        if (programTextDetails.getPicName128().isPresent()) {
            if (!programTextDetails.getPicName128().get().endsWith(imagesNamesExpectedExtension)) return false;
        }
        if (programTextDetails.getPicName512().isPresent()) {
            if (!programTextDetails.getPicName512().get().endsWith(imagesNamesExpectedExtension)) return false;
        }
        return true;
    }

    private boolean hasUniqueImagesNames(ProgramTextDetails programTextDetails) {
        if (programTextDetails.getPicName128().isPresent() && programTextDetails.getPicName512().isPresent()) {
            if (programTextDetails.getPicName128().get().equals(programTextDetails.getPicName512().get())) return false;
        }
        return true;
    }


}
