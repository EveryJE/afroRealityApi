package com.example.afrorealityapi.voting.entity;

import com.example.afrorealityapi.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "voting_categories")
public class VotingCategory extends BaseEntity {

    @Column(name = "event_id", insertable = false, updatable = false)
    private UUID eventId;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "event_id", nullable = false)
    private com.example.afrorealityapi.event.entity.Event event;

    @jakarta.persistence.OneToMany(mappedBy = "category", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true, fetch = jakarta.persistence.FetchType.LAZY)
    private java.util.List<VotingOption> votingOptions;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "order_idx", nullable = false)
    @Builder.Default
    private int orderIdx = 0;

    @Column(name = "max_votes_per_user", nullable = false)
    @Builder.Default
    private int maxVotesPerUser = 1;

    @Column(name = "allow_multiple", nullable = false)
    @Builder.Default
    private boolean allowMultiple = false;

    @Column(name = "allow_public_nomination", nullable = false)
    @Builder.Default
    private boolean allowPublicNomination = false;

    @Column(name = "nomination_deadline")
    private OffsetDateTime nominationDeadline;

    @Column(name = "require_approval", nullable = false)
    @Builder.Default
    private boolean requireApproval = true;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "template_config")
    private Map<String, Object> templateConfig;

    @Column(name = "template_image")
    private String templateImage;

    @Column(name = "show_final_image", nullable = false)
    @Builder.Default
    private boolean showFinalImage = true;

    @Column(name = "nomination_price", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal nominationPrice = BigDecimal.ZERO;

    @Column(name = "vote_price", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal votePrice = BigDecimal.ZERO;

    @Column(name = "max_nominees_per_user", nullable = false)
    @Builder.Default
    private int maxNomineesPerUser = 1;

    @Column(name = "show_total_votes_publicly", nullable = false)
    @Builder.Default
    private boolean showTotalVotesPublicly = true;
}
