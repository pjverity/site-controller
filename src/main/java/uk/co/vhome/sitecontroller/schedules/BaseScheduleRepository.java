package uk.co.vhome.sitecontroller.schedules;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

public interface BaseScheduleRepository extends CrudRepository<Schedule, Integer>
{
	@SuppressWarnings("unused")
	@Query("FROM Schedule s WHERE s.active = true ORDER BY commences DESC, time DESC")
	List<Schedule> findAllByActiveTrueOrderByCommencesDescTimeDesc();
}
