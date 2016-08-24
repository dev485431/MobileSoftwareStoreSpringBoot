package com.java.softwarestore.web;

import com.java.softwarestore.exceptions.FtpException;
import com.java.softwarestore.exceptions.ProgramFileProcessingException;
import com.java.softwarestore.model.domain.Category;
import com.java.softwarestore.model.domain.Program;
import com.java.softwarestore.model.domain.Statistics;
import com.java.softwarestore.model.dto.ProgramForm;
import com.java.softwarestore.model.dto.ProgramTextDetails;
import com.java.softwarestore.service.CategoryManager;
import com.java.softwarestore.service.ProgramManager;
import com.java.softwarestore.utils.FtpTransferHandler;
import com.java.softwarestore.utils.ProgramInfoHandler;
import com.java.softwarestore.utils.ProgramZipFileHandler;
import com.java.softwarestore.utils.UrlsHandler;
import com.java.softwarestore.validation.AfterUploadFilesValidator;
import com.java.softwarestore.validation.ProgramFormValidator;
import com.java.softwarestore.validation.ProgramTextDetailsValidator;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.Map;

@Controller
public class ProgramController {

    private static final Logger LOG = LoggerFactory.getLogger(ProgramController.class);
    private static final String PROGRAM_SUBMIT_PAGE = "submit";
    private static final String PROGRAM_DETAILS_PAGE = "details";
    private static final String REDIRECT_TO_SUBMIT_PAGE = "redirect:/submit";
    private static final Long INITIAL_DOWNLOADS = 0L;

    private MessageSourceAccessor websiteMessages;
    private ProgramManager programManager;
    private CategoryManager categoryManager;
    private ProgramFormValidator programFormValidator;
    private AfterUploadFilesValidator afterUploadFilesValidator;
    private ProgramZipFileHandler programZipFileHandler;
    private ProgramInfoHandler programInfoHandler;
    private ProgramTextDetailsValidator programTextDetailsValidator;
    private FtpTransferHandler ftpTransferHandler;
    private UrlsHandler urlsHandler;

    @Value("${spring.http.multipart.max-file-size}")
    private String uploadedFileMaxSize;
    @Value("${program.zip.required.inner.files}")
    private String[] zipFileRequiredInnerFiles;
    @Value("${uploaded.file.extension}")
    private String uploadedFileExtension;
    @Value("${spring.http.multipart.location}")
    private String tempUploadDir;
    @Value("${program.zip.inner.app.filename}")
    private String zipInnerAppFile;
    @Value("${program.zip.inner.txt.info.file}")
    private String zipInnerTxtInfoFile;
    @Value("${programs.main.url.domain}")
    private String programsMainUrlDomain;
    @Value("${programs.main.url.path}")
    private String programsMainUrlPath;
    @Value("${program.file.download.mime.type}")
    private String programDownloadMimeType;
    @Value("${program.file.download.extension}")
    private String programDownloadExtension;
    @Value("${program.default.img512}")
    private String defaultImg512;


    @Autowired
    public ProgramController(MessageSourceAccessor websiteMessages, ProgramManager
            programManager, CategoryManager categoryManager, ProgramFormValidator programFormValidator,
                             AfterUploadFilesValidator afterUploadFilesValidator,
                             ProgramZipFileHandler programZipFileHandler, ProgramInfoHandler programInfoHandler,
                             ProgramTextDetailsValidator programTextDetailsValidator,
                             FtpTransferHandler ftpTransferHandler, UrlsHandler urlsHandler) {
        this.websiteMessages = websiteMessages;
        this.programManager = programManager;
        this.categoryManager = categoryManager;
        this.programFormValidator = programFormValidator;
        this.afterUploadFilesValidator = afterUploadFilesValidator;
        this.programZipFileHandler = programZipFileHandler;
        this.programInfoHandler = programInfoHandler;
        this.programTextDetailsValidator = programTextDetailsValidator;
        this.ftpTransferHandler = ftpTransferHandler;
        this.urlsHandler = urlsHandler;
    }

    @InitBinder("programForm")
    private void initProgramFormValidation(WebDataBinder binder) {
        binder.addValidators(programFormValidator);
    }

