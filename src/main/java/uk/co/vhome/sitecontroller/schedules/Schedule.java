package uk.co.vhome.sitecontroller.schedules;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Simple representation of a scheduled event
 */
@Entity
@Table(name = "schedules")
public class Schedule
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@SuppressWarnings("unused")
	private LocalDate commences;

	@SuppressWarnings("unused")
	private LocalTime time;

	@SuppressWarnings("unused")
	private Duration duration;

	@SuppressWarnings("unused")
	private String name;

	@SuppressWarnings("unused")
	private String location;

	@SuppressWarnings("unused")
	private Boolean active;

	Schedule()
	{
	}

	// Needed by Micronaut Data
	@SuppressWarnings("WeakerAccess")
	public Integer getId()
	{
		return id;
	}

	// Getters required for JSON serialisation

	@SuppressWarnings("unused")
	public LocalDate getCommences()
	{
		return commences;
	}

	@SuppressWarnings("unused")
	public LocalTime getTime()
	{
		return time;
	}

	@SuppressWarnings("unused")
	public Duration getDuration()
	{
		return duration;
	}

	@SuppressWarnings("unused")
	public String getName()
	{
		return name;
	}

	@SuppressWarnings("unused")
	public String getLocation()
	{
		return location;
	}

	@SuppressWarnings("unused")
	public Boolean getActive()
	{
		return active;
	}

	@Override
	public String toString()
	{
		return "Schedule{" +
				       "id=" + id +
				       ", commences=" + commences +
				       ", time=" + time +
				       ", duration=" + duration +
				       ", name='" + name + '\'' +
				       ", location='" + location + '\'' +
				       ", active=" + active +
				       '}';
	}
}
