DROP INDEX `idx_dcs_projectid_id` ON dcs;

DROP INDEX `idx_dcsitem_parentid_id` ON dcs_item;

DROP INDEX `idx_dcsitem_parentid_itemno` ON dcs_item;

DROP INDEX `idx_frs_projectid_id` ON frs;

DROP INDEX `idx_frsdetail_aprentid_reqid` ON frs_detail;

DROP INDEX `idex_project_name` ON project;

DROP INDEX `idx_script_instruction_stepno` ON script_instruction;

DROP INDEX `idx_testcase_parentid_testcaseid` ON test_case;

DROP INDEX `idx_testscript_projectid_id` ON test_script;

DROP INDEX `idx_urs_projectid_id` ON urs;

DROP INDEX `idx_ursdetail_parentid_reqid` ON urs_detail;

ALTER TABLE `dcs_item`
	DROP FOREIGN KEY `fk_dcs_item_parentid`;

ALTER TABLE `frs_detail`
	DROP FOREIGN KEY `fk_frs_detail_parent_id`;

ALTER TABLE `urs_frs_relation`
	DROP FOREIGN KEY `fk_urs_frs_relation_frsid`;

ALTER TABLE `urs`
	DROP FOREIGN KEY `kf_urs_project_id`;

ALTER TABLE `frs`
	DROP FOREIGN KEY `fk_frs_project_id`;

ALTER TABLE `dcs`
	DROP FOREIGN KEY `fk_dcs_project_id`;

ALTER TABLE `test_script`
	DROP FOREIGN KEY `fk_test_script_project_id`;

ALTER TABLE `script_instruction`
	DROP FOREIGN KEY `fk_script_instruction_parent_id`;

ALTER TABLE `test_case`
	DROP FOREIGN KEY `fk_test_case_parent_id`;

ALTER TABLE `urs_detail`
	DROP FOREIGN KEY `fk_urs_detail_parent_id`;

ALTER TABLE `urs_frs_relation`
	DROP FOREIGN KEY `fk_urs_frs_relation_ursid`;

ALTER TABLE `urs_frs_relation`
	DROP INDEX `uk_urs_frs_relation_ursid_frsid`;

DROP TABLE IF EXISTS `dcs`;

DROP TABLE IF EXISTS `dcs_item`;

DROP TABLE IF EXISTS `frs`;

DROP TABLE IF EXISTS `frs_detail`;

DROP TABLE IF EXISTS `project`;

DROP TABLE IF EXISTS `script_instruction`;

DROP TABLE IF EXISTS `test_case`;

DROP TABLE IF EXISTS `test_script`;

DROP TABLE IF EXISTS `urs`;

DROP TABLE IF EXISTS `urs_detail`;

DROP TABLE IF EXISTS `urs_frs_relation`;

CREATE TABLE `dcs`  ( 
	`id`                	bigint(11) UNSIGNED AUTO_INCREMENT NOT NULL,
	`project_id`        	bigint(11) UNSIGNED NOT NULL,
	`document_id`       	varchar(100) NULL,
	`version`           	varchar(25) NULL,
	`file_name`         	varchar(100) NULL,
	`purpose`           	varchar(2000) NULL,
	`scope`             	varchar(2000) NULL,
	`out_of_scope`      	varchar(2000) NULL,
	`assumptions`       	varchar(2000) NULL,
	`limitations`       	varchar(2000) NULL,
	`dependencies`      	varchar(2000) NULL,
	`system_description`	varchar(2000) NULL,
	`created_at`        	datetime NULL DEFAULT CURRENT_TIMESTAMP,
	`last_modified_at`  	datetime NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
)
ENGINE = InnoDB;

CREATE TABLE `dcs_item`  ( 
	`id`              	bigint(11) UNSIGNED AUTO_INCREMENT NOT NULL,
	`parent_id`       	bigint(11) UNSIGNED NOT NULL,
	`item_number`     	varchar(20) NULL,
	`item_title`      	varchar(100) NULL,
	`specification`   	varchar(2000) NULL,
	`created_at`      	datetime NULL DEFAULT CURRENT_TIMESTAMP,
	`last_modified_at`	datetime NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
)
ENGINE = InnoDB;

CREATE TABLE `frs`  ( 
	`id`                          	bigint(11) UNSIGNED AUTO_INCREMENT NOT NULL,
	`project_id`                  	bigint(11) UNSIGNED NOT NULL,
	`document_id`                 	varchar(100) NULL,
	`version`                     	varchar(25) NULL,
	`file_name`                   	varchar(100) NULL,
	`purpose`                     	varchar(2000) NULL,
	`score`                       	varchar(2000) NULL,
	`out_of_scope`                	varchar(2000) NULL,
	`assumptions`                 	varchar(2000) NULL,
	`limitations`                 	varchar(2000) NULL,
	`dependencies`                	varchar(2000) NULL,
	`business_process_description`	varchar(2000) NULL,
	`system_description`          	varchar(2000) NULL,
	`created_at`                  	datetime NULL DEFAULT CURRENT_TIMESTAMP,
	`last_modified_at`            	datetime NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
)
ENGINE = InnoDB;