    @RequestMapping(value = "/submit", method = RequestMethod.GET)
    public String getAddProgramForm(Model model) {
        LOG.debug("Getting program submit form");
        model.addAttribute("programForm", new ProgramForm());
        model.addAttribute("allCategories", categoryManager.getAllCategories());
        model.addAttribute("maxFileSize", uploadedFileMaxSize);
        model.addAttribute("requiredInnerFiles", zipFileRequiredInnerFiles);
        model.addAttribute("uploadedFileExtension", uploadedFileExtension);
        return PROGRAM_SUBMIT_PAGE;
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public String submitAddProgramForm(Model model, @ModelAttribute("programForm") @Valid ProgramForm programForm,
                                       BindingResult result, RedirectAttributes redirect) throws
            ProgramFileProcessingException, FtpException {
        model.addAttribute("allCategories", categoryManager.getAllCategories());
        model.addAttribute("maxFileSize", uploadedFileMaxSize);
        model.addAttribute("requiredInnerFiles", zipFileRequiredInnerFiles);
        model.addAttribute("uploadedFileExtension", uploadedFileExtension);
        if (result.hasErrors()) return PROGRAM_SUBMIT_PAGE;

        File uploadedZipFile = null;
        File extractPath = null;
        ProgramTextDetails programTextDetails = null;
        try {
            LOG.debug("Current temp upload dir: " + tempUploadDir);
            uploadedZipFile = programZipFileHandler.transferFileToDir(programForm.getFile(), new File(tempUploadDir));
            extractPath = new File(FilenameUtils.removeExtension(uploadedZipFile.getAbsolutePath()));
            Map<String, File> extractedFiles = programZipFileHandler.extractZipFile(uploadedZipFile, extractPath);
            if (afterUploadFilesValidator.areThereEmptyFiles(extractedFiles)) {
                LOG.debug("Some extracted files are empty");
                redirect.addFlashAttribute("errorMessage", websiteMessages.getMessage("error.contains.empty.files"));
                return REDIRECT_TO_SUBMIT_PAGE;
            }

            programTextDetails = programInfoHandler.getProgramTextDetails(extractedFiles.get(zipInnerTxtInfoFile));
            if (!programTextDetailsValidator.isValid(programTextDetails)) {
                LOG.debug("Zip inner txt file has wrong format or is missing required fields");
                redirect.addFlashAttribute("errorMessage", websiteMessages.getMessage("error.zip.txt.file.format"));
                return REDIRECT_TO_SUBMIT_PAGE;
            }

            String targetUploadDir = programForm.getName();
            ftpTransferHandler.uploadFiles(extractedFiles, targetUploadDir);
        } catch (IOException e) {
            LOG.error("Error during processing zip file: " + e.getMessage());
            redirect.addFlashAttribute("errorMessage", websiteMessages.getMessage("error.processing.zip"));
            return REDIRECT_TO_SUBMIT_PAGE;
        } finally {
            LOG.debug("Attempting to remove temporary files");
            programZipFileHandler.batchRemoveFiles(uploadedZipFile, extractPath);
        }

        LOG.debug("Adding new program: " + programForm);
        Category category = categoryManager.getCategoryById(programForm.getCategoryId());
        Statistics statistics = new Statistics(OffsetDateTime.now(), INITIAL_DOWNLOADS);
        Program newProgram = new Program(programForm.getName(), programForm.getDescription(), programTextDetails
                .getPicName128().orElse(null),
                programTextDetails.getPicName512().orElse(null), category, statistics);
        programManager.addProgram(newProgram);
        redirect.addFlashAttribute("successMessage", websiteMessages.getMessage("msg.program.added"));
        return REDIRECT_TO_SUBMIT_PAGE;
    }

    @RequestMapping(value = "/details/{programId}", method = RequestMethod.GET)
    public String getProgramDetailsPage(Model model, @PathVariable int programId) {
        model.addAttribute("allCategories", categoryManager.getAllCategories());
        model.addAttribute("programDetails", programManager.getProgramDetailsById(programId));
        return PROGRAM_DETAILS_PAGE;
    }

    @RequestMapping(value = "/download/{programId}", method = RequestMethod.GET)
    public void downloadProgram(HttpServletResponse response, @PathVariable Integer programId) {
        Program program = programManager.getProgramById(programId);
        response.setContentType(programDownloadMimeType);
        response.setHeader("Content-Disposition", "attachment;filename=" + program.getName() +
                programDownloadExtension);

        URL downloadLink = urlsHandler.getProgramDownloadUrl(program);
        try (InputStream input = downloadLink.openStream()) {
            LOG.debug("Downloading program from link: " + downloadLink);
            IOUtils.copy(input, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            LOG.error("Failed to trigger file download for: " + downloadLink + ", Error is: " + e.getMessage());
        }
        programManager.incrementDownloads(programId);
    }

}
