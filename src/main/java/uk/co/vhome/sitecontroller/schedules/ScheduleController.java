package uk.co.vhome.sitecontroller.schedules;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.reactivex.Flowable;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ScheduleController
{
	private final ScheduleRepository scheduleRepository;

	public ScheduleController(ScheduleRepository scheduleRepository)
	{
		this.scheduleRepository = scheduleRepository;
	}

	@Get("/schedules")
	HttpResponse<Flowable<Schedule>> retrieveSchedules()
	{
		var flowableSchedules = Flowable.fromIterable(scheduleRepository.findAllByActiveTrueOrderByCommencesDescTimeDesc());
		return HttpResponse.ok(flowableSchedules);
	}

	@Post("/schedule")
	Single<HttpResponse<Schedule>> saveSchedule(@Body Single<Schedule> newSchedule)
	{
		return newSchedule.map( schedule -> {
			var savedSchedule = scheduleRepository.save(schedule);

			if ( savedSchedule.getId() != null )
			{
				log.info("Saved new schedule: {}", savedSchedule);
				return HttpResponse.created(savedSchedule);
			}

			log.warn("The new schedule was not saved: {}", schedule);
			return HttpResponse.noContent();
		});

	}
}
