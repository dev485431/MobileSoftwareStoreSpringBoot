package com.dataart.softwarestore.utils;

import com.dataart.softwarestore.exceptions.ProgramFileProcessingException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Component
public class ProgramZipFileHandler {

    private static final Logger LOG = Logger.getLogger(ProgramZipFileHandler.class);

    public File transferFileToDir(CommonsMultipartFile sourceFile, File targetDir) throws IOException,
            ProgramFileProcessingException {
        if (!targetDir.exists()) {
            if (!targetDir.mkdir()) {
                throw new ProgramFileProcessingException("Failed to create directory for file upload");
            }
        }
        File targetFile = new File(targetDir, sourceFile.getOriginalFilename());
        LOG.debug("Transferring program file to: " + targetFile.getAbsolutePath());
        sourceFile.transferTo(targetFile);
        return targetFile;
    }

    public Map<String, File> extractZipFile(File file, File extractPath) throws IOException,
            ProgramFileProcessingException {
        ZipFile zipFile = new ZipFile(file);
        Map<String, File> extractedEntries = new HashMap<>();

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
        zipFile.close();
        return extractedEntries;
    }

    public void batchRemoveFiles(File... files) {
        for (File file : files) {
            if (file.exists()) {
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

}
