package com.java.softwarestore.validation;

import com.java.softwarestore.model.dto.ProgramTextFileDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProgramTextDetailsValidator {

    @Value("${program.zip.txt.images.extension}")
    private String imagesNamesExpectedExtension;

    public boolean isValid(ProgramTextFileDetails programTextFileDetails) {
        return hasRequiredNonEmptyFields(programTextFileDetails) && hasExpectedImagesExtensions
                (programTextFileDetails) && hasUniqueImagesNames(programTextFileDetails);
    }

    private boolean hasRequiredNonEmptyFields(ProgramTextFileDetails programTextFileDetails) {
        return programTextFileDetails.getProgramName().isPresent() && !programTextFileDetails.getProgramName().get()
                .isEmpty() && programTextFileDetails.getPackageName().isPresent() && !programTextFileDetails
                .getPackageName().get().isEmpty();
    }

    private boolean hasExpectedImagesExtensions(ProgramTextFileDetails programTextFileDetails) {
        return !(programTextFileDetails.getPicName128().isPresent() && !programTextFileDetails.getPicName128().get()
                .endsWith(imagesNamesExpectedExtension) || programTextFileDetails.getPicName512().isPresent() &&
                !programTextFileDetails.getPicName512().get().endsWith(imagesNamesExpectedExtension));
    }

    private boolean hasUniqueImagesNames(ProgramTextFileDetails programTextFileDetails) {
        return !(programTextFileDetails.getPicName128().isPresent() && programTextFileDetails.getPicName512().isPresent
                () && programTextFileDetails.getPicName128().get().equals(programTextFileDetails.getPicName512().get
                ()));
    }

}
