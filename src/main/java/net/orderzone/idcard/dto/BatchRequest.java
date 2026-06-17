package net.orderzone.idcard.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class BatchRequest {

    @NotEmpty(message = "Batch must contain at least one profile")
    @Valid
    private List<ProfileRequest> profiles;

    public BatchRequest() {}

    public List<ProfileRequest> getProfiles() { return profiles; }
    public void setProfiles(List<ProfileRequest> profiles) { this.profiles = profiles; }
}
