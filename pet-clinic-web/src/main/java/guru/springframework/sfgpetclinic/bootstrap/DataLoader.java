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
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;

@Component
@Profile("bootstrap-data")
public class DataLoader implements CommandLineRunner, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private final DataLoaderConfig dataLoaderConfig;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public DataLoader(DataLoaderConfig dataLoaderConfig) {
        this.dataLoaderConfig = dataLoaderConfig;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Running data loader...");
        bootstrapData();
    }

    private void bootstrapData() {
        URL bootstrapFolderUrl = Thread.currentThread().getContextClassLoader().getResource(dataLoaderConfig.getBootstrapFilesFolder());
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
            String typeClassName = StringUtils.capitalize(file.getName()).replace(".json", "");
            String serviceInterfaceName = typeClassName + "Service";

            Class typeClass = Class.forName(dataLoaderConfig.getBaseModelPackage() + "." + typeClassName);
            Class arrayClass = Array.newInstance(typeClass, 0).getClass();
            Class serviceInterface = Class.forName(dataLoaderConfig.getBaseServicePackage() + "." + serviceInterfaceName);
            Optional<Object[]> data = Optional.of((Object[]) new ObjectMapper().readValue(file, arrayClass));
            CrudService crudService = (CrudService) applicationContext.getBean(serviceInterface);

            data.ifPresent(objects -> {
                Arrays.stream(objects).forEach(object -> {
                        crudService.save(object);
                        logger.debug("Loaded record into {} ", file.getName());
                        logger.debug("Record {} ", object.toString());
                });
            });
            logger.info("Loaded file {} ", file.getName());
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
