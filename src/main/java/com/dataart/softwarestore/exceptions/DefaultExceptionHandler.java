package com.dataart.softwarestore.exceptions;

import org.apache.commons.fileupload.FileUploadBase;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@ControllerAdvice
public class DefaultExceptionHandler {

    private static final Logger LOG = Logger.getLogger(DefaultExceptionHandler.class);
    private static final int SIZE_DIVIDER = 1024;
    private static final String DEFAULT_ERROR_VIEW = "error";
    private static final String VIEW_404 = "error404";
    private static final String SUBMIT_PROGRAM_VIEW = "submit";
    @Autowired
    private MessageSourceAccessor websiteMessages;

    @ExceptionHandler({ProgramFileProcessingException.class, FtpException.class})
    public RedirectView handleProgramFileUploadExceptions(Exception ex, HttpServletRequest request) {
        LOG.error("Exception while processing program file: " + ex.getMessage() + ". Url: " + request
                .getRequestURL());
        RedirectView rv = new RedirectView(SUBMIT_PROGRAM_VIEW);
        FlashMap flashAttributes = RequestContextUtils.getOutputFlashMap(request);
        if (ex instanceof FtpException) {
            flashAttributes.put("errorMessage", websiteMessages.getMessage("exception.file.ftp.upload"));
        } else {
            flashAttributes.put("errorMessage", websiteMessages.getMessage("exception.file.processing"));
        }
        return rv;
    }

    @ExceptionHandler(MultipartException.class)
    public RedirectView handleMultipartException(Exception ex, HttpServletRequest request) {
        RedirectView rv = new RedirectView(SUBMIT_PROGRAM_VIEW);
        FlashMap flashAttributes = RequestContextUtils.getOutputFlashMap(request);
        MultipartException mEx = (MultipartException) ex;

        if (ex.getCause() instanceof FileUploadBase.FileSizeLimitExceededException) {
            FileUploadBase.FileSizeLimitExceededException flEx = (FileUploadBase.FileSizeLimitExceededException) mEx
                    .getCause();
            float permittedSize = flEx.getPermittedSize() / (float) SIZE_DIVIDER;
            String message = websiteMessages.getMessage(
                    "error.file.maxsize",
                    new Object[]{flEx.getFileName(), permittedSize});
            flashAttributes.put("fileError", message);
        } else {
            flashAttributes.put("errorMessage", websiteMessages.getMessage("msg.contact.admin") +
                    ex.getMessage());
        }
        return rv;
    }

    @ExceptionHandler(IOException.class)
    public RedirectView handleIOException(Exception ex, HttpServletRequest request) {
        RedirectView model = new RedirectView(SUBMIT_PROGRAM_VIEW);
        FlashMap flash = RequestContextUtils.getOutputFlashMap(request);
        flash.put("errorMessage", websiteMessages.getMessage("error.file.io") + ex
                .getMessage());
        return model;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String requestHandlingNoHandlerFound() {
        return VIEW_404;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView defaultExceptionHandler(HttpServletRequest req, Exception ex) throws Exception {
        LOG.error(String.format("Exception : %s. Cause: %s", ex.getMessage(), ex.getCause()));
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }

}
