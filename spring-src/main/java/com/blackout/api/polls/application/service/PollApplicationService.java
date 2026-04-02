package com.blackout.api.polls.application.service;

import com.blackout.api.polls.application.port.out.LoadPollPort;
import com.blackout.api.polls.application.port.out.SavePollPort;
import com.blackout.api.polls.domain.Poll;
import com.blackout.api.polls.domain.PollOption;
import com.blackout.api.polls.domain.PollVote;
import com.blackout.api.polls.infrastructure.web.dto.*;
import com.blackout.api.shared.domain.BadRequestException;
import com.blackout.api.shared.domain.ForbiddenException;
import com.blackout.api.shared.domain.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PollApplicationService {

    private final LoadPollPort loadPoll;
    private final SavePollPort savePoll;

    public PollApplicationService(LoadPollPort loadPoll, SavePollPort savePoll) {
        this.loadPoll = loadPoll;
        this.savePoll = savePoll;
    }

    // ── List ───────────────────────────────────────────────────────────────────

    public List<PollResponse> findAll(String bandId) {
        return loadPoll.findAllByBandId(bandId).stream()
                .map(this::toResponse).toList();
    }

    // ── Get one ────────────────────────────────────────────────────────────────

    public PollResponse get(String bandId, String id) {
        return toResponse(findOwned(bandId, id));
    }

    // ── Create ─────────────────────────────────────────────────────────────────

    @Transactional
    public PollResponse create(String bandId, CreatePollRequest req) {
        if (req.linkedGigId() != null && !loadPoll.existsGigByIdAndBandId(req.linkedGigId(), bandId)) {
            throw new ResourceNotFoundException("Concierto no encontrado");
        }
        Poll poll = new Poll(bandId, req.title(), req.type(), req.createdBy());
        poll.setDescription(req.description());
        if (req.deadline() != null && !req.deadline().isBlank()) poll.setDeadline(java.time.Instant.parse(req.deadline()));
        poll.setLinkedGigId(req.linkedGigId());

        if (req.options() != null) {
            for (String text : req.options()) {
                if (text != null && !text.isBlank()) {
                    poll.getOptions().add(new PollOption(null, text, req.createdBy()));
                }
            }
        }
        Poll saved = savePoll.save(poll);
        // fix pollId on options (set after save when id is generated)
        return toResponse(saved);
    }

    // ── Status ─────────────────────────────────────────────────────────────────

    @Transactional
    public PollResponse setStatus(String bandId, String id, String status) {
        Poll poll = findOwned(bandId, id);
        poll.setStatus(status);
        return toResponse(savePoll.save(poll));
    }

    // ── Delete ─────────────────────────────────────────────────────────────────

    @Transactional
    public void delete(String bandId, String id) {
        findOwned(bandId, id);
        savePoll.deleteById(id);
    }

    // ── Options ────────────────────────────────────────────────────────────────

    @Transactional
    public PollResponse addOption(String bandId, String pollId, String text, String proposedBy) {
        Poll poll = findOwned(bandId, pollId);
        if ("closed".equals(poll.getStatus()) || "archived".equals(poll.getStatus())) {
            throw new ForbiddenException("No se pueden añadir opciones a una votación cerrada");
        }
        if ("yes_no".equals(poll.getType())) {
            throw new BadRequestException("Las votaciones Sí/No no tienen opciones");
        }
        poll.getOptions().add(new PollOption(poll.getId(), text, proposedBy));
        return toResponse(savePoll.save(poll));
    }

    @Transactional
    public PollResponse deleteOption(String bandId, String pollId, String optionId) {
        Poll poll = findOwned(bandId, pollId);
        if ("open".equals(poll.getStatus())) {
            throw new ForbiddenException("No se pueden eliminar opciones con la votación abierta");
        }
        poll.getOptions().removeIf(o -> o.getId().equals(optionId));
        return toResponse(savePoll.save(poll));
    }

    // ── Votes ──────────────────────────────────────────────────────────────────

    @Transactional
    public PollResponse castVote(String bandId, String pollId, CastPollVoteRequest req) {
        Poll poll = findOwned(bandId, pollId);
        if (!"open".equals(poll.getStatus())) {
            throw new ForbiddenException("La votación no está abierta");
        }

        if ("yes_no".equals(poll.getType())) {
            String v = req.value();
            if (v == null || (!v.equals("yes") && !v.equals("no") && !v.equals("abstain"))) {
                throw new BadRequestException("Valor inválido: yes | no | abstain");
            }
            // Remove previous yes_no vote from this voter (no optionId)
            poll.getVotes().removeIf(pv -> pv.getVoterName().equals(req.voterName()) && pv.getOptionId() == null);
            poll.getVotes().add(new PollVote(poll.getId(), null, req.voterName(), v, req.comment()));
        } else {
            // approval / proposal — replace all votes from this voter
            poll.getVotes().removeIf(pv -> pv.getVoterName().equals(req.voterName()));
            List<String> approvedIds = req.approvedOptionIds();
            if (approvedIds != null) {
                for (String optionId : approvedIds) {
                    poll.getVotes().add(new PollVote(poll.getId(), optionId, req.voterName(), "approve", req.comment()));
                }
            }
        }
        return toResponse(savePoll.save(poll));
    }

    // ── Results ────────────────────────────────────────────────────────────────

    public Object getResults(String bandId, String pollId) {
        Poll poll = findOwned(bandId, pollId);

        if ("yes_no".equals(poll.getType())) {
            long yes     = poll.getVotes().stream().filter(v -> "yes".equals(v.getValue())).count();
            long no      = poll.getVotes().stream().filter(v -> "no".equals(v.getValue())).count();
            long abstain = poll.getVotes().stream().filter(v -> "abstain".equals(v.getValue())).count();
            long total   = yes + no + abstain;
            List<VoterBreakdown> voters = poll.getVotes().stream()
                    .map(v -> new VoterBreakdown(v.getVoterName(), v.getValue(), v.getComment()))
                    .toList();
            return new YesNoResults(
                    "yes_no", total,
                    yes,     total > 0 ? Math.round(yes     * 100.0 / total) : 0,
                    no,      total > 0 ? Math.round(no      * 100.0 / total) : 0,
                    abstain, total > 0 ? Math.round(abstain * 100.0 / total) : 0,
                    voters);
        } else {
            // count approvals per option
            Map<String, Long> countByOption = poll.getVotes().stream()
                    .filter(v -> v.getOptionId() != null)
                    .collect(Collectors.groupingBy(PollVote::getOptionId, Collectors.counting()));
            long totalVoters = poll.getVotes().stream()
                    .map(PollVote::getVoterName).distinct().count();
            List<OptionWithCount> options = poll.getOptions().stream()
                    .map(o -> new OptionWithCount(
                            o.getId(), o.getText(), o.getProposedBy(),
                            countByOption.getOrDefault(o.getId(), 0L)))
                    .sorted(Comparator.comparingLong(OptionWithCount::voteCount).reversed())
                    .toList();
            return new OptionResults(poll.getType(), totalVoters, options);
        }
    }

    // ── Private ────────────────────────────────────────────────────────────────

    private Poll findOwned(String bandId, String id) {
        return loadPoll.findByIdAndBandId(id, bandId)
                .orElseThrow(() -> new ResourceNotFoundException("Votación no encontrada"));
    }

    private PollResponse toResponse(Poll p) {
        long uniqueVoters = p.getVotes().stream()
                .map(PollVote::getVoterName).distinct().count();
        List<PollOptionResponse> options = p.getOptions().stream()
                .map(o -> new PollOptionResponse(o.getId(), p.getId(), o.getText(), o.getProposedBy(), o.getCreatedAt()))
                .toList();
        List<PollVoteResponse> votes = p.getVotes().stream()
                .map(v -> new PollVoteResponse(v.getId(), p.getId(), v.getOptionId(), v.getVoterName(), v.getValue(), v.getComment(), v.getCreatedAt()))
                .toList();
        return new PollResponse(
                p.getId(), p.getBandId(), p.getTitle(), p.getDescription(),
                p.getType(), p.getStatus(), p.getCreatedBy(),
                p.getDeadline(), p.getLinkedGigId(), p.getCreatedAt(),
                options, votes, uniqueVoters);
    }
}
