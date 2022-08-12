package ir.kavoshgaran.keycloakauth.entity.queue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(generator = "sequence")
    @GenericGenerator(name = "sequence", strategy = "org.hibernate.id.enhanced.TableGenerator",
            parameters = {
                    @Parameter(name = "table_name", value = "SEQUENCE_GENERATOR"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1"),
                    @Parameter(name = "prefer_entity_table_as_segment_value", value = "true")
            })
    private Long id;

    @ColumnDefault("0")
    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate = new Date();

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate = new Date();

}
