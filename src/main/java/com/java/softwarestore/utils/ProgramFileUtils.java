package com.java.softwarestore.utils;

import com.java.softwarestore.exceptions.ProgramFileProcessingException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Component
public class ProgramFileUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ProgramFileUtils.class);
    @Autowired
    private HttpServletRequest servletRequest;
    @Value("${temp.upload.dir}")
    private String tempUploadDir;

    public Map<String, File> processZipFile(MultipartFile zipFile) throws IOException,
            ProgramFileProcessingException {
        File uploadedZipFile = null;
        File extractPath;
        Map<String, File> extractedFiles;
        try {
            uploadedZipFile = transferFileToDir(zipFile, new File(servletRequest.getSession()
                    .getServletContext().getRealPath(tempUploadDir)));
            extractPath = new File(FilenameUtils.removeExtension(uploadedZipFile.getAbsolutePath()));
            extractedFiles = extractZipFile(uploadedZipFile, extractPath);
        } finally {
            LOG.debug("Attempting to remove uploaded program file");
            removeFileOrDir(uploadedZipFile);
        }
        return extractedFiles;
    }

    private File transferFileToDir(MultipartFile sourceFile, File targetDir) throws IOException,
            ProgramFileProcessingException {
        if (!targetDir.exists() && !targetDir.mkdir()) {
            throw new ProgramFileProcessingException("Failed to create directory for file upload");
        }
        File targetFile = new File(targetDir, sourceFile.getOriginalFilename());
        LOG.debug("Transferring program file to: " + targetFile.getAbsolutePath());
        sourceFile.transferTo(targetFile);
        return targetFile;
    }

    private Map<String, File> extractZipFile(File file, File extractPath) throws IOException,
            ProgramFileProcessingException {

        Map<String, File> extractedEntries = new HashMap<>();
        try (ZipFile zipFile = new ZipFile(file)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File entryDestination = new File(extractPath, entry.getName());
                extractedEntries.put(entry.getName(), entryDestination);

                if (entry.isDirectory()) {
                    if (!entryDestination.exists() && !entryDestination.mkdirs()) {
                        throw new ProgramFileProcessingException("Unable to extract directory from zip file: " + entry
                                .getName());
                    }
                } else {
                    if (entryDestination.getParentFile() != null) {
                        if (!entryDestination.getParentFile().exists() && !entryDestination.getParentFile().mkdirs()) {
                            throw new ProgramFileProcessingException("Unable to create directory for file extraction:" +
                                    " " + entryDestination.getName());
                        }
                    }

                    try (InputStream in = zipFile.getInputStream(entry); OutputStream out = new FileOutputStream
                            (entryDestination)) {
                        IOUtils.copy(in, out);
                    }
                }
            }
        }
        return extractedEntries;
    }

    public void removeFileOrDir(File file) {
        if (file != null && file.exists()) {
            LOG.debug("Removing file or dir: " + file.getAbsolutePath());
            try {
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                LOG.error("Unable to remove file or dir: " + file.getAbsolutePath() + ", Error msg: " + e
                        .getMessage());
            }
        }
    }

}
