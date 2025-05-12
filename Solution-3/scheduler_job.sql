-- Schedule weekly job every Monday at 2 AM
BEGIN
  DBMS_SCHEDULER.create_job (
    job_name        => 'JOB_WEEKLY_HOURS',
    job_type        => 'STORED_PROCEDURE',
    job_action      => 'calc_weekly_hours',
    start_date      => TRUNC(SYSDATE) + 1 + (2/24),
    repeat_interval => 'FREQ=WEEKLY;BYDAY=MON;BYHOUR=2;BYMINUTE=0;BYSECOND=0',
    enabled         => TRUE
  );
END;
/