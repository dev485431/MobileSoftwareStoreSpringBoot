package com.java.softwarestore.validation;

import com.java.softwarestore.model.dto.ProgramForm;
import com.java.softwarestore.service.ProgramManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ProgramFormValidator implements Validator {

    private static final Logger LOG = LoggerFactory.getLogger(ProgramFormValidator.class);
    @Autowired
    private ProgramManager programManager;
    @Autowired
    private BeforeUploadFileValidator beforeUploadFileValidator;
    @Value("${uploaded.file.extension}")
    private String uploadedFileExtension;
    @Value("${program.name.restricted.names}")
    private String[] restrictedProgramNames;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(ProgramForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (!errors.hasErrors()) {
            ProgramForm programForm = (ProgramForm) target;
            MultipartFile programFile = ((ProgramForm) target).getFile();

            if (programFile.getSize() == ValidationConfig.ZERO) {
                LOG.debug("The file is empty");
                errors.rejectValue("file", "error.empty.file");
            }
            if (!programFile.getOriginalFilename().toLowerCase().endsWith(uploadedFileExtension)) {
                errors.rejectValue("file", "error.file.extension");
            }

            for (String restrictedName : restrictedProgramNames) {
                if (programForm.getName().toLowerCase().equals(restrictedName.toLowerCase())) {
                    errors.rejectValue("name", "error.program.name.restricted");
                }
            }

            if (programManager.programNameExists(programForm.getName())) {
                LOG.debug("Program name already exists in the database");
                errors.rejectValue("name", "error.program.name.exists");
            }
            if (!beforeUploadFileValidator.containsExpectedFiles(programForm.getFile())) {
                errors.rejectValue("file", "error.invalid.file");
            }
        }
    }
}
