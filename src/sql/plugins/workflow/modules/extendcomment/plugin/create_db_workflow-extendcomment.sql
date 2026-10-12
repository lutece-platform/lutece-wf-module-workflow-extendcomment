-- liquibase formatted sql
-- changeset workflow-extendcomment:create_db_workflow-extendcomment.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM task_forms_extend_comment_notification_cf WHERE 1 = 0
--
-- Table structure for task_forms_extend_comment_notification_cf 
--
DROP TABLE IF EXISTS task_forms_extend_comment_notification_cf;
CREATE TABLE task_forms_extend_comment_notification_cf(
  id_task INT NOT NULL,
  message LONG VARCHAR DEFAULT NULL,
  subject LONG VARCHAR DEFAULT NULL,
  sender_name VARCHAR(255) DEFAULT NULL,
  sender_email VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (id_task)
);
