-- Procedure to calculate weekly hours
CREATE OR REPLACE PROCEDURE calc_weekly_hours AS
BEGIN
  DELETE FROM weekly_hours
    WHERE week_start >= TRUNC(SYSDATE, 'IW');

  INSERT /*+ APPEND */ INTO weekly_hours(emp_id, week_start, total_hrs)
  SELECT
    emp_id,
    TRUNC(work_date,'IW') AS week_start,
    SUM(hours)
  FROM employee_hours
  WHERE work_date >= TRUNC(SYSDATE,'IW') - 7
  GROUP BY emp_id, TRUNC(work_date,'IW');
  COMMIT;
END;
/