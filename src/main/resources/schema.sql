/*
DROP VIEW IF EXISTS view_user_details;
CREATE VIEW view_user_details (id, name, contact, email, is_validated, password, status, gst_number, root_id, vehicle_type_id, zonename, statename, cityname, code, role_name, roleid, vehicletypename) AS  SELECT u.id,
    u.name,
    u.contact,
    u.email,
    u.is_validated,
    u.password,
    u.status,
    u.gst_number,
    u.root_id,
    u.vehicle_type_id,
    z.name AS zonename,
    s.state_name AS statename,
    city.name AS cityname,
    zipcode.code,
    ur.role AS role_name,
    ur.id AS roleid,
    vt.name AS vehicletypename
   FROM user_detail u
     LEFT JOIN zone z ON u.zone_id = z.id
     LEFT JOIN state s ON u.state_id = s.state_id
     LEFT JOIN city city ON u.city_id = city.id
     LEFT JOIN zipcode zipcode ON u.zipcode_id = zipcode.id
     LEFT JOIN user_role ur ON u.role_id = ur.id
     LEFT JOIN vehicle_type vt ON u.vehicle_type_id = vt.id
     AND u.status = 'ACTIVE';

*/


/**
DROP VIEW IF EXISTS view_live_product;
CREATE VIEW view_live_product (id, engine, is_live, name, no_of_seats, price, status, available_for_lease, vehicle_body_type, vehicle_fuel_type, vehicle_model_name, vehicle_type_name, vehicle_tras_name, userdetails_name, chassis_number, color, displacement, engine_type, front_brake_type, fuel_supply_system, gear_box, height, is_turbo_charger, length, no_ofcylinder, power, rear_brake_type, seating_capacity, steering_type, torque, tyre_type, values_configuration, values_per_cylinder, wheel_base, width) AS  SELECT p.id,
    p.engine,
    p.is_live,
    p.name,
    p.no_of_seats,
    p.price,
    p.status,
    p.available_for_lease,
    vby.name AS vehicle_body_type,
    vft.name AS vehicle_fuel_type,
    vm.model_name AS vehicle_model_name,
    vtype.name AS vehicle_type_name,
    vtras.name AS vehicle_tras_name,
    userdetails.name AS userdetails_name,
    ps.chassis_number,
    ps.color,
    ps.displacement,
    ps.engine_type,
    ps.front_brake_type,
    ps.fuel_supply_system,
    ps.gear_box,
    ps.height,
    ps.is_turbo_charger,
    ps.length,
    ps.no_ofcylinder,
    ps.power,
    ps.rear_brake_type,
    ps.seating_capacity,
    ps.steering_type,
    ps.torque,
    ps.tyre_type,
    ps.values_configuration,
    ps.values_per_cylinder,
    ps.wheel_base,
    ps.width
   FROM product p
     LEFT JOIN vehicle_detail vd ON p.vehicle_detail_id = vd.id
     LEFT JOIN vehicle_fuel_type vft ON p.vehicle_fuel_type_id = vft.id
     LEFT JOIN vehicle_model vm ON p.vehicle_model_id = vm.id
     LEFT JOIN vehicle_type vtype ON p.vehicle_type_id = vtype.id
     LEFT JOIN vehicle_transmission vtras ON p.vehicle_transmission_id = vtras.id
     LEFT JOIN product_specification ps ON p.id = ps.product_id
     LEFT JOIN user_detail userdetails ON p.user_detail_id = userdetails.id
     LEFT JOIN vehicle_body_type   vby ON vby.id =  vd.vehicle_body_type_id
  WHERE p.status::text = 'ACTIVE'::text;


DROP VIEW IF EXISTS view_product_lease;
CREATE VIEW view_product_lease (product_id, product_name, status, lease_id, description, down_payment, duration, km_validity, monthly_emi, created_date, updated_date, image_id) AS  SELECT p.id AS product_id,
    p.name AS product_name,
    p.status,
    lease.id AS lease_id,
    lease.description,
    lease.down_payment,
    lease.duration,
    lease.km_validity,
    lease.monthly_emi,
    lease.created_date,
    lease.updated_date,
    image.id AS image_id
   FROM lease_detail lease
     JOIN product p ON lease.product_id = p.id
     JOIN images image ON image.lease_id = lease.id;


INSERT INTO shop_category (id, description, name, status) VALUES (100, 'Slider section', 'Slider', 'ACTIVE');
INSERT INTO shop_category (id, description, name, status) VALUES (101, 'Two Wheeler section', 'Two Wheeler', 'ACTIVE');
INSERT INTO shop_category (id, description, name, status) VALUES (102, 'Three Wheeler section', 'Three Wheeler', 'ACTIVE');
INSERT INTO shop_category (id, description, name, status) VALUES (103, 'Four Wheeler section', 'Four Wheeler', 'ACTIVE');
INSERT INTO shop_category (id, description, name, status) VALUES (104, 'Spare Parts section', 'Spare Parts', 'ACTIVE');
select * from shop_category;


*/
