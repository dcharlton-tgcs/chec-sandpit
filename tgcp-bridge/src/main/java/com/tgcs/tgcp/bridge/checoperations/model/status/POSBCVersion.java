package com.tgcs.tgcp.bridge.checoperations.model.status;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "POSBCVersion")
@XmlAccessorType(XmlAccessType.FIELD)
public class POSBCVersion {

    @XmlElement(name = "Version")
    private String version;

    @XmlElement(name = "Release")
    private String release;

    @XmlElement(name = "MaintenanceLevel")
    private String maintenanceLevel;

    @XmlElement(name = "Description")
    private String description;

    public String getVersion() {
        return version;
    }

    public POSBCVersion setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getRelease() {
        return release;
    }

    public POSBCVersion setRelease(String release) {
        this.release = release;
        return this;
    }

    public String getMaintenanceLevel() {
        return maintenanceLevel;
    }

    public POSBCVersion setMaintenanceLevel(String maintenanceLevel) {
        this.maintenanceLevel = maintenanceLevel;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public POSBCVersion setDescription(String description) {
        this.description = description;
        return this;
    }
}
