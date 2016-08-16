package com.dataart.softwarestore.utils;

import com.dataart.softwarestore.model.domain.Program;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Component
public class UrlsHandler {

    private static final Logger LOG = Logger.getLogger(UrlsHandler.class);
    private static final String PROTOCOL = "http";
    private static final String BACKSLASH = "/";
    @Value("${programs.main.url.domain}")
    private String programsMainUrlDomain;
    @Value("${programs.main.url.path}")
    private String programsMainUrlPath;
    @Value("${program.default.images.path}")
    private String programsDefaultImagesPath;
    @Value("${program.zip.inner.app.filename}")
    private String zipInnerAppFile;
    @Value("${program.default.img128}")
    private String defaultImg128;
    @Value("${program.default.img512}")
    private String defaultImg512;

    public URL getImageUrl(Program program, ImageUrlType imageUrlType) {
        URI uri;
        URL url = null;
        String imagePath;
        try {
            switch (imageUrlType) {
                case IMAGE_128:
                    imagePath = program.getImg128().isPresent() ? programsMainUrlPath + program.getName() + BACKSLASH
                            + program.getImg128().get() : programsDefaultImagesPath + defaultImg128;
                    uri = new URI(
                            PROTOCOL,
                            programsMainUrlDomain,
                            imagePath,
                            null);
                    break;

                case IMAGE_512:
                    imagePath = program.getImg512().isPresent() ? programsMainUrlPath + program.getName() + BACKSLASH
                            + program.getImg512().get() : programsDefaultImagesPath + defaultImg512;
                    uri = new URI(
                            PROTOCOL,
                            programsMainUrlDomain,
                            imagePath,
                            null);
                    break;

                default:
                    uri = new URI(
                            PROTOCOL,
                            programsMainUrlDomain,
                            programsDefaultImagesPath + defaultImg512,
                            null);
                    break;
            }
            url = uri.toURL();
            LOG.debug("Prepared image url: " + url);
        } catch (URISyntaxException | MalformedURLException e) {
            LOG.error("Failed to prepare image url: " + e.getMessage());
        }
        return url;
    }

    public URL getProgramDownloadUrl(Program program) {
        URL url = null;
        try {
            URI uri = new URI(
                    "http",
                    programsMainUrlDomain,
                    programsMainUrlPath + program.getName() + BACKSLASH + zipInnerAppFile,
                    null);
            url = uri.toURL();
            LOG.debug("Prepared program download url: " + url);
        } catch (URISyntaxException | MalformedURLException e) {
            LOG.error("Failed to prepare download url: " + e.getMessage());
        }
        return url;
    }

}
