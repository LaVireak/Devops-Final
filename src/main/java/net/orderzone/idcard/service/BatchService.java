package net.orderzone.idcard.service;

import com.google.zxing.WriterException;
import net.orderzone.idcard.dto.BatchRequest;
import net.orderzone.idcard.dto.ProfileResponse;
import net.orderzone.idcard.model.Profile;
import net.orderzone.idcard.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BatchService {

    private final ProfileService profileService;
    private final PdfExportService pdfExportService;
    private final ProfileRepository profileRepository;

    public BatchService(ProfileService profileService,
                        PdfExportService pdfExportService,
                        ProfileRepository profileRepository) {
        this.profileService = profileService;
        this.pdfExportService = pdfExportService;
        this.profileRepository = profileRepository;
    }

    public List<ProfileResponse> createBatch(BatchRequest request) {
        List<ProfileResponse> results = new ArrayList<>();
        for (var req : request.getProfiles()) {
            results.add(profileService.createProfile(req));
        }
        return results;
    }

    public byte[] exportBatchPdf(List<Long> profileIds) throws IOException, WriterException {
        List<Profile> profiles = profileRepository.findAllById(profileIds);
        return pdfExportService.exportBatchZip(profiles);
    }

    public byte[] exportAllPdf() throws IOException, WriterException {
        List<Profile> profiles = profileRepository.findAll();
        return pdfExportService.exportBatchZip(profiles);
    }
}
