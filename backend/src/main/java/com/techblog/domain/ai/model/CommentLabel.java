package com.techblog.domain.ai.model;

import com.techblog.common.audit.Auditable;
import com.techblog.common.enums.SentimentLabel;
import com.techblog.domain.comment.model.Comment;
import com.techblog.domain.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "comment_labels")
public class CommentLabel extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "labeled_by", nullable = false)
    private User labeledBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "label", nullable = false, length = 20)
    private SentimentLabel label;

    @Column(name = "note", length = 1000)
    private String note;

    @Column(name = "is_training_data", nullable = false)
    private boolean trainingData = true;
}