CREATE TABLE `frs_detail`  ( 
	`id`                  	bigint(11) UNSIGNED AUTO_INCREMENT NOT NULL,
	`parent_id`           	bigint(11) UNSIGNED NOT NULL,
	`requirement_category`	varchar(50) NULL,
	`related_urs`         	varchar(200) NULL,
	`requirement_id`      	varchar(50) NULL,
	`description`         	varchar(2000) NULL,
	`risk_area`           	varchar(100) NULL,
	`criticality_rating`  	varchar(100) NULL,
	`created_at`          	datetime NULL DEFAULT CURRENT_TIMESTAMP,
	`last_modified_at`    	datetime NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
)
ENGINE = InnoDB;

CREATE TABLE `project`  ( 
	`id`              	bigint(11) UNSIGNED AUTO_INCREMENT NOT NULL,
	`name`            	varchar(200) NULL,
	`created_at`      	datetime NULL DEFAULT CURRENT_TIMESTAMP,
	`last_modified_at`	datetime NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
)
ENGINE = InnoDB
CHARACTER SET utf8mb4;

CREATE TABLE `script_instruction`  ( 
	`id`                 	bigint(11) UNSIGNED AUTO_INCREMENT NOT NULL,
	`parent_id`          	bigint(11) UNSIGNED NOT NULL,
	`step_number`        	varchar(25) NULL,
	`step_instruction`   	varchar(2000) NULL,
	`expected_results`   	varchar(2000) NULL,
	`tested_requirements`	varchar(1000) NULL,
	`created_at`         	datetime NULL DEFAULT CURRENT_TIMESTAMP,
	`last_modified_at`   	datetime NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
)
ENGINE = InnoDB;

CREATE TABLE `test_case`  ( 
	`id`                      	bigint(11) UNSIGNED AUTO_INCREMENT NOT NULL,
	`parent_id`               	bigint(11) UNSIGNED NOT NULL,
	`test_case_id`            	varchar(50) NULL,
	`test_case_title`         	varchar(50) NULL,
	`test_objective`          	varchar(2000) NULL,
	`tested_requirements`     	varchar(2000) NULL,
	`test_acceptance_criteria`	varchar(2048) NULL,
	`created_at`              	datetime NULL DEFAULT CURRENT_TIMESTAMP,
	`last_modified_at`        	datetime NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
)
ENGINE = InnoDB;

CREATE TABLE `test_script`  ( 
	`id`              	bigint(11) UNSIGNED AUTO_INCREMENT NOT NULL,
	`project_id`      	bigint(11) UNSIGNED NOT NULL,
	`document_id`     	varchar(100) NULL,
	`version`         	varchar(25) NULL,
	`file_name`       	varchar(100) NULL,
	`test_category`   	varchar(50) NULL,
	`purpose`         	varchar(2000) NULL,
	`prerequisites`   	varchar(2000) NULL,
	`created_at`      	datetime NULL DEFAULT CURRENT_TIMESTAMP,
	`last_modified_at`	datetime NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
)
ENGINE = InnoDB;

CREATE TABLE `urs`  ( 
	`id`                          	bigint(11) UNSIGNED AUTO_INCREMENT NOT NULL,
	`project_id`                  	bigint(11) UNSIGNED NOT NULL,
	`document_id`                 	varchar(100) NULL,
	`version`                     	varchar(25) NULL,
	`file_name`                   	varchar(100) NULL,
	`purpose`                     	varchar(2000) NULL,
	`scope`                       	varchar(2000) NULL,
	`out_of_scope`                	varchar(2000) NULL,
	`assumptions`                 	varchar(2000) NULL,
	`limitations`                 	varchar(2000) NULL,
	`dependencies`                	varchar(2000) NULL,
	`business_process_description`	varchar(2000) NULL,
	`system_description`          	varchar(2000) NULL,
	`created_at`                  	datetime NULL DEFAULT CURRENT_TIMESTAMP,
	`last_modified_at`            	datetime NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
)
ENGINE = InnoDB
CHARACTER SET utf8mb4;

CREATE TABLE `urs_detail`  ( 
	`id`                  	bigint(11) UNSIGNED AUTO_INCREMENT NOT NULL,
	`parent_id`           	bigint(11) UNSIGNED NOT NULL,
	`requirement_category`	varchar(50) NULL,
	`requirement_id`      	varchar(50) NULL,
	`description`         	varchar(2000) NULL,
	`risk_area`           	varchar(100) NULL,
	`criticality_rating`  	varchar(100) NULL,
	`created_at`          	datetime NULL DEFAULT CURRENT_TIMESTAMP,
	`last_modified_at`    	datetime NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
)
ENGINE = InnoDB;

