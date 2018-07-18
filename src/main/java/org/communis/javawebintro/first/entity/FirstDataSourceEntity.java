package org.communis.javawebintro.first.entity;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entity dedicated to first datasource
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "firstDataSourceWiredEntity")
@EqualsAndHashCode(exclude = "firstDataSourceWiredEntity")
@Table(name = "first")
public class FirstDataSourceEntity implements Serializable {

    /**
     * pk
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(
            name = "NAME",
            nullable = false
    )
    private String name;

    /**
     * Wired entity
     */
    @OneToOne(mappedBy = "firstDataSourceEntity", cascade = MERGE, optional = true)
    private FirstDataSourceWiredEntity firstDataSourceWiredEntity;

}
