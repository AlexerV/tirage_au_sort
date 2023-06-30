
--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'DRAW_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('DRAW_MANAGEMENT','draw.adminFeature.ManageDraw.name',1,'jsp/admin/plugins/draw/ManageDraws.jsp','draw.adminFeature.ManageDraw.description',0,'draw',NULL,NULL,NULL,4);


--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'DRAW_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('DRAW_MANAGEMENT',1);

