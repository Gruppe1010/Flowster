SET FOREIGN_KEY_CHECKS=0;	

  

DROP DATABASE if exists flowster;  

DROP DATABASE if exists flowster_kea;  

DROP DATABASE if exists flowster_alpha_solutions;  

DROP DATABASE if exists flowster_gruppe10;  

  

CREATE SCHEMA `flowster` ;  

CREATE SCHEMA `flowster_kea` ;  

CREATE SCHEMA `flowster_alpha_solutions` ;  

CREATE SCHEMA `flowster_gruppe10` ;  

  

SET FOREIGN_KEY_CHECKS=1;  

  

CREATE TABLE `flowster`.`emails` (  

  `id_email` INT NOT NULL AUTO_INCREMENT,  

  `email` VARCHAR(320) NOT NULL,  

  PRIMARY KEY (`id_email`, `email`),  

  UNIQUE INDEX `id_email_UNIQUE` (`id_email` ASC) VISIBLE,  

  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE);  

CREATE TABLE `flowster`.`organisations` (  

  `id_organisation` INT NOT NULL AUTO_INCREMENT,  

  `organisation_name` VARCHAR(100) NOT NULL,  

  PRIMARY KEY (`organisation_name`, `id_organisation`),  

  UNIQUE INDEX `id_alpha_solutions_UNIQUE` (`id_organisation` ASC) VISIBLE,  

  UNIQUE INDEX `organisation_name_UNIQUE` (`organisation_name` ASC) VISIBLE);  

ALTER TABLE flowster.organisations AUTO_INCREMENT = 100; 

 

CREATE TABLE `flowster`.`emails_organisations` ( 
 `f_id_email` INT NOT NULL,   
 `f_id_organisation` INT NOT NULL,    
  
  PRIMARY KEY (`f_id_email`), 
  UNIQUE INDEX `f_id_email_UNIQUE` (`f_id_email` ASC) VISIBLE);
  
  
ALTER TABLE `flowster`.`emails_organisations`

ADD CONSTRAINT `f_id_email_emails_organisations`

FOREIGN KEY (`f_id_email`)

REFERENCES `flowster`.`emails` (`id_email`)

ON DELETE CASCADE

ON UPDATE CASCADE,

ADD CONSTRAINT `f_id_organisation_emails_organisations`

FOREIGN KEY (`f_id_organisation`)

REFERENCES `flowster`.`organisations`(`id_organisation`)

ON DELETE CASCADE

ON UPDATE CASCADE;

 

 

CREATE TABLE `flowster`.`job_types` (  

  `id_job_type` INT(2) ZEROFILL NOT NULL AUTO_INCREMENT,  

  `job_type` VARCHAR(100) NOT NULL,  

  PRIMARY KEY (`id_job_type`),  

  UNIQUE INDEX `id_job_type_UNIQUE` (`id_job_type` ASC) VISIBLE,  

  UNIQUE INDEX `job_type_UNIQUE` (`job_type` ASC) VISIBLE);  

  
