package edu.java.domain.jpa.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.PastOrPresent;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity(name = "Links")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Link {
    @Id
    @SequenceGenerator(name = "links_seq", sequenceName = "links_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "links_seq", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "url")
    private String url;

    @PastOrPresent
    @Column(name = "last_modified_time")
    private OffsetDateTime lastModified;

    @PastOrPresent
    @Column(name = "last_check_time")
    private OffsetDateTime lastChecked;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
        name = "chats_links",
        joinColumns = @JoinColumn(name = "link_id"),
        inverseJoinColumns = @JoinColumn(name = "chat_id")
    )
    private List<Chat> chats;
}
