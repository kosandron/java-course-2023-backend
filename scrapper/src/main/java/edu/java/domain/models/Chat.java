package edu.java.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "Chats")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chat {
    @Id
    @Column(name = "id")
    private Long id;

    @ManyToMany(mappedBy = "chats", fetch = FetchType.LAZY)
    private List<Link> links;
}
