/*
 * cred-manager is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2018, Gluu
 */
package org.gluu.casa.conf;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gluu.casa.core.AssetsService;
import org.gluu.casa.core.ResourceExtractor;
import org.gluu.casa.misc.Utils;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jgomer
 */
@ApplicationScoped
public class MainSettingsProducer {

    private static final String DEFAULT_GLUU_BASE = "/etc/gluu";
    private static final String CONF_FILE_RELATIVE_PATH = "conf/casa.json";

    @Inject
    private Logger logger;

    @Inject
    private ResourceExtractor resourceExtractor;

    private static String getGluuBase() {
        String candidateGluuBase = System.getProperty("gluu.base");
        return (candidateGluuBase == null && !Utils.onWindows()) ? DEFAULT_GLUU_BASE : candidateGluuBase;
    }

    /**
     * Returns a reference to the configuration file of the application (casa.json)
     * @param baseDir Path to configuration file without the CONF_FILE_RELATIVE_PATH part
     * @return A File object
     */
    private File getConfigFile(String baseDir) {
        Path path = Paths.get(baseDir, CONF_FILE_RELATIVE_PATH);
        return Files.exists(path) ? path.toFile() : null;
    }

    @Produces @ApplicationScoped
    public MainSettings instance() {

        MainSettings settings = null;
        logger.info("init. Obtaining global settings");

        String gluuBase = getGluuBase();
        logger.info("init. Gluu base inferred was {}", gluuBase);

        if (gluuBase != null) {
            //Get a reference to the config-file
            File srcConfigFile = getConfigFile(gluuBase);

            if (srcConfigFile == null) {
                logger.error("init. Cannot read configuration file {}", CONF_FILE_RELATIVE_PATH);
            } else {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                    //Parses config file in a Configs instance
                    settings = mapper.readValue(srcConfigFile, MainSettings.class);
                    settings.setSourceFile(srcConfigFile);

                    List<String> enabledMethods = settings.getEnabledMethods();
                    if (Utils.isNotEmpty(enabledMethods)) {
                        //If acr plugin mapping does not exist and deprecated "enabled_methods" property does, migrate data
                        Map<String, String> acrPluginMapping = settings.getAcrPluginMap();

                        if (Utils.isEmpty(acrPluginMapping)) {
                            acrPluginMapping = new HashMap<>();
                            for (String acr : enabledMethods) {
                                acrPluginMapping.put(acr, null);
                            }
                            settings.setAcrPluginMap(acrPluginMapping);
                        }
                        //Dismiss "enabled_methods" contents
                        settings.setEnabledMethods(null);
                        //Later, configuration handler will revise that the mapping generated makes sense and will save to disk
                        //Additionally script reloader timer will only keep entries for enabled acrs
                    }

                    String brandingPathStr = settings.getBrandingPath();
                    if (Utils.isNotEmpty(brandingPathStr)) {

                        try {
                            Path brandingPath = Paths.get(brandingPathStr);
                            resourceExtractor.createDirectory(brandingPath, Paths.get(AssetsService.CUSTOM_FILEPATH));

                            logger.info("Transfer of files from {} to static directory completed", brandingPathStr);
                            logger.warn("Review custom branding documentation page to learn how external css branding works now");
                            settings.setUseExternalBranding(true);
                        } catch (Exception e) {
                            logger.error("Error transfering contents from {} to static directory", brandingPathStr);
                            logger.error(e.getMessage(), e);
                        }
                        //Dismiss "branding_path" contents regardless of success
                        settings.setBrandingPath(null);
                    }
                } catch (Exception e) {
                    logger.error("Error parsing configuration file {}", CONF_FILE_RELATIVE_PATH);
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return settings;

    }

}
