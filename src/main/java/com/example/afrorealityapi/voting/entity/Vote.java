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

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "votes")
public class Vote extends BaseEntity {

    @Column(name = "event_id", insertable = false, updatable = false)
    private UUID eventId;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "event_id", nullable = false)
    private com.example.afrorealityapi.event.entity.Event event;

    @Column(name = "option_id", insertable = false, updatable = false)
    private UUID optionId;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "option_id", nullable = false)
    private VotingOption option;

    @Column(name = "category_id", insertable = false, updatable = false)
    private UUID categoryId;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "category_id")
    private VotingCategory category;

    @Column(name = "voter_id", insertable = false, updatable = false)
    private UUID voterId;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "voter_id")
    private com.example.afrorealityapi.user.entity.Profile voter;

    @Column(name = "payment_id", insertable = false, updatable = false)
    private UUID paymentId;

    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "payment_id")
    private com.example.afrorealityapi.payment.entity.Payment payment;

    @Column(name = "event_member_id")
    private UUID eventMemberId;

    @Column(name = "vote_count", nullable = false)
    @Builder.Default
    private int voteCount = 1;

    @Column(name = "voter_email")
    private String voterEmail;

    @Column(name = "voter_phone")
    private String voterPhone;

    @Column(name = "sms_sent", nullable = false)
    @Builder.Default
    private boolean smsSent = false;

    @Column(name = "whatsapp_sent", nullable = false)
    @Builder.Default
    private boolean whatsappSent = false;
}
