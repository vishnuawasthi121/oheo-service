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
     JOIN vehicle_detail vd ON p.vehicle_detail_id = vd.id
     JOIN vehicle_fuel_type vft ON p.vehicle_fuel_type_id = vft.id
     JOIN vehicle_model vm ON p.vehicle_model_id = vm.id
     JOIN vehicle_type vtype ON p.vehicle_type_id = vtype.id
     JOIN vehicle_transmission vtras ON p.vehicle_transmission_id = vtras.id
     JOIN product_specification ps ON p.id = ps.product_id
     JOIN user_detail userdetails ON p.user_detail_id = userdetails.id
     JOIN vehicle_body_type   vby ON vby.id =  vd.vehicle_body_type_id
  WHERE p.status::text = 'ACTIVE'::text;


*/
