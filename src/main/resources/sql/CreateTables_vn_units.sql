DROP TABLE IF EXISTS wards;
DROP TABLE IF EXISTS districts;
DROP TABLE IF EXISTS provinces;
DROP TABLE IF EXISTS administrative_units;
DROP TABLE IF EXISTS administrative_regions;

CREATE SCHEMA vn;

-- CREATE administrative_regions TABLE
CREATE TABLE vn.administrative_regions (
	id integer NOT NULL,
	"name" varchar(255) NOT NULL,
	name_en varchar(255) NOT NULL,
	code_name varchar(255) NULL,
	code_name_en varchar(255) NULL,
	CONSTRAINT administrative_regions_pkey PRIMARY KEY (id)
);


-- CREATE administrative_units TABLE
CREATE TABLE vn.administrative_units (
	id integer NOT NULL,
	full_name varchar(255) NULL,
	full_name_en varchar(255) NULL,
	short_name varchar(255) NULL,
	short_name_en varchar(255) NULL,
	code_name varchar(255) NULL,
	code_name_en varchar(255) NULL,
	CONSTRAINT administrative_units_pkey PRIMARY KEY (id)
);


-- CREATE provinces TABLE
CREATE TABLE vn.provinces (
	code varchar(20) NOT NULL,
	"name" varchar(255) NOT NULL,
	name_en varchar(255) NULL,
	full_name varchar(255) NOT NULL,
	full_name_en varchar(255) NULL,
	code_name varchar(255) NULL,
	administrative_unit_id integer NULL,
	administrative_region_id integer NULL,
	CONSTRAINT provinces_pkey PRIMARY KEY (code)
);


-- provinces foreign keys

ALTER TABLE vn.provinces ADD CONSTRAINT provinces_administrative_region_id_fkey FOREIGN KEY (administrative_region_id) REFERENCES vn.administrative_regions(id);
ALTER TABLE vn.provinces ADD CONSTRAINT provinces_administrative_unit_id_fkey FOREIGN KEY (administrative_unit_id) REFERENCES vn.administrative_units(id);

CREATE INDEX idx_provinces_region ON vn.provinces(administrative_region_id);
CREATE INDEX idx_provinces_unit ON vn.provinces(administrative_unit_id);


-- CREATE districts TABLE
CREATE TABLE vn.districts (
	code varchar(20) NOT NULL,
	"name" varchar(255) NOT NULL,
	name_en varchar(255) NULL,
	full_name varchar(255) NULL,
	full_name_en varchar(255) NULL,
	code_name varchar(255) NULL,
	province_code varchar(20) NULL,
	administrative_unit_id integer NULL,
	CONSTRAINT districts_pkey PRIMARY KEY (code)
);


-- districts foreign keys

ALTER TABLE vn.districts ADD CONSTRAINT districts_administrative_unit_id_fkey FOREIGN KEY (administrative_unit_id) REFERENCES vn.administrative_units(id);
ALTER TABLE vn.districts ADD CONSTRAINT districts_province_code_fkey FOREIGN KEY (province_code) REFERENCES vn.provinces(code);

CREATE INDEX idx_districts_province ON vn.districts(province_code);
CREATE INDEX idx_districts_unit ON vn.districts(administrative_unit_id);



-- CREATE wards TABLE
CREATE TABLE vn.wards (
	code varchar(20) NOT NULL,
	"name" varchar(255) NOT NULL,
	name_en varchar(255) NULL,
	full_name varchar(255) NULL,
	full_name_en varchar(255) NULL,
	code_name varchar(255) NULL,
	district_code varchar(20) NULL,
	administrative_unit_id integer NULL,
	CONSTRAINT wards_pkey PRIMARY KEY (code)
);


-- wards foreign keys

ALTER TABLE vn.wards ADD CONSTRAINT wards_administrative_unit_id_fkey FOREIGN KEY (administrative_unit_id) REFERENCES vn.administrative_units(id);
ALTER TABLE vn.wards ADD CONSTRAINT wards_district_code_fkey FOREIGN KEY (district_code) REFERENCES vn.districts(code);

CREATE INDEX idx_wards_district ON vn.wards(district_code);
CREATE INDEX idx_wards_unit ON vn.wards(administrative_unit_id);
