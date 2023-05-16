/*
DROP VIEW IF EXISTS view_user_details;
CREATE VIEW view_user_details (id, name, contact, email, is_validated, password, status,root_id, zonename, statename, cityname, code, rolename,roleId) AS  SELECT u.id,
    u.name,
    u.contact,
    u.email,
    u.is_validated,
    u.password,
    u.status,
    u.root_id AS root_id,
    z.name AS zonename,
    s.state_name AS statename,
    city.name AS cityname,
    zipcode.code,
    role.role AS rolename,
    role.id AS roleId
   FROM user_detail u
     JOIN zone z ON u.zone_id = z.id
     JOIN state s ON u.state_id = s.state_id
     JOIN city city ON u.city_id = city.id
     JOIN zipcode zipcode ON u.zipcode_id = zipcode.id
     JOIN user_role role ON u.role_id = role.id;
*/