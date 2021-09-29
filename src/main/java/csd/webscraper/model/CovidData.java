package csd.webscraper.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CovidData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "hospitalised")
    @NotNull(message = "Hospitalised count must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int hospitalised;

    @Column(name = "hospitalised_icu")
    @NotNull(message = "Hospitalised ICU count must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int hospitalisedICU;

    @Column(name = "require_oxygen_supplementation")
    @NotNull(message = "Require Oxygen Supplementation count must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int requireOxygen;

    @Column(name = "deaths")
    @NotNull(message = "Death count must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int deaths;

    @Column(name = "total_swabs")
    @NotNull(message = "Total swab count must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int totalSwab;

    @Column(name = "average_daily_swab_per_week")
    @NotNull(message = "Average daily number of swab count must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int averageDailySwabPerWeek;

    @Column(name = "total_swabs_per_million")
    @NotNull(message = "Total swabs per 1,000,000 in total population must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int totalSwabPerMillion;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;
}
