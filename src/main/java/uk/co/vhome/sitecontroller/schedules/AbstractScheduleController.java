package uk.co.vhome.sitecontroller.schedules;

import io.micronaut.core.reflect.ReflectionUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

public abstract class AbstractScheduleController<T extends BaseScheduleRepository>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractScheduleController.class);
	
	private final T scheduleRepository;

	protected AbstractScheduleController(T scheduleRepository)
	{
		this.scheduleRepository = scheduleRepository;
	}

	@Get("{?activeOnly}")
	public HttpResponse<Flowable<Schedule>> retrieveSchedules(@QueryValue @Nullable Boolean activeOnly)
	{
		if (activeOnly == null)
		{
			return HttpResponse.ok(Flowable.fromIterable(scheduleRepository.findAll()));
		}

		return HttpResponse.ok(Flowable.fromIterable(scheduleRepository.findAllByActiveTrueOrderByCommencesDescTimeDesc()));
	}

	@Post
	public Single<HttpResponse<Schedule>> saveSchedule(@Body Single<Schedule> newSchedule)
	{
		return newSchedule.map(schedule -> {
			var savedSchedule = scheduleRepository.save(schedule);

			if (savedSchedule.getId() != null)
			{
				LOGGER.info("Saved new schedule: {}", savedSchedule);
				return HttpResponse.created(savedSchedule);
			}

			LOGGER.warn("The new schedule was not saved: {}", schedule);
			return HttpResponse.noContent();
		});
	}

	@Delete("/{id}")
	public Single<HttpResponse> deleteSchedule(Integer id)
	{
		return Single.create(emitter -> {
			scheduleRepository.deleteById(id);
			LOGGER.info("Deleted schedule with Id: {}", id);
			emitter.onSuccess(HttpResponse.ok());
		});
	}

	@Patch("/{id}")
	public Single<HttpResponse<Schedule>> updateSchedule(Integer id, @Body Map<String, String> body)
	{
		return Single.create(emitter -> updateSchedule(emitter, id, body));
	}

	protected void updateSchedule(SingleEmitter<HttpResponse<Schedule>> emitter, Integer id, @Body Map<String, String> body)
	{
		scheduleRepository.findById(id)
				.ifPresentOrElse(schedule -> patchSchedule(emitter, body, schedule),
				                 () -> emitter.onSuccess(HttpResponse.notFound()));
	}

	private void patchSchedule(SingleEmitter<HttpResponse<Schedule>> emitter, Map<String, String> updatedFields, Schedule schedule)
	{
		LOGGER.info("Updating schedule {} with amendments: {}", schedule, updatedFields);

		updatedFields.forEach((k, v) ->
				                      ReflectionUtils.findField(Schedule.class, k)
						                      .ifPresent(field -> trySetField(field, schedule, v))
		);

		var savedSchedule = scheduleRepository.save(schedule);

		LOGGER.info("Schedule saved as: {}", savedSchedule);

		emitter.onSuccess(HttpResponse.ok(savedSchedule));
	}

	private void trySetField(Field field, Schedule schedule, Object value)
	{
		try
		{
			field.trySetAccessible();
			switch (field.getType().getTypeName())
			{
				case "java.lang.Boolean":
					field.set(schedule, Boolean.valueOf(value.toString()));
					break;
				case "java.lang.Integer":
					field.set(schedule, Integer.parseInt(value.toString()));
					break;
				case "java.lang.String":
					field.set(schedule, value);
					break;
				case "java.time.Duration":
					field.set(schedule, Duration.parse(value.toString()));
					break;
				case "java.time.LocalTime":
					field.set(schedule, LocalTime.parse(value.toString()));
					break;
				case "java.time.LocalDate":
					field.set(schedule, LocalDate.parse(value.toString()));
					break;
			}
		}
		catch (IllegalAccessException e)
		{
			LOGGER.error("Failed to set field {} to {}", field.getName(), value);
			throw new RuntimeException(e);
		}
	}
}
