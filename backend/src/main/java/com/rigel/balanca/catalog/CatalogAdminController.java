package com.rigel.balanca.catalog;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/catalogs")
public class CatalogAdminController {

    private final CatalogService catalogService;

    public CatalogAdminController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/moods")
    public List<CatalogOptionView> listMoods() {
        return catalogService.listAllMoodOptions();
    }

    @PostMapping("/moods")
    public CatalogOptionView createMood(@Valid @RequestBody CatalogUpsertRequest request) {
        return catalogService.createMood(request);
    }

    @PatchMapping("/moods/{id}")
    public CatalogOptionView updateMood(@PathVariable UUID id, @Valid @RequestBody CatalogUpsertRequest request) {
        return catalogService.updateMood(id, request);
    }

    @GetMapping("/reasons")
    public List<ReasonOptionView> listReasons() {
        return catalogService.listAllReasonOptions();
    }

    @PostMapping("/reasons")
    public ReasonOptionView createReason(@Valid @RequestBody CatalogUpsertRequest request) {
        return catalogService.createReason(request);
    }

    @PatchMapping("/reasons/{id}")
    public ReasonOptionView updateReason(@PathVariable UUID id, @Valid @RequestBody CatalogUpsertRequest request) {
        return catalogService.updateReason(id, request);
    }

    @GetMapping("/batch-textures")
    public List<CatalogOptionView> listBatchTextures() {
        return catalogService.listAllBatchTextureOptions();
    }

    @PostMapping("/batch-textures")
    public CatalogOptionView createBatchTexture(@Valid @RequestBody CatalogUpsertRequest request) {
        return catalogService.createBatchTexture(request);
    }

    @PatchMapping("/batch-textures/{id}")
    public CatalogOptionView updateBatchTexture(@PathVariable UUID id, @Valid @RequestBody CatalogUpsertRequest request) {
        return catalogService.updateBatchTexture(id, request);
    }

    @GetMapping("/batch-smells")
    public List<CatalogOptionView> listBatchSmells() {
        return catalogService.listAllBatchSmellOptions();
    }

    @PostMapping("/batch-smells")
    public CatalogOptionView createBatchSmell(@Valid @RequestBody CatalogUpsertRequest request) {
        return catalogService.createBatchSmell(request);
    }

    @PatchMapping("/batch-smells/{id}")
    public CatalogOptionView updateBatchSmell(@PathVariable UUID id, @Valid @RequestBody CatalogUpsertRequest request) {
        return catalogService.updateBatchSmell(id, request);
    }
}
