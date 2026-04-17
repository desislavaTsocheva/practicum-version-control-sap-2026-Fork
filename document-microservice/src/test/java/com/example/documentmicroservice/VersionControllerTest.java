package com.example.documentmicroservice;

import com.example.documentmicroservice.controllers.VersionController;
import com.example.documentmicroservice.models.Version;
import com.example.documentmicroservice.services.VersionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VersionController.class)
class VersionControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    public VersionService versionService;

    @Test
    void approveVersion_ShouldReturnOk() throws Exception {
        UUID versionId = UUID.randomUUID();
        doNothing().when(versionService).approveVersion(versionId);

        mockMvc.perform(patch("/api/versions/" + versionId + "/accept"))
                .andExpect(status().isOk())
                .andExpect(content().string("Version approved successfully."));

        verify(versionService, times(1)).approveVersion(versionId);
    }

    @Test
    void approveVersion_ShouldReturnNotFound_WhenVersionDoesNotExist() throws Exception {
        UUID versionId = UUID.randomUUID();
        doThrow(new RuntimeException("Version not found")).when(versionService).approveVersion(versionId);

        mockMvc.perform(patch("/api/versions/" + versionId + "/accept"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Version not found"));
    }

    @Test
    void getCurrentVersion_ShouldReturnVersionJson() throws Exception {
        UUID docId = UUID.randomUUID();
        Version version = new Version();
        version.setVersionNumber(1);
        version.setMessage("v1.pdf");

        when(versionService.getLastApprovedVersion(docId)).thenReturn(version);

        mockMvc.perform(get("/api/versions/document/" + docId + "/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.versionNumber").value(1))
                .andExpect(jsonPath("$.message").value("v1.pdf"));
    }

    @Test
    void getCurrentVersion_ShouldReturnNotFound_WhenNoApprovedVersion() throws Exception {
        UUID docId = UUID.randomUUID();
        when(versionService.getLastApprovedVersion(docId)).thenThrow(new RuntimeException("No version"));

        mockMvc.perform(get("/api/versions/document/" + docId + "/current"))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejectVersion_ShouldDeleteAndReturnOk() throws Exception {
        UUID versionId = UUID.randomUUID();
        doNothing().when(versionService).deleteVersion(versionId);

        mockMvc.perform(post("/api/versions/" + versionId + "/reject"))
                .andExpect(status().isOk());

        verify(versionService, times(1)).deleteVersion(versionId);
    }
}
