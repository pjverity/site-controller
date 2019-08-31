package uk.co.vhome.sitecontroller.schedules;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.StringJoiner;

/**
 * Simple representation of a scheduled event
 */
@Entity
@Table(name = "schedules")
@ToString
@Getter
public class Schedule
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private LocalDate commences;

	private LocalTime time;

	private Duration duration;

	private String name;

	private String location;

	private Boolean active;

	public Schedule()
	{
	}

}