CREATE TABLE `urs_frs_relation`  ( 
	`id`              	bigint(11) UNSIGNED AUTO_INCREMENT NOT NULL,
	`urs_detail_id`   	bigint(11) UNSIGNED NOT NULL,
	`frs_detail_id`   	bigint(11) UNSIGNED NOT NULL,
	`created_at`      	datetime NULL DEFAULT CURRENT_TIMESTAMP,
	`last_modified_at`	datetime NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(`id`)
)
ENGINE = InnoDB;

ALTER TABLE `urs_frs_relation`
	ADD CONSTRAINT `uk_urs_frs_relation_ursid_frsid`
	UNIQUE (`urs_detail_id`, `frs_detail_id`);

ALTER TABLE `dcs_item`
	ADD CONSTRAINT `fk_dcs_item_parentid`
	FOREIGN KEY(`parent_id`)
	REFERENCES `dcs`(`id`);

ALTER TABLE `frs_detail`
	ADD CONSTRAINT `fk_frs_detail_parent_id`
	FOREIGN KEY(`parent_id`)
	REFERENCES `frs`(`id`);

ALTER TABLE `urs_frs_relation`
	ADD CONSTRAINT `fk_urs_frs_relation_frsid`
	FOREIGN KEY(`frs_detail_id`)
	REFERENCES `frs_detail`(`id`)
	ON DELETE CASCADE ;

ALTER TABLE `urs`
	ADD CONSTRAINT `kf_urs_project_id`
	FOREIGN KEY(`project_id`)
	REFERENCES `project`(`id`)
	ON DELETE CASCADE 
	ON UPDATE NO ACTION ;

ALTER TABLE `frs`
	ADD CONSTRAINT `fk_frs_project_id`
	FOREIGN KEY(`project_id`)
	REFERENCES `project`(`id`)
	ON DELETE CASCADE ;

ALTER TABLE `dcs`
	ADD CONSTRAINT `fk_dcs_project_id`
	FOREIGN KEY(`project_id`)
	REFERENCES `project`(`id`)
	ON DELETE CASCADE ;

ALTER TABLE `test_script`
	ADD CONSTRAINT `fk_test_script_project_id`
	FOREIGN KEY(`project_id`)
	REFERENCES `project`(`id`)
	ON DELETE CASCADE ;

ALTER TABLE `script_instruction`
	ADD CONSTRAINT `fk_script_instruction_parent_id`
	FOREIGN KEY(`parent_id`)
	REFERENCES `test_case`(`id`)
	ON DELETE CASCADE ;

ALTER TABLE `test_case`
	ADD CONSTRAINT `fk_test_case_parent_id`
	FOREIGN KEY(`parent_id`)
	REFERENCES `test_script`(`id`)
	ON DELETE CASCADE ;

ALTER TABLE `urs_detail`
	ADD CONSTRAINT `fk_urs_detail_parent_id`
	FOREIGN KEY(`parent_id`)
	REFERENCES `urs`(`id`);

ALTER TABLE `urs_frs_relation`
	ADD CONSTRAINT `fk_urs_frs_relation_ursid`
	FOREIGN KEY(`urs_detail_id`)
	REFERENCES `urs_detail`(`id`)
	ON DELETE CASCADE ;

CREATE INDEX `idx_dcs_projectid_id` USING BTREE 
	ON `dcs`(`project_id`, `id`);

CREATE INDEX `idx_dcsitem_parentid_id` USING BTREE 
	ON `dcs_item`(`parent_id`, `id`);

CREATE INDEX `idx_dcsitem_parentid_itemno` USING BTREE 
	ON `dcs_item`(`parent_id`, `item_number`);

CREATE INDEX `idx_frs_projectid_id`
	ON `frs`(`project_id`, `id`);

CREATE INDEX `idx_frsdetail_aprentid_reqid`
	ON `frs_detail`(`parent_id`, `requirement_id`);

CREATE UNIQUE INDEX `idex_project_name` USING BTREE 
	ON `project`(`name` DESC);

CREATE INDEX `idx_script_instruction_stepno` USING BTREE 
	ON `script_instruction`(`parent_id`, `step_number`);

CREATE INDEX `idx_testcase_parentid_testcaseid` USING BTREE 
	ON `test_case`(`parent_id`, `test_case_id`);

CREATE INDEX `idx_testscript_projectid_id` USING BTREE 
	ON `test_script`(`project_id`, `id`);

CREATE INDEX `idx_urs_projectid_id` USING BTREE 
	ON `urs`(`project_id`, `id`);

CREATE INDEX `idx_ursdetail_parentid_reqid`
	ON `urs_detail`(`parent_id`, `requirement_id`);

