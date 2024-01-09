package dev.efekos.pg.util;

import dev.efekos.pg.data.schema.Certificate;
import dev.efekos.pg.data.schema.EducationInfo;
import dev.efekos.pg.data.schema.ExperienceInfo;
import dev.efekos.pg.data.schema.GeneralInfo;

import java.util.List;

public class WorkContext {
    private EducationInfo grabbedEducationInfo;
    private GeneralInfo grabbedGeneralInfo;
    private ExperienceInfo experienceInfo;
    private String binPath;
    private List<Certificate> certificates;

    public WorkContext() {
    }

    public List<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<Certificate> certificates) {
        this.certificates = certificates;
    }

    public ExperienceInfo getExperienceInfo() {
        return experienceInfo;
    }

    public void setExperienceInfo(ExperienceInfo experienceInfo) {
        this.experienceInfo = experienceInfo;
    }

    public EducationInfo getGrabbedEducationInfo() {
        return grabbedEducationInfo;
    }

    public void setGrabbedEducationInfo(EducationInfo grabbedEducationInfo) {
        this.grabbedEducationInfo = grabbedEducationInfo;
    }

    public GeneralInfo getGrabbedGeneralInfo() {
        return grabbedGeneralInfo;
    }

    public void setGrabbedGeneralInfo(GeneralInfo grabbedGeneralInfo) {
        this.grabbedGeneralInfo = grabbedGeneralInfo;
    }

    public String getBinPath() {
        return binPath;
    }

    public void setBinPath(String binPath) {
        this.binPath = binPath;
    }
}
