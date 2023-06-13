/*
DROP VIEW IF EXISTS view_user_details;


CREATE VIEW view_user_details (id, name, contact, email, is_validated, password, status, gst_number, root_id,VEHICLE_TYPE_ID ,zonename, statename, cityname, code, role_name, roleid,vehicleTypeName) AS  SELECT u.id,
    u.name,
    u.contact,
    u.email,
    u.is_validated,
    u.password,
    u.status,
    u.gst_number,
    u.root_id,
    u.VEHICLE_TYPE_ID,
    z.name AS zonename,
    s.state_name AS statename,
    city.name AS cityname,
    zipcode.code,
    ur.role AS role_name,
    ur.id AS roleid,
    vt.name AS  vehicleTypeName
   FROM user_detail u
     JOIN zone z ON u.zone_id = z.id
     JOIN state s ON u.state_id = s.state_id
     JOIN city city ON u.city_id = city.id
     JOIN zipcode zipcode ON u.zipcode_id = zipcode.id
     JOIN user_role ur ON u.role_id = ur.id
     JOIN VEHICLE_TYPE  vt ON u.VEHICLE_TYPE_ID = vt.id;



*/