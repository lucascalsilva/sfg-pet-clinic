package guru.springframework.sfgpetclinic.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import guru.springframework.sfgpetclinic.services.CrudService;
import guru.springframework.sfgpetclinic.services.OwnerService;
import guru.springframework.sfgpetclinic.services.PetTypeService;
import guru.springframework.sfgpetclinic.services.VetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;

@Component
public class DataLoader implements CommandLineRunner, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private final String baseModelPackage = "guru.springframework.sfgpetclinic.model";
    private final String boostrapResourceFolder = "bootstrap";
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public void run(String... args) throws Exception {
        logger.info("Running data loader...");
        bootstrapData();
    }

    private void bootstrapData() {
        URL bootstrapFolderUrl = Thread.currentThread().getContextClassLoader().getResource(boostrapResourceFolder);
        if (bootstrapFolderUrl != null) {
            Optional<File[]> files = Optional.ofNullable(new File(bootstrapFolderUrl.getPath()).listFiles());
            files.ifPresent(files_ -> {
                Arrays.stream(files_).forEach(this::loadDataFile);
            });
        } else {
            logger.warn("Bootstrap folder not found...");
        }
    }

    private void loadDataFile(File file) {
        try {
            ObjectNode rawJson = new ObjectMapper().readValue(file, ObjectNode.class);
            if (rawJson.has("typeClass") && rawJson.has("serviceBean") && rawJson.has("data")) {
                String typeClass = rawJson.get("typeClass").textValue();
                String serviceBean = rawJson.get("serviceBean").textValue();

                Class typeClass_ = Class.forName(baseModelPackage + "." + StringUtils.capitalize(typeClass));
                Class arrayClass = Array.newInstance(typeClass_, 0).getClass();
                Optional<Object[]> data = Optional.of((Object[]) new ObjectMapper().readValue(rawJson.get("data").toString(), arrayClass));
                CrudService crudService = (CrudService) applicationContext.getBean(serviceBean);

                data.ifPresent(objects -> {
                    Arrays.stream(objects).forEach(crudService::save);
                });
                logger.info("Loaded file {} ", file.getName());
            } else {
                logger.warn("File {} does not have either typeClass, serviceBean or data.", file.getName());
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Issue on loading data for file {}", file.getName());
            logger.error("Exception: ", e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
