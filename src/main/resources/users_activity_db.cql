DROP KEYSPACE IF EXISTS users_activity_db;

CREATE KEYSPACE users_activity_db WITH replication = {'class':'SimpleStrategy','replication_factor':2};

USE users_activity_db;


CREATE TABLE IF NOT EXISTS events_by_correlation_id
(
client_ip inet,
correlation_id text,
reference text,
event_type text,
details text,
user_agent text,
user_agent_filtered text,
event_date_time timestamp,
PRIMARY KEY (correlation_id, event_date_time)
) WITH CLUSTERING ORDER BY (event_date_time DESC);

ALTER TABLE events_by_correlation_id WITH COMPACTION = {'class':'DateTieredCompactionStrategy', 
'timestamp_resolution':'MICROSECONDS', 
'base_time_seconds':'3600', 
'max_sstable_age_days':'730'};

CREATE TABLE IF NOT EXISTS events_by_reference_and_year
(
reference text,
year int,
client_ip inet,
correlation_id text,
event_type text,
details text,
user_agent text,
user_agent_filtered text,
event_date_time timestamp,
PRIMARY KEY ((reference, year), event_date_time)
) WITH CLUSTERING ORDER BY (event_date_time DESC);

ALTER TABLE events_by_reference_and_year WITH COMPACTION = {'class':'DateTieredCompactionStrategy', 
'timestamp_resolution':'MICROSECONDS', 
'base_time_seconds':'3600', 
'max_sstable_age_days':'730'};