package org.communis.javawebintro.second.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;


@Data
@Entity
@Table(name = "second")
public class SecondDataSourceEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(
            name = "NAME",
            nullable = false
    )
    private String name;

}
