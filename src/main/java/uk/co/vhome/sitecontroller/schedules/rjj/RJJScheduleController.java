package uk.co.vhome.sitecontroller.schedules.rjj;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.reactivex.SingleEmitter;
import org.springframework.transaction.annotation.Transactional;
import uk.co.vhome.sitecontroller.schedules.AbstractScheduleController;
import uk.co.vhome.sitecontroller.schedules.Schedule;

import java.util.Map;

@Controller("/rjj/schedules")
public class RJJScheduleController extends AbstractScheduleController<RJJScheduleRepository>
{
	public RJJScheduleController(RJJScheduleRepository scheduleRepository)
	{
		super(scheduleRepository);
	}

	@Override
	@Transactional("reigatejuniorjoggers")
	protected void updateSchedule(SingleEmitter<HttpResponse<Schedule>> emitter, Integer id, Map<String, String> body)
	{
		super.updateSchedule(emitter, id, body);
	}
}