CREATE TABLE `flowster_kea`.`users` (  

  `id_user` INT NOT NULL AUTO_INCREMENT,  

  `f_id_job_type` INT(2) ZEROFILL NOT NULL,  

  `f_id_email` INT NOT NULL,  

  `firstname` VARCHAR(100) NOT NULL,  

  `surname` VARCHAR(100) NOT NULL,  

  `password` VARCHAR(45) NOT NULL,  

  `manhours` DOUBLE NOT NULL,  

  `profile_picture` LONGBLOB,  

  PRIMARY KEY (`id_user`),  

  UNIQUE INDEX `id_user_UNIQUE` (`id_user` ASC) VISIBLE,  

  UNIQUE INDEX `f_id_email_UNIQUE` (`f_id_email` ASC) VISIBLE,  

  INDEX `f_id_job_type_idx` (`f_id_job_type` ASC) VISIBLE,  

  CONSTRAINT `f_id_job_type_users`  

    FOREIGN KEY (`f_id_job_type`)  

    REFERENCES `flowster`.`job_types` (`id_job_type`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE,  

  CONSTRAINT `f_id_email_users`  

    FOREIGN KEY (`f_id_email`)  

    REFERENCES `flowster`.`emails` (`id_email`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE);  

CREATE TABLE `flowster_kea`.`teams` (  

  `id_team` INT NOT NULL AUTO_INCREMENT,  

  `team_name` VARCHAR(100) NOT NULL,  

  PRIMARY KEY (`id_team`),  

  UNIQUE INDEX `id_team_UNIQUE` (`id_team` ASC) VISIBLE,  

  UNIQUE INDEX `team_name_UNIQUE` (`team_name` ASC) VISIBLE);  

CREATE TABLE `flowster_kea`.`teams_users` (  

`id_teams_users` INT NOT NULL AUTO_INCREMENT,  
 
`f_id_team` INT NOT NULL,  

`f_id_user` INT NOT NULL, 
 

  PRIMARY KEY (`id_teams_users`),  

UNIQUE INDEX `id_teams_users_UNIQUE` (`id_teams_users` ASC) VISIBLE,

INDEX `f_id_user_idx` (`f_id_user` ASC) VISIBLE,
  
  CONSTRAINT `f_id_team`  

    FOREIGN KEY (`f_id_team`)  

    REFERENCES `flowster_kea`.`teams` (`id_team`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE,  

  CONSTRAINT `f_id_user`  

    FOREIGN KEY (`f_id_user`)  

    REFERENCES `flowster_kea`.`users` (`id_user`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE);   

CREATE TABLE `flowster_kea`.`projects` (  

  `id_project` INT NOT NULL AUTO_INCREMENT,  

  `project_title` VARCHAR(100) NOT NULL,  

  `project_description` VARCHAR(10000) NULL,  
  

  `project_deadline` DATE NULL,  

  `project_manhours` DOUBLE NULL,  

  PRIMARY KEY (`id_project`),  

  UNIQUE INDEX `id_project_UNIQUE` (`id_project` ASC) VISIBLE,  

  UNIQUE INDEX `project_title_UNIQUE` (`project_title` ASC) VISIBLE);  

CREATE TABLE `flowster_kea`.`teams_projects` (  
`id_teams_projects` INT NOT NULL AUTO_INCREMENT,  

  `f_id_team` INT NOT NULL,  

  `f_id_project` INT NOT NULL,  

  PRIMARY KEY (`id_teams_projects`),  
UNIQUE INDEX `id_teams_projects _UNIQUE` (`id_teams_projects` ASC) VISIBLE,

  INDEX `f_id_team_idx` (`f_id_team` ASC) VISIBLE,  

  CONSTRAINT `f_id_team_teams_projects`  

    FOREIGN KEY (`f_id_team`)  

    REFERENCES `flowster_kea`.`teams` (`id_team`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE,  

  CONSTRAINT `f_id_project_teams_projects`  

    FOREIGN KEY (`f_id_project`)  

    REFERENCES `flowster_kea`.`projects` (`id_project`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE);  

CREATE TABLE `flowster_kea`.`projects_users` (
  `id_projects_users` INT NOT NULL AUTO_INCREMENT,
  `f_id_project` INT NOT NULL,
  `f_id_user` INT NOT NULL,
  PRIMARY KEY (`id_projects_users`),
  UNIQUE INDEX `id_projects_users_UNIQUE` (`id_projects_users` ASC) VISIBLE,
  UNIQUE INDEX `f_id_project_UNIQUE` (`f_id_project` ASC) VISIBLE,
  INDEX `f_id_user_projects_users_idx` (`f_id_user` ASC) VISIBLE,
  CONSTRAINT `f_id_project_projects_users`
    FOREIGN KEY (`f_id_project`)
    REFERENCES `flowster_kea`.`projects` (`id_project`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `f_id_user_projects_users`
    FOREIGN KEY (`f_id_user`)
    REFERENCES `flowster_kea`.`users` (`id_user`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);


CREATE TABLE `flowster_kea`.`subprojects` (  

  `id_subproject` INT NOT NULL AUTO_INCREMENT,  

  `subproject_title` VARCHAR(100) NOT NULL,  

  `subproject_description` VARCHAR(10000) NULL,  

  `subproject_deadline` DATE NULL,  

  `subproject_manhours` DOUBLE NULL,  

  PRIMARY KEY (`id_subproject`),  

  UNIQUE INDEX `id_subproject_UNIQUE` (`id_subproject` ASC) VISIBLE);


CREATE TABLE `flowster_kea`.`projects_subprojects` (  
  `id_projects_subprojects` INT NOT NULL AUTO_INCREMENT,  

  `f_id_project` INT NOT NULL,  

  `f_id_subproject` INT NOT NULL,  

  PRIMARY KEY (`id_projects_subprojects`),  
UNIQUE INDEX `id_projects_subprojects_UNIQUE` (`id_projects_subprojects` ASC) VISIBLE,  

  UNIQUE INDEX `f_id_subproject_UNIQUE` (`f_id_subproject` ASC) VISIBLE,  

  CONSTRAINT `f_id_project_projects_subprojects`  

    FOREIGN KEY (`f_id_project`)  

    REFERENCES `flowster_kea`.`projects` (`id_project`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE,  

  CONSTRAINT `f_id_subprojectt_projects_subprojects`  

    FOREIGN KEY (`f_id_subproject`)  

    REFERENCES `flowster_kea`.`subprojects` (`id_subproject`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE);  


CREATE TABLE `flowster_kea`.`tasks` (  

  `id_task` INT NOT NULL AUTO_INCREMENT,  

  `task_title` VARCHAR(45) NULL,  
`task_manhours` DOUBLE NULL, 

  PRIMARY KEY (`id_task`),  

  UNIQUE INDEX `id_task_UNIQUE` (`id_task` ASC) VISIBLE);  


CREATE TABLE `flowster_kea`.`subprojects_tasks` (  
  `id_subprojects_tasks` INT NOT NULL AUTO_INCREMENT,  

  `f_id_subproject` INT NOT NULL,  

  `f_id_task` INT NOT NULL,  

  PRIMARY KEY (`id_subprojects_tasks`),  
  UNIQUE INDEX ` id_subprojects_tasks_UNIQUE` (`id_subprojects_tasks` ASC) VISIBLE,  
  UNIQUE INDEX `f_id_task_UNIQUE` (`f_id_task` ASC) VISIBLE,  

  CONSTRAINT `f_id_subproject_subprojects_tasks`  

    FOREIGN KEY (`f_id_subproject`)  

    REFERENCES `flowster_kea`.`subprojects` (`id_subproject`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE,  

  CONSTRAINT `f_id_task_subprojects_tasks`  

    FOREIGN KEY (`f_id_task`)  

    REFERENCES `flowster_kea`.`tasks` (`id_task`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE);  

CREATE TABLE `flowster_kea`.`subtasks` (  

  `id_subtask` INT NOT NULL AUTO_INCREMENT,  

  `subtask_title` VARCHAR(100) NOT NULL,  
`subtask_manhours` DOUBLE NULL,

  PRIMARY KEY (`id_subtask`),  

  UNIQUE INDEX `id_subtask_UNIQUE` (`id_subtask` ASC) VISIBLE);  

CREATE TABLE `flowster_kea`.`tasks_subtasks` (  
  `id_tasks_subtasks` INT NOT NULL AUTO_INCREMENT,  

  `f_id_task` INT NOT NULL,  

  `f_id_subtask` INT NOT NULL,  

  PRIMARY KEY (`id_tasks_subtasks`),  
  UNIQUE INDEX ` id_tasks_subtasks_UNIQUE` (`id_tasks_subtasks` ASC) VISIBLE,  

  UNIQUE INDEX `f_id_subtask_UNIQUE` (`f_id_subtask` ASC) VISIBLE,  

  CONSTRAINT `f_id_task_tasks_subtasks`  

    FOREIGN KEY (`f_id_task`)  

    REFERENCES `flowster_kea`.`tasks` (`id_task`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE,  

  CONSTRAINT `f_id_subtask_tasks_subtasks`  

    FOREIGN KEY (`f_id_subtask`)  

    REFERENCES `flowster_kea`.`subtasks` (`id_subtask`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE);  

  


  

CREATE TABLE `flowster_alpha_solutions`.`users` (  

  `id_user` INT NOT NULL AUTO_INCREMENT,  

  `f_id_job_type` INT(2) ZEROFILL NOT NULL,  

  `f_id_email` INT NOT NULL,  

  `firstname` VARCHAR(100) NOT NULL,  

  `surname` VARCHAR(100) NOT NULL,  

  `password` VARCHAR(45) NOT NULL,  

  `manhours` DOUBLE NOT NULL,  

  `profile_picture` LONGBLOB,  

  PRIMARY KEY (`id_user`),  

  UNIQUE INDEX `id_user_UNIQUE` (`id_user` ASC) VISIBLE,  

  UNIQUE INDEX `f_id_email_UNIQUE` (`f_id_email` ASC) VISIBLE,  

  INDEX `f_id_job_type_idx` (`f_id_job_type` ASC) VISIBLE,  

  CONSTRAINT `f_id_job_type_users`  

    FOREIGN KEY (`f_id_job_type`)  

    REFERENCES `flowster`.`job_types` (`id_job_type`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE,  

  CONSTRAINT `f_id_email_users`  

    FOREIGN KEY (`f_id_email`)  

    REFERENCES `flowster`.`emails` (`id_email`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE);  

CREATE TABLE `flowster_alpha_solutions`.`teams` (  

  `id_team` INT NOT NULL AUTO_INCREMENT,  

  `team_name` VARCHAR(100) NOT NULL,  

  PRIMARY KEY (`id_team`),  

  UNIQUE INDEX `id_team_UNIQUE` (`id_team` ASC) VISIBLE,  

  UNIQUE INDEX `team_name_UNIQUE` (`team_name` ASC) VISIBLE);  

CREATE TABLE `flowster_alpha_solutions`.`teams_users` (  

`id_teams_users` INT NOT NULL AUTO_INCREMENT,  
 
`f_id_team` INT NOT NULL,  

`f_id_user` INT NOT NULL, 
 

  PRIMARY KEY (`id_teams_users`),  

UNIQUE INDEX `id_teams_users_UNIQUE` (`id_teams_users` ASC) VISIBLE,

INDEX `f_id_user_idx` (`f_id_user` ASC) VISIBLE,
  
  CONSTRAINT `f_id_team`  

    FOREIGN KEY (`f_id_team`)  

    REFERENCES `flowster_alpha_solutions`.`teams` (`id_team`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE,  

  CONSTRAINT `f_id_user`  

    FOREIGN KEY (`f_id_user`)  

    REFERENCES `flowster_alpha_solutions`.`users` (`id_user`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE);   

CREATE TABLE `flowster_alpha_solutions`.`projects` (  

  `id_project` INT NOT NULL AUTO_INCREMENT,  

  `project_title` VARCHAR(100) NOT NULL,  

  `project_description` VARCHAR(10000) NULL,  



  `project_deadline` DATE NULL,  

  `project_manhours` DOUBLE NULL,  

  PRIMARY KEY (`id_project`),  

  UNIQUE INDEX `id_project_UNIQUE` (`id_project` ASC) VISIBLE,  

  UNIQUE INDEX `project_title_UNIQUE` (`project_title` ASC) VISIBLE);  

CREATE TABLE `flowster_alpha_solutions`.`projects_users` (
  `id_projects_users` INT NOT NULL AUTO_INCREMENT,
  `f_id_project` INT NOT NULL,
  `f_id_user` INT NOT NULL,
  PRIMARY KEY (`id_projects_users`),
  UNIQUE INDEX `id_projects_users_UNIQUE` (`id_projects_users` ASC) VISIBLE,
  UNIQUE INDEX `f_id_project_UNIQUE` (`f_id_project` ASC) VISIBLE,
  INDEX `f_id_user_projects_users_idx` (`f_id_user` ASC) VISIBLE,
  CONSTRAINT `f_id_project_projects_users`
    FOREIGN KEY (`f_id_project`)
    REFERENCES `flowster_alpha_solutions`.`projects` (`id_project`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `f_id_user_projects_users`
    FOREIGN KEY (`f_id_user`)
    REFERENCES `flowster_alpha_solutions`.`users` (`id_user`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

CREATE TABLE `flowster_alpha_solutions`.`teams_projects` (  
`id_teams_projects` INT NOT NULL AUTO_INCREMENT,  

  `f_id_team` INT NOT NULL,  

  `f_id_project` INT NOT NULL,  

  PRIMARY KEY (`id_teams_projects`),  
UNIQUE INDEX `id_teams_projects _UNIQUE` (`id_teams_projects` ASC) VISIBLE,

  INDEX `f_id_team_idx` (`f_id_team` ASC) VISIBLE,  

  CONSTRAINT `f_id_team_teams_projects`  

    FOREIGN KEY (`f_id_team`)  

    REFERENCES `flowster_alpha_solutions`.`teams` (`id_team`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE,  

  CONSTRAINT `f_id_project_teams_projects`  

    FOREIGN KEY (`f_id_project`)  

    REFERENCES `flowster_alpha_solutions`.`projects` (`id_project`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE);  

CREATE TABLE `flowster_alpha_solutions`.`subprojects` (  

  `id_subproject` INT NOT NULL AUTO_INCREMENT,  

  `subproject_title` VARCHAR(100) NOT NULL,  

  `subproject_description` VARCHAR(10000) NULL,  

  `subproject_deadline` DATE NULL,  

  `subproject_manhours` DOUBLE NULL,  

  PRIMARY KEY (`id_subproject`),  

  UNIQUE INDEX `id_subproject_UNIQUE` (`id_subproject` ASC) VISIBLE);  

CREATE TABLE `flowster_alpha_solutions`.`projects_subprojects` (  
  `id_projects_subprojects` INT NOT NULL AUTO_INCREMENT,  

  `f_id_project` INT NOT NULL,  

  `f_id_subproject` INT NOT NULL,  

  PRIMARY KEY (`id_projects_subprojects`),  
UNIQUE INDEX `id_projects_subprojects_UNIQUE` (`id_projects_subprojects` ASC) VISIBLE,  

  UNIQUE INDEX `f_id_subproject_UNIQUE` (`f_id_subproject` ASC) VISIBLE,  

  CONSTRAINT `f_id_project_projects_subprojects`  

    FOREIGN KEY (`f_id_project`)  

    REFERENCES `flowster_alpha_solutions`.`projects` (`id_project`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE,  

  CONSTRAINT `f_id_subprojectt_projects_subprojects`  

    FOREIGN KEY (`f_id_subproject`)  

    REFERENCES `flowster_alpha_solutions`.`subprojects` (`id_subproject`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE);  

CREATE TABLE `flowster_alpha_solutions`.`tasks` (  

  `id_task` INT NOT NULL AUTO_INCREMENT,  

  `task_title` VARCHAR(45) NULL,  
`task_manhours` DOUBLE NULL,

  PRIMARY KEY (`id_task`),  

  UNIQUE INDEX `id_task_UNIQUE` (`id_task` ASC) VISIBLE);  

CREATE TABLE `flowster_alpha_solutions`.`subprojects_tasks` (  
  `id_subprojects_tasks` INT NOT NULL AUTO_INCREMENT,  

  `f_id_subproject` INT NOT NULL,  

  `f_id_task` INT NOT NULL,  

  PRIMARY KEY (`id_subprojects_tasks`),  
  UNIQUE INDEX ` id_subprojects_tasks_UNIQUE` (`id_subprojects_tasks` ASC) VISIBLE,  
  UNIQUE INDEX `f_id_task_UNIQUE` (`f_id_task` ASC) VISIBLE,  

  CONSTRAINT `f_id_subproject_subprojects_tasks`  

    FOREIGN KEY (`f_id_subproject`)  

    REFERENCES `flowster_alpha_solutions`.`subprojects` (`id_subproject`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE,  

  CONSTRAINT `f_id_task_subprojects_tasks`  

    FOREIGN KEY (`f_id_task`)  

    REFERENCES `flowster_alpha_solutions`.`tasks` (`id_task`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE);  

CREATE TABLE `flowster_alpha_solutions`.`subtasks` (  

  `id_subtask` INT NOT NULL AUTO_INCREMENT,  

  `subtask_title` VARCHAR(100) NOT NULL,  
`subtask_manhours` DOUBLE NULL,

  PRIMARY KEY (`id_subtask`),  

  UNIQUE INDEX `id_subtask_UNIQUE` (`id_subtask` ASC) VISIBLE);  

CREATE TABLE `flowster_alpha_solutions`.`tasks_subtasks` (  
  `id_tasks_subtasks` INT NOT NULL AUTO_INCREMENT,  

  `f_id_task` INT NOT NULL,  

  `f_id_subtask` INT NOT NULL,  

  PRIMARY KEY (`id_tasks_subtasks`),  
  UNIQUE INDEX ` id_tasks_subtasks_UNIQUE` (`id_tasks_subtasks` ASC) VISIBLE,  

  UNIQUE INDEX `f_id_subtask_UNIQUE` (`f_id_subtask` ASC) VISIBLE,  

  CONSTRAINT `f_id_task_tasks_subtasks`  

    FOREIGN KEY (`f_id_task`)  

    REFERENCES `flowster_alpha_solutions`.`tasks` (`id_task`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE,  

  CONSTRAINT `f_id_subtask_tasks_subtasks`  

    FOREIGN KEY (`f_id_subtask`)  

    REFERENCES `flowster_alpha_solutions`.`subtasks` (`id_subtask`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE); 


CREATE TABLE `flowster_gruppe10`.`users` (  

  `id_user` INT NOT NULL AUTO_INCREMENT,  

  `f_id_job_type` INT(2) ZEROFILL NOT NULL,  

  `f_id_email` INT NOT NULL,  

  `firstname` VARCHAR(100) NOT NULL,  

  `surname` VARCHAR(100) NOT NULL,  

  `password` VARCHAR(45) NOT NULL,  

  `manhours` DOUBLE NOT NULL,  

  `profile_picture` LONGBLOB,  

  PRIMARY KEY (`id_user`),  

  UNIQUE INDEX `id_user_UNIQUE` (`id_user` ASC) VISIBLE,  

  UNIQUE INDEX `f_id_email_UNIQUE` (`f_id_email` ASC) VISIBLE,  

  INDEX `f_id_job_type_idx` (`f_id_job_type` ASC) VISIBLE,  

  CONSTRAINT `f_id_job_type_users`  

    FOREIGN KEY (`f_id_job_type`)  

    REFERENCES `flowster`.`job_types` (`id_job_type`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE,  

  CONSTRAINT `f_id_email_users`  

    FOREIGN KEY (`f_id_email`)  

    REFERENCES `flowster`.`emails` (`id_email`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE);  

CREATE TABLE `flowster_gruppe10`.`teams` (  

  `id_team` INT NOT NULL AUTO_INCREMENT,  

  `team_name` VARCHAR(100) NOT NULL,  

  PRIMARY KEY (`id_team`),  

  UNIQUE INDEX `id_team_UNIQUE` (`id_team` ASC) VISIBLE,  

  UNIQUE INDEX `team_name_UNIQUE` (`team_name` ASC) VISIBLE);  

CREATE TABLE `flowster_gruppe10`.`teams_users` (  

`id_teams_users` INT NOT NULL AUTO_INCREMENT,  
 
`f_id_team` INT NOT NULL,  

`f_id_user` INT NOT NULL, 
 

  PRIMARY KEY (`id_teams_users`),  

UNIQUE INDEX `id_teams_users_UNIQUE` (`id_teams_users` ASC) VISIBLE,

INDEX `f_id_user_idx` (`f_id_user` ASC) VISIBLE,
  
  CONSTRAINT `f_id_team`  

    FOREIGN KEY (`f_id_team`)  

    REFERENCES `flowster_gruppe10`.`teams` (`id_team`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE,  

  CONSTRAINT `f_id_user`  

    FOREIGN KEY (`f_id_user`)  

    REFERENCES `flowster_gruppe10`.`users` (`id_user`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE);   

CREATE TABLE `flowster_gruppe10`.`projects` (  

  `id_project` INT NOT NULL AUTO_INCREMENT,  

  `project_title` VARCHAR(100) NOT NULL,  

  `project_description` VARCHAR(10000) NULL,  

  

  `project_deadline` DATE NULL,  

  `project_manhours` DOUBLE NULL,  

  PRIMARY KEY (`id_project`),  

  UNIQUE INDEX `id_project_UNIQUE` (`id_project` ASC) VISIBLE,  

  UNIQUE INDEX `project_title_UNIQUE` (`project_title` ASC) VISIBLE);  

CREATE TABLE `flowster_gruppe10`.`teams_projects` (  
`id_teams_projects` INT NOT NULL AUTO_INCREMENT,  

  `f_id_team` INT NOT NULL,  

  `f_id_project` INT NOT NULL,  

  PRIMARY KEY (`id_teams_projects`),  
UNIQUE INDEX `id_teams_projects _UNIQUE` (`id_teams_projects` ASC) VISIBLE,

  INDEX `f_id_team_idx` (`f_id_team` ASC) VISIBLE,  

  CONSTRAINT `f_id_team_teams_projects`  

    FOREIGN KEY (`f_id_team`)  

    REFERENCES `flowster_gruppe10`.`teams` (`id_team`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE,  

  CONSTRAINT `f_id_project_teams_projects`  

    FOREIGN KEY (`f_id_project`)  

    REFERENCES `flowster_gruppe10`.`projects` (`id_project`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE);  

CREATE TABLE `flowster_gruppe10`.`projects_users` (
  `id_projects_users` INT NOT NULL AUTO_INCREMENT,
  `f_id_project` INT NOT NULL,
  `f_id_user` INT NOT NULL,
  PRIMARY KEY (`id_projects_users`),
  UNIQUE INDEX `id_projects_users_UNIQUE` (`id_projects_users` ASC) VISIBLE,
  UNIQUE INDEX `f_id_project_UNIQUE` (`f_id_project` ASC) VISIBLE,
  INDEX `f_id_user_projects_users_idx` (`f_id_user` ASC) VISIBLE,
  CONSTRAINT `f_id_project_projects_users`
    FOREIGN KEY (`f_id_project`)
    REFERENCES `flowster_gruppe10`.`projects` (`id_project`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `f_id_user_projects_users`
    FOREIGN KEY (`f_id_user`)
    REFERENCES `flowster_gruppe10`.`users` (`id_user`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

CREATE TABLE `flowster_gruppe10`.`subprojects` (  

  `id_subproject` INT NOT NULL AUTO_INCREMENT,  

  `subproject_title` VARCHAR(100) NOT NULL,  

  `subproject_description` VARCHAR(10000) NULL,  

  `subproject_deadline` DATE NULL,  

  `subproject_manhours` DOUBLE NULL,  

  PRIMARY KEY (`id_subproject`),  

  UNIQUE INDEX `id_subproject_UNIQUE` (`id_subproject` ASC) VISIBLE);  

CREATE TABLE `flowster_gruppe10`.`projects_subprojects` (  
  `id_projects_subprojects` INT NOT NULL AUTO_INCREMENT,  

  `f_id_project` INT NOT NULL,  

  `f_id_subproject` INT NOT NULL,  

  PRIMARY KEY (`id_projects_subprojects`),  
UNIQUE INDEX `id_projects_subprojects_UNIQUE` (`id_projects_subprojects` ASC) VISIBLE,  

  UNIQUE INDEX `f_id_subproject_UNIQUE` (`f_id_subproject` ASC) VISIBLE,  

  CONSTRAINT `f_id_project_projects_subprojects`  

    FOREIGN KEY (`f_id_project`)  

    REFERENCES `flowster_gruppe10`.`projects` (`id_project`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE,  

  CONSTRAINT `f_id_subprojectt_projects_subprojects`  

    FOREIGN KEY (`f_id_subproject`)  

    REFERENCES `flowster_gruppe10`.`subprojects` (`id_subproject`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE);  

CREATE TABLE `flowster_gruppe10`.`tasks` (  

  `id_task` INT NOT NULL AUTO_INCREMENT,  

  `task_title` VARCHAR(45) NULL,  
`task_manhours` DOUBLE NULL,

  PRIMARY KEY (`id_task`),  

  UNIQUE INDEX `id_task_UNIQUE` (`id_task` ASC) VISIBLE);  

CREATE TABLE `flowster_gruppe10`.`subprojects_tasks` (  
  `id_subprojects_tasks` INT NOT NULL AUTO_INCREMENT,  

  `f_id_subproject` INT NOT NULL,  

  `f_id_task` INT NOT NULL,  

  PRIMARY KEY (`id_subprojects_tasks`),  
  UNIQUE INDEX ` id_subprojects_tasks_UNIQUE` (`id_subprojects_tasks` ASC) VISIBLE,  
  UNIQUE INDEX `f_id_task_UNIQUE` (`f_id_task` ASC) VISIBLE,  

  CONSTRAINT `f_id_subproject_subprojects_tasks`  

    FOREIGN KEY (`f_id_subproject`)  

    REFERENCES `flowster_gruppe10`.`subprojects` (`id_subproject`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE,  

  CONSTRAINT `f_id_task_subprojects_tasks`  

    FOREIGN KEY (`f_id_task`)  

    REFERENCES `flowster_gruppe10`.`tasks` (`id_task`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE);  

CREATE TABLE `flowster_gruppe10`.`subtasks` (  

  `id_subtask` INT NOT NULL AUTO_INCREMENT,  

  `subtask_title` VARCHAR(100) NOT NULL,  
`subtask_manhours` DOUBLE NULL,

  PRIMARY KEY (`id_subtask`),  

  UNIQUE INDEX `id_subtask_UNIQUE` (`id_subtask` ASC) VISIBLE);  

CREATE TABLE `flowster_gruppe10`.`tasks_subtasks` (  
  `id_tasks_subtasks` INT NOT NULL AUTO_INCREMENT,  

  `f_id_task` INT NOT NULL,  

  `f_id_subtask` INT NOT NULL,  

  PRIMARY KEY (`id_tasks_subtasks`),  
  UNIQUE INDEX ` id_tasks_subtasks_UNIQUE` (`id_tasks_subtasks` ASC) VISIBLE,  

  UNIQUE INDEX `f_id_subtask_UNIQUE` (`f_id_subtask` ASC) VISIBLE,  

  CONSTRAINT `f_id_task_tasks_subtasks`  

    FOREIGN KEY (`f_id_task`)  

    REFERENCES `flowster_gruppe10`.`tasks` (`id_task`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE,  

  CONSTRAINT `f_id_subtask_tasks_subtasks`  

    FOREIGN KEY (`f_id_subtask`)  

    REFERENCES `flowster_gruppe10`.`subtasks` (`id_subtask`)  

    ON DELETE CASCADE  

    ON UPDATE CASCADE);  


  



insert into flowster.organisations (organisation_name) value ("KEA"); 

insert into flowster.organisations (organisation_name) value ("Alpha Solutions"); 

insert into flowster.organisations (organisation_name) value ("Gruppe10"); 


insert into flowster.job_types (job_type) value ("Projektleder"); 

insert into flowster.job_types (job_type) value ("Almen medarbejder"); 

 


