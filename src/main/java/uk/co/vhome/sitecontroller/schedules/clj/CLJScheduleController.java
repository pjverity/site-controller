package uk.co.vhome.sitecontroller.schedules.clj;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.spring.tx.annotation.Transactional;
import io.reactivex.SingleEmitter;
import uk.co.vhome.sitecontroller.schedules.AbstractScheduleController;
import uk.co.vhome.sitecontroller.schedules.Schedule;

import java.util.Map;

@Controller("/clj/schedules")
public class CLJScheduleController extends AbstractScheduleController<CLJScheduleRepository>
{
	public CLJScheduleController(CLJScheduleRepository scheduleRepository)
	{
		super(scheduleRepository);
	}

	@Override
	@Transactional
	protected void updateSchedule(SingleEmitter<HttpResponse<Schedule>> emitter, Integer id, Map<String, String> body)
	{
		super.updateSchedule(emitter, id, body);
	}
}
