package com.dataart.softwarestore.model.dto;

import java.util.Optional;

public class ProgramTextDetails {

    private Optional<String> programName;
    private Optional<String> packageName;
    private Optional<String> picName128;
    private Optional<String> picName512;

    public ProgramTextDetails(Optional<String> programName, Optional<String> packageName, Optional<String> picName128, Optional<String> picName512) {
        this.programName = programName;
        this.packageName = packageName;
        this.picName128 = picName128;
        this.picName512 = picName512;
    }

    public Optional<String> getProgramName() {
        return programName;
    }

    public void setProgramName(Optional<String> programName) {
        this.programName = programName;
    }

    public Optional<String> getPackageName() {
        return packageName;
    }

    public void setPackageName(Optional<String> packageName) {
        this.packageName = packageName;
    }

    public Optional<String> getPicName128() {
        return picName128;
    }

    public void setPicName128(Optional<String> picName128) {
        this.picName128 = picName128;
    }

    public Optional<String> getPicName512() {
        return picName512;
    }

    public void setPicName512(Optional<String> picName512) {
        this.picName512 = picName512;
    }
}
