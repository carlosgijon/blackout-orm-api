package com.blackout.api.polls.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "poll_votes")
public class PollVote {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "poll_id", nullable = false)
    private String pollId;

    @Column(name = "option_id")
    private String optionId;

    @Column(name = "voter_name", nullable = false)
    private String voterName;

    /** yes_no: "yes"|"no"|"abstain"  |  approval/proposal: "approve" */
    @Column(nullable = false)
    private String value;

    private String comment;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected PollVote() {}

    public PollVote(String pollId, String optionId, String voterName, String value, String comment) {
        this.pollId = pollId;
        this.optionId = optionId;
        this.voterName = voterName;
        this.value = value;
        this.comment = comment;
    }

    public String  getId()        { return id; }
    public String  getPollId()    { return pollId; }
    public String  getOptionId()  { return optionId; }
    public String  getVoterName() { return voterName; }
    public String  getValue()     { return value; }
    public String  getComment()   { return comment; }
    public Instant getCreatedAt() { return createdAt; }
}
