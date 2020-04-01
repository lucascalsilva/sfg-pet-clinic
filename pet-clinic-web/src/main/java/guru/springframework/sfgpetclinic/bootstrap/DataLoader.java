package guru.springframework.sfgpetclinic.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.sfgpetclinic.services.CrudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ObjectMapper mapper;

    public DataLoader(DataLoaderConfig dataLoaderConfig, ObjectMapper mapper) {
        this.dataLoaderConfig = dataLoaderConfig;
        this.mapper = mapper;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Running data loader...");
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
            log.warn("Bootstrap folder not found...");
        }
    }

    private void loadDataFile(File file) {
        try {
            String typeClassName = StringUtils.capitalize(file.getName().replace(".json", "")
                    .split("-", 0)[1]);
            String serviceInterfaceName = typeClassName + "Service";

            Class typeClass = Class.forName(dataLoaderConfig.getBaseModelPackage() + "." + typeClassName);
            Class arrayClass = Array.newInstance(typeClass, 0).getClass();
            Class serviceClass = Class.forName(dataLoaderConfig.getBaseServicePackage() + "." + serviceInterfaceName);
            Optional<Object[]> data = Optional.of((Object[]) mapper.readValue(file, arrayClass));
            CrudService service = (CrudService) applicationContext.getBean(serviceClass);

            data.ifPresent(objects -> {
                Arrays.stream(objects).forEach(object -> {
                    try {
                        service.save(object);
                        log.debug("Loaded record into {} ", file.getName());
                        log.debug("Record {} ", object.toString());
                    }
                    catch(Exception e){
                        log.error("Couldn't load record {}", object.toString());
                        log.error("Exception: ", e);
                    }
                });
            });
            log.info("Loaded file {} ", file.getName());
        } catch (IOException | ClassNotFoundException e) {
            log.error("Issue on loading data for file {}", file.getName());
            log.error("Exception: ", e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
