package org.communis.javawebintro.first.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Entity dedicated to 1st datasource wired with {@link FirstDataSourceEntity} as one2one
 */
@Data
@Entity
@ToString(exclude = "firstDataSourceEntity")
@EqualsAndHashCode(exclude = "firstDataSourceEntity")
@Table(name = "wired")
public class FirstDataSourceWiredEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "PARENT_ID")
    private FirstDataSourceEntity firstDataSourceEntity;

    @Column(name = "NAME")
    private String name;

}
