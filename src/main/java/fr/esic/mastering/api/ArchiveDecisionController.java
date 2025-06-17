package fr.esic.mastering.api;

import fr.esic.mastering.dto.ArchiveDecisionDto;
import fr.esic.mastering.services.ArchiveDecisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/archive-decisions")
public class ArchiveDecisionController {

    @Autowired
    private ArchiveDecisionService archiveDecisionService;

    @PostMapping
    public ArchiveDecisionDto archive(@RequestBody ArchiveDecisionDto dto) {
        return archiveDecisionService.save(dto);
    }

    @GetMapping
    public List<ArchiveDecisionDto> getAll() {
        return archiveDecisionService.findAll();
    }
}