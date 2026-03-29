package com.rigel.balanca.entry;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
public class RegisterController {

    private final RegisterContextService registerContextService;
    private final EntryService entryService;
    private final EntryQueryService entryQueryService;

    public RegisterController(RegisterContextService registerContextService,
                              EntryService entryService,
                              EntryQueryService entryQueryService) {
        this.registerContextService = registerContextService;
        this.entryService = entryService;
        this.entryQueryService = entryQueryService;
    }

    @GetMapping("/register/context")
    public RegisterContextResponse getContext() {
        return registerContextService.getContext();
    }

    @PostMapping("/entries")
    public CreateEntryResponse createEntry(@Valid @RequestBody CreateEntryRequest request) {
        return entryService.createEntry(request);
    }

    @GetMapping("/entries")
    public EntryHistoryResponse listEntries(@RequestParam(name = "page", required = false) Integer page,
                                            @RequestParam(name = "page_size", required = false) Integer pageSize,
                                            @RequestParam(name = "from", required = false) LocalDate from,
                                            @RequestParam(name = "to", required = false) LocalDate to,
                                            @RequestParam(name = "mood_option_id", required = false) UUID moodOptionId,
                                            @RequestParam(name = "reason_option_id", required = false) UUID reasonOptionId,
                                            @RequestParam(name = "batch_id", required = false) UUID batchId) {
        return entryQueryService.listEntries(page, pageSize, from, to, moodOptionId, reasonOptionId, batchId);
    }
}
