-- Create employee_hours and weekly_hours tables
CREATE TABLE employee_hours (
  emp_id     NUMBER,
  work_date  DATE,
  hours      NUMBER(5,2),
  PRIMARY KEY(emp_id, work_date)
);

CREATE TABLE weekly_hours (
  emp_id      NUMBER,
  week_start  DATE,
  total_hrs   NUMBER(7,2),
  PRIMARY KEY(emp_id, week_start)
);
