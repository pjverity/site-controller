package uk.co.vhome.sitecontroller.schedules;

import io.micronaut.data.repository.CrudRepository;

import java.util.List;

public interface BaseScheduleRepository extends CrudRepository<Schedule, Integer>
{
	List<Schedule> findAllByActiveTrue();
}
