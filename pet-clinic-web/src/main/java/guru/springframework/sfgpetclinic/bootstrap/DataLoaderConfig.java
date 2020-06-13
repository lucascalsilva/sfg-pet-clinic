package guru.springframework.sfgpetclinic.bootstrap;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:custom.properties")
@ConfigurationProperties(prefix = "dataloader")
public class DataLoaderConfig {

    private String baseModelPackage = "guru.springframework.sfgpetclinic.model";
    private String baseServicePackage = "guru.springframework.sfgpetclinic.services";
    private String bootstrapFilesFolder = "bootstrap";

    public String getBaseModelPackage() {
        return baseModelPackage;
    }

    public void setBaseModelPackage(String baseModelPackage) {
        this.baseModelPackage = baseModelPackage;
    }

    public String getBaseServicePackage() {
        return baseServicePackage;
    }

    public void setBaseServicePackage(String baseServicePackage) {
        this.baseServicePackage = baseServicePackage;
    }

    public String getBootstrapFilesFolder() {
        return bootstrapFilesFolder;
    }

    public void setBootstrapFilesFolder(String bootstrapFilesFolder) {
        this.bootstrapFilesFolder = bootstrapFilesFolder;
    }
}
